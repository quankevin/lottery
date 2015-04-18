package com.xmtq.lottery;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;

/**
 * dialog
 * 
 * @author 907170
 * 
 */
public abstract class BaseDialog extends Dialog {

	private LinearLayout mRootView;

	private TextView mTitle;

	private FrameLayout mContentView;

	private Button btn1;
	private Button btn2;

	private View.OnClickListener mBtn1Listener;
	private View.OnClickListener mBtn2Listener;

	private boolean isAutoDismiss1 = true;

	private boolean isAutoDismiss2 = true;

	public BaseDialog(Context context) {
		super(context);
		super.setCanceledOnTouchOutside(false);
		// this.getContext().setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		this.getContext().setTheme(R.style.baseDialogStyle);

		mRootView = (LinearLayout) getLayoutInflater().inflate(
				R.layout.base_dialog, null);
		super.setContentView(mRootView);

		mTitle = (TextView) mRootView.findViewById(R.id.common_dialog_title);
		mContentView = (FrameLayout) mRootView.findViewById(R.id.content);

		setTitle(R.string.base_dialog_title);

		btn1 = (Button) mRootView.findViewById(R.id.btn_1);
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mBtn1Listener != null) {
					mBtn1Listener.onClick(v);
				}
				if (isAutoDismiss1) {
					dismiss();
				}
			}
		});

		btn2 = (Button) mRootView.findViewById(R.id.btn_2);
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mBtn2Listener != null) {
					mBtn2Listener.onClick(v);
				}
				if (isAutoDismiss2) {
					dismiss();
				}
			}
		});

		Window window = this.getWindow();
		int wdith = window.getWindowManager().getDefaultDisplay().getWidth();
		window.setLayout((int) (wdith * 0.9), LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle.setText(title);
	}

	@Override
	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}

	@Override
	public void setContentView(int layoutResID) {
		View v = getLayoutInflater().inflate(layoutResID, null);
		setContentView(v);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		mContentView.addView(view, params);
	}

	@Override
	public void setContentView(View view) {
		mContentView.addView(view);
	}

	protected void setBtn1Text(String text) {
		btn1.setText(text);
	}

	protected void setBtn2Text(String text) {
		btn2.setText(text);
	}

	protected void setBtn1Text(int resId) {
		setBtn1Text(getContext().getString(resId));
	}

	protected void setBtn2Text(int resId) {
		setBtn2Text(getContext().getString(resId));
	}

	public void setBtn1ClickListener(View.OnClickListener listener) {
		this.mBtn1Listener = listener;
	}

	public void setBtn2ClickListener(View.OnClickListener listener) {
		this.mBtn2Listener = listener;
	}

	protected void setAutoDismiss1(boolean isAutoDismiss) {
		this.isAutoDismiss1 = isAutoDismiss;
	}

	protected void setAutoDismiss2(boolean isAutoDismiss) {
		this.isAutoDismiss2 = isAutoDismiss;
	}

	protected void setBtn1Visible(boolean visible) {
		if (visible) {
			btn1.setVisibility(View.VISIBLE);
		} else {
			btn1.setVisibility(View.GONE);
		}
	}

	protected void setBtn2Visible(boolean visible) {
		if (visible) {
			btn2.setVisibility(View.VISIBLE);
		} else {
			btn2.setVisibility(View.GONE);
		}
	}

}
