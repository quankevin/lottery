package com.xmtq.lottery.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.PhoneMessageNotFirstResponse;
import com.xmtq.lottery.bean.PhonePayNotFirstResponse;
import com.xmtq.lottery.bean.UserBankBean;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 快捷支付
 * 
 * @author mwz123
 * 
 */
public class QuickPaymentActivity extends BaseActivity {

	private ImageButton btn_back;
	private TextView quick_pay_money;
	private UserBankBean mUserBankBean;
	private String requestOrderId;
	private String userid;
	private TextView send_verification;
	private EditText quickpay_verification;
	private String randomValidateId;
	private String tradeId;
	private String rechargeMoney;
	private TextView quick_pay_bank;
	private TextView recharge_money;

	private boolean isGetCoding = false;
	private CountDown mCountDown;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.quick_payment);

	}

	@Override
	public void dealLogicBeforeInitView() {
		// TODO Auto-generated method stub

		mUserBankBean = (UserBankBean) getIntent().getSerializableExtra(
				"selectedBankBean");
		requestOrderId = getIntent().getStringExtra("requestId");

		rechargeMoney = getIntent().getStringExtra("rechargeMoney");

		userid = SharedPrefHelper.getInstance(getApplicationContext()).getUid();

	}

	@Override
	public void initView() {

		quick_pay_bank = (TextView) findViewById(R.id.quick_payment_bank);
		recharge_money = (TextView) findViewById(R.id.recharge_money);
		if (mUserBankBean != null) {
			String bankCount = mUserBankBean.getBankAccount().substring(
					mUserBankBean.getBankAccount().length() - 4,
					mUserBankBean.getBankAccount().length());

			quick_pay_bank.setText("使用尾号为  " + bankCount + " 的"
					+ checkBankType(mUserBankBean));
			recharge_money.setText("充值 " + rechargeMoney + " 元");
		}

		send_verification = (TextView) findViewById(R.id.send_verification);

		send_verification.setOnClickListener(this);
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		quick_pay_money = (TextView) findViewById(R.id.quick_pay_money);
		quick_pay_money.setOnClickListener(this);
		quickpay_verification = (EditText) findViewById(R.id.quickpay_verification);
	}

	private String checkBankType(UserBankBean mUserBankBean) {
		String bankType = "";
		if (mUserBankBean.getBankCardTypeUsed().equals("0")) {
			bankType = "储蓄卡";
		} else if (mUserBankBean.getBankCardTypeUsed().equals("1")) {
			bankType = "信用卡";
		}
		return bankType;
	}

	@Override
	public void dealLogicAfterInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.quick_pay_money:
			requestNotFirstPay();

			break;

		case R.id.send_verification:
			requestVerification();
			break;
		default:
			break;
		}
	}

	/**
	 * 短信验证码接口
	 */
	private void requestVerification() {
		if (isGetCoding) {
			return;
		}
		if (mUserBankBean != null) {
			String bankCode = mUserBankBean.getBankCodeUsed();
			String bindId = mUserBankBean.getBindId();
			Log.d("xm", "bankCode" + bankCode + "bindId" + bindId);
			mLoadingDialog.show("数据记载中...");
			RequestMaker mRequestMaker = RequestMaker.getInstance();
			HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
			mAsyncTask.execute(mRequestMaker.getFengMessagePayNotFirst(
					requestOrderId, bankCode, bindId, userid));
			mAsyncTask.setOnCompleteListener(mMessageOnCompleteListener);
		}

	}

	/**
	 * 信用卡短信验证码请求返回实例
	 */
	private OnCompleteListener<PhoneMessageNotFirstResponse> mMessageOnCompleteListener = new OnCompleteListener<PhoneMessageNotFirstResponse>() {

		@Override
		public void onComplete(PhoneMessageNotFirstResponse result,
				String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					isGetCoding = true;
					mCountDown = new CountDown(60000, 1000);
					mCountDown.start();

					PhoneMessageNotFirstResponse mMessageResponse = (PhoneMessageNotFirstResponse) result;
					randomValidateId = mMessageResponse.randomValidateId;
					tradeId = mMessageResponse.tradeId;

					Log.d("xm", "tradeId" + tradeId + "++++"
							+ "randomValidateId" + randomValidateId);
				} else {
					ToastUtil.showCenterToast(QuickPaymentActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(QuickPaymentActivity.this, "请求失败");
			}

			mLoadingDialog.dismiss();
		}
	};

	/**
	 * 非首次快捷支付
	 */

	private void requestNotFirstPay() {
		String randomCode = quickpay_verification.getText().toString();
		if (TextUtils.isEmpty(randomCode)) {
			ToastUtil.showCenterToast(QuickPaymentActivity.this, "请先输入验证码");
			return;
		}
		if (mUserBankBean != null) {
			String bankCode = mUserBankBean.getBankCodeUsed();
			String bindId = mUserBankBean.getBindId();
			Log.d("xm", "bankCode" + bankCode + "bindId" + bindId);

			mLoadingDialog.show("数据加载中...");
			RequestMaker mRequestMaker = RequestMaker.getInstance();
			HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
			mAsyncTask.execute(mRequestMaker.getFengPayNotFirst(requestOrderId,
					bankCode, bindId, userid, randomValidateId, randomCode,
					tradeId));
			mAsyncTask.setOnCompleteListener(mPhonePayNotfistListener);
		}
	}

	private OnCompleteListener<PhonePayNotFirstResponse> mPhonePayNotfistListener = new OnCompleteListener<PhonePayNotFirstResponse>() {

		@Override
		public void onComplete(PhonePayNotFirstResponse result,
				String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {

					PhonePayNotFirstResponse mPayResponse = (PhonePayNotFirstResponse) result;

					String money = mPayResponse.money;
					Log.d("xm", "money" + money + "++++");

					Intent intent = new Intent(QuickPaymentActivity.this,
							QuickPaymentSuccessActivity.class);
					intent.putExtra("money", money);
					startActivity(intent);
					finish();
				} else {
					ToastUtil.showCenterToast(QuickPaymentActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(QuickPaymentActivity.this, "请求失败");
			}

			mLoadingDialog.dismiss();
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
			send_verification.setBackgroundResource(R.drawable.btn_gray_normal);
			send_verification.setClickable(false);
			// 显示出来
		}

		@Override
		public void onFinish() {
			send_verification.setText("发送验证码");
			send_verification.setClickable(true);
			send_verification.setBackgroundResource(R.drawable.btn_pressed9);
			isGetCoding = false;
		}
	}

}
