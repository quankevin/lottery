package com.xmtq.lottery.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xmtq.lottery.R;

/**
 * 测试
 * 
 * @author mwz123
 * 
 */
public class TextActivity extends BaseActivity {

	private TextView find_password;
	private TextView register;
	private ImageButton btn_back;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.login);

	}

	@Override
	public void dealLogicBeforeInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		find_password = (TextView) findViewById(R.id.find_password);
		find_password.setOnClickListener(this);
		register = (TextView) findViewById(R.id.register);
		register.setOnClickListener(this);

		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void dealLogicAfterInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickEvent(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.find_password:
			intent = new Intent(TextActivity.this, FindPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.register:
			intent = new Intent(TextActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
