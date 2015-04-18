package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.PurchaseRecordsBean;
import com.xmtq.lottery.utils.OddsUtil;

public class BetRecordListAdapter extends BaseAdapter {
	private Context mContext;
	private List<PurchaseRecordsBean> mRecordsBeansList;

	public BetRecordListAdapter(Context c,
			List<PurchaseRecordsBean> RecordsBeansList) {
		this.mContext = c;
		this.mRecordsBeansList = RecordsBeansList;
	}

	@Override
	public int getCount() {

		return mRecordsBeansList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mRecordsBeansList.get(arg0);
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
					R.layout.bet_record_list_item, null);
			holder.bet_date = (TextView) convertView
					.findViewById(R.id.bet_date);
			holder.bet_time = (TextView) convertView
					.findViewById(R.id.bet_time);
			holder.bet_style = (TextView) convertView
					.findViewById(R.id.bet_style);
			holder.bet_money = (TextView) convertView
					.findViewById(R.id.bet_money);
			holder.bet_state = (TextView) convertView
					.findViewById(R.id.bet_state);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String data = OddsUtil.getGameData((mRecordsBeansList.get(arg0)
				.getAddtime()));
		String time = OddsUtil.getGameTime((mRecordsBeansList.get(arg0)
				.getAddtime()));

		holder.bet_date.setVisibility(View.VISIBLE);
		if (arg0 > 0) {
			if (OddsUtil
					.getGameData((mRecordsBeansList.get(arg0).getAddtime()))
					.equals(OddsUtil.getGameData((mRecordsBeansList
							.get(arg0 - 1).getAddtime())))) {
				holder.bet_date.setVisibility(View.GONE);
			}
		}

		holder.bet_date.setText(data);
		holder.bet_time.setText(time);
		holder.bet_state.setText(getAfterMoney(mRecordsBeansList.get(arg0)
				.getBonusAfterfax(), mRecordsBeansList.get(arg0).getState()));
		holder.bet_style.setText(playStyle(mRecordsBeansList.get(arg0)
				.getPlaytype()));
		holder.bet_money.setText(mRecordsBeansList.get(arg0).getProjectPrize()+"元");

		return convertView;
	}

	public class Holder {

		TextView bet_date;
		TextView bet_time;
		TextView bet_style;
		TextView bet_money;
		TextView bet_state;
	}

	private String playStyle(String type) {
		String playType = "";
		if (type.equals("1")) {
			playType = "胜平负";
		} else if (type.equals("2")) {
			playType = "比分";
		} else if (type.equals("3")) {
			playType = "总进球";
		} else if (type.equals("4")) {
			playType = "半全场";
		} else if (type.equals("5")) {
			playType = "胜平负";
		} else if (type.equals("6")) {
			playType = "混投";
		}
		return playType;

	}

	private String getAfterMoney(String money, String state) {

		String result = "";
		if (!TextUtils.isEmpty(state)) {
			if (state.equals("已结算")) {
				if (Float.parseFloat(money) > 0) {
					result = "已中奖" + money + "元";
				} else {
					result = "未中奖";
				}
			}  else {
				result = state;
			}
		} else {
			result = "";
		}
		return result;
	}
}
