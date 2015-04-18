package com.xmtq.lottery.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.WelCheckDialog;
import com.xmtq.lottery.activity.AccountDetailActivity;
import com.xmtq.lottery.activity.BetRecordActivity;
import com.xmtq.lottery.activity.ExtractMoneyActivity;
import com.xmtq.lottery.activity.ModifiPasswordActivity;
import com.xmtq.lottery.activity.PersonDataActivity;
import com.xmtq.lottery.activity.RechargeMoneyActivity;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.NewUserLoginBean;
import com.xmtq.lottery.bean.UserInfoBean;
import com.xmtq.lottery.bean.UserInfoResponse;
import com.xmtq.lottery.bean.VersionBean;
import com.xmtq.lottery.bean.VersionResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.utils.VersionUtil;

/**
 * 个人中心
 * 
 * @author mwz123
 * 
 */
public class UserInfoFragment extends BaseFragment {
	private RelativeLayout rl_repassword, rl_bet_record, rl_userinfo;
	private RelativeLayout account_information;
	private TextView tv_esc_login, user_name, account_balance;

	private UserInfoBean userInfoBean;
	private boolean isAddUserInfo = false;
	private boolean isAddBankInfo = false;
	private RelativeLayout check_version;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.userinfo, container, false);
		initView(view);
		initData();
		requestUserData();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().registerReceiver(mBroadcastReceiver,
				new IntentFilter(Consts.ACTION_REFRESH_USERINFO));
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	public void initView(View v) {
		rl_repassword = (RelativeLayout) v.findViewById(R.id.rl_repassword);
		rl_bet_record = (RelativeLayout) v.findViewById(R.id.rl_bet_record);
		rl_userinfo = (RelativeLayout) v.findViewById(R.id.rl_userinfo);
		check_version = (RelativeLayout) v.findViewById(R.id.check_version);
		tv_esc_login = (TextView) v.findViewById(R.id.exit_loading);
		tv_esc_login.setOnClickListener(this);
		account_information = (RelativeLayout) v
				.findViewById(R.id.account_information);
		// 充值
		TextView recharge_money = (TextView) v.findViewById(R.id.recharge);
		// 提现
		TextView extract_money = (TextView) v.findViewById(R.id.extract_money);
		user_name = (TextView) v.findViewById(R.id.user_name);
		account_balance = (TextView) v.findViewById(R.id.account_balance);

		rl_bet_record.setOnClickListener(this);
		rl_repassword.setOnClickListener(this);
		rl_userinfo.setOnClickListener(this);
		account_information.setOnClickListener(this);
		check_version.setOnClickListener(this);
		recharge_money.setOnClickListener(this);
		extract_money.setOnClickListener(this);
	}

	/**
	 * 从其他Fragment传送过来的用户数据
	 */
	private void initData() {
		try {
			NewUserLoginBean newUserLoginBean = (NewUserLoginBean) getArguments()
					.getSerializable("newUserLoginBean");
			updateUserInfo(newUserLoginBean);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 更新用户信息
	 * 
	 * @param newUserLoginBean
	 */
	private void updateUserInfo(NewUserLoginBean newUserLoginBean) {
		if (newUserLoginBean != null) {
			user_name.setText(newUserLoginBean.getUsername());
			account_balance.setText(newUserLoginBean.getMoney());

			SharedPrefHelper.getInstance(getActivity()).setUserName(
					newUserLoginBean.getUsername());
			SharedPrefHelper.getInstance(getActivity()).setAccountBalance(
					newUserLoginBean.getMoney());
		}
	}

	/**
	 * 获取用户信息
	 */
	private void requestUserData() {
		String uid = SharedPrefHelper.getInstance(getActivity()).getUid();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getUserInfo(uid));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 获取用户信息
	 */
	private OnCompleteListener<BaseResponse> mOnCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					onSuccess(result);
				} else {
					ToastUtil.showCenterToast(getActivity(), result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(getActivity(), Consts.REQUEST_ERROR);
			}
		}
	};

	private void onSuccess(BaseResponse result) {
		UserInfoResponse response = (UserInfoResponse) result;
		userInfoBean = response.userInfoBean;
		checkUserInfo(userInfoBean);
		checkBankInfo(userInfoBean);
		// SharedPrefHelper.getInstance(getActivity()).setRealName(userInfoBean.getRealname());
		// SharedPrefHelper.getInstance(getActivity()).setCardId(userInfoBean.getCardid());
	}

	@Override
	public void onClickEvent(View v) {
		Intent intent;
		switch (v.getId()) {
		// 投注记录
		case R.id.rl_bet_record:
			intent = new Intent(getActivity(), BetRecordActivity.class);
			startActivity(intent);

			// // 测试dialog
			// BalanceNotEnoughDialog dialog = new BalanceNotEnoughDialog(
			// getActivity(), new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			// dialog.show();

			break;
		// 个人资料
		case R.id.rl_userinfo:
			intent = new Intent(getActivity(), PersonDataActivity.class);
			intent.putExtra("userInfoBean", userInfoBean);
			startActivity(intent);

			break;
		// 账户明细
		case R.id.account_information:
			intent = new Intent(getActivity(), AccountDetailActivity.class);
			startActivity(intent);

			break;
		// 修改密码
		case R.id.rl_repassword:
			intent = new Intent(getActivity(), ModifiPasswordActivity.class);
			startActivity(intent);

			break;
		case R.id.check_version:
			requestVersion();
			break;

		// 充值
		case R.id.recharge:
			intent = new Intent(getActivity(), RechargeMoneyActivity.class);
			startActivity(intent);

			break;
		// 提现
		case R.id.extract_money:
			if (isAddBankInfo && isAddUserInfo) {
				intent = new Intent(getActivity(), ExtractMoneyActivity.class);
				intent.putExtra("userInfoBean", userInfoBean);
				intent.putExtra("accountbalance", account_balance.getText());
				startActivity(intent);
			} else {
				ToastUtil.showCenterToast(getActivity(), "请完善个人信息");
				intent = new Intent(getActivity(), PersonDataActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.exit_loading:

			LoginFragment fragment = new LoginFragment();
			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_frame, fragment).commit();

			SharedPrefHelper spf = SharedPrefHelper.getInstance(getActivity());
			spf.setIsLogin(false);
			spf.setIsRememberPwd(false);
			spf.cleanUserName();
			spf.cleanPassword();
			break;

		default:
			break;
		}
	}

	/**
	 * 判断是否添加用户信息
	 */
	private void checkUserInfo(UserInfoBean userInfoBean) {
		String realName = userInfoBean.getRealname();
		String cardId = userInfoBean.getCardid();

		if (isValidValue(realName) && isValidValue(cardId)) {
			isAddUserInfo = true;
		}
	}

	/**
	 * 判断是否添加银行卡信息
	 */
	private void checkBankInfo(UserInfoBean userInfoBean) {
		String bankName = userInfoBean.getBankname();
		String bankAcount = userInfoBean.getBankaccount();
		String bankAddress = userInfoBean.getBankaddress();
		// String acount = userInfoBean.getAccount(); // 帐户余额

		if (isValidValue(bankName) && isValidValue(bankAcount)
				&& isValidValue(bankAddress)) {
			isAddBankInfo = true;
		}
	}

	public boolean isValidValue(String vaule) {
		if (TextUtils.isEmpty(vaule) || vaule.equals("*")) {
			return false;
		}
		return true;
	}

	private void requestVersion() {
		mLoadingDialog.show("版本检查中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getVersion(VersionUtil
				.getVersionName(getActivity())));
		mAsyncTask.setOnCompleteListener(mVersionCompleteListener);
	}

	private String update = "0";
	private String message = "";
	private OnCompleteListener<VersionResponse> mVersionCompleteListener = new OnCompleteListener<VersionResponse>() {

		@Override
		public void onComplete(VersionResponse result, String resultString) {
			if (result != null) {
				if (result.errorcode.equals("1")) {
					VersionResponse mResponse = result;
					VersionBean mBean = mResponse.versionBean;
					String newVersion = mBean.getVersion();

					final String appPath = mBean.getDowload();
					update = mBean.getUpdate();
					message = mBean.getMessage();
					int oldVersion = VersionUtil.getVersionCode(getActivity());
					// if (Integer.parseInt(newVersion.replace(".", "")) >
					// oldVersion) {
					getActivity().runOnUiThread(new Runnable() {

						public void run() {
							WelCheckDialog dialog = new WelCheckDialog(
									getActivity(), message, appPath, null,
									keylistener, update);
							dialog.show();

						}
					});
					// } else {
					// ToastUtil.showCenterToast(getActivity(), "当前已是最新版本");
					// }

				} else {
					ToastUtil.showCenterToast(getActivity(), result.errormsg);
				}

			} else {
				ToastUtil.showCenterToast(getActivity(), "数据请求失败");
			}

			mLoadingDialog.dismiss();

		}
	};

	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
					&& update.equals("1")) {
				ToastUtil.showCenterToast(getActivity(), "升级后才可以正常使用");
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 更新用户信息广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			NewUserLoginBean newUserLoginBean = (NewUserLoginBean) intent
					.getSerializableExtra("newUserLoginBean");
			updateUserInfo(newUserLoginBean);
		}

	};
}
