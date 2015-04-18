package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.BetDetailBean;
import com.xmtq.lottery.widget.CustomListView;

public class BetOrderDetailListAdapter extends BaseAdapter {
	private Context mContext;
	private List<BetDetailBean> mBetDetailBeans;

	public BetOrderDetailListAdapter(Context c,
			List<BetDetailBean> betDetailBeans) {
		this.mContext = c;
		this.mBetDetailBeans = betDetailBeans;
	}

	@Override
	public int getCount() {

		return mBetDetailBeans.size();
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
					R.layout.bet_detail_list_item, null);
			holder.bet_detail_week = (TextView) convertView
					.findViewById(R.id.bet_detail_week);
			holder.bet_detail_team = (TextView) convertView
					.findViewById(R.id.bet_detail_team);
			holder.bet_odds_listview = (CustomListView) convertView
					.findViewById(R.id.bet_odds_listview);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.bet_detail_week.setText(mBetDetailBeans.get(arg0).getName()
				+ "\n" + mBetDetailBeans.get(arg0).getNumber());
		holder.bet_detail_team.setText(mBetDetailBeans.get(arg0).getMatchteam()
				+ "\nvs\n" + mBetDetailBeans.get(arg0).getHostteam());

		BetOddsListAdapter adapter = new BetOddsListAdapter(mContext,
				mBetDetailBeans.get(arg0).getmBetOddBeans());
		holder.bet_odds_listview.setAdapter(adapter);
		return convertView;
	}

	public class Holder {
		TextView bet_detail_week;
		TextView bet_detail_team;
		CustomListView bet_odds_listview;
	}

}
