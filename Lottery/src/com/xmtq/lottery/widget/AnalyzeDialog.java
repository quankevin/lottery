package com.xmtq.lottery.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;

public class AnalyzeDialog {

	private Context context;
	private Dialog mdialog;
	private LinearLayout layout;
	private OnClickListener myShureListener;

	private String content;
	private TextView dialog_edit;

	public AnalyzeDialog(Context context, OnClickListener myShureListener,
			String analyze) {
		this.context = context;
		this.myShureListener = myShureListener;
		this.content = analyze;
	}

	private void initview() {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.analyze_dialog, null);
		dialog_edit = (TextView) layout.findViewById(R.id.dialog_edit);
		dialog_edit.setText(content);
	}

	private void setListener() {
		ImageView dialog_commit = (ImageView) layout
				.findViewById(R.id.dialog_commit);
		dialog_commit.setOnClickListener(myShureListener);
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
}
