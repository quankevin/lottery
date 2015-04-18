package com.xmtq.lottery.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.widget.LoadingDialog;

public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {
	// protected MainApp softApplication;
	// protected boolean isAllowFullScreen;// 是否允许全屏
	protected boolean hasMenu;// 是否有菜单显示
	private ProgressDialog progressDialog;
	public LoadingDialog mLoadingDialog;
	protected Resources resources;
	protected SharedPrefHelper mSharedPrefHelper;
	// public UserBean mUserBean;
	public int currentPage = 1;
	public final static int pageItemSize = 10;
	public String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLoadingDialog = new LoadingDialog(this);
		resources = getResources();
		// softApplication = (MainApp) getApplicationContext();
		// userid = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
		// .getString(Config.USER_ID_PARAMS, "");
		mSharedPrefHelper = SharedPrefHelper.getInstance(this);
		// mUserBean = mSharedPrefHelper.readUserBean();
		// if (isAllowFullScreen) {
		setFullScreen(true);
		// } else {
		// setFullScreen(false);
		// }

		setContentLayout();
		dealLogicBeforeInitView();
		initView();
		dealLogicAfterInitView();

	}

	/**
	 * 用户退出时需要重写这个方法
	 */
	protected void doSomeForUserLogout() {
		// TODO Auto-generated method stub

	}

	protected FragmentManager mFragmentManager;
	protected FragmentTransaction mFragmentTransaction;

	protected String mCurrentFragmentMenuTag;

	protected void attachFragment(int layout, Fragment f, String tag) {
		if (f != null) {
			if (f.isDetached()) {
				ensureTransaction();
				mFragmentTransaction.attach(f);
			} else if (!f.isAdded()) {
				ensureTransaction();
				mFragmentTransaction.add(layout, f, tag);
			}
		}
	}

	protected FragmentTransaction ensureTransaction() {
		if (mFragmentTransaction == null) {
			mFragmentTransaction = mFragmentManager.beginTransaction();
			mFragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}

		return mFragmentTransaction;
	}

	protected void commitTransactions() {
		if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
			mFragmentTransaction.commit();
			mFragmentTransaction = null;
		}
	}

	/**
	 * 设置布局文件
	 */
	public abstract void setContentLayout();

	/**
	 * 在实例化布局之前处理的逻辑
	 */
	public abstract void dealLogicBeforeInitView();

	/**
	 * 实例化布局文件/组件
	 */
	public abstract void initView();

	/**
	 * 在实例化布局之后处理的逻辑
	 */
	public abstract void dealLogicAfterInitView();

	/**
	 * 得到屏幕宽度
	 * 
	 * @return 宽度
	 */
	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	/**
	 * 得到屏幕高度
	 * 
	 * @return 高度
	 */
	public int getScreenHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 是否全屏和显示标题，true为全屏和无标题，false为无标题，请在setContentView()方法前调用
	 * 
	 * @param fullScreen
	 */
	public void setFullScreen(boolean fullScreen) {
		if (fullScreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToast(String info) {
		Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToastLong(String info) {
		Toast.makeText(this, info, Toast.LENGTH_LONG).show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToastLong(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
	}

	/**
	 * onClick方法的封装，在此方法中处理点击
	 * 
	 * @param view
	 *            被点击的View对象
	 */
	abstract public void onClickEvent(View view);

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.title_back:
		// finish();
		// break;

		default:
			onClickEvent(v);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 显示正在加载的进度条
	 * 
	 */
	public void showProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(BaseActivity.this);
		progressDialog.setMessage("加载中...");
		try {
			progressDialog.show();
		} catch (BadTokenException exception) {
			exception.printStackTrace();
		}
	}

	public void showProgressDialog(String msg) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(BaseActivity.this);
		progressDialog.setMessage(msg);
		try {
			progressDialog.show();
		} catch (BadTokenException exception) {
			exception.printStackTrace();
		}
	}

	public ProgressDialog createProgressDialog(String msg) {
		ProgressDialog progressDialog = new ProgressDialog(BaseActivity.this);
		progressDialog.setMessage(msg);
		return progressDialog;
	}

	/**
	 * 隐藏正在加载的进度条
	 * 
	 */
	public void dismissProgressDialog() {
		if (null != progressDialog && progressDialog.isShowing() == true) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * 改变视频播放屏幕
	 * 
	 */

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	}

	// public static boolean isLogin() {
	// // 是否已经登录
	// SharedPreferences spf = getSharedPreferences("isLogin",
	// Context.MODE_PRIVATE);
	//
	// boolean isLogin = false;
	// isLogin = spf.getBoolean("isLogin", true);
	// return isLogin;
	// }
}
