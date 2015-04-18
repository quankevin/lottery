package com.xmtq.lottery.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.RechargeBindingListAdapter;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.CreateOrderBean;
import com.xmtq.lottery.bean.CreateOrderResponse;
import com.xmtq.lottery.bean.UserBankBean;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.StringUtil;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.widget.LoadingDialog;

/**
 * 充值
 * 
 * @author Administrator
 * 
 */
public class RechargeMoneyActivity extends BaseActivity {

	private LinearLayout check_bank;
	private EditText search_edit;
	// private LinearLayout recharge_bank;
	private CreateOrderBean mCreateOrderBean;
	// 订单号
	private String requestId;
	private ListView recharge_binding_list;
	private TextView tv_select_bank;

	private RechargeBindingListAdapter adapter;
	private boolean isCheckedBank = false;
	private TextView recharge_commit;

	private String rechargeMoney;

	private UserBankBean selectedBankBean;

	public static Map<String, String> bankMap = new HashMap<String, String>();
	public static Map<String, String> bankCMap = new HashMap<String, String>();
	private LoadingDialog mDialog;

	// private TextView bank_name;
	// private TextView bank_tail_num;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.recharge);
	}

	@Override
	public void dealLogicBeforeInitView() {
		mDialog = new LoadingDialog(this);
		request("10", 0);
	}

	@Override
	public void initView() {

		recharge_commit = (TextView) findViewById(R.id.recharge_commit);

		recharge_commit.setOnClickListener(this);
		recharge_binding_list = (ListView) findViewById(R.id.recharge_binding_list);
		tv_select_bank = (TextView) findViewById(R.id.tv_select_bank);

		recharge_binding_list.setOnItemClickListener(bindingBankListener);

		check_bank = (LinearLayout) findViewById(R.id.check_bank);

		// bank_name = (TextView) findViewById(R.id.bank_name);
		// bank_tail_num = (TextView) findViewById(R.id.bank_card_tail_num);

		ImageButton back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(this);
		check_bank.setOnClickListener(this);
		search_edit = (EditText) findViewById(R.id.search_edit);
		RadioGroup check_money = (RadioGroup) findViewById(R.id.check_money);

		check_money.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rec_ten) {
					search_edit.setText("10");
				} else if (checkedId == R.id.rec_fifty) {
					search_edit.setText("50");
				} else if (checkedId == R.id.rec_hundred) {
					search_edit.setText("100");
				} else if (checkedId == R.id.rec_five_hundred) {
					search_edit.setText("500");

				}
			}
		});

	}

	@Override
	public void dealLogicAfterInitView() {

	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		// 选择银行卡
		case R.id.check_bank:

			rechargeMoney = search_edit.getText().toString().trim();

			if (StringUtil.isNullOrEmpty(rechargeMoney)) {
				Toast.makeText(RechargeMoneyActivity.this, "请输入充值金额",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!isNumeric(rechargeMoney)) {
				 Toast.makeText(RechargeMoneyActivity.this, "充值金额必须为整数",
				 Toast.LENGTH_SHORT).show();
				 return;
			}
			if (Integer.parseInt(rechargeMoney) < 5) {
				Toast.makeText(RechargeMoneyActivity.this, "充值金额不小于五元",
						Toast.LENGTH_SHORT).show();
				return;
			}

			request(rechargeMoney, 1);
			break;
		case R.id.recharge_commit:
			rechargeMoney = search_edit.getText().toString().trim();

			if (StringUtil.isNullOrEmpty(rechargeMoney)) {
				Toast.makeText(RechargeMoneyActivity.this, "请输入充值金额",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!isNumeric(rechargeMoney)) {
				 Toast.makeText(RechargeMoneyActivity.this, "充值金额必须为整数",
				 Toast.LENGTH_SHORT).show();
				 return;
			}
			if (Integer.parseInt(rechargeMoney) < 5) {
				 Toast.makeText(RechargeMoneyActivity.this, "充值金额不小于五元",
				 Toast.LENGTH_SHORT).show();
				 return;
			}

			if (!isCheckedBank) {
				Toast.makeText(RechargeMoneyActivity.this, "请选择银行卡",
						Toast.LENGTH_SHORT).show();
				return;
			}

			request(rechargeMoney, 2);
			break;
		default:
			break;
		}

	}

	private OnItemClickListener bindingBankListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			isCheckedBank = true;
			adapter.setSelectedPos(arg2);
			selectedBankBean = mCreateOrderBean.getUserBankList().get(arg2);
		}
	};

	/**
	 * 创建支付订单
	 * <ul>
	 * <li>0 : 获取绑定的银行卡</li>
	 * <li>1  ：首次支付</li>
	 * <li>2 : 快捷支付</li>
	 * </ul>
	 * 
	 */
	private void request(String totalPrice, int type) {
		mDialog.show("数据正在加载中...");
		String userid = SharedPrefHelper.getInstance(this).getUid();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getFengPay(userid,
				totalPrice));
		if (type == 0) {
			mAsyncTask.setOnCompleteListener(mOnCompleteListener);
		} else if (type == 1) {
			mAsyncTask.setOnCompleteListener(mOnFirstPayCompleteListener);
		} else if (type == 2) {
			mAsyncTask.setOnCompleteListener(mOnQuickPayCompleteListener);
		}

	}

	/**
	 * 获取绑定的银行卡
	 */
	private OnCompleteListener<BaseResponse> mOnCompleteListener = new OnCompleteListener<BaseResponse>() {

		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					onSuccess(result);
				} else {
					ToastUtil.showCenterToast(RechargeMoneyActivity.this,
							result.errormsg);
					recharge_binding_list.setVisibility(View.GONE);
				}
			} else {
				ToastUtil.showCenterToast(RechargeMoneyActivity.this, "请求失败");
				recharge_binding_list.setVisibility(View.GONE);
			}

			mDialog.dismiss();
		}
	};

	/**
	 * 首次支付
	 */
	private OnCompleteListener<BaseResponse> mOnFirstPayCompleteListener = new OnCompleteListener<BaseResponse>() {

		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					onSuccess(result);
					Intent intent;
					intent = new Intent(RechargeMoneyActivity.this,
							CheckBankFirstActivity.class);
					intent.putExtra("requestId", requestId);
					intent.putExtra("mCreateOrderBean", mCreateOrderBean);
					startActivity(intent);
					
				} else {
					ToastUtil.showCenterToast(RechargeMoneyActivity.this,
							result.errormsg);
					recharge_binding_list.setVisibility(View.GONE);
				}
			} else {
				ToastUtil.showCenterToast(RechargeMoneyActivity.this, "请求失败");
				recharge_binding_list.setVisibility(View.GONE);
			}

			mDialog.dismiss();
		}
	};

	/**
	 * 快捷支付
	 */
	private OnCompleteListener<BaseResponse> mOnQuickPayCompleteListener = new OnCompleteListener<BaseResponse>() {

		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					onSuccess(result);
					Intent intent;
					intent = new Intent(RechargeMoneyActivity.this,
							QuickPaymentActivity.class);
					intent.putExtra("requestId", requestId);
					intent.putExtra("selectedBankBean", selectedBankBean);
					intent.putExtra("rechargeMoney", rechargeMoney);

					startActivity(intent);
				} else {
					ToastUtil.showCenterToast(RechargeMoneyActivity.this,
							result.errormsg);
					recharge_binding_list.setVisibility(View.GONE);
				}
			} else {
				ToastUtil.showCenterToast(RechargeMoneyActivity.this, "请求失败");
				recharge_binding_list.setVisibility(View.GONE);
			}

			mDialog.dismiss();
		}
	};

	private void onSuccess(BaseResponse result) {
		CreateOrderResponse mOrderResponse = (CreateOrderResponse) result;
		if (mOrderResponse.requestId != null) {
			requestId = mOrderResponse.requestId;
		}

		mCreateOrderBean = mOrderResponse.createOrderBean;
		if (mCreateOrderBean != null) {

			if (mCreateOrderBean.getBankList().size() > 0) {
				for (int i = 0; i < mCreateOrderBean.getBankList().size(); i++) {
					bankMap.put(mCreateOrderBean.getBankList().get(i)
							.getBankCode(),
							mCreateOrderBean.getBankList().get(i).getBankName());
				}
			}
			if (mCreateOrderBean.getBankCList().size() > 0) {
				for (int j = 0; j < mCreateOrderBean.getBankCList().size(); j++) {
					bankCMap.put(mCreateOrderBean.getBankCList().get(j)
							.getBankCode(), mCreateOrderBean.getBankCList()
							.get(j).getBankName());
				}
			}

		}

		if (mCreateOrderBean.getUserBankList().size() > 0) {

			adapter = new RechargeBindingListAdapter(
					RechargeMoneyActivity.this,
					mCreateOrderBean.getUserBankList());
			recharge_binding_list.setAdapter(adapter);
			tv_select_bank.setVisibility(View.VISIBLE);
			recharge_binding_list.setVisibility(View.VISIBLE);
			recharge_commit.setVisibility(View.VISIBLE);
		}
	}

}
