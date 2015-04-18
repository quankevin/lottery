package com.xmtq.lottery.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.AccountDetailBean;
import com.xmtq.lottery.utils.OddsUtil;

public class AccountDetailListAdapter extends BaseAdapter {
	private Context mContext;
	private List<AccountDetailBean> mList;

	public AccountDetailListAdapter(Context c,
			List<AccountDetailBean> historyBeansList) {
		this.mContext = c;
		this.mList = historyBeansList;
	}

	@Override
	public int getCount() {

		return mList.size();
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.account_detail_list_item, null);
			holder.bet_count = (TextView) convertView
					.findViewById(R.id.bet_count);
			holder.bet_time = (TextView) convertView
					.findViewById(R.id.bet_time);
			holder.bet_style = (TextView) convertView
					.findViewById(R.id.bet_style);
			holder.bet_date = (TextView) convertView
					.findViewById(R.id.bet_date);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String date = OddsUtil.getGameData(mList.get(arg0).getEntertime());
		String time = OddsUtil.getGameTime(mList.get(arg0).getEntertime());

		String style = "";
		String money = "";
		if (mList.size() > 0) {
			// 这里根据Mflag判断money是+还是-，style可直接取返回的字段
			// if (mList.get(arg0).getMflag().equals("5")) {
			// style = "提现";
			// money = "- " + mList.get(arg0).getMoney() + "元";
			// } else if (mList.get(arg0).getMflag().equals("93")) {
			// style = "快捷支付";
			// money = mList.get(arg0).getMoney() + "元";
			// } else if (mList.get(arg0).getMflag().equals("1")) {
			// style = "充值";
			// money = mList.get(arg0).getMoney() + "元";
			// } else {
			style = mList.get(arg0).getRemark();
			money = mList.get(arg0).getMoney() + "元";
			// }
			holder.bet_count.setText(money);
			if (money.contains("+")) {
				holder.bet_count.setTextColor(mContext.getResources().getColor(
						R.color.account_color));
			} else {
				holder.bet_count.setTextColor(mContext.getResources().getColor(
						R.color.white));
			}
			holder.bet_style.setText(style);

			holder.bet_date.setVisibility(View.VISIBLE);
			if (arg0 > 0) {
				if (OddsUtil.getGameData((mList.get(arg0).getEntertime()))
						.equals(OddsUtil.getGameData((mList.get(arg0 - 1)
								.getEntertime())))) {
					holder.bet_date.setVisibility(View.GONE);
				}
			}

			holder.bet_date.setText(date);
			holder.bet_time.setText(time);
		}

		return convertView;
	}

	public class Holder {

		TextView bet_date;
		TextView bet_time;
		TextView bet_style;
		TextView bet_count;
	}

}
