package com.xmtq.lottery.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.xmtq.lottery.Consts;
import com.xmtq.lottery.R;
import com.xmtq.lottery.WelCheckDialog;
import com.xmtq.lottery.adapter.FragmentPagerAdater;
import com.xmtq.lottery.bean.NewUserLoginBean;
import com.xmtq.lottery.bean.VersionBean;
import com.xmtq.lottery.bean.VersionResponse;
import com.xmtq.lottery.fragment.LoginFragment;
import com.xmtq.lottery.fragment.RecomendFragment;
import com.xmtq.lottery.fragment.RecomendHistoryFragment;
import com.xmtq.lottery.fragment.UserInfoFragment;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.utils.VersionUtil;
import com.xmtq.lottery.view.slidingmenu.SlidingMenu;
import com.xmtq.lottery.view.slidingmenu.app.SlidingFragmentActivity;

/**
 * 首页推荐
 * 
 * @author mwz123
 * 
 */
public class RecomendActivity extends SlidingFragmentActivity implements
		OnClickListener {

	private long exitTime;
	private final static long TIME_DIFF = 2 * 1000;
	private SlidingMenu menu;
	private SharedPrefHelper spfs;
	private ViewPager vp;
	private FragmentPagerAdater fragmentPagerAdater;
	private RecomendFragment recomendFragment;
	private RecomendHistoryFragment historyFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		spfs = SharedPrefHelper.getInstance(this);
		// 开户APP默认不登陆
		spfs.setIsLogin(false);
		initView();
		registerReceiver(mBroadcastReceiver, new IntentFilter(
				Consts.ACTION_AUTO_LOGIN));
	}

	public void initView() {
		initMenuDrawer();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		default:
			break;
		}
	}

	private void initMenuDrawer() {

		// 用于提现后重新登录
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LoginFragment()).commit();
		requestVersion();

		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// middle view
		// setContentView(R.layout.content_frame);
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.content_frame, new RecomendFragment()).commit();

		// // right sliding menu
		// menu.setSecondaryMenu(R.layout.menu_frame_two);
		// menu.setSecondaryShadowDrawable(R.drawable.shadowright);
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.menu_frame_two, new BetRecordFragment()).commit();

		vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		recomendFragment = new RecomendFragment();
		historyFragment = new RecomendHistoryFragment();
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(recomendFragment);
		fragments.add(historyFragment);
		fragmentPagerAdater = new FragmentPagerAdater(
				getSupportFragmentManager(), fragments);
		vp.setAdapter(fragmentPagerAdater);
		setContentView(vp);

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_NONE);
					break;
				}
			}

		});

		vp.setCurrentItem(0);
	}

	public void openLeftDrawer() {
		menu.showMenu();
	}

	public void openRightDrawer() {
		// menu.showSecondaryMenu();
		vp.setCurrentItem(1);
		fragmentPagerAdater.notifyDataSetChanged();
	}

	public void closeRightDrawer() {
		// menu.showSecondaryMenu();
		vp.setCurrentItem(0);
		fragmentPagerAdater.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// 关闭右边的Fragment
			if (vp != null && vp.getCurrentItem() != 0) {
				closeRightDrawer();
				return true;
			}

			if ((System.currentTimeMillis() - exitTime) > TIME_DIFF) {
				Toast.makeText(RecomendActivity.this, "再按一次退出",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		recomendFragment.setIntentResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void requestVersion() {
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getVersion(VersionUtil
				.getVersionName(this)));
		mAsyncTask.setOnCompleteListener(mVersionCompleteListener);
	}

	private String update = "0";
	private String message = "";
	private OnCompleteListener<VersionResponse> mVersionCompleteListener = new OnCompleteListener<VersionResponse>() {

		@Override
		public void onComplete(VersionResponse result, String resultString) {
			if (result != null) {
				if (result.errorcode.equals("1")) {
					VersionResponse mResponse = result;
					VersionBean mBean = mResponse.versionBean;
					String newVersion = mBean.getVersion();

					final String appPath = mBean.getDowload();
					update = mBean.getUpdate();
					message = mBean.getMessage();
					int oldVersion = VersionUtil
							.getVersionCode(RecomendActivity.this);
					// if (Integer.parseInt(newVersion.replace(".", "")) >
					// oldVersion) {
					RecomendActivity.this.runOnUiThread(new Runnable() {

						public void run() {
							WelCheckDialog dialog = new WelCheckDialog(
									RecomendActivity.this, message, appPath,
									null, keylistener, update);
							dialog.show();

						}
					});
					// } else {
					// // ToastUtil.showCenterToast(RecomendActivity.this,
					// // "当前已是最新版本");
					// }

				} else {
					ToastUtil.showCenterToast(RecomendActivity.this,
							result.errormsg);
				}

			} else {
				ToastUtil.showCenterToast(RecomendActivity.this, "数据请求失败");
			}

		}
	};

	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
					&& update.equals("1")) {
				ToastUtil.showCenterToast(RecomendActivity.this, "升级后才可以正常使用");
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 自动登录广播
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			NewUserLoginBean newUserLoginBean = (NewUserLoginBean) intent
					.getSerializableExtra("newUserLoginBean");

			if (newUserLoginBean != null) {
				spfs.setIsLogin(true);
				UserInfoFragment fragment = new UserInfoFragment();
				Bundle b = new Bundle();
				b.putSerializable("newUserLoginBean", newUserLoginBean);
				fragment.setArguments(b);
				RecomendActivity.this.getSupportFragmentManager()
						.beginTransaction().replace(R.id.menu_frame, fragment)
						.commitAllowingStateLoss();
			}
		}

	};

}
