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

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.activity.BetDetailActivity;
import com.xmtq.lottery.adapter.BetRecordListAdapter;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.PurchaseRecordsBean;
import com.xmtq.lottery.bean.PurchaseRecordsResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.widget.CustomPullListView;
import com.xmtq.lottery.widget.CustomPullListView.OnLoadMoreListener;
import com.xmtq.lottery.widget.CustomPullListView.OnRefreshListener;

/**
 * 投注记录Fragment
 * <ul>
 * <li>全部</li>
 * <li>中奖</li>
 * <li>待开</li>
 * </ul>
 * 
 * 
 */
@SuppressLint("HandlerLeak")
public class BetRecordFragment extends BaseFragment {
	private CustomPullListView mBetRecordListview;
	private List<PurchaseRecordsBean> mRecordsBeansList;
	private BetRecordListAdapter mAdapter;
	private String mStatue = "0";
	private int currentPageNum = 1;
	private int pageSize = 10;
	private int count = 0;

	public BetRecordFragment() {
		// TODO Auto-generated constructor stub
	}

	public BetRecordFragment(String statue) {
		// TODO Auto-generated constructor stub
		this.mStatue = statue;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bet_record, container,
				false);
		dealLogicBeforeInitView();
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		request("130", "", "", "1", mStatue);
		super.onActivityCreated(savedInstanceState);
	}

	public void dealLogicBeforeInitView() {
	}

	public void initView(View v) {
		mBetRecordListview = (CustomPullListView) v
				.findViewById(R.id.bet_record_listview);
		mBetRecordListview.setOnItemClickListener(betDetailListener);
		mBetRecordListview.setCanRefresh(true);
		mBetRecordListview.setCanLoadMore(true);

		// 下拉刷新
		mBetRecordListview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessageDelayed(Consts.REFRESH_DATA_FINISH,
						1000);
			}
		});

		// 上拉加载
		mBetRecordListview.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (currentPageNum * pageSize > count) {
					ToastUtil.showCenterToast(getActivity(), "已获取全部数据");
					mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
					return;
				}
				currentPageNum++;
				request("130", "", "", "1", mStatue);
			}
		});
	}

	/**
	 * 请求投注记录
	 * 
	 * @param lotteryid
	 * @param startdate
	 * @param enddate
	 * @param investtype
	 * @param statue
	 */
	private void request(String lotteryid, String startdate, String enddate,
			String investtype, String statue) {
		String userid = SharedPrefHelper.getInstance(getActivity()).getUid();

		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getPurchaseRecords(userid, lotteryid,
				startdate, enddate, investtype, String.valueOf(currentPageNum),
				String.valueOf(pageSize), statue));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 请求回调
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
			mLoadingDialog.dismiss();
		}
	};

	/**
	 * 请求成功
	 */
	private void onSuccess(BaseResponse result) {
		PurchaseRecordsResponse response = (PurchaseRecordsResponse) result;

		if (currentPageNum == 1) {
			count = Integer.parseInt(response.count);
			mRecordsBeansList = response.purchaseRecordsBeans;
			if (mRecordsBeansList.size() == 0) {
				ToastUtil.showCenterToast(getActivity(), "没有投注记录");
				mBetRecordListview.setVisibility(View.GONE);
			}
			mAdapter = new BetRecordListAdapter(getActivity(),
					mRecordsBeansList);
			mBetRecordListview.setAdapter(mAdapter);
		} else {
			mRecordsBeansList.addAll(response.purchaseRecordsBeans);
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
		mBetRecordListview.setVisibility(View.GONE);
		mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
	}

	/**
	 * 投注详情 <li><strong>注意：ListView包含HeaderView时，position要减1 </strong></li>
	 */
	private OnItemClickListener betDetailListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int position = arg2 - 1;
			Intent intent = new Intent(getActivity(), BetDetailActivity.class);
			intent.putExtra("serialid", mRecordsBeansList.get(position)
					.getSerialid());
			if (mRecordsBeansList.get(position).getBonusAfterfax() != null) {

				intent.putExtra("winMoney", mRecordsBeansList.get(position)
						.getBonusAfterfax());
			}
			startActivity(intent);

		}
	};

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Consts.REFRESH_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					mBetRecordListview.onRefreshComplete();
				}
				break;

			case Consts.LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					mBetRecordListview.onLoadMoreComplete();
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

}
