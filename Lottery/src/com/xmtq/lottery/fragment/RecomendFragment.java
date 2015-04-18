package com.xmtq.lottery.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.universe.lottery.util.SplitLotteryJCZC;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.R;
import com.xmtq.lottery.activity.OddsDetailActivity;
import com.xmtq.lottery.activity.RecomendActivity;
import com.xmtq.lottery.adapter.RecomendListAdapter;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.BetInfoBean;
import com.xmtq.lottery.bean.GameCanBetBean;
import com.xmtq.lottery.bean.GameCanBetResponse;
import com.xmtq.lottery.bean.Odds;
import com.xmtq.lottery.bean.PassType;
import com.xmtq.lottery.bean.RecomendWinRecordBean;
import com.xmtq.lottery.bean.RecomendWinRecordResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.DateUtil;
import com.xmtq.lottery.utils.OddsUtil;
import com.xmtq.lottery.utils.OnRefreshListener;
import com.xmtq.lottery.utils.PassTypeUtil;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.widget.BalanceNotEnoughDialog;
import com.xmtq.lottery.widget.BetConfirmDialog;
import com.xmtq.lottery.widget.CheckChuanGuanDialog;
import com.xmtq.lottery.widget.ChuanGuanDialog;
import com.xmtq.lottery.widget.CustomPullListView;
import com.xmtq.lottery.widget.CustomPullListView.OnLoadMoreListener;
import com.xmtq.lottery.widget.LoadingDialog;
import com.xmtq.lottery.widget.MarqueeText;

/**
 * 首页推荐
 * 
 * @author mwz123
 * 
 */
@SuppressLint("HandlerLeak")
public class RecomendFragment extends BaseFragment {

	/**
	 * 更多竞猜玩法
	 */
	private final static int ODDS_REQUEST_CODE = 1;

	/**
	 * 拼接投注串
	 */
	private final static int TYPE_BETTING = 0;

	/**
	 * 计算注数
	 */
	private final static int TYPE_VOTENUMS = 1;

	/**
	 * 没有选择串关
	 */
	private final static String NO_PASS_TYPE = "no_pass_type";

	private ImageButton imgBtnLeft, imgBtnRight;
	private ImageButton recomend_refresh;
	private CustomPullListView recomend_list;
	private TextView recomend_lottery_times;
	private TextView recomend_date;
	private TextView recomend_week;
	private TextView recomend_commit;
	// private TextView betting_votenums;
	// private TextView betting_multiple;
	// private TextView betting_buymoney;
	private TextView betting_info;

	private int currentPageNum = 1;
	private int pageSize = 10;
	private int count = 0;
	private List<GameCanBetBean> gameCanBetBeans;
	private RecomendListAdapter mAdapter;

	private static final int LOAD_DATA_CHANGE = 9;// 刷新日期
	private static final int LOAD_DATA_FINISH = 10;// 上拉加载
	private static final int REFRESH_DATA_FINISH = 11;// 下拉刷新
	private static final int REFRESH_BET_INFO = 12;// 投注信息刷新
	private RadioButton chuan_guan;

	private ChuanGuanDialog mChuanGuanDialog;
	private RadioButton check_chuan_guan;
	private List<PassType> simplePassList;
	private List<PassType> morePassList;
	private LoadingDialog mLoadingDialog;
	private CheckChuanGuanDialog mCheckChuanGuanDialog;
	private MarqueeText win_record;
	private boolean isSupportDg = false;

