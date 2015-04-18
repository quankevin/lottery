package com.xmtq.lottery.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmtq.lottery.R;

/**
 * 快捷支付成功
 * 
 * @author mwz123
 * 
 */
public class QuickPaymentSuccessActivity extends BaseActivity {

	private ImageButton btn_back;
	private TextView quick_pay_done;
	private TextView quick_pay_done_money;
	private String money;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.quick_payment_success);

	}

	@Override
	public void dealLogicBeforeInitView() {
		money = getIntent().getStringExtra("money");

	}

	@Override
	public void initView() {
		quick_pay_done_money = (TextView) findViewById(R.id.quick_pay_done_money);
		if (money != null) {
			quick_pay_done_money.setText(money);
		}
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		quick_pay_done = (TextView) findViewById(R.id.quick_pay_done);
		quick_pay_done.setOnClickListener(this);
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
		case R.id.quick_pay_done:
			// Intent intent = new Intent(QuickPaymentSuccessActivity.this,
			// RecomendActivity.class);
			// startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

}
