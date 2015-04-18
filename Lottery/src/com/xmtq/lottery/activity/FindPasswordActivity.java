package com.xmtq.lottery.activity;

import android.view.View;
import android.widget.ImageButton;

import com.xmtq.lottery.R;

/**
 * 找回密码
 * 
 * @author mwz123
 * 
 */
public class FindPasswordActivity extends BaseActivity {

	private ImageButton btn_back;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.find_password);

	}

	@Override
	public void dealLogicBeforeInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
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

		default:
			break;
		}
	}

}
