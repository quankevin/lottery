package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.BetOddBean;
import com.xmtq.lottery.utils.LogUtil;

public class BetOddsListAdapter extends BaseAdapter {
	private Context mContext;
	private List<BetOddBean> mBetOddBeans;

	public BetOddsListAdapter(Context c, List<BetOddBean> betOddBeans) {
		this.mContext = c;
		this.mBetOddBeans = betOddBeans;
	}

	@Override
	public int getCount() {

		return mBetOddBeans.size();
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
					R.layout.bet_odds_list_item, null);
			holder.bet_detail_spf = (TextView) convertView
					.findViewById(R.id.bet_detail_spf);
			holder.bet_detail_f = (TextView) convertView
					.findViewById(R.id.bet_detail_f);
			holder.bet_detail_s = (TextView) convertView
					.findViewById(R.id.bet_detail_s);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.bet_detail_spf.setText(mBetOddBeans.get(arg0).getPlayname());
		holder.bet_detail_s.setText(mBetOddBeans.get(arg0).getGameresult());
		holder.bet_detail_f.setText(mBetOddBeans.get(arg0).getBetinfo());

		LogUtil.log("mBetOddBeans.get(arg0).getPlayname()："
				+ mBetOddBeans.get(arg0).getPlayname());
		LogUtil.log("mBetOddBeans.get(arg0).getGameresult():"
				+ mBetOddBeans.get(arg0).getGameresult());
		LogUtil.log("mBetOddBeans.get(arg0).getBetinfo():"
				+ mBetOddBeans.get(arg0).getBetinfo());
		return convertView;
	}

	public class Holder {
		TextView bet_detail_spf;
		TextView bet_detail_f;
		TextView bet_detail_s;
	}

}
