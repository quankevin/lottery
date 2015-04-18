package com.xmtq.lottery.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.UserInfoBean;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.CardIdUtil;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 个人资料
 * 
 * @author mwz123
 * 
 */
public class PersonDataActivity extends BaseActivity {

	private RadioGroup radiobtn_userinfo;
	private LinearLayout userdata_userinfo;
	private LinearLayout userdata_bankcard;
	private LinearLayout userinfo_no_add;
	private LinearLayout userinfo_added;
	private LinearLayout bankcard_no_add;
	private LinearLayout bankcard_added;
	private ImageView img_checkbank;
	private ImageButton btn_back;
	private TextView userinfo_commit;
	private TextView bankcard_commit;

	// 个人信息
	private EditText id_card;
	private EditText real_name;

	// 银行卡信息
	private EditText bankcard_person;
	private EditText bankcard_person_id;
	private EditText bank_name;
	private EditText bankcard_id;
	private EditText bank_address;
	private EditText user_password;

	// 用户已完善信息
	private TextView uid_added;
	private TextView real_name_added;
	private TextView id_card_add;

	// 判断是否已经添加用户信息、银行卡信息
	private UserInfoBean userInfoBean;
	private boolean isAddUserInfo = false;
	private boolean isAddBankInfo = false;
	private TextView bank_card_name;
	private TextView bank_card_tail_num;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.userdata_first);

	}

	@Override
	public void dealLogicBeforeInitView() {
		// TODO Auto-generated method stub
		userInfoBean = (UserInfoBean) getIntent().getSerializableExtra(
				"userInfoBean");
		if (userInfoBean != null) {
			checkUserInfo(userInfoBean);
			checkBankInfo(userInfoBean);
		}
	}

	@Override
	public void initView() {
		// 动态加载布局
		checkView();

		btn_back = (ImageButton) findViewById(R.id.back);
		radiobtn_userinfo = (RadioGroup) findViewById(R.id.userdata_radiogroup);
		img_checkbank = (ImageView) findViewById(R.id.img_checkbank);
		userdata_userinfo = (LinearLayout) findViewById(R.id.userdata_userinfo);
		userdata_bankcard = (LinearLayout) findViewById(R.id.userdata_bank_card);
		userinfo_commit = (TextView) findViewById(R.id.userinfo_commit);
		bankcard_commit = (TextView) findViewById(R.id.bankcard_commit);
		id_card = (EditText) findViewById(R.id.id_card);
		real_name = (EditText) findViewById(R.id.real_name);

		bankcard_person = (EditText) findViewById(R.id.bankcard_person);
		bankcard_person_id = (EditText) findViewById(R.id.bankcard_person_id);
		bank_name = (EditText) findViewById(R.id.bank_name);
		bankcard_id = (EditText) findViewById(R.id.bankcard_id);
		bank_address = (EditText) findViewById(R.id.bank_address);
		user_password = (EditText) findViewById(R.id.password);

		btn_back.setOnClickListener(this);
		img_checkbank.setOnClickListener(this);
		userinfo_commit.setOnClickListener(this);
		bankcard_commit.setOnClickListener(this);
		radiobtn_userinfo
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						if (arg1 == R.id.radiobtn_userinfo) {
							userdata_userinfo.setVisibility(View.VISIBLE);
							userdata_bankcard.setVisibility(View.GONE);
						} else if (arg1 == R.id.radiobtn_bank_card) {
							userdata_userinfo.setVisibility(View.GONE);
							userdata_bankcard.setVisibility(View.VISIBLE);
						}
					}
				});

		// 获取用户数据
		// getUserData();
	}

	@Override
	public void dealLogicAfterInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.img_checkbank:
			Intent intent = new Intent(PersonDataActivity.this,
					CheckBankActivity.class);
			startActivity(intent);
			break;

		case R.id.back:
			this.finish();
			break;

		case R.id.userinfo_commit:
			commitUserInfo();
			break;

		case R.id.bankcard_commit:
			commitBankcard();
			break;

		default:
			break;
		}
	}

	/**
	 * 动态判断是否添加了用户信息和银行卡信息
	 */
	public void checkView() {
		userinfo_no_add = (LinearLayout) findViewById(R.id.userinfo_no_add);
		userinfo_added = (LinearLayout) findViewById(R.id.userinfo_added);
		bankcard_no_add = (LinearLayout) findViewById(R.id.bankcard_no_add);
		bankcard_added = (LinearLayout) findViewById(R.id.bankcard_added);
		bank_card_name = (TextView) findViewById(R.id.bank_card_name);
		bank_card_tail_num = (TextView) findViewById(R.id.bank_card_tail_num);

		if (isAddUserInfo) {
			userinfo_no_add.setVisibility(View.GONE);
			userinfo_added.setVisibility(View.VISIBLE);

			uid_added = (TextView) findViewById(R.id.uid_added);
			real_name_added = (TextView) findViewById(R.id.real_name_added);
			id_card_add = (TextView) findViewById(R.id.id_card_add);
			uid_added.setText(SharedPrefHelper.getInstance(this).getUserName());
			if (userInfoBean.getRealname().length() > 1) {
				real_name_added.setText(userInfoBean.getRealname().substring(0,
						1)
						+ " * *");
			}
			id_card_add.setText(userInfoBean.getCardid().substring(0, 6)
					+ "**********");
		}
		if (isAddBankInfo) {
			bankcard_no_add.setVisibility(View.GONE);
			bankcard_added.setVisibility(View.VISIBLE);
			bank_card_name.setText(userInfoBean.getBankname());
			if (userInfoBean.getBankaccount().length() > 4) {

				String tail = userInfoBean.getBankaccount().substring(
						userInfoBean.getBankaccount().length() - 4,
						userInfoBean.getBankaccount().length());
				bank_card_tail_num.setText("尾号 " + tail);
			}
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

	/**
	 * 获取用户数据(仅用于测试)
	 */
	// private void getUserData() {
	// id_card.setText(SharedPrefHelper.getInstance(this).getCardId());
	// real_name.setText(SharedPrefHelper.getInstance(this).getRealName());
	//
	// bankcard_person.setText(SharedPrefHelper.getInstance(this)
	// .getRealName());
	// bankcard_person_id.setText(SharedPrefHelper.getInstance(this)
	// .getCardId());
	// bank_name.setText(SharedPrefHelper.getInstance(this).getBankName());
	// bankcard_id.setText(SharedPrefHelper.getInstance(this).getBankCardId());
	// bank_address.setText(SharedPrefHelper.getInstance(this)
	// .getBankAddress());
	// user_password.setText(SharedPrefHelper.getInstance(this).getPassword());
	// }

	/**
	 * 保存用户数据（仅用于测试）
	 */
	// private void saveUserData() {
	// SharedPrefHelper.getInstance(this).setRealName(
	// bankcard_person.getText().toString().trim());
	// SharedPrefHelper.getInstance(this).setCardId(
	// bankcard_person_id.getText().toString().trim());
	// SharedPrefHelper.getInstance(this).setBankName(
	// bank_name.getText().toString().trim());
	// SharedPrefHelper.getInstance(this).setBankCardId(
	// bankcard_id.getText().toString().trim());
	// SharedPrefHelper.getInstance(this).setBankAddress(
	// bank_address.getText().toString().trim());
	// SharedPrefHelper.getInstance(this).setPassword(
	// user_password.getText().toString().trim());
	// }

	/**
	 * 提交个人信息
	 */
	private void commitUserInfo() {
		// TODO Auto-generated method stub
		String uid = SharedPrefHelper.getInstance(this).getUid();
		String idCard = id_card.getText().toString().trim();
		String realName = real_name.getText().toString().trim();
		// SharedPrefHelper.getInstance(this).setRealName(realName);
		// SharedPrefHelper.getInstance(this).setCardId(idCard);

		if (StringUtil.isNullOrEmpty(idCard)) {
			ToastUtil.showCenterToast(this, "请输入身份证号");
			return;
		} else if (!CardIdUtil.isIDCard(idCard)) {
			// 判断身份证号是否合法
			ToastUtil.showCenterToast(this, "请输入正确的身份证号");
			return;
		}

		if (StringUtil.isNullOrEmpty(realName)) {
			ToastUtil.showCenterToast(this, "请输入真实姓名");
			return;
		}

		mLoadingDialog.show("数据加载中...");
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getPerfectUserInfo(uid,
				realName, idCard));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 提交银行卡信息
	 */
	private void commitBankcard() {
		// TODO Auto-generated method stub
		String uid = SharedPrefHelper.getInstance(this).getUid();
		String bankCardPerson = bankcard_person.getText().toString().trim();
		String bankCardPersonId = bankcard_person_id.getText().toString()
				.trim();
		String bankName = bank_name.getText().toString().trim();
		String bankCardId = bankcard_id.getText().toString().trim();
		String bankAddress = bank_address.getText().toString().trim();
		String password = user_password.getText().toString().trim();

		// 保存用户数据 仅用于测试
		// saveUserData();

		if (StringUtil.isNullOrEmpty(bankCardPerson)) {
			ToastUtil.showCenterToast(this, "请输入持卡人姓名");
			return;
		}

		if (StringUtil.isNullOrEmpty(bankCardPersonId)) {
			ToastUtil.showCenterToast(this, "请输入持卡人身份证号");
			return;
		}

		if (StringUtil.isNullOrEmpty(bankName)) {
			ToastUtil.showCenterToast(this, "请输入银行名称");
			return;
		}

		if (StringUtil.isNullOrEmpty(bankCardId)) {
			ToastUtil.showCenterToast(this, "请输入银行卡号");
			return;
		}

		if (StringUtil.isNullOrEmpty(bankAddress)) {
			ToastUtil.showCenterToast(this, "请输入开户地");
			return;
		}

		if (StringUtil.isNullOrEmpty(password)) {
			ToastUtil.showCenterToast(this, "请输入密码");
			return;
		}
		mLoadingDialog.show("数据加载中...");

		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getPerfectUserInfo(uid,
				bankCardPerson, bankCardPersonId, "", bankName, bankCardId,
				bankAddress, password));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	/**
	 * 完善个人信息回调
	 */
	private OnCompleteListener<BaseResponse> mOnCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					ToastUtil.showCenterToast(PersonDataActivity.this,
							"完善个人信息成功");
					// ImproveUserInfoResponse response =
					// (ImproveUserInfoResponse) result;
					// ImproveUserInfoBean improveUserInfoBean =
					// response.improveUserInfoBean;

					// 完善个人资料成功后，是否需要刷新
				} else {
					ToastUtil.showCenterToast(PersonDataActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(PersonDataActivity.this,
						Consts.REQUEST_ERROR);
			}

			mLoadingDialog.dismiss();
		}
	};

	public boolean isValidValue(String vaule) {
		if (TextUtils.isEmpty(vaule) || vaule.equals("*")) {
			return false;
		}
		return true;
	}

}
