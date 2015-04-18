package com.xmtq.lottery.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.ExtractCashResponse;
import com.xmtq.lottery.bean.UserInfoBean;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 提现
 * 
 * @author Administrator
 * 
 */
public class ExtractMoneyActivity extends BaseActivity {

	private ImageButton btn_back;
	// private ImageView img_checkbank;
	private TextView ectract_money_commit;
	private TextView tv_bank_name;
	private TextView bank_card_tail_num;
	private UserInfoBean userInfoBean;
	private TextView tv_balance;
	private EditText edit_extract_money;
	private String drawalmoney;
	private LinearLayout extract_checkbank;
	private String accountbalance;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.extract_money);

	}

	@Override
	public void dealLogicBeforeInitView() {
		userInfoBean = (UserInfoBean) getIntent().getSerializableExtra(
				"userInfoBean");
		accountbalance = getIntent().getStringExtra("accountbalance");

		if (TextUtils.isEmpty(userInfoBean.getBankaccount())) {

			ToastUtil.showCenterToast(ExtractMoneyActivity.this, "请先完善个人信息");
		}
	}

	@Override
	public void initView() {

		extract_checkbank = (LinearLayout) findViewById(R.id.extract_checkbank);
		if (userInfoBean != null) {
			extract_checkbank.setVisibility(View.VISIBLE);
		}
		edit_extract_money = (EditText) findViewById(R.id.edit_extract_money);
		tv_bank_name = (TextView) findViewById(R.id.bank_name);
		bank_card_tail_num = (TextView) findViewById(R.id.bank_card_tail_num);
		tv_balance = (TextView) findViewById(R.id.balance);
		String str = userInfoBean.getBankaccount();
		str = str.substring(str.length() - 4, str.length());

		tv_bank_name.setText(userInfoBean.getBankname());
		bank_card_tail_num.setText("尾号" + str);
		tv_balance.setText(accountbalance);

		// img_checkbank = (ImageView) findViewById(R.id.img_checkbank);
		ectract_money_commit = (TextView) findViewById(R.id.ectract_money_commit);
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		// img_checkbank.setOnClickListener(this);
		ectract_money_commit.setOnClickListener(this);
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
		// case R.id.img_checkbank:
		// intent = new Intent(ExtractMoneyActivity.this,
		// CheckBankActivity.class);
		// startActivity(intent);
		// break;
		case R.id.ectract_money_commit:

			request();

			break;
		default:
			break;
		}

	}

	private void request() {
		drawalmoney = edit_extract_money.getText().toString().trim();
		if (TextUtils.isEmpty(drawalmoney)) {
			ToastUtil.showCenterToast(ExtractMoneyActivity.this, "请输入金额");
			return;
		}

		if (Double.parseDouble(drawalmoney) > Double.parseDouble(userInfoBean
				.getAccount())) {
			ToastUtil.showCenterToast(ExtractMoneyActivity.this, "余额不足");
			return;
		}

		String userid = SharedPrefHelper.getInstance(getApplicationContext())
				.getUid();

		String password = SharedPrefHelper.getInstance(getApplicationContext())
				.getUserPassward();
		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getExtractCash(userid, password,
				drawalmoney + "00"));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);

	}

	private OnCompleteListener<ExtractCashResponse> mOnCompleteListener = new OnCompleteListener<ExtractCashResponse>() {

		@Override
		public void onComplete(ExtractCashResponse result, String resultString) {

			if (result != null) {
				if (result.errorcode.equals("0")) {
					// ExtractCashResponse mResponse = result;
					ToastUtil
							.showCenterToast(ExtractMoneyActivity.this, "提现成功");

					Intent intent = new Intent(ExtractMoneyActivity.this,
							ExtractMoneySuccessActivity.class);
					intent.putExtra("drawalmoney", drawalmoney);
					startActivity(intent);
					finish();
				} else {
					ToastUtil.showCenterToast(ExtractMoneyActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(ExtractMoneyActivity.this,
						Consts.REQUEST_ERROR);
			}

			mLoadingDialog.dismiss();
		}
	};

}