	// 选中的比赛场次
	private int gameCheckNum = 0;
	private int lastCheckNum = 0;
	int scroll_state = OnScrollListener.SCROLL_STATE_IDLE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoadingDialog = new LoadingDialog(getActivity());
		requestWinRecord("10");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recomend, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mLoadingDialog.show("加载数据...");
		request();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 请求可投注的场次
	 */
	private void request() {
		// TODO Auto-generated method stub
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getGameCanBet(
				"" + currentPageNum, "" + pageSize));
		mAsyncTask.setOnCompleteListener(mOnGameCompleteListener);
	}

	/**
	 * 点击投注
	 */
	private void requestBetting() {
		// 判断是否登录
		if (!SharedPrefHelper.getInstance(getActivity()).getIsLogin()) {
			((RecomendActivity) getActivity()).openLeftDrawer();
			ToastUtil.showCenterToast(getActivity(), "请先登录");
			return;
		}
		String uid = SharedPrefHelper.getInstance(getActivity()).getUid();
		String lotteryid = Consts.Lottery_ID;
		String votetype = Consts.VOTE_TYPE;
		String playtype = Consts.PLAY_TYPE;
		String protype = Consts.PRO_TYPE;
		int multiple = getMultiple();
		String passtype = getPassType();
		String voteinfo = getOddsData(TYPE_BETTING, String.valueOf(multiple),
				passtype);
		if (TextUtils.isEmpty(voteinfo)) {
			ToastUtil.showCenterToast(getActivity(), "请选择投注场次");
			return;
		}
		if (voteinfo.equals(NO_PASS_TYPE)) {
			ToastUtil.showCenterToast(getActivity(), "请选择串关方式");
			return;
		}

		// 仅用于计算注数
		String sVoteinfo = getOddsData(TYPE_VOTENUMS, String.valueOf(multiple),
				passtype);
		int votenums = getVoteNums(sVoteinfo);
		if (votenums == 0) {
			ToastUtil.showCenterToast(getActivity(), "串关方式有误");
			return;
		}
		int buymoney = multiple * votenums * 2;
		int totalmoney = buymoney;

		// 过关类型转换，passtype为1_1
		passtype = passtype.replace("*", "_");

		// 保存投注数据，传递到其他组件
		BetInfoBean betInfoBean = new BetInfoBean();
		betInfoBean.setUid(uid);
		betInfoBean.setLotteryid(lotteryid);
		betInfoBean.setVotetype(votetype);
		betInfoBean.setVotenums(String.valueOf(votenums));
		betInfoBean.setMultiple(String.valueOf(multiple));
		betInfoBean.setVoteinfo(voteinfo);
		betInfoBean.setTotalmoney(String.valueOf(totalmoney));
		betInfoBean.setPlaytype(playtype);
		betInfoBean.setPasstype(passtype);
		betInfoBean.setBuymoney(String.valueOf(buymoney));
		betInfoBean.setProtype(protype);

		// 判断用户余额，用于投注跳转
		String sAccountBalance = SharedPrefHelper.getInstance(getActivity())
				.getAccountBalance();
		betInfoBean.setAccountBalance(sAccountBalance);
		double accountBalance = Double.valueOf(sAccountBalance);
		if (accountBalance >= buymoney) {
			// 余额充足
			BetConfirmDialog betConfirmDialog = new BetConfirmDialog(
					getActivity(), betInfoBean);
			betConfirmDialog.show();
			betConfirmDialog.setOnRefreshListener(onRefreshDataListener);
		} else {
			// 余额不足
			BalanceNotEnoughDialog balanceNotEnoughDialog = new BalanceNotEnoughDialog(
					getActivity(), betInfoBean);
			balanceNotEnoughDialog.show();
		}
	}

	/**
	 * 请求中奖用户信息
	 * 
	 * @param size
	 */
	private void requestWinRecord(String size) {
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getGameWinRecord(size));
		mAsyncTask.setOnCompleteListener(mWinRecordCompleteListener);
	}

	public void initView(View v) {
		win_record = (MarqueeText) v.findViewById(R.id.win_record);
		imgBtnLeft = (ImageButton) v.findViewById(R.id.recomend_left);
		imgBtnRight = (ImageButton) v.findViewById(R.id.recomend_right);
		recomend_refresh = (ImageButton) v.findViewById(R.id.recomend_refresh);
		chuan_guan = (RadioButton) v.findViewById(R.id.chuan_guan);
		check_chuan_guan = (RadioButton) v.findViewById(R.id.check_chuan_guan);
		check_chuan_guan.setOnClickListener(this);
		chuan_guan.setOnClickListener(this);
		imgBtnLeft.setOnClickListener(this);
		imgBtnRight.setOnClickListener(this);
		recomend_refresh.setOnClickListener(this);
		recomend_list = (CustomPullListView) v.findViewById(R.id.recomend_list);
		recomend_list.setCanRefresh(true);
		recomend_list.setCanLoadMore(true);

		recomend_lottery_times = (TextView) v
				.findViewById(R.id.recomend_lottery_times);
		recomend_date = (TextView) v.findViewById(R.id.recomend_date);
		recomend_week = (TextView) v.findViewById(R.id.recomend_week);
		recomend_commit = (TextView) v.findViewById(R.id.recomend_commit);
		recomend_commit.setOnClickListener(this);
		// betting_votenums = (TextView) v.findViewById(R.id.betting_votenums);
		// betting_multiple = (TextView) v.findViewById(R.id.betting_multiple);
		// betting_buymoney = (TextView) v.findViewById(R.id.betting_buymoney);
		betting_info = (TextView) v.findViewById(R.id.betting_info);

		dealLogicAfterInitView();
	}

	public void dealLogicAfterInitView() {
		Date date = new Date(System.currentTimeMillis());
		String time = DateUtil.getyyyy_MM_ddTime(date);
		String week = DateUtil.getWeek(date);
		recomend_date.setText(time);
		recomend_week.setText(week);

		initPassType();
	}

	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.recomend_left:
			((RecomendActivity) getActivity()).openLeftDrawer();
			break;

		case R.id.recomend_right:
			((RecomendActivity) getActivity()).openRightDrawer();
			break;

		case R.id.chuan_guan:
			if (gameCheckNum == 0) {
				ToastUtil.showCenterToast(getActivity(), "请选择投注场次");
				return;
			}

			if (gameCheckNum != lastCheckNum) {
				initPassType();
			}

			mChuanGuanDialog = new ChuanGuanDialog(getActivity(),
					mCancelListener, mCommitListener, simplePassList,
					morePassList, isSupportDg);
			mChuanGuanDialog.show();
			break;

		case R.id.check_chuan_guan:
			mCheckChuanGuanDialog = new CheckChuanGuanDialog(getActivity());
			mCheckChuanGuanDialog
					.setOnCompleteListener(new CheckChuanGuanDialog.OnCompleteListener() {

						@Override
						public void onComplete(String resultString) {
							// TODO Auto-generated method stub
							if (resultString != null) {
								if (Integer.parseInt(resultString) == 0) {
									ToastUtil.showCenterToast(getActivity(),
											"倍数不能为0");
									return;
								} else if (Integer.parseInt(resultString) <= 1000) {
									check_chuan_guan
											.setText(resultString + "倍");
									refreshBet();
								} else {
									ToastUtil.showCenterToast(getActivity(),
											"倍数不能大于一千倍");
								}

							}
						}
					});
			mCheckChuanGuanDialog.show();
			break;

		case R.id.recomend_commit:
			requestBetting();
			break;

		case R.id.recomend_refresh:
			reLoad();
			break;

		default:
			break;
		}
	}

	/**
	 * 重新加载
	 */
	private void reLoad() {
		currentPageNum = 1;
		mLoadingDialog.show("加载数据...");
		initData();
		request();
	}

	/**
	 * 串关选择取消
	 */
	private OnClickListener mCancelListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			mChuanGuanDialog.dismiss();

			// 点击串关取消后，初始化过关数据
			refreshBet();
		}
	};

	/**
	 * 串关选择确定
	 */
	private OnClickListener mCommitListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			mChuanGuanDialog.dismiss();
			refreshBet();
		}
	};

	/**
	 * 可竞猜场次回调处理
	 */
	private OnCompleteListener<BaseResponse> mOnGameCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					onSuccess(result);
				} else {
					onFailure(result.errormsg);
				}
			} else {
				onFailure(Consts.REQUEST_ERROR);
			}
			mLoadingDialog.dismiss();
		}
	};

	/**
	 * 获奖记录回调
	 */
	private OnCompleteListener<RecomendWinRecordResponse> mWinRecordCompleteListener = new OnCompleteListener<RecomendWinRecordResponse>() {
		@Override
		public void onComplete(RecomendWinRecordResponse result,
				String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					RecomendWinRecordResponse mWinRecordResponse = result;
					List<RecomendWinRecordBean> mRecordBeans = mWinRecordResponse.mWinRecordBeans;
					StringBuffer sb = new StringBuffer();
					String record = "";
					for (int i = 0; i < mRecordBeans.size(); i++) {
						record = mRecordBeans.get(i).getUsername()
								+ mRecordBeans.get(i).getLotteryname()
								+ mRecordBeans.get(i).getGuoguan() + "   "
								+ mRecordBeans.get(i).getBonus()
								+ "元            ";
						sb.append(record);
					}

					win_record.setText(sb.toString());

				} else {
					onFailure(result.errormsg);
				}
			} else {
				onFailure(Consts.REQUEST_ERROR);
			}
		}
	};

	/**
	 * 请求成功
	 */
	private void onSuccess(BaseResponse result) {
		GameCanBetResponse gameCanBetResponse = (GameCanBetResponse) result;

		if (currentPageNum == 1) {
			count = Integer.parseInt(gameCanBetResponse.count);
			int comCount = Integer.parseInt(gameCanBetResponse.comCount);
			if (comCount > 0) {
				recomend_lottery_times.setText("推荐" + comCount + "场比赛");
			}
			gameCanBetBeans = gameCanBetResponse.gameCanBetBeans;
			mAdapter = new RecomendListAdapter(getActivity(), gameCanBetBeans);
			mAdapter.setOnMoreListener(onMoreListener);
			mAdapter.setOnRefreshListener(onRefreshBetListener);
			recomend_list.setAdapter(mAdapter);

			setRecommendGame(gameCanBetBeans);
			mHandler.sendEmptyMessage(REFRESH_DATA_FINISH);

			// 下拉刷新
			recomend_list
					.setOnRefreshListener(new CustomPullListView.OnRefreshListener() {

						@Override
						public void onRefresh() {
							// TODO Auto-generated method stub
							reLoad();
							// mHandler.sendEmptyMessageDelayed(
							// REFRESH_DATA_FINISH, 1000);
						}
					});

			// 上拉加载
			recomend_list.setOnLoadListener(new OnLoadMoreListener() {

				@Override
				public void onLoadMore() {
					// TODO Auto-generated method stub
					if (currentPageNum * pageSize > count) {
						ToastUtil.showCenterToast(getActivity(), "已获取全部数据");
						mHandler.sendEmptyMessage(LOAD_DATA_FINISH);
						return;
					}
					currentPageNum++;
					request();
				}
			});

			// 滑动事件
			recomend_list
					.setMyOnScrollListener(new AbsListView.OnScrollListener() {

						@Override
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							scroll_state = scrollState;
							if (scroll_state == OnScrollListener.SCROLL_STATE_IDLE) {
								mHandler.removeMessages(LOAD_DATA_CHANGE);
								Message msg = new Message();
								msg.what = LOAD_DATA_CHANGE;
								msg.arg1 = view.getFirstVisiblePosition();
								mHandler.sendMessage(msg);
							}
						}

						@Override
						public void onScroll(AbsListView view,
								int firstVisibleItem, int visibleItemCount,
								int totalItemCount) {
						}
					});

		} else {
			gameCanBetBeans.addAll(gameCanBetResponse.gameCanBetBeans);
			mHandler.sendEmptyMessage(LOAD_DATA_FINISH);
		}
	}

	/**
	 * 请求失败
	 * 
	 * @param msg
	 */
	private void onFailure(String msg) {
		ToastUtil.showCenterToast(getActivity(), msg);
		mHandler.sendEmptyMessage(REFRESH_DATA_FINISH);
		mHandler.sendEmptyMessage(LOAD_DATA_FINISH);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case REFRESH_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					recomend_list.onRefreshComplete();
				}
				break;

			case LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					recomend_list.onLoadMoreComplete();
				}
				break;

			case REFRESH_BET_INFO:
				refresh();
				break;
			case LOAD_DATA_CHANGE:
				String realDate = DateUtil.getRealDate(gameCanBetBeans.get(
						msg.arg1).getGameTime());
				recomend_date.setText(realDate);

				Date date = DateUtil.stringToDateFormat(realDate, "yyyy-MM-dd");
				String dayOfWeek = DateUtil.getWeek(date);
				recomend_week.setText(dayOfWeek);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 处理回传数据
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void setIntentResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == FragmentActivity.RESULT_OK) {
			GameCanBetBean gameCanBetBean = (GameCanBetBean) data
					.getSerializableExtra("GameCanBetBean");
			int position = data.getIntExtra("position", 0);
			gameCanBetBeans.set(position, gameCanBetBean);
			mAdapter.notifyDataSetChanged();
			onRefreshBetListener.onRefresh();
		}
	}

	/**
	 * 更多玩法点击Listener 说明：Adapter的context没有startActivityForResult方法
	 */
	private OnClickListener onMoreListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int position = (Integer) view.getTag();
			Intent intent = new Intent(getActivity(), OddsDetailActivity.class);
			intent.putExtra("GameCanBetBean", gameCanBetBeans.get(position));
			intent.putExtra("position", position);
			getActivity().startActivityForResult(intent, ODDS_REQUEST_CODE);
		}
	};

	/**
	 * 刷新比赛数据
	 */
	private OnRefreshListener onRefreshDataListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			initData();
		}
	};

	/**
	 * 设置推荐比赛默认选中
	 */
	private void setRecommendGame(List<GameCanBetBean> gameCanBetBeans) {
		for (GameCanBetBean gameCanBetBean : gameCanBetBeans) {
			// 胜负平推荐
			String content = gameCanBetBean.getSpContent();
			List<Odds> oddsList = gameCanBetBean.getSpOddsList();
			setDefaultCheck(content, oddsList);

			// 让球胜负平推荐
			content = gameCanBetBean.getRqContent();
			oddsList = gameCanBetBean.getRqOddsList();
			setDefaultCheck(content, oddsList);

			// 比分推荐
			content = gameCanBetBean.getBfContent();
			oddsList = gameCanBetBean.getBfOddsList();
			setDefaultCheck(content, oddsList);

			// 进球推荐
			content = gameCanBetBean.getJqContent();
			oddsList = gameCanBetBean.getJqOddsList();
			setDefaultCheck(content, oddsList);

			// 半全场推荐
			content = gameCanBetBean.getBqContent();
			oddsList = gameCanBetBean.getBqOddsList();
			setDefaultCheck(content, oddsList);
		}
		refreshBet();
	}

	/**
	 * 设置默认选中
	 * 
	 * @param content
	 * @param oddsList
	 */
	private void setDefaultCheck(String content, List<Odds> oddsList) {
		if (!TextUtils.isEmpty(content) && oddsList != null) {
			for (Odds odds : oddsList) {
				if (odds.getResult().equals(content)) {
					odds.setChecked(true);
					break;
				}
			}
		}
	}

	/**
	 * 初始化所有数据
	 */
	private void initData() {
		initGameData();
		initPassType();
		refresh();
	}

	/**
	 * 初始化比赛数据
	 */
	private void initGameData() {
		if (gameCanBetBeans != null) {
			for (GameCanBetBean gameCanBetBean : gameCanBetBeans) {
				List<Odds> spOddsList = gameCanBetBean.getSpOddsList();
				List<Odds> rqOddsList = gameCanBetBean.getRqOddsList();
				List<Odds> bfOddsList = gameCanBetBean.getBfOddsList();
				List<Odds> bqOddsList = gameCanBetBean.getBqOddsList();
				List<Odds> jqOddsList = gameCanBetBean.getJqOddsList();

				initOddsList(spOddsList);
				initOddsList(rqOddsList);
				initOddsList(bfOddsList);
				initOddsList(bqOddsList);
				initOddsList(jqOddsList);
			}
			mAdapter.notifyDataSetChanged();
		}

		gameCheckNum = 0;
		lastCheckNum = 0;
		isSupportDg = false;
		check_chuan_guan.setText("倍数");
	}

	/**
	 * 初始化赔率玩法
	 * 
	 * @param oddsList
	 */
	private void initOddsList(List<Odds> oddsList) {
		for (Odds odds : oddsList) {
			odds.setChecked(false);
		}
	}

	/**
	 * 初始化过关类型
	 */
	private void initPassType() {
		simplePassList = PassTypeUtil.getSimplePassList(gameCheckNum);
		morePassList = PassTypeUtil.getMorePassList(gameCheckNum);

		// 不支持单关
		if (!isSupportDg) {
			if (simplePassList != null && simplePassList.size() > 0) {
				simplePassList.remove(0);
			}
		}
	}

	/**
	 * 刷新界面投注信息
	 */
	private OnRefreshListener onRefreshBetListener = new OnRefreshListener() {

		public void onRefresh() {
			// TODO Auto-generated method stub

			// 刷新过关类型，选中比赛item的回调才执行
			initPassType();

			// 刷新投注信息
			refreshBet();
		}
	};

	/**
	 * 刷新比赛数据
	 */
	private void refreshBet() {
		sendEmptyMessage(REFRESH_BET_INFO);
	}

	/**
	 * 发送空消息
	 * 
	 * @param what
	 */
	private void sendEmptyMessage(int what) {
		mHandler.removeMessages(what);
		mHandler.sendEmptyMessage(what);
	}

	/**
	 * 刷新投注信息
	 */
	private void refresh() {
		int multiple = getMultiple();
		String passType = getPassType();
		String voteinfo = getOddsData(TYPE_VOTENUMS, String.valueOf(multiple),
				passType);
		if (voteinfo != null && voteinfo.equals(NO_PASS_TYPE)) {
			voteinfo = null;
		}
		int votenums = getVoteNums(voteinfo);
		int buymoney = votenums * multiple * 2;
		betting_info.setText(votenums + "注" + multiple + "倍" + "  共" + buymoney
				+ "元");
	}

	/**
	 * 获取注数
	 * 
	 * @param voteinfo
	 * @return
	 */
	private int getVoteNums(String voteinfo) {
		int votenums = 0;
		if (!TextUtils.isEmpty(voteinfo)) {
			votenums = SplitLotteryJCZC.exeNum(voteinfo);
		}
		return votenums;
	}

	/**
	 * 获取倍数
	 * 
	 * @param multiple
	 * @return
	 */
	private int getMultiple() {
		int num = 1;
		String multiple = check_chuan_guan.getText().toString();
		if (multiple.equals("倍数")) {
			num = 1;
		} else {
			multiple = multiple.replace("倍", "");
			try {
				num = Integer.parseInt(multiple);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return num;
	}

	/**
	 * 获取过关类型
	 * 
	 * @return
	 */
	private String getPassType() {
		StringBuilder sb = new StringBuilder();
		List<String> checkList = new ArrayList<String>();
		for (PassType passType : simplePassList) {
			if (passType.isChecked()) {
				if (!(passType.getName().equals("单关") && !isSupportDg)) {
					checkList.add(passType.getValue());
				}
			}
		}

		for (PassType passType : morePassList) {
			if (passType.isChecked()) {
				checkList.add(passType.getValue());
			}
		}

		if (checkList.size() > 0) {
			for (int i = 0; i < checkList.size(); i++) {
				if (i == 0) {
					sb.append(checkList.get(i));
				} else {
					sb.append("," + checkList.get(i));
				}
			}
		} else {
			// 默认过关类型
			// if (isSupportDg) {
			// sb.append("1*1");
			// }
		}

		return sb.toString();
	}

	/**
	 * 竞猜数据拼接 type 0：投注串(每场比赛之间用"_"连接) 1：计算注数(每场比赛之间用"&"连接)
	 */
	private String getOddsData(int type, String multiple, String passType) {
		if (gameCanBetBeans == null || gameCanBetBeans.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		isSupportDg = true;
		sb.append("HT");
		sb.append("@");
		List<String> gameCheckList = new ArrayList<String>();
		for (GameCanBetBean gameCanBetBean : gameCanBetBeans) {
			List<String> gameOddsList = new ArrayList<String>();
			StringBuilder gameSb = new StringBuilder();

			// 胜负平拼接
			String spOddsData = OddsUtil.getSpOddsData(gameCanBetBean
					.getSpOddsList());
			if (!TextUtils.isEmpty(spOddsData)) {
				if (gameCanBetBean.getSpDg().equals("0")) {
					isSupportDg = false;
				}
				gameOddsList.add(spOddsData);
			}

			// 让球胜负平拼接
			String rqOddsData = OddsUtil.getRqOddsData(gameCanBetBean
					.getRqOddsList());
			if (!TextUtils.isEmpty(rqOddsData)) {
				if (gameCanBetBean.getRqDg().equals("0")) {
					isSupportDg = false;
				}
				gameOddsList.add(rqOddsData);
			}

			// 比分拼接
			String bfOddsData = OddsUtil.getBfOddsData(gameCanBetBean
					.getBfOddsList());
			if (!TextUtils.isEmpty(bfOddsData)) {
				if (gameCanBetBean.getBfDg().equals("0")) {
					isSupportDg = false;
				}
				gameOddsList.add(bfOddsData);
			}

			// 进球拼接
			String jqOddsData = OddsUtil.getJqOddsData(gameCanBetBean
					.getJqOddsList());
			if (!TextUtils.isEmpty(jqOddsData)) {
				if (gameCanBetBean.getJqDg().equals("0")) {
					isSupportDg = false;
				}
				gameOddsList.add(jqOddsData);
			}

			// 半全场拼接
			String bqOddsData = OddsUtil.getBqOddsData(gameCanBetBean
					.getBqOddsList());
			if (!TextUtils.isEmpty(bqOddsData)) {
				if (gameCanBetBean.getBqDg().equals("0")) {
					isSupportDg = false;
				}
				gameOddsList.add(bqOddsData);
			}

			// 本场比赛所有玩法拼接
			if (gameOddsList.size() > 0) {
				gameSb.append(gameCanBetBean.getMatchId() + "|");
				for (int i = 0; i < gameOddsList.size(); i++) {
					if (i == 0) {
						gameSb.append(gameOddsList.get(i));
					} else {
						gameSb.append("," + gameOddsList.get(i));
					}
				}
			}

			// 保存本场比赛所有投注数据
			if (!TextUtils.isEmpty(gameSb)) {
				gameCheckList.add(gameSb.toString());
			}
		}

		// 所有比赛投注拼接
		lastCheckNum = gameCheckNum;
		gameCheckNum = gameCheckList.size();
		if (gameCheckList.size() == 0) {
			return null;
		} else {
			for (int i = 0; i < gameCheckList.size(); i++) {
				if (i == 0) {
					sb.append(gameCheckList.get(i));
				} else {
					if (type == 0) {
						sb.append("_" + gameCheckList.get(i));
					} else if (type == 1) {
						sb.append("&" + gameCheckList.get(i));
					}
				}
			}
		}

		// 串关
		if (TextUtils.isEmpty(passType)) {
			return NO_PASS_TYPE;
		}
		sb.append("@");
		sb.append(passType);

		// 倍数
		sb.append("@");
		sb.append(multiple);

		// LogUtil.log("getOddsData isSupportDg:" + isSupportDg);
		return sb.toString();
	}

	

}
