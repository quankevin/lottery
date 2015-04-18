package com.xmtq.lottery.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.BankSavingCListAdapter;
import com.xmtq.lottery.adapter.BankSavingListAdapter;
import com.xmtq.lottery.bean.BankBean;
import com.xmtq.lottery.bean.BankCBean;
import com.xmtq.lottery.bean.CreateOrderBean;

/**
 * 选择银行卡
 * 
 * @author Administrator
 * 
 */
public class CheckBankActivity extends BaseActivity {

	private ListView bank_savings_list;
	private ListView bank_card_list;
	private CreateOrderBean mCreateOrderBean;
	private List<BankCBean> bankCList;
	private List<BankBean> bankList;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.check_bank_card);

	}

	@Override
	public void dealLogicBeforeInitView() {
		mCreateOrderBean = (CreateOrderBean) getIntent().getSerializableExtra(
				"mCreateOrderBean");

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);

		bank_savings_list = (ListView) findViewById(R.id.bank_savings_list);
		bank_card_list = (ListView) findViewById(R.id.bank_card_list);
		bank_savings_list.setOnItemClickListener(bankCardListener);
		bank_card_list.setOnItemClickListener(bankCCardListener);
		RadioGroup check_card = (RadioGroup) findViewById(R.id.check_card);

		check_card.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.bank_savings) {
					bank_savings_list.setVisibility(View.VISIBLE);
					bank_card_list.setVisibility(View.GONE);
				} else if (checkedId == R.id.bank_credit_card) {
					bank_savings_list.setVisibility(View.GONE);
					bank_card_list.setVisibility(View.VISIBLE);
				}
			}
		});

	}

	public void dealLogicAfterInitView() {
		bankList = new ArrayList<BankBean>();
		bankCList = new ArrayList<BankCBean>();
		if (mCreateOrderBean != null) {
			bankList = mCreateOrderBean.getBankList();
			bankCList = mCreateOrderBean.getBankCList();

		}

		BankSavingListAdapter mAdapter = new BankSavingListAdapter(
				CheckBankActivity.this, bankList);
		bank_savings_list.setAdapter(mAdapter);

		BankSavingCListAdapter mCAdapter = new BankSavingCListAdapter(
				CheckBankActivity.this, bankCList);
		bank_card_list.setAdapter(mCAdapter);

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}

	private OnItemClickListener bankCCardListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			if (bankCList.get(arg2).getBankName() != null) {

				intent.putExtra("bankName", bankCList.get(arg2).getBankName());
				intent.putExtra("bankCode", bankCList.get(arg2).getBankCode());
			}
			setResult(1 * 1000, intent);
			finish();
		}
	};

	private OnItemClickListener bankCardListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			Intent intent = new Intent();
			intent.putExtra("bankName", bankList.get(arg2).getBankName());
			intent.putExtra("bankCode", bankList.get(arg2).getBankCode());
			setResult(2 * 1000, intent);
			finish();
		}
	};

}
