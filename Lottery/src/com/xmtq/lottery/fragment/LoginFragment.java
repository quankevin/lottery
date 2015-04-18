package com.xmtq.lottery.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.xmtq.lottery.Consts;
import com.xmtq.lottery.R;
import com.xmtq.lottery.activity.FindPasswordActivity;
import com.xmtq.lottery.activity.RegisterActivity;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.NewUserLoginBean;
import com.xmtq.lottery.bean.NewUserLoginResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 登录
 * 
 * @author mwz123
 * 
 */
public class LoginFragment extends BaseFragment {

	private TextView find_password;
	private TextView register;
	private TextView login;
	private SharedPrefHelper spfs;
	private CheckBox remember_passwod;
	private EditText user_name;
	private EditText user_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login, container, false);
		dealLogicBeforeInitView();
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (spfs.getIsRememberPwd()
				&& !TextUtils.isEmpty(spfs.getUserName())
				&& !TextUtils.isEmpty(spfs.getUserPassward())) {
			login(false);
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void dealLogicBeforeInitView() {
		spfs = SharedPrefHelper.getInstance(getActivity());
	}

	public void initView(View v) {
		find_password = (TextView) v.findViewById(R.id.find_password);
		register = (TextView) v.findViewById(R.id.register);
		login = (TextView) v.findViewById(R.id.login);
		remember_passwod = (CheckBox) v.findViewById(R.id.remember_passwod);
		user_name = (EditText) v.findViewById(R.id.user_name);
		user_password = (EditText) v.findViewById(R.id.password);

		find_password.setOnClickListener(this);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
		remember_passwod.setOnCheckedChangeListener(mOnCheckedChangeListener);
		if (spfs.getIsRememberPwd()) {
			user_name.setText(spfs.getUserName());
			user_password.setText(spfs.getUserPassward());
			remember_passwod.setChecked(true);
		}
	}

	@Override
	public void onClickEvent(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.find_password:
			intent = new Intent(getActivity(), FindPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.register:
			intent = new Intent(getActivity(), RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.login:
			login(true);
			break;
		default:
			break;
		}
	}

	/**
	 * 用户登陆
	 * 
	 * @param isShowDialog
	 *            是否显示登录dialog
	 */
	private void login(boolean isShowDialog) {
		String userName = user_name.getText().toString().trim();
		String password = user_password.getText().toString().trim();

		if (StringUtil.isNullOrEmpty(userName)) {
			ToastUtil.showCenterToast(getActivity(), "请输入用户名");
			return;
		}

		if (StringUtil.isNullOrEmpty(password)) {
			ToastUtil.showCenterToast(getActivity(), "请输入密码");
			return;
		} else if (!StringUtil.matchPwd(password)) {
			// ToastUtil.showCenterToast(getActivity(),
			// "请输入6-16位密码,密码必须包含数字和字母");
			// return;
		}

		if (isShowDialog) {
			mLoadingDialog.show("登录中，请稍候...");
		}

		if (spfs.getIsRememberPwd()) {
			spfs.setUserPassward(password);
			spfs.setUserName(userName);
		}
		hideImm(user_name);
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

		// 登陆成功，跳转另一个页面
		UserInfoFragment fragment = new UserInfoFragment();
		Bundle b = new Bundle();
		b.putSerializable("newUserLoginBean", newUserLoginBean);
		fragment.setArguments(b);
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, fragment).commit();
	}

	/**
	 * 登陆失败
	 * 
	 * @param msg
	 */
	private void onFailure(String msg) {
		ToastUtil.showCenterToast(getActivity(), msg);
	}

	/**
	 * 是否记住密码
	 */
	private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if (arg1) {
				spfs.setIsRememberPwd(true);
			} else {
				spfs.setIsRememberPwd(false);
				spfs.cleanUserName();
				spfs.cleanUserPassward();
			}
		}
	};

	public void hideImm(View v) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
		
	}
}
