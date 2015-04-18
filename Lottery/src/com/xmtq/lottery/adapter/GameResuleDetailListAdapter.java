package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.RecomendHistoryBean;
import com.xmtq.lottery.utils.OddsUtil;

public class GameResuleDetailListAdapter extends BaseAdapter {
	private Context mContext;
	private List<RecomendHistoryBean> mHistoryBeansList;

	public GameResuleDetailListAdapter(Context c,
			List<RecomendHistoryBean> mHistoryBeansList) {
		this.mContext = c;
		this.mHistoryBeansList = mHistoryBeansList;
	}

	@Override
	public int getCount() {

		return mHistoryBeansList.size();
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
					R.layout.game_result_list_item, null);
			holder.tv_gameTime = (TextView) convertView
					.findViewById(R.id.tv_gameTime);
			holder.tv_league = (TextView) convertView
					.findViewById(R.id.tv_league);
			holder.tv_matchId = (TextView) convertView
					.findViewById(R.id.tv_matchId);
			holder.tv_matchteam = (TextView) convertView
					.findViewById(R.id.tv_matchteam);
			holder.tv_hostTeam = (TextView) convertView
					.findViewById(R.id.tv_hostTeam);
			holder.ll_gamedetail = (LinearLayout) convertView
					.findViewById(R.id.ll_gamedetail);
			holder.bfkj = (TextView) convertView.findViewById(R.id.tv_bfkj);
			holder.bfkjbj = (TextView) convertView.findViewById(R.id.tv_bfkjbc);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String gameTime = OddsUtil.getGameTime(mHistoryBeansList.get(arg0)
				.getGameTime());
		holder.tv_gameTime.setText(gameTime);

		holder.tv_league.setText(mHistoryBeansList.get(arg0).getLeague());
		holder.tv_matchId.setText(mHistoryBeansList.get(arg0).getNum());
		holder.tv_matchteam.setText(mHistoryBeansList.get(arg0).getMatchTeam());
		holder.tv_hostTeam.setText(mHistoryBeansList.get(arg0).getHostTeam());

		if (mHistoryBeansList.get(arg0).getHit().equals("0")) {
			holder.ll_gamedetail.setBackgroundResource(R.drawable.game_score_error);
			holder.bfkj.setTextColor(mContext.getResources().getColor(
					R.color.green));
			holder.bfkjbj.setTextColor(mContext.getResources().getColor(
					R.color.green));
		}

		if (mHistoryBeansList.get(arg0).getHit().equals("1")) {
			holder.ll_gamedetail.setBackgroundResource(R.drawable.game_score_right);
			holder.bfkj.setTextColor(mContext.getResources().getColor(
					R.color.text_gold));
			holder.bfkjbj.setTextColor(mContext.getResources().getColor(
					R.color.text_gold));
		}
		
		if (!TextUtils.isEmpty(mHistoryBeansList.get(arg0).getBfkjBc())) {
			holder.bfkjbj.setVisibility(View.VISIBLE);
			holder.bfkjbj.setText("半"
					+ mHistoryBeansList.get(arg0).getBfkjBc());
		} else {
			holder.bfkjbj.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(mHistoryBeansList.get(arg0).getBfkj())) {
			holder.bfkj.setVisibility(View.VISIBLE);
			holder.bfkj.setText("全" + mHistoryBeansList.get(arg0).getBfkj());
		} else {
			holder.bfkj.setVisibility(View.GONE);
		}
		return convertView;
	}

	public class Holder {
		TextView tv_league;
		TextView tv_matchId;
		TextView tv_gameTime;
		TextView tv_matchteam;
		TextView tv_hostTeam;
		LinearLayout ll_gamedetail;
		TextView bfkj;
		TextView bfkjbj;

	}

}
