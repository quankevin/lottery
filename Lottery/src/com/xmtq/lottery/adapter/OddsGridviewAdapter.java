package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.xmtq.lottery.R;
import com.xmtq.lottery.bean.Odds;

/**
 * 赔率详情GridView
 *
 */
public class OddsGridviewAdapter extends BaseAdapter {

	private Context context;
	private List<Odds> oddsList;

	public OddsGridviewAdapter(Context context, List<Odds> oddsList) {
		this.context = context;
		this.oddsList = oddsList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return oddsList.size();
	}

	@Override
	public Odds getItem(int arg0) {
		// TODO Auto-generated method stub
		return oddsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(
				R.layout.odds_gridview_item, null);
		ToggleButton toggleButton = (ToggleButton)convertView.findViewById(R.id.odds_button);
		Odds odds = oddsList.get(position);
		setText(toggleButton, odds);
		return convertView;
	}

	
	/**
	 * 设置开关按钮
	 * 
	 * @param toggleButton
	 * @param text
	 */
	private void setText(ToggleButton toggleButton, Odds odds) {
		String sOdds = odds.getResult()+"\n"+odds.getOdds();
		toggleButton.setText(sOdds);
		toggleButton.setTextOn(sOdds);
		toggleButton.setTextOff(sOdds);

		toggleButton.setTag(odds);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Odds odds = (Odds) arg0.getTag();
				if (arg1) {
					odds.setChecked(true);
				} else {
					odds.setChecked(false);
				}
			}
		});
		
		if (odds.isChecked()) {
			toggleButton.setChecked(true);
		} else {
			toggleButton.setChecked(false);
		}
	}

}
