package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.activity.RechargeMoneyActivity;
import com.xmtq.lottery.bean.UserBankBean;

public class RechargeBindingListAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserBankBean> userBankList;
	private int selectedPosition = -1;

	public RechargeBindingListAdapter(Context c, List<UserBankBean> userBankList) {
		this.mContext = c;
		this.userBankList = userBankList;
	}

	@Override
	public int getCount() {

		return userBankList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.recharge_binding_list_item, null);
			holder.bank_name = (TextView) convertView
					.findViewById(R.id.bank_name);
			holder.bank_tail_num = (TextView) convertView
					.findViewById(R.id.bank_card_tail_num);
			holder.recharge_ll_item = (LinearLayout) convertView
					.findViewById(R.id.recharge_ll_item);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		if (selectedPosition == arg0) {
			holder.recharge_ll_item.setBackgroundResource(R.color.bank_selected_color);
		} else {
			holder.recharge_ll_item.setBackgroundResource(R.color.soft_gray);
		}

		if (userBankList.get(arg0).getBankCardTypeUsed().equals("0")) {
			holder.bank_name.setText(RechargeMoneyActivity.bankMap
					.get(userBankList.get(arg0).getBankCodeUsed()));
		} else if (userBankList.get(arg0).getBankCardTypeUsed().equals("1")) {
			holder.bank_name.setText(RechargeMoneyActivity.bankCMap
					.get(userBankList.get(arg0).getBankCodeUsed()));
		}

		if (userBankList.get(arg0).getBankCardTypeUsed().equals("0")) {
			holder.bank_tail_num.setText("尾号  "
					+ userBankList.get(arg0).getBankAccount().replace("*", "") + "     储蓄卡");
		} else if (userBankList.get(arg0).getBankCardTypeUsed().equals("1")) {
			holder.bank_tail_num.setText("尾号  "
					+ userBankList.get(arg0).getBankAccount().replace("*", "") + "     信用卡");
		}

		return convertView;
	}

	public class Holder {
		LinearLayout recharge_ll_item;
		TextView bank_name;
		TextView bank_tail_num;
	}

	public void setSelectedPos(int pos) {
		this.selectedPosition = pos;
		notifyDataSetChanged();
	}

}
