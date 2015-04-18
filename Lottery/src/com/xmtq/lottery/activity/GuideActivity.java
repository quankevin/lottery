package com.xmtq.lottery.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.xmtq.lottery.R;

/**
 * 用户引导页面
 *
 */
public class GuideActivity extends BaseActivity {

	@Override
	public void setContentLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.guide_layout);
	}

	@Override
	public void dealLogicBeforeInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dealLogicAfterInitView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickEvent(View view) {
		// TODO Auto-generated method stub

	}

	/**
	 * 界面点击一次，退出引导页
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mSharedPrefHelper.getFirstLogin()) {
			mSharedPrefHelper.setFirstLogin(false);
			Intent intent = new Intent(GuideActivity.this,
					RecomendActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onTouchEvent(event);
	}
}
