package com.xmtq.lottery.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 修改密码
 *
 */
public class ModifiPasswordActivity extends BaseActivity {
	private ImageButton btn_back;
	private TextView repassword_commit;
	private EditText old_password;
	private EditText new_password;
	private EditText confirm_password;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.repassword);
	}

	@Override
	public void dealLogicBeforeInitView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initView() {
		btn_back = (ImageButton) findViewById(R.id.back);
		repassword_commit = (TextView) findViewById(R.id.repassword_commit);
		old_password = (EditText) findViewById(R.id.old_password);
		new_password = (EditText) findViewById(R.id.new_password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);

		btn_back.setOnClickListener(this);
		repassword_commit.setOnClickListener(this);
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
		case R.id.repassword_commit:
			commit();
			break;
		default:
			break;
		}

	}

	/**
	 * 修改密码
	 */
	private void commit() {
		// TODO Auto-generated method stub
		String uid = SharedPrefHelper.getInstance(this).getUid();
		String oldPassword = old_password.getText().toString().trim();
		String newPassword = new_password.getText().toString().trim();
		String confirmPassword = confirm_password.getText().toString().trim();

		if (StringUtil.isNullOrEmpty(oldPassword)) {
			ToastUtil.showCenterToast(this, "请输入原密码");
			return;
		}

		if (StringUtil.isNullOrEmpty(newPassword)) {
			ToastUtil.showCenterToast(this, "请输入新密码");
			return;
		} else if (!StringUtil.matchPwd(newPassword)) {
			// ToastUtil.showCenterToast(this, "请输入6-16位密码,新密码必须包含数字和字母");
			// return;
		}

		if (oldPassword.equals(newPassword)) {
			ToastUtil.showCenterToast(this, "新密码与原密码一致，请重新输入");
			return;
		}

		if (StringUtil.isNullOrEmpty(confirmPassword)
				|| !confirmPassword.equals(newPassword)) {
			ToastUtil.showCenterToast(this, "两次输入新密码不一致，请重新输入");
			return;
		}

		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getModifyPassword(uid,
				oldPassword, newPassword));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 修改密码回调处理
	 */
	private OnCompleteListener<BaseResponse> mOnCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					ToastUtil.showCenterToast(ModifiPasswordActivity.this,
							"密码修改成功");

					// 修改成功，返回前一个页面
					finish();
				} else {
					ToastUtil.showCenterToast(ModifiPasswordActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(ModifiPasswordActivity.this,
						Consts.REQUEST_ERROR);
			}
		}
	};

}
