package com.xmtq.lottery.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.NewUserLoginBean;
import com.xmtq.lottery.bean.NewUserLoginResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 提现成功
 * 
 * @author mwz123
 * 
 */
public class ExtractMoneySuccessActivity extends BaseActivity {

	private ImageButton btn_back;
	private TextView extract_money_done;
	private TextView extract_money;
	private String drawalmoney;
	private SharedPrefHelper spfs;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.extract_money_success);

	}

	@Override
	public void dealLogicBeforeInitView() {
		drawalmoney = getIntent().getStringExtra("drawalmoney");
		spfs = SharedPrefHelper.getInstance(this);
	}

	@Override
	public void initView() {
		extract_money = (TextView) findViewById(R.id.extract_money);
		if (!TextUtils.isEmpty(drawalmoney)) {
			extract_money.setText(drawalmoney);
		}
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		extract_money_done = (TextView) findViewById(R.id.extract_money_done);
		extract_money_done.setOnClickListener(this);
	}

	@Override
	public void dealLogicAfterInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			requestLogin();
			break;
		case R.id.extract_money_done:
			requestLogin();
			break;

		default:
			break;
		}
	}

	private void requestLogin() {
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getUserLogin(
				spfs.getUserName(), spfs.getUserPassward()));
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
			finish();
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			requestLogin();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 登陆成功
	 */
	private void onSuccess(BaseResponse result) {
		NewUserLoginResponse response = (NewUserLoginResponse) result;
		NewUserLoginBean newUserLoginBean = response.newUserLoginBean;

		// 保存用户登陆状态及信息
		spfs.setIsLogin(true);
		spfs.setUid(newUserLoginBean.getUid());

		// 更新用户账户余额
		Intent intent = new Intent(Consts.ACTION_REFRESH_USERINFO);
		intent.putExtra("newUserLoginBean", newUserLoginBean);
		sendBroadcast(intent);
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
