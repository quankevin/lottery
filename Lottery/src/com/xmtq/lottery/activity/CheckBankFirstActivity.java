package com.xmtq.lottery.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.CreateOrderBean;
import com.xmtq.lottery.bean.PhoneMessageDepositResponse;
import com.xmtq.lottery.bean.PhoneMessageResponse;
import com.xmtq.lottery.bean.PhonePayDepositFirstResponse;
import com.xmtq.lottery.bean.PhonePayFirstResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 选择银行卡或者信用卡
 * 
 * @author Administrator
 * 
 */
public class CheckBankFirstActivity extends BaseActivity {

	private ImageButton btn_back;
	private ImageView iv_checkBank;
	private CreateOrderBean mCreateOrderBean;
	private LinearLayout ll_bank_c;
	private String requestOrderId;
	private String bankCode;
	private EditText et_card_name;
	private EditText et_id_card;
	private TextView et_bank_name;
	private EditText et_bank_account;
	private EditText et_valid_date;
	private EditText et_cvn_code;
	private EditText et_mobile_phone;
	private EditText veri_code;
	private TextView commit;
	private TextView send_verification;
	/**
	 * 是否是信用卡
	 */
	private boolean isCreditCard = false;
	private String bankCardType = "0";
	private String mobilePhone;
	private String randomCode;
	private String bank_name;
	private String idNumber;
	private String name;

	private String valid_date = "";
	private String cvn_code = "";
	private String bankAccount;
	private String userid;
	private CountDown mCountDown;
	/**
	 * 动态校验码编号
	 */
	private String randomValidateId;
	/**
	 * 交易流水号
	 */
	private String tradeId;
	/**
	 * 是否请求短信验证码
	 */
	private boolean isGetMessage = false;

