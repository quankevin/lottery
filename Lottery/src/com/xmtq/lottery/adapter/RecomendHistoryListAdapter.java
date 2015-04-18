package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.GameHistoryDateBean;

public class RecomendHistoryListAdapter extends BaseAdapter {
	private Context mContext;
	private List<GameHistoryDateBean> mList;

	public RecomendHistoryListAdapter(Context c, List<GameHistoryDateBean> mList) {
		this.mContext = c;
		this.mList = mList;
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

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.recomend_history_list_item, null);
			holder.hitcount_count = (TextView) convertView
					.findViewById(R.id.hitcount_count);
			holder.game_date = (TextView) convertView
					.findViewById(R.id.game_date);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.game_date.setText(mList.get(arg0).getDate());
		holder.hitcount_count.setText(mList.get(arg0).getHitcount() + "/"
				+ mList.get(arg0).getCount());
		if (mList.get(arg0).getHitcount().equals(mList.get(arg0).getCount())) {
			holder.hitcount_count.setTextColor(mContext.getResources()
					.getColor(R.color.green));
		} else {
			holder.hitcount_count.setTextColor(mContext.getResources()
					.getColor(R.color.white));
		}
		// holder.tv_program_name.setText(childList.get(position).getTitle());

		return convertView;
	}

	public class Holder {

		TextView game_date;
		TextView hitcount_count;
	}

}
