package com.xmtq.lottery.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.fragment.AccountFragment;

/**
 * 账户明细
 * 
 * @author Administrator
 * 
 */
public class AccountDetailActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private ImageButton btn_back;
	private TextView head_right;
	private String mFormerTag;
	private final static String TOTAL_TAG = "";
	private final static String RECHARGE_TAG = "0,76,77,93";
	private final static String DEPOSIT_TAG = "6";
	private AccountFragment totalAccountFragment;
	private AccountFragment rechargeAccountFragment;
	private AccountFragment depositAccountFragment;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.account_dedail);

	}

	@Override
	public void dealLogicBeforeInitView() {
		totalAccountFragment = new AccountFragment(TOTAL_TAG);
		rechargeAccountFragment = new AccountFragment(RECHARGE_TAG);
		depositAccountFragment = new AccountFragment(DEPOSIT_TAG);
	}

	@Override
	public void initView() {
		head_right = (TextView) findViewById(R.id.head_right);
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		head_right.setOnClickListener(this);
		RadioGroup account_detail_radiogroup = (RadioGroup) findViewById(R.id.account_detail_radiogroup);
		account_detail_radiogroup.setOnCheckedChangeListener(this);

		mFormerTag = TOTAL_TAG;
		getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, totalAccountFragment, TOTAL_TAG)
				.commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		FragmentTransaction mTransaction = getSupportFragmentManager()
				.beginTransaction();
		mTransaction.hide(getSupportFragmentManager().findFragmentByTag(
				mFormerTag));

		if (checkedId == R.id.account_my) {
			mFormerTag = TOTAL_TAG;
			if (totalAccountFragment.isAdded()) {
				mTransaction.show(totalAccountFragment).commit();
			} else {
				mTransaction.add(R.id.content_frame, totalAccountFragment,
						TOTAL_TAG).commit();
			}
		} else if (checkedId == R.id.account_recharge) {
			mFormerTag = RECHARGE_TAG;
			if (rechargeAccountFragment.isAdded()) {
				mTransaction.show(rechargeAccountFragment).commit();
			} else {
				mTransaction.add(R.id.content_frame, rechargeAccountFragment,
						RECHARGE_TAG).commit();
			}
		} else if (checkedId == R.id.account_deposit) {
			mFormerTag = DEPOSIT_TAG;
			if (depositAccountFragment.isAdded()) {
				mTransaction.show(depositAccountFragment).commit();
			} else {
				mTransaction.add(R.id.content_frame, depositAccountFragment,
						DEPOSIT_TAG).commit();
			}
		}
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
		case R.id.head_right:

			// 近一周的交易信息，应该使用日期去查询
			Intent intent = new Intent(AccountDetailActivity.this,
					AccountDetailLastweekActivity.class);
			startActivity(intent);
			// if (mHistoryBeansList != null && mHistoryBeansList.size() > 0) {
			// intent.putExtra("mHistoryBeansList",
			// (Serializable) mHistoryBeansList);
			// intent.putExtra("pay", pay);
			// intent.putExtra("income", income);
			// } else {
			// ToastUtil.showCenterToast(AccountDetailActivity.this,
			// "近一周没有交易信息");
			// }
			break;
		default:
			break;
		}

	}
}
