package com.xmtq.lottery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.utils.ToastUtil;

public class WelCheckDialog extends BaseDialog {

	private Context ctx;
	// private String version;
	// private String updateintroduce;
	private String downurl;
	private Handler handler;
	// 是否强制升级 （1为强制）
	private String isMandatory;

	public WelCheckDialog(Context context, String updateintroduce,
			String downurl, Handler callbackHandler,
			OnKeyListener onKeyListener, String isMandatory) {
		super(context);

		this.ctx = context;
		// this.version = version;
		// this.updateintroduce = updateintroduce;
		this.downurl = downurl;
		this.handler = callbackHandler;
		this.isMandatory = isMandatory;

		super.setTitle(R.string.pers_check_version_title);
		super.setContentView(R.layout.pers_check_version);
		this.setOnKeyListener(onKeyListener);
		setBtn1Visible(true);
		if (isMandatory.equals("1")) {

			setBtn2Visible(false);
		} else {
			setBtn2Visible(true);
		}

		setAutoDismiss1(true);
		setAutoDismiss2(true);
		setBtn1Text(R.string.pers_check_now);
		setBtn2Text(R.string.pers_check_later);

		TextView checkContent = (TextView) findViewById(R.id.check_version_text);
		checkContent.setTextSize(18);
		checkContent.setText(updateintroduce);

		setBtn1ClickListener(sendListener);
		// setBtn2ClickListener(btn2Listener);
	}

	private View.OnClickListener sendListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (!TextUtils.isEmpty(downurl)) {
				Uri uri = Uri.parse(downurl);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				ctx.startActivity(intent);
				setAutoDismiss1(true);
			} else {
				ToastUtil.showCenterToast(ctx, "没有下载地址");
			}

			// handler.sendEmptyMessage(Integer.MIN_VALUE);
		}
	};

	// private View.OnClickListener btn2Listener = new View.OnClickListener() {
	// @Override
	// public void onClick(View view) {
	// System.exit(0);
	// }
	// };
}
