package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.BankBean;

/**
 * 储蓄卡adapter
 * 
 * @author mwz123
 * 
 */
public class BankSavingListAdapter extends BaseAdapter {
	private Context mContext;
	private List<BankBean> bankList;
	private int[] a = { R.drawable.a3, R.drawable.a1, R.drawable.a5,
			R.drawable.a9, R.drawable.a13, R.drawable.a12, R.drawable.a11,
			R.drawable.a10, R.drawable.a8 };

	public BankSavingListAdapter(Context c, List<BankBean> bankList) {
		this.mContext = c;
		this.bankList = bankList;
	}

	@Override
	public int getCount() {

		return bankList.size();
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
					R.layout.bank_saving_list_item, null);
			holder.tv_bank_name = (TextView) convertView
					.findViewById(R.id.bank_name);
			holder.img_bank_icon = (ImageView) convertView
					.findViewById(R.id.img_bank_icon);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.img_bank_icon.setBackgroundResource(a[arg0]);
		holder.tv_bank_name.setText(bankList.get(arg0).getBankName());

		return convertView;
	}

	public class Holder {

		ImageView img_bank_icon;
		TextView tv_bank_name;
	}

}
