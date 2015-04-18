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
import com.xmtq.lottery.bean.PassType;

public class ChuanGuanMoreAdapter extends BaseAdapter {

	private Context context;
	private List<PassType> simplePassList;

	public ChuanGuanMoreAdapter(Context context, List<PassType> simplePassList) {
		this.context = context;
		this.simplePassList = simplePassList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return simplePassList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return simplePassList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.chuanguan_gridview_item, null);
			holder = new Holder();
			holder.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.toggle_chuanguan_more);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		setText(holder.toggleButton, simplePassList.get(position));
		return convertView;
	}

	private class Holder {
		ToggleButton toggleButton;
	}

	/**
	 * 设置开关按钮的文字
	 * 
	 * @param toggleButton
	 * @param text
	 */
	private void setText(ToggleButton toggleButton, PassType passType) {
		String text = passType.getName();
		toggleButton.setText(text);
		toggleButton.setTextOn(text);
		toggleButton.setTextOff(text);

		toggleButton.setTag(passType);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				PassType passType = (PassType) arg0.getTag();
				if (arg1) {
					passType.setChecked(true);
				} else {
					passType.setChecked(false);
				}
			}
		});

		if (passType.isChecked()) {
			toggleButton.setChecked(true);
		} else {
			toggleButton.setChecked(false);
		}
	}

}
