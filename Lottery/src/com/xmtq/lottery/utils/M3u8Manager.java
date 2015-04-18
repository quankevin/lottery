package com.xmtq.lottery.utils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

public class M3u8Manager {

	/**
	 * 播放器类型
	 */
	public static final int TYPE_LIVE = 1;
	public static final int TYPE_TIMESHIFT = 2;
	public static final int TYPE_PLAYBACK = 3;

	/**
	 * 时移类型
	 */
	public static final int TYPE_BACK = 10;
	public static final int TYPE_FORWARD = 11;
	public static final int TYPE_PAUSE = 12;
	public static final int TYPE_PLAY = 13;
	public static final int TYPE_SEEK_NONE = 14;

	/**
	 * 直播M3U8请求间隔
	 */
	private static final int TIME_PERIOD_LIVE = 10;

	/**
	 * 时移M3U8请求间隔
	 */
	private static final int TIME_PERIOD_TIMESHIFT = 30;

	/**
	 * 时移第一次请求时间量
	 */
	private static final int TIME_PERIOD_FIRST_REQUEST = 60;

	/**
	 * 内部参数
	 */
	private Context mContext;
	private OnCompletionListerner mOnCompletionListerner;
	private M3u8TimerTask mM3u8TimerTask;
	private Timer mTimer;
	private String mNetWorkUrl;
	private int mPlayerType;
	private long mEndTime;
	private long mStartTime;
	private long mPosition;
	private String mUrl;
	private int mSeekType;
	boolean isFirstRequest = true;

