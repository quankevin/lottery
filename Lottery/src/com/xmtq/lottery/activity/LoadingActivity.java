package com.xmtq.lottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.xmtq.lottery.R;
import com.xmtq.lottery.utils.SharedPrefHelper;

/**
 * 启动加载页面
 *
 */
public class LoadingActivity extends Activity {
	private AlphaAnimation aa = null;
	private View view = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.load_layout, null);
		setContentView(view);
		aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				handler.sendEmptyMessage(0);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(SharedPrefHelper.getInstance(LoadingActivity.this).getFirstLogin()){
				Intent intent = new Intent(LoadingActivity.this,
						GuideActivity.class);
				startActivity(intent);
				finish();
			}else{
				Intent mainIntent = new Intent(LoadingActivity.this,
						RecomendActivity.class);
				startActivity(mainIntent);
				finish();
			}
		}
	};
}
