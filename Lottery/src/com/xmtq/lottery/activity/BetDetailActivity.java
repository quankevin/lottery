package com.xmtq.lottery.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.adapter.BetOrderDetailListAdapter;
import com.xmtq.lottery.bean.BetDetailAllBean;
import com.xmtq.lottery.bean.BetDetailAllResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.OddsUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 投注详情
 * 
 * @author Administrator
 * 
 */
public class BetDetailActivity extends BaseActivity {

	private ListView bet_record_detail;
	private ImageButton btn_back;
	private BetDetailAllBean mBetDetailAllBean;
	private String winMoney;

	private TextView bet_detail_guoguantype;
	private TextView bet_detail_periods;
	private TextView bet_detail_pay_money;
	private TextView bet_detail_state;
	private TextView bet_detail_win_money;
	private TextView bet_detail_date;
	private TextView bet_detail_play_style;
	private TextView bet_detail_pay_date;
	private TextView bet_detail_project_num;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.bet_orderdetail);
	}

	@Override
	public void dealLogicBeforeInitView() {

		String serialid = getIntent().getStringExtra("serialid");
		winMoney = getIntent().getStringExtra("winMoney");
		if (!TextUtils.isEmpty(serialid)) {
			request(serialid);
		}

	}

	@Override
	public void initView() {
		bet_detail_guoguantype = (TextView) findViewById(R.id.bet_detail_guoguantype);
		bet_detail_periods = (TextView) findViewById(R.id.bet_detail_periods);
		bet_detail_pay_money = (TextView) findViewById(R.id.bet_detail_pay_money);
		bet_detail_state = (TextView) findViewById(R.id.bet_detail_state);
		bet_detail_win_money = (TextView) findViewById(R.id.bet_detail_win_money);
		bet_detail_date = (TextView) findViewById(R.id.bet_detail_date);
		bet_detail_play_style = (TextView) findViewById(R.id.bet_detail_play_style);
		bet_detail_pay_date = (TextView) findViewById(R.id.bet_detail_pay_date);
		bet_detail_project_num = (TextView) findViewById(R.id.bet_detail_project_num);

		bet_record_detail = (ListView) findViewById(R.id.bet_orderdetail_list);
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
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

	private void request(String serialid) {
		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getPurchaseRecordsDetail(serialid));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	private OnCompleteListener<BetDetailAllResponse> mOnCompleteListener = new OnCompleteListener<BetDetailAllResponse>() {

		@Override
		public void onComplete(BetDetailAllResponse result, String resultString) {
			BetDetailAllResponse mAllResponse = result;
			if (result != null) {
				if (result.errorcode.equals("0")) {
					mBetDetailAllBean = mAllResponse.mBetDetailAllBean;

					if (mBetDetailAllBean != null) {
						bet_detail_guoguantype.setText(OddsUtil
								.playStyle(mBetDetailAllBean.getPlaytype()));
						bet_detail_periods.setText(mBetDetailAllBean
								.getProjecttime());
						bet_detail_pay_money.setText(mBetDetailAllBean
								.getProjectprize()+"元");
						bet_detail_state.setText(mBetDetailAllBean.getState());

						bet_detail_win_money.setText(mBetDetailAllBean
								.getBonus() + "元");
						bet_detail_date.setText(mBetDetailAllBean
								.getProjecttime());
						bet_detail_play_style.setText(mBetDetailAllBean
								.getGuoguantype()
								+ "    "
								+ mBetDetailAllBean.getUserballot()
								+ "注    "
								+ mBetDetailAllBean.getMultiple() + "倍");
						bet_detail_pay_date.setText(mBetDetailAllBean
								.getProjecttime());
						bet_detail_project_num.setText(mBetDetailAllBean
								.getProjectno());

						BetOrderDetailListAdapter mAdapter = new BetOrderDetailListAdapter(
								BetDetailActivity.this,
								mBetDetailAllBean.getmBetDetailBeans());
						bet_record_detail.setAdapter(mAdapter);

					}

				} else {
					ToastUtil.showCenterToast(BetDetailActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(BetDetailActivity.this,
						Consts.REQUEST_ERROR);
			}
			mLoadingDialog.dismiss();
		}
	};
	

}