	/**
	 * 类实例化
	 * 
	 * @param
	 */
	public M3u8Manager(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	/**
	 * 开始请求M3U8
	 * 
	 * @param url
	 * @param playerType
	 * @param startTime
	 * @param endTime
	 * @param seekType
	 */
	public void start(String url, int playerType, long position, int seekType) {
		reset();
		mPosition = position;
		mSeekType = seekType;
		mPlayerType = playerType;
		mTimer = new Timer();
		mM3u8TimerTask = new M3u8TimerTask();
		mUrl = url;
		if (mPlayerType == TYPE_LIVE) {
			mNetWorkUrl = url;
			mTimer.schedule(mM3u8TimerTask, 0, TIME_PERIOD_LIVE * 1000);
		} else if (mPlayerType == TYPE_TIMESHIFT) {
			// mNetWorkUrl = getTimeShiftUrl(url);
			mTimer.schedule(mM3u8TimerTask, 0, TIME_PERIOD_TIMESHIFT * 1000);
		}
	}

	/**
	 * 停止请求M3U8
	 */
	public void stop() {
		reset();
	}

	/**
	 * 参数重置
	 */
	private void reset() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mM3u8TimerTask != null) {
			mM3u8TimerTask.cancel();
			mM3u8TimerTask = null;
		}
		mPlayerType = TYPE_LIVE;
		mNetWorkUrl = null;
		mStartTime = 0;
		mEndTime = 0;
		mPosition = 0;
		mSeekType = 0;
		isFirstRequest = true;
		// deleteLocalUrl();
	}

	/**
	 * 设置监听器
	 * 
	 * @param listerner
	 */
	public void setOnCompeletionListerner(OnCompletionListerner listerner) {
		mOnCompletionListerner = listerner;
	}

	/**
	 * 请求网络M3U8
	 * 
	 * @param url
	 * @return
	 */
	private String requstM3u8(String url) {
		if (url == null) {
			return null;
		}
		// String response = HttpUtil.postHttpClient(url, null, null);
		String response = HttpUtil.getHttpClient(url);
		return parseM3u8(response);
	}

	/**
	 * 
	 * 解析M3U8:(针对四川省UT前端进行解析)
	 * http://124.161.62.197:9000/TV/0/00000000000000050000000000000014/9.m3u8
	 * http://124.161.62.197:9000/TV/0/00000000000000050000000000000014/9.m3u8?
	 * bitrate=auto&tvodstarttime=1410364800&tvodendtime=1410368400
	 * http://124.161
	 * .62.197:9000/TV/0/00000000000000050000000000000014/9.m3u8?tvodstarttime
	 * =1410364800&tvodendtime=1410368400
	 * 
	 * 直播URL(带bitrate=auto参数): #EXTM3U
	 * #EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=3053568
	 * http://124.161.62.197:9000
	 * /TV/0/00000000000000050000000000000014/9.m3u8?tvodstarttime
	 * =1410364800&tvodendtime=1410368400
	 * 
	 * 直播嵌套URL: #EXTM3U #EXT-X-MEDIA-SEQUENCE:141385986 #EXT-X-TARGETDURATION:10
	 * #EXTINF:10, http://124.161.62.197:9000/VOD/tvod_root/0/
	 * 00000000000000050000000000000014/9/1413849600/1413856800/1413859860.ts
	 * #EXTINF:10, http://124.161.62.197:9000/VOD/tvod_root/0/
	 * 00000000000000050000000000000014/9/1413849600/1413856800/1413859870.ts
	 * #EXTINF:10, http://124.161.62.197:9000/VOD/tvod_root/0/
	 * 00000000000000050000000000000014/9/1413849600/1413856800/1413859880.ts
	 * 
	 * @param response
	 * @return
	 */
	private String parseM3u8(String response) {
		if (response == null || response == "")
			return null;

		String[] params = response.split("#");
		for (int i = 0; i < params.length; i++) {

			if (params[i].contains("EXT-X-STREAM-INF")) {
				String[] array = params[i].split("\n");
				mNetWorkUrl = array[1].trim();
				return requstM3u8(mNetWorkUrl);
			}

			if (mPlayerType == TYPE_TIMESHIFT) {
				if (response.contains("#EXT-X-ENDLIST")) {
					response = response.replace("#EXT-X-MEDIA-SEQUENCE:0",
							"#EXT-X-MEDIA-SEQUENCE:" + mStartTime / 10);
					response = response.replace("#EXT-X-ENDLIST", "");
				}
			}
		}
		String localUrl = createLocalUrl(response);
		return localUrl;
	}

	/**
	 * 创建本地M3U8文件
	 * 
	 * @param response
	 * @return
	 */
	private String createLocalUrl(String response) {
		Cache.writeCacheData(getLocalPath(), response);
		String localUrl = "file://" + getLocalPath();
		return localUrl;
	}

	/**
	 * 删除本地M3U8文件
	 */
	private void deleteLocalUrl() {
		File file = new File(getLocalPath());
		if (file.exists())
			file.delete();
	}

	/**
	 * 获取本地M3U8存储位置
	 * 
	 * @return
	 */
	private String getLocalPath() {
		return mContext.getCacheDir() + "/" + "test.m3u8";
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取时移URL
	 * 
	 * @param url
	 * @param isFirstRequest
	 * @return
	 */
	private String getTimeShiftUrl(String url, boolean isFirstRequest) {
		if (TextUtils.isEmpty(url))
			return null;
		StringBuffer sb = new StringBuffer(url);
		if (mSeekType == M3u8Manager.TYPE_BACK) {
			if (isFirstRequest) {
				// the firt time
				mStartTime = mPosition - TIME_PERIOD_FIRST_REQUEST;
				mEndTime = mStartTime + TIME_PERIOD_FIRST_REQUEST;
			} else {
				// next time
				mStartTime += TIME_PERIOD_TIMESHIFT;
				mEndTime += TIME_PERIOD_TIMESHIFT;
			}
		} else if (mSeekType == M3u8Manager.TYPE_FORWARD) {
			if (isFirstRequest) {
				// the firt time
				mStartTime = mPosition + TIME_PERIOD_FIRST_REQUEST;
				mEndTime = mStartTime + TIME_PERIOD_FIRST_REQUEST;
			} else {
				// next time
				mStartTime += TIME_PERIOD_TIMESHIFT;
				mEndTime += TIME_PERIOD_TIMESHIFT;
			}
		} else if (mSeekType == M3u8Manager.TYPE_PLAY) {
			if (isFirstRequest) {
				// the firt time
				mStartTime = mPosition;
				mEndTime = mStartTime + TIME_PERIOD_FIRST_REQUEST;
			} else {
				// next time
				mStartTime += TIME_PERIOD_TIMESHIFT;
				mEndTime += TIME_PERIOD_TIMESHIFT;
			}
		}

//		Log.d("Debug",
//				"mStartTime:"
//						+ DateFormat.format("HH:mm:ss", mStartTime * 1000)
//								.toString()
//						+ "systemTime:"
//						+ DateFormat.format("HH:mm:ss", getCurrentTime())
//								.toString());
		if (mStartTime >= (getCurrentTime() / 1000) - TIME_PERIOD_TIMESHIFT
				|| mStartTime <= 1000000000) {
			mOnCompletionListerner.onCompletion(null, mStartTime, mEndTime,
					true);
			mPlayerType = TYPE_LIVE;
			return null;
		}

		if (url.contains("?")) {
			sb.append("&tvodstarttime=" + mStartTime);
			sb.append("&tvodendtime=" + mEndTime);
		} else {
			sb.append("?tvodstarttime=" + mStartTime);
			sb.append("&tvodendtime=" + mEndTime);
		}
		// Log.d("Debug", "getTimeShiftUrl:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 设置播放器类型
	 * 
	 * @param playerType
	 */
	public void setPlayerType(int playerType) {
		this.mPlayerType = playerType;
	}

	/**
	 * 获取播放器类型
	 * 
	 * @return
	 */
	public int getPlayerType() {
		return mPlayerType;
	}

	/**
	 * 获取开始时间
	 * 
	 * @return
	 */
	public long getStartTime() {
		return mStartTime;
	}

	/**
	 * 获取结束时间
	 * 
	 * @return
	 */
	public long getEndTime() {
		return mEndTime;
	}

	/**
	 * 设置时移类型
	 * 
	 * @param seekType
	 */
	public void setSeekType(int seekType) {
		this.mSeekType = seekType;
	}

	/**
	 * 获取时移类型
	 * 
	 * @return
	 */
	public int getSeekType() {
		return mSeekType;
	}

	public boolean isFirstRequest() {
		return isFirstRequest;
	}

	/**
	 * 本地M3U8实时更新Task
	 * 
	 */
	private class M3u8TimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String localUrl = null;
			if (mPlayerType == TYPE_LIVE)
				localUrl = requstM3u8(mUrl);
			else if (mPlayerType == TYPE_TIMESHIFT) {
				mNetWorkUrl = getTimeShiftUrl(mUrl, isFirstRequest);
				if (!TextUtils.isEmpty(mNetWorkUrl))
					localUrl = requstM3u8(mNetWorkUrl);
			}
			if (mOnCompletionListerner != null) {
				mOnCompletionListerner.onCompletion(localUrl, mStartTime,
						mEndTime, false);
			}
			isFirstRequest = false;
		}
	};

	/**
	 * 监听器接口定义
	 * 
	 */
	public static interface OnCompletionListerner {
		public void onCompletion(String url, long startTime, long endTime,
				boolean isLive);
	}

}
