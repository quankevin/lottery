package com.xmtq.lottery.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.Consts;
import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.AccountDetailListAdapter;
import com.xmtq.lottery.bean.AccountDetailBean;
import com.xmtq.lottery.bean.AccountDetailResponse;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.widget.CustomPullListView;
import com.xmtq.lottery.widget.CustomPullListView.OnLoadMoreListener;
import com.xmtq.lottery.widget.CustomPullListView.OnRefreshListener;

/**
 * 账户明细Fragment
 * <ul>
 * <li>账户</li>
 * <li>充值</li>
 * <li>提现</li>
 * </ul>
 * 
 * 
 */
@SuppressLint("HandlerLeak")
public class AccountFragment extends BaseFragment {
	private CustomPullListView mListView;
	private List<AccountDetailBean> mBeanList;
	private AccountDetailListAdapter mAdapter;
	private LinearLayout account_ll;
	private TextView income;
	private TextView pay;
	private String mFlag = "0";
	private int currentPageNum = 1;
	private int pageSize = 10;
	private int count = 0;

	public AccountFragment() {
		// TODO Auto-generated constructor stub
	}

	public AccountFragment(String flag) {
		// TODO Auto-generated constructor stub
		this.mFlag = flag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_account, container,
				false);
		dealLogicBeforeInitView();
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		request(mFlag);
		super.onActivityCreated(savedInstanceState);
	}

	public void dealLogicBeforeInitView() {
	}

	public void initView(View v) {
		income = (TextView) v.findViewById(R.id.income);
		pay = (TextView) v.findViewById(R.id.pay);
		account_ll = (LinearLayout) v.findViewById(R.id.account_ll);
		mListView = (CustomPullListView) v.findViewById(R.id.account_listview);
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(true);

		// 下拉刷新
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessageDelayed(Consts.REFRESH_DATA_FINISH,
						1000);
			}
		});

		// 上拉加载
		mListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				if (currentPageNum * pageSize > count) {
					ToastUtil.showCenterToast(getActivity(), "已获取全部数据");
					mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
					return;
				}
				currentPageNum++;
				request(mFlag);
			}
		});
	}

	/**
	 * 请求数据
	 * 
	 * @param mFlag
	 */
	private void request(String mFlag) {
		String userid = SharedPrefHelper.getInstance(getActivity()).getUid();
		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask
				.execute(mRequestMaker.getAccountDetail("", "", userid, mFlag,
						String.valueOf(currentPageNum),
						String.valueOf(pageSize)));
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

		AccountDetailResponse response = (AccountDetailResponse) result;
		if (currentPageNum == 1) {
			count = Integer.parseInt(response.getCount());
			mBeanList = response.accountDetailList;
			if (mBeanList.size() == 0) {
				mListView.setVisibility(View.GONE);
				ToastUtil.showCenterToast(getActivity(), "没有账户明细");
				income.setText("");
				income.setText("");
			} else {
				mAdapter = new AccountDetailListAdapter(getActivity(),
						mBeanList);
				mListView.setAdapter(mAdapter);

				// 总帐户显示这个字段
				if (mFlag == "") {
					account_ll.setVisibility(View.VISIBLE);
					if (TextUtils.isEmpty(response.getPay())) {
						pay.setText("");
					} else {
						pay.setText("支出：" + response.getPay() + "元");
					}
					if (TextUtils.isEmpty(response.getIncome())) {
						income.setText("");
					} else {
						income.setText("收入：" + response.getIncome() + "元");
					}
				} 
			}
		} else {
			mBeanList.addAll(response.accountDetailList);
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
		mListView.setVisibility(View.GONE);
		mHandler.sendEmptyMessage(Consts.LOAD_DATA_FINISH);
	}

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
					mListView.onRefreshComplete();
				}
				break;

			case Consts.LOAD_DATA_FINISH:
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					mListView.onLoadMoreComplete();
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

}
