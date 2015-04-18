package com.xmtq.lottery.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.NewUserLoginBean;
import com.xmtq.lottery.bean.NewUserLoginResponse;
import com.xmtq.lottery.bean.UserBean;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.utils.Util;

/**
 * 注册
 * 
 * @author mwz123
 * 
 */
public class RegisterSecondActivity extends BaseActivity {

	private RequestMaker mRequestMaker;
	private ImageButton btn_back;
	private TextView send_verification;
	private TextView register_commit;
	private EditText mVeriCodeView;

	private CountDown mCountDown;
	private boolean isGetCoding = false;
	private UserBean userBean;
	private SharedPrefHelper spfs;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.register_second);

	}

	@Override
	public void dealLogicBeforeInitView() {
		spfs = SharedPrefHelper.getInstance(this);
		userBean = (UserBean) getIntent().getSerializableExtra("userBean");

		mRequestMaker = RequestMaker.getInstance();
	}

	@Override
	public void initView() {
		btn_back = (ImageButton) findViewById(R.id.back);
		register_commit = (TextView) findViewById(R.id.register_commit);
		send_verification = (TextView) findViewById(R.id.send_verification);
		mVeriCodeView = (EditText) findViewById(R.id.veri_code);
		btn_back.setOnClickListener(this);
		register_commit.setOnClickListener(this);
		send_verification.setOnClickListener(this);
	}

	@Override
	public void dealLogicAfterInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mCountDown != null) {
			mCountDown.cancel();
		}
		super.onDestroy();
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;

		// 注册提交
		case R.id.register_commit:
			commitRegister();
			break;

		// 发送验证码
		case R.id.send_verification:
			getVeriCode();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	public void getVeriCode() {
		if (isGetCoding) {
			return;
		}
		String phoneNum = "";
		if (userBean != null && userBean.getPhoneNum() != null) {

			phoneNum = userBean.getPhoneNum();
		}
		if (Util.isMobileNO(phoneNum)) {
			HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
			mAsyncTask.execute(mRequestMaker.getMessageVerification(phoneNum,
					Consts.PHONE_REGISTER_VERI));
			mAsyncTask.setOnCompleteListener(mOnCodeCompleteListener);
			ToastUtil.showCenterToast(this, "验证码已发送，请注意查收");
		} else {
			ToastUtil.showCenterToast(this, "请输入正确的手机号码");
			return;
		}
	}

	/**
	 * 获取验证码回调处理
	 */
	private OnCompleteListener<BaseResponse> mOnCodeCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					isGetCoding = true;
					mCountDown = new CountDown(60000, 1000);
					mCountDown.start();
				} else {
					ToastUtil.showCenterToast(RegisterSecondActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(RegisterSecondActivity.this,
						"获取验证码失败！");
			}
		}
	};

	/**
	 * 提交注册
	 */
	private void commitRegister() {
		// TODO Auto-generated method stub

		String veriCode = mVeriCodeView.getText().toString().trim();

		if (StringUtil.isNullOrEmpty(veriCode)) {
			ToastUtil.showCenterToast(this, "请获取验证码");
			return;
		}

		mLoadingDialog.show("提交数据中，请稍候");
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getUserRegister(
				userBean.getUsername(), "", userBean.getPassword(),
				userBean.getPhoneNum(), "1111111111", Consts.PHONE_REGISTER,
				veriCode));
		mAsyncTask.setOnCompleteListener(mOnRegisterCompleteListener);
	}

	/**
	 * 提交注册回调处理
	 */
	private OnCompleteListener<BaseResponse> mOnRegisterCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					isGetCoding = true;
					// ToastUtil.showCenterToast(RegisterSecondActivity.this,
					// "注册成功");

					if (mCountDown != null) {
						mCountDown.cancel();
					}

					requestLogin(userBean);

				} else {
					ToastUtil.showCenterToast(RegisterSecondActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(RegisterSecondActivity.this,
						Consts.REQUEST_ERROR);
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

	private void requestLogin(UserBean userBean) {
		mLoadingDialog.show("注册成功,正在自动登录...");

		String userName = userBean.getUsername();
		String password = userBean.getPassword();
		spfs.setIsRememberPwd(true);
		spfs.setUserPassward(password);
		spfs.setUserName(userName);

		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getUserLogin(userName,
				password));
		mAsyncTask.setOnCompleteListener(mOnLoginCompleteListener);
	}

	/**
	 * 用户登陆回调处理
	 */
	private OnCompleteListener<BaseResponse> mOnLoginCompleteListener = new OnCompleteListener<BaseResponse>() {
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
	 * 登陆成功
	 */
	private void onSuccess(BaseResponse result) {
		NewUserLoginResponse response = (NewUserLoginResponse) result;
		NewUserLoginBean newUserLoginBean = response.newUserLoginBean;

		// 保存用户登陆状态及信息
		spfs.setIsLogin(true);
		spfs.setUid(newUserLoginBean.getUid());

		ToastUtil.showCenterToast(this, "登录成功");

		// 自动登录，更新用户信息
		Intent intent = new Intent(Consts.ACTION_AUTO_LOGIN);
		intent.putExtra("newUserLoginBean", newUserLoginBean);
		sendBroadcast(intent);
		finish();
	}

	/**
	 * 登陆失败
	 * 
	 * @param msg
	 */
	private void onFailure(String msg) {
		ToastUtil.showCenterToast(this, msg);
	}

}