	private boolean isGetCoding = false;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.check_bankcard);

	}

	@Override
	public void dealLogicBeforeInitView() {
		mCreateOrderBean = (CreateOrderBean) getIntent().getSerializableExtra(
				"mCreateOrderBean");
		requestOrderId = getIntent().getStringExtra("requestId");

		userid = SharedPrefHelper.getInstance(getApplicationContext()).getUid();

	}

	@Override
	public void initView() {
		ll_bank_c = (LinearLayout) findViewById(R.id.ll_bank_c);

		iv_checkBank = (ImageView) findViewById(R.id.img_checkbank);

		et_card_name = (EditText) findViewById(R.id.et_card_name);
		et_id_card = (EditText) findViewById(R.id.et_id_card);
		et_bank_name = (TextView) findViewById(R.id.et_bank_name);
		et_bank_account = (EditText) findViewById(R.id.et_bank_account);
		et_valid_date = (EditText) findViewById(R.id.et_valid_date);
		et_cvn_code = (EditText) findViewById(R.id.et_cvn_code);
		et_mobile_phone = (EditText) findViewById(R.id.et_mobile_phone);
		veri_code = (EditText) findViewById(R.id.veri_code);
		commit = (TextView) findViewById(R.id.commit);

		send_verification = (TextView) findViewById(R.id.send_verification);
		commit.setOnClickListener(this);
		send_verification.setOnClickListener(this);
		iv_checkBank.setOnClickListener(this);
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
		case R.id.img_checkbank:
			Intent intent = new Intent(CheckBankFirstActivity.this,
					CheckBankActivity.class);
			intent.putExtra("mCreateOrderBean", mCreateOrderBean);
			startActivityForResult(intent, 0xffff);
			break;

		case R.id.send_verification:
			isGetMessage = true;
			request(true);
			break;
		case R.id.commit:
			isGetMessage = false;
			request(false);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1 * 1000) {
			// mType = "Y";
			// sendMessage();
			ll_bank_c.setVisibility(View.VISIBLE);
			isCreditCard = true;
			bankCardType = "1";
		} else {
			ll_bank_c.setVisibility(View.GONE);
			isCreditCard = false;
			bankCardType = "0";
		}
		if (data != null) {
			String bankName = "";
			bankName = data.getStringExtra("bankName");
			bankCode = data.getStringExtra("bankCode");
			et_bank_name.setText(bankName);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 发送请求
	 * 
	 * @param isGetMessage
	 */
	private void request(boolean isGetMessage) {
		name = et_card_name.getText().toString();
		if (StringUtil.isNullOrEmpty(name)) {
			ToastUtil.showCenterToast(this, "请输入持卡人姓名");
			return;
		}
		idNumber = et_id_card.getText().toString();
		if (StringUtil.isNullOrEmpty(idNumber)) {
			ToastUtil.showCenterToast(this, "请输入身份证号");
			return;
		}

		bank_name = et_bank_name.getText().toString();
		if (StringUtil.isNullOrEmpty(bank_name)) {
			ToastUtil.showCenterToast(this, "请选择银行");
			return;
		}

		bankAccount = et_bank_account.getText().toString();
		if (StringUtil.isNullOrEmpty(bankAccount)) {
			ToastUtil.showCenterToast(this, "请输入银行卡号");
			return;
		}

		if (isCreditCard) {
			valid_date = et_valid_date.getText().toString();
			if (StringUtil.isNullOrEmpty(valid_date)) {
				ToastUtil.showCenterToast(this, "请输入信用卡有效期");
				return;
			}

			cvn_code = et_cvn_code.getText().toString();
			if (StringUtil.isNullOrEmpty(cvn_code)) {
				ToastUtil.showCenterToast(this, "请输入信用卡CVN码");
				return;
			}
		}

		mobilePhone = et_mobile_phone.getText().toString();
		if (StringUtil.isNullOrEmpty(mobilePhone)) {
			ToastUtil.showCenterToast(this, "请输入预留手机号");
			return;
		}
		// 是信用卡
		if (isCreditCard) {
			if (isGetMessage) {

				requestCreditVerification();
			} else {

				creditCardRequest();
			}
		}
		// 非信用卡
		else {
			if (isGetMessage) {
				requestDepositVerification();
			} else {
				depositCardRequest();
			}
		}

	}

	/**
	 * 储蓄卡充值 请求
	 */
	private void depositCardRequest() {

		randomCode = veri_code.getText().toString();
		if (StringUtil.isNullOrEmpty(randomCode)) {
			ToastUtil.showCenterToast(this, "请输入验证码");
			return;
		}
		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		Log.d("xm", "储蓄卡 请求 depositCardRequest()");
		if (requestOrderId != null && bankCode != null) {
			mAsyncTask.execute(mRequestMaker.getFengPayDepositFirst(
					requestOrderId, bankCode, bankAccount, bankCardType, "0",
					idNumber, name, mobilePhone, "1", userid, randomValidateId,
					randomCode, tradeId));
		}
		mAsyncTask.setOnCompleteListener(mDepositOnCompleteListener);
	}

	/**
	 * 储蓄卡充值请求返回实例
	 */
	private OnCompleteListener<PhonePayDepositFirstResponse> mDepositOnCompleteListener = new OnCompleteListener<PhonePayDepositFirstResponse>() {

		@Override
		public void onComplete(PhonePayDepositFirstResponse result,
				String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					ToastUtil.showCenterToast(CheckBankFirstActivity.this,
							"充值成功");

				} else {
					ToastUtil.showCenterToast(CheckBankFirstActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(CheckBankFirstActivity.this, "请求失败");
			}

			mLoadingDialog.dismiss();
		}
	};

	/**
	 * 信用卡 请求
	 */
	private void creditCardRequest() {
		randomCode = veri_code.getText().toString();
		if (StringUtil.isNullOrEmpty(randomCode)) {
			ToastUtil.showCenterToast(this, "请先获取验证码");
			return;
		}
		Log.d("xm", "信用卡 creditCardRequest()");

		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		if (requestOrderId != null && bankCode != null) {
			mAsyncTask.execute(mRequestMaker.getFengPayFirst(requestOrderId,
					bankCode, bankAccount, bankCardType, valid_date, cvn_code,
					"0", idNumber, name, mobilePhone, "1", userid,
					randomValidateId, randomCode, tradeId));
		}
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 信用卡充值请求返回实例
	 */
	private OnCompleteListener<PhonePayFirstResponse> mOnCompleteListener = new OnCompleteListener<PhonePayFirstResponse>() {

		@Override
		public void onComplete(PhonePayFirstResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					PhonePayFirstResponse mPayFirstResponse = (PhonePayFirstResponse) result;
					ToastUtil.showCenterToast(CheckBankFirstActivity.this,
							"充值成功");
					
					String money = mPayFirstResponse.money;
					Intent intent = new Intent(CheckBankFirstActivity.this,
							QuickPaymentSuccessActivity.class);
					intent.putExtra("money", money);
					startActivity(intent);
					finish();

				} else {
					ToastUtil.showCenterToast(CheckBankFirstActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(CheckBankFirstActivity.this, "请求失败");
			}

			mLoadingDialog.dismiss();
		}
	};

	/**
	 * 信用卡短信验证码接口
	 */
	private void requestCreditVerification() {
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		if (requestOrderId != null && bankCode != null) {
			mAsyncTask.execute(mRequestMaker.getFengMessagePayFirst(
					requestOrderId, bankCode, bankAccount, valid_date,
					bankCardType, cvn_code, "0", idNumber, name, mobilePhone,
					userid));
		}
		mAsyncTask.setOnCompleteListener(mCreditMessageOnCompleteListener);
	}

	/**
	 * 信用卡短信验证码请求返回实例
	 */
	private OnCompleteListener<PhoneMessageResponse> mCreditMessageOnCompleteListener = new OnCompleteListener<PhoneMessageResponse>() {

		@Override
		public void onComplete(PhoneMessageResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					isGetCoding = true;
					mCountDown = new CountDown(60000, 1000);
					mCountDown.start();

					PhoneMessageResponse mMessageResponse = (PhoneMessageResponse) result;
					randomValidateId = mMessageResponse.randomValidateId;
					tradeId = mMessageResponse.tradeId;

					Log.d("xm", "tradeId" + tradeId + "++++"
							+ "randomValidateId" + randomValidateId);
				} else {
					if (result.errormsg.equals("9999")) {
						ToastUtil.showCenterToast(CheckBankFirstActivity.this,
								"系统异常");
					} else {
						ToastUtil.showCenterToast(CheckBankFirstActivity.this,
								result.errormsg);
					}
				}
			} else {
				ToastUtil.showCenterToast(CheckBankFirstActivity.this, "请求失败");
			}
		}
	};

	/**
	 * 储蓄卡短信验证码接口
	 */
	private void requestDepositVerification() {
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		if (requestOrderId != null && bankCode != null) {
			mAsyncTask.execute(mRequestMaker.getFengMessagePayDepositFirst(
					requestOrderId, bankCode, bankAccount, bankCardType, "0",
					idNumber, name, mobilePhone, userid));
			mAsyncTask.setOnCompleteListener(mDepositMessageOnCompleteListener);
		}
	}

	/**
	 * 储蓄卡短信验证码请求返回实例
	 */
	private OnCompleteListener<PhoneMessageDepositResponse> mDepositMessageOnCompleteListener = new OnCompleteListener<PhoneMessageDepositResponse>() {

		@Override
		public void onComplete(PhoneMessageDepositResponse result,
				String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					isGetCoding = true;
					mCountDown = new CountDown(60000, 1000);
					mCountDown.start();
					PhoneMessageDepositResponse mMessageResponse = (PhoneMessageDepositResponse) result;
					randomValidateId = mMessageResponse.randomValidateId;
					tradeId = mMessageResponse.tradeId;

				} else {
					ToastUtil.showCenterToast(CheckBankFirstActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(CheckBankFirstActivity.this, "请求失败");
			}
		}
	};

	/**
	 * 获取验证码时间计时
	 */
	private class CountDown extends CountDownTimer {

		public CountDown(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			send_verification.setText("重获验证码("
					+ StringUtil.getTime(millisUntilFinished) + ")"); // 将剩余的时间
			send_verification.setBackgroundResource(R.color.black);
			send_verification.setClickable(false);
			// 显示出来
		}

		@Override
		public void onFinish() {
			send_verification.setText("发送验证码");
			send_verification.setClickable(true);
			send_verification.setBackgroundResource(R.color.green);
			isGetCoding = false;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mCountDown != null) {
			mCountDown.cancel();
		}
		super.onDestroy();
	}
}
