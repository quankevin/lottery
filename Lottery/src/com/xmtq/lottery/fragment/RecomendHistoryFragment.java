package com.xmtq.lottery.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.activity.GameResultActivity;
import com.xmtq.lottery.activity.RecomendActivity;
import com.xmtq.lottery.adapter.RecomendHistoryListAdapter;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.GameHistoryDateBean;
import com.xmtq.lottery.bean.GameHistoryDateResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.widget.CustomPullListView;
import com.xmtq.lottery.widget.CustomPullListView.OnLoadMoreListener;
import com.xmtq.lottery.widget.CustomPullListView.OnRefreshListener;

/*
 * 推荐历史
 * 
 * @author mwz123
 * 
 */
@SuppressLint("HandlerLeak")
public class RecomendHistoryFragment extends BaseFragment {
	private CustomPullListView recomend_history_list;
	private ImageButton btn_back;
	private List<GameHistoryDateBean> mHistoryBeansList;
	private RecomendHistoryListAdapter mAdapter;
	private int currentPageNum = 1;
	private int pageSize = 10;
	private int count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recomend_history, container,
				false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		request();
		super.onActivityCreated(savedInstanceState);
	}

	public void initView(View v) {

		recomend_history_list = (CustomPullListView) v
				.findViewById(R.id.recomend_history_list);
		btn_back = (ImageButton) v.findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		recomend_history_list.setOnItemClickListener(historyListOnItemListener);
		recomend_history_list.setCanRefresh(true);
		recomend_history_list.setCanLoadMore(true);

		// 下拉刷新
		recomend_history_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessageDelayed(Consts.REFRESH_DATA_FINISH,
						1000);
			}
		});

		// 上拉加载
		recomend_history_list.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (currentPageNum * pageSize > count) {
					ToastUtil.showCenterToast(getActivity(), "已获取全部数据");
					mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
					return;
				}
				currentPageNum++;
				request();
			}
		});

	}

	/**
	 * 请求数据
	 */
	private void request() {
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getGameHistoryDateList("", "",
				String.valueOf(currentPageNum), String.valueOf(pageSize)));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 请求数据回调
	 */
	private OnCompleteListener<BaseResponse> mOnCompleteListener = new OnCompleteListener<BaseResponse>() {

		@Override
		public void onComplete(BaseResponse result, String resultString) {
			if (result != null) {
				if (result.errorcode.equals("0")) {
					onSuccess(result);
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
		GameHistoryDateResponse response = (GameHistoryDateResponse) result;
		if (currentPageNum == 1) {
			count = Integer.parseInt(response.count);
			mHistoryBeansList = response.mHistoryDateBeansList;
			if (mHistoryBeansList != null) {
				mAdapter = new RecomendHistoryListAdapter(getActivity(),
						mHistoryBeansList);
				recomend_history_list.setAdapter(mAdapter);
			}
		} else {
			mHistoryBeansList.addAll(response.mHistoryDateBeansList);
			mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
		}
	}

	/**
	 * 请求失败
	 * 
	 * @param msg
	 */
	private void onFailure(String msg) {
		ToastUtil.showCenterToast(getActivity(), msg);
		mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
	}

	/**
	 * 点击推荐详情 <br>
	 * <li><strong>注意ListView包含HeaderView时，position要减1 </strong></li>
	 */
	private OnItemClickListener historyListOnItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent i = new Intent(getActivity(), GameResultActivity.class);
			i.putExtra("mHistoryBean", mHistoryBeansList.get(arg2 - 1));
			startActivity(i);

		}
	};

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClickEvent(View v) {
		switch (v.getId()) {
		case R.id.back:
			((RecomendActivity) getActivity()).closeRightDrawer();
			break;
		default:
			break;
		}
	}

	/**
	 * 消息处理
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Consts.REFRESH_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					recomend_history_list.onRefreshComplete();
				}
				break;

			case Consts.LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					recomend_history_list.onLoadMoreComplete();
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

}
