package com.xmtq.lottery.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xmtq.lottery.R;
import com.xmtq.lottery.fragment.BetRecordFragment;

/**
 * 投注记录
 * 
 * @author Administrator
 * 
 */
public class BetRecordActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private ImageButton btn_back;
	private String mFormerTag;
	private final static String ALL_TAG = "0";
	private final static String WIN_TAG = "1";
	private final static String WAIT_TAG = "2";
	private BetRecordFragment recordAllFragment;
	private BetRecordFragment recordWinFragment;
	private BetRecordFragment recordWaitFragment;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.bet_record);

	}

	@Override
	public void dealLogicBeforeInitView() {
		
		// request("130", "", "", "1", statue);
		recordAllFragment = new BetRecordFragment(ALL_TAG);
		recordWinFragment = new BetRecordFragment(WIN_TAG);
		recordWaitFragment = new BetRecordFragment(WAIT_TAG);

	}

	

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);

		RadioGroup bet_record_radiogroup = (RadioGroup) findViewById(R.id.bet_record_radiogroup);

		bet_record_radiogroup.setOnCheckedChangeListener(this);
		mFormerTag = ALL_TAG;
		getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, recordAllFragment, ALL_TAG).commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub
		FragmentTransaction mTransaction = getSupportFragmentManager()
				.beginTransaction();
		mTransaction.hide(getSupportFragmentManager().findFragmentByTag(
				mFormerTag));

		if (checkedId == R.id.bet_record_all) {
			mFormerTag = ALL_TAG;
			if (recordAllFragment.isAdded()) {
				mTransaction.show(recordAllFragment).commit();
			} else {
				mTransaction
						.add(R.id.content_frame, recordAllFragment, ALL_TAG)
						.commit();
			}
		} else if (checkedId == R.id.bet_record_win) {
			mFormerTag = WIN_TAG;
			if (recordWinFragment.isAdded()) {
				mTransaction.show(recordWinFragment).commit();
			} else {
				mTransaction
						.add(R.id.content_frame, recordWinFragment, WIN_TAG)
						.commit();
			}
		} else if (checkedId == R.id.bet_record_wait) {
			mFormerTag = WAIT_TAG;
			if (recordWaitFragment.isAdded()) {
				mTransaction.show(recordWaitFragment).commit();
			} else {
				mTransaction.add(R.id.content_frame, recordWaitFragment,
						WAIT_TAG).commit();
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

		default:
			break;
		}

	}
}
