package com.xmtq.lottery.widget;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.ChuanGuanMoreAdapter;
import com.xmtq.lottery.bean.PassType;

public class ChuanGuanDialog {

	private Context context;
	private Dialog mdialog;
	private LinearLayout layout;
	private LinearLayout tv_more_style;

	private GridView chuanguan_more_gridview;
	private GridView chuanguan_gridview;
	private Button btn_cancel;
	private Button btn_commit;
	private OnClickListener myCancelListener;
	private OnClickListener myCommitClickListener;
	private List<PassType> simplePassList;
	private List<PassType> morePassList;

	public ChuanGuanDialog(Context context,
			OnClickListener cancelClickListener,
			OnClickListener commitClickListener, List<PassType> simplePassList,
			List<PassType> morePassList, boolean isSupportDg) {
		this.context = context;
		this.myCancelListener = cancelClickListener;
		this.myCommitClickListener = commitClickListener;
		this.simplePassList = simplePassList;
		this.morePassList = morePassList;
	}

	private void initview() {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.chuan_guan_dialog, null);
		btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
		btn_commit = (Button) layout.findViewById(R.id.btn_commit);

		tv_more_style = (LinearLayout) layout.findViewById(R.id.tv_more_style);
		chuanguan_gridview = (GridView) layout.findViewById(R.id.chuanguan);
		chuanguan_more_gridview = (GridView) layout
				.findViewById(R.id.chuanguan_more);

		ChuanGuanMoreAdapter simpleAdapter = new ChuanGuanMoreAdapter(context,
				simplePassList);
		chuanguan_gridview.setAdapter(simpleAdapter);

		ChuanGuanMoreAdapter moreAdapter = new ChuanGuanMoreAdapter(context,
				morePassList);
		chuanguan_more_gridview.setAdapter(moreAdapter);

		if (morePassList != null && morePassList.size() > 0) {
			tv_more_style.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (chuanguan_more_gridview.getVisibility() == View.GONE) {
						chuanguan_more_gridview.setVisibility(View.VISIBLE);
					} else {
						chuanguan_more_gridview.setVisibility(View.GONE);
					}
				}
			});
		}

	}

	private void setListener() {
		btn_cancel.setOnClickListener(myCancelListener);
		btn_commit.setOnClickListener(myCommitClickListener);
	}

	private void createDialog() {
		mdialog = new Dialog(context, R.style.dialog_style);
		mdialog.setCanceledOnTouchOutside(true);
		mdialog.setContentView(layout);
		mdialog.getWindow().setGravity(Gravity.CENTER);
		mdialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mdialog.show();
		mdialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mdialog = null;
			}
		});
		mdialog.setOnKeyListener(keylistener);
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

	OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				myCancelListener.onClick(btn_cancel);
				return true;
			} else {
				return false;
			}
		}
	};

}
