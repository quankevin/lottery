package com.xmtq.lottery.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xmtq.lottery.R;
import com.xmtq.lottery.widget.CheckChuanGuanDialog.OnCompleteListener;

public class DisagreeDialog {

	private Context context;
	private Dialog mdialog;
	private LinearLayout layout;
	private OnCompleteListener onCompleteListener;

	public DisagreeDialog(Context context) {
		this.context = context;
	}

	private void initview() {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.disagree_dialog, null);
		dialog_edit = (EditText) layout.findViewById(R.id.dialog_edit);

	}

	private void setListener() {
		ImageView dialog_commit = (ImageView) layout
				.findViewById(R.id.dialog_commit);
		if (myShureListener != null) {
			dialog_commit.setOnClickListener(myShureListener);
		}
	}

	private void createDialog() {
		mdialog = new Dialog(context, R.style.dialog_style);
		mdialog.setCanceledOnTouchOutside(true);
		mdialog.setContentView(layout);
		mdialog.getWindow().setGravity(Gravity.CENTER);
		mdialog.show();
		mdialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mdialog = null;
			}
		});
	}

	public void show() {
		initview();
		setListener();
		createDialog();
	}

	public void dismiss() {
		if (mdialog != null) {
			mdialog.dismiss();
		}
	}

	private OnClickListener myShureListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String edit_text = dialog_edit.getText().toString();
			if (!TextUtils.isEmpty(edit_text) && onCompleteListener != null) {
				onCompleteListener.onComplete(edit_text);
			}
			dismiss();
		}
	};
	private EditText dialog_edit;

	public interface OnCompleteListener {
		public void onComplete(String resultString);
	}

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}
}
