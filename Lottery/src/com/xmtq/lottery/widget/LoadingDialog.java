package com.xmtq.lottery.widget;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

	private Context context;
	private ProgressDialog mDialog;

	public LoadingDialog(Context context) {
		this.context = context;
	}

	private void createLoadingDialog(String message) {
		// mLayout.setOnTouchListener(mListener);

		mDialog = new ProgressDialog(context);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setMessage(message);
		mDialog.show();

	}

	public void show(String message) {
		if (mDialog != null) {
			mDialog.setMessage(message);
			mDialog.show();

		} else {
			createLoadingDialog(message);
		}

	}

	public void dismiss() {
		if (mDialog != null) {
			try {
				mDialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
