package com.xmtq.lottery.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.activity.RechargeMoneyActivity;
import com.xmtq.lottery.bean.BetInfoBean;

public class BalanceNotEnoughDialog {

	private Context context;
	private Dialog mdialog;
	private LinearLayout layout;
	private BetInfoBean betInfoBean;

	public BalanceNotEnoughDialog(Context context, BetInfoBean betInfoBean) {
		this.context = context;
		this.betInfoBean = betInfoBean;
	}

	private void initview() {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.balance_not_enough_dialog, null);
		TextView bug_money = (TextView) layout.findViewById(R.id.bug_money);
		TextView account_balance = (TextView) layout
				.findViewById(R.id.account_balance);

		bug_money.setText(betInfoBean.getBuymoney() + "元");
		account_balance.setText(betInfoBean.getAccountBalance() + "元");
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

	private OnClickListener myShureListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context, RechargeMoneyActivity.class);
			context.startActivity(intent);
			mdialog.dismiss();
		}
	};;;
}
