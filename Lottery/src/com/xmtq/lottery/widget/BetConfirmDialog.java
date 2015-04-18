package com.xmtq.lottery.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.BetInfoBean;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.OnRefreshListener;
import com.xmtq.lottery.utils.ToastUtil;

public class BetConfirmDialog {

	private Context context;
	private Dialog mdialog;
	private LinearLayout layout;
	private BetInfoBean betInfoBean;
	private OnRefreshListener onRefreshListener;
	private ImageView dialog_commit;

	public BetConfirmDialog(Context context, BetInfoBean betInfoBean) {
		this.context = context;
		this.betInfoBean = betInfoBean;
	}

	private void initview() {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.bet_confirm_dialog, null);
		TextView bug_money = (TextView) layout.findViewById(R.id.bug_money);
		TextView account_balance = (TextView) layout
				.findViewById(R.id.account_balance);

		bug_money.setText(betInfoBean.getBuymoney() + "元");
		account_balance
				.setText("账户余额：" + betInfoBean.getAccountBalance() + "元");
	}

	private void setListener() {
		dialog_commit = (ImageView) layout
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
			
			// 投注入设置按钮不可点击
			bet();
			dialog_commit.setClickable(false);
			onRefreshListener.onRefresh();
		}
	};

	private void bet() {
		String uid = betInfoBean.getUid();
		String lotteryid = betInfoBean.getLotteryid();
		String votetype = betInfoBean.getVotetype();
		String playtype = betInfoBean.getPlaytype();
		String protype = betInfoBean.getProtype();
		String multiple = betInfoBean.getMultiple();
		String passtype = betInfoBean.getPasstype();
		String voteinfo = betInfoBean.getVoteinfo();
		String votenums = betInfoBean.getVotenums();
		String buymoney = betInfoBean.getBuymoney();
		String totalmoney = betInfoBean.getTotalmoney();
		
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getBettingBusiness(uid,
				lotteryid, votetype, votenums,
				multiple, voteinfo, totalmoney,
				playtype, passtype, buymoney, protype));
		mAsyncTask.setOnCompleteListener(mOnBettingCompleteListener);
	}
	
	/**
	 * 投注回调处理
	 */
	private OnCompleteListener<BaseResponse> mOnBettingCompleteListener = new OnCompleteListener<BaseResponse>() {
		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					BetSuccessDialog betSuccessDialog  = new BetSuccessDialog(context);
					betSuccessDialog.show();
				} else {
					ToastUtil.showCenterToast(context, result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(context, Consts.REQUEST_ERROR);
			}
			
			dismiss();
		}
	};
	

	/**
	 * 设置刷新Listener
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}
}
