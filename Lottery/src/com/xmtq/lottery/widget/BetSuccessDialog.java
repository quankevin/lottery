package com.xmtq.lottery.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.activity.BetRecordActivity;

public class BetSuccessDialog {

	private Context context;
	private Dialog mdialog;
	private LinearLayout layout;

	public BetSuccessDialog(Context context) {
		this.context = context;
		// this.myCancelListener = myCancelListener;
	}

	private void initview() {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.bet_success_dialog, null);
		TextView check_order = (TextView)layout.findViewById(R.id.check_order);
		TextView return_home = (TextView)layout.findViewById(R.id.return_home);
		
		check_order.setOnClickListener(onClickListener);
		return_home.setOnClickListener(onClickListener);
	}

	private void setListener() {
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
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.check_order:
				Intent intent = new Intent(context, BetRecordActivity.class);
				context.startActivity(intent);
				mdialog.dismiss();
				break;
				
			case R.id.return_home:
				mdialog.dismiss();
				break;

			default:
				break;
			}
		}
	};
}
