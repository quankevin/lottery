package com.xmtq.lottery.activity;

import java.util.Date;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.AccountDetailListAdapter;
import com.xmtq.lottery.bean.AccountDetailBean;
import com.xmtq.lottery.bean.AccountDetailResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.DateUtil;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 近一周
 * 
 * @author Administrator
 * 
 */
public class AccountDetailLastweekActivity extends BaseActivity {

	private ImageButton btn_back;
	private ListView account_detail_lastweek_list;
	private TextView income;
	private TextView pay;
	private String pay2;
	private String income2;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.account_dedail_last_week);
	}

	@Override
	public void dealLogicBeforeInitView() {
		request(getDate(-7), getDate(0));
		// pay2 = getIntent().getStringExtra("pay");
		// income2 = getIntent().getStringExtra("income");
	}

	@Override
	public void initView() {
		pay = (TextView) findViewById(R.id.pay);
		income = (TextView) findViewById(R.id.income);
		// pay.setText("收入：" + pay2);
		// income.setText("支出：" + income2);
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		account_detail_lastweek_list = (ListView) findViewById(R.id.account_detail_lastweek_list);

		RadioGroup account_detail_radiogroup = (RadioGroup) findViewById(R.id.account_detail_radiogroup);

		account_detail_radiogroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (checkedId == R.id.last_week) {
							request(getDate(-7), getDate(0));
						} else if (checkedId == R.id.last_month) {
							request(getDate(-30), getDate(0));
						} else if (checkedId == R.id.last_three_month) {
							request(getDate(-90), getDate(0));
						} else if (checkedId == R.id.last_six_month) {
							request(getDate(-180), getDate(0));
						}
						// account_detail_lastweek_list.setAdapter(adapter);
					}
				});
	}

	private void request(String startdate, String enddate) {
		String userid = SharedPrefHelper.getInstance(getApplicationContext())
				.getUid();
		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getAccountDetail(startdate, enddate,
				userid, "", "1", "10"));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	private OnCompleteListener<AccountDetailResponse> mOnCompleteListener = new OnCompleteListener<AccountDetailResponse>() {

		@Override
		public void onComplete(AccountDetailResponse result, String resultString) {
			if (result != null) {
				AccountDetailResponse mResponse = result;
				List<AccountDetailBean> mHistoryBeansList = mResponse.accountDetailList;
				pay2 = mResponse.getPay();
				if (!TextUtils.isEmpty(pay2) && !pay2.equals("null")) {
					pay.setText("支出：" + pay2);
				} else {
					pay.setText("支出: 无");
				}

				income2 = mResponse.getIncome();
				if (!TextUtils.isEmpty(income2) && !income2.equals("null")) {
					income.setText("收入：" + income2);
				} else {
					income.setText("收入：无");
				}

				if (mHistoryBeansList != null) {
					AccountDetailListAdapter mAdapter = new AccountDetailListAdapter(
							AccountDetailLastweekActivity.this,
							mHistoryBeansList);
					account_detail_lastweek_list.setAdapter(mAdapter);
					mLoadingDialog.dismiss();
				}
			} else {
				ToastUtil.showCenterToast(AccountDetailLastweekActivity.this,
						"数据请求失败");
			}

		}
	};

	private String getDate(int num) {
		Date date = DateUtil.addDate(new Date(), num);
		String d = DateUtil.getDate(date);
		return d;
	}

	@Override
	public void dealLogicAfterInitView() {

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;

		default:
			break;
		}

	}

}
