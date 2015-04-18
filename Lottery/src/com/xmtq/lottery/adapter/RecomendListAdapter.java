package com.xmtq.lottery.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xmtq.lottery.R;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.bean.GameCanBetBean;
import com.xmtq.lottery.bean.Odds;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.OddsUtil;
import com.xmtq.lottery.utils.OnRefreshListener;
import com.xmtq.lottery.utils.SharedPrefHelper;
import com.xmtq.lottery.utils.ToastUtil;
import com.xmtq.lottery.widget.AnalyzeDialog;
import com.xmtq.lottery.widget.DisagreeDialog;
import com.xmtq.lottery.widget.DisagreeDialog.OnCompleteListener;

public class RecomendListAdapter extends BaseAdapter {
	private Context mContext;
	private List<GameCanBetBean> gameCanBetBeans;
	private DisagreeDialog disagreeDialog;
	private AnalyzeDialog analyzeDialog;
	private OnClickListener onMoreListener;
	private OnRefreshListener onRefreshListener;

	public RecomendListAdapter(Context c, List<GameCanBetBean> gameCanBetBeans) {
		this.mContext = c;
		this.gameCanBetBeans = gameCanBetBeans;
	}

	@Override
	public int getCount() {
		return gameCanBetBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gameCanBetBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.recomend_list_item, null);
			holder.game_time = (TextView) convertView
					.findViewById(R.id.game_time);
			holder.match_team = (TextView) convertView
					.findViewById(R.id.match_team);
			holder.league = (TextView) convertView.findViewById(R.id.league);
			holder.host_team = (TextView) convertView
					.findViewById(R.id.host_team);
			holder.play_type = (TextView) convertView
					.findViewById(R.id.play_type);
			holder.win = (ToggleButton) convertView.findViewById(R.id.win);
			holder.draw = (ToggleButton) convertView.findViewById(R.id.draw);
			holder.lose = (ToggleButton) convertView.findViewById(R.id.lose);
			holder.analyze = (TextView) convertView.findViewById(R.id.analyze);
			holder.dis_agree = (ImageView) convertView
					.findViewById(R.id.dis_agree);
			holder.odds_more = (LinearLayout) convertView
					.findViewById(R.id.odds_more);
			holder.item_view = convertView
					.findViewById(R.id.recomend_item_view);
			holder.recomend_ll_analyze = (LinearLayout) convertView
					.findViewById(R.id.recomend_ll_analyze);
			if (onMoreListener != null) {
				holder.odds_more.setOnClickListener(onMoreListener);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		// 赔率详情
		holder.odds_more.setTag(position);

		// 比赛时间
		String gameTimeData = gameCanBetBeans.get(position).getGameTime();
		if (!TextUtils.isEmpty(gameTimeData)) {
			String gameTime = OddsUtil.getGameTime(gameTimeData);
			if (!TextUtils.isEmpty(gameTime)) {
				holder.game_time.setText(gameTime);
			}
		}

		// 比赛队伍
		holder.match_team.setText(gameCanBetBeans.get(position).getMatchTeam());
		holder.league.setText(gameCanBetBeans.get(position).getLeague());
		holder.host_team.setText(gameCanBetBeans.get(position).getHostTeam());

		// 胜负平赔率
		holder.play_type.setText("胜负平");
		List<Odds> spOddsList = gameCanBetBeans.get(position).getSpOddsList();
		if (spOddsList.size() > 0) {
			for (int j = 0; j < spOddsList.size(); j++) {
				Odds odds = spOddsList.get(j);
				if (odds.getResult().equals("胜")) {
					setText(holder.win, odds);
				} else if (odds.getResult().equals("平")) {
					setText(holder.draw, odds);
				} else if (odds.getResult().equals("负")) {
					setText(holder.lose, odds);
				}
			}
		}
		String content = gameCanBetBeans.get(position).getContent();
		String matchid = gameCanBetBeans.get(position).getMatchId();

		// 赛事分析
		if (TextUtils.isEmpty(content)) {
			holder.analyze.setVisibility(View.GONE);
		} else {
			holder.analyze.setVisibility(View.VISIBLE);
			holder.analyze.setTag(content);
			holder.analyze.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String content = (String) arg0.getTag();
					// 这个Dialog需要传赛事分析文字
					analyzeDialog = new AnalyzeDialog(mContext,
							mAnalyzeListener, content);
					analyzeDialog.show();
				}
			});
		}

		// 我不赞同
		if (TextUtils.isEmpty(gameCanBetBeans.get(position).getSpContent())
				&& TextUtils.isEmpty(gameCanBetBeans.get(position)
						.getRqContent())
				&& TextUtils.isEmpty(gameCanBetBeans.get(position)
						.getBqContent())
				&& TextUtils.isEmpty(gameCanBetBeans.get(position)
						.getJqContent())
				&& TextUtils.isEmpty(gameCanBetBeans.get(position)
						.getBfContent())) {
			holder.dis_agree.setVisibility(View.GONE);
		} else {
			holder.dis_agree.setVisibility(View.VISIBLE);
			holder.dis_agree.setTag(matchid);
			holder.dis_agree.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					final String matchid = (String) arg0.getTag();
					disagreeDialog = new DisagreeDialog(mContext);
					disagreeDialog.show();
					disagreeDialog
							.setOnCompleteListener(new OnCompleteListener() {

								@Override
								public void onComplete(String resultString) {
									if (resultString != null && matchid != null) {
										requestDisagree(matchid, resultString);
									}
								}
							});
				}
			});
		}

		if (holder.dis_agree.getVisibility() == View.GONE
				&& holder.analyze.getVisibility() == View.GONE) {
			holder.recomend_ll_analyze.setVisibility(View.GONE);
			holder.item_view.setVisibility(View.GONE);
		} else {
			holder.recomend_ll_analyze.setVisibility(View.VISIBLE);
			holder.item_view.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	/**
	 * 我不赞同
	 * 
	 * @param matchId
	 * @param content
	 */
	private void requestDisagree(String matchId, String content) {
		// vote = 0代表赞同，vote = 1代表不赞同
		String userid = SharedPrefHelper.getInstance(mContext).getUid();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(RequestMaker.getInstance().getGameTodayRecomend(
				userid, matchId, "1", content));
		mAsyncTask.setOnCompleteListener(mDisagreeCompleteListener);

	}

	private com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener<BaseResponse> mDisagreeCompleteListener = new com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener<BaseResponse>() {

		@Override
		public void onComplete(BaseResponse result, String resultString) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result.errorcode.equals("0")) {
					ToastUtil.showCenterToast(mContext, "投票成功");
				} else {
					ToastUtil.showCenterToast(mContext, result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(mContext, Consts.REQUEST_ERROR);
			}
		}
	};

	public class Holder {
		LinearLayout odds_more;
		TextView game_time;
		TextView match_team;
		TextView league;
		TextView host_team;
		TextView play_type;
		ToggleButton win;
		ToggleButton draw;
		ToggleButton lose;
		LinearLayout analyze_ll;
		TextView analyze;
		ImageView dis_agree;
		View item_view;
		LinearLayout recomend_ll_analyze;
	}

	// private OnClickListener mDisAgreeListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// disagreeDialog.dismiss();
	// }
	// };

	private OnClickListener mAnalyzeListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			analyzeDialog.dismiss();
		}
	};

	/**
	 * 设置开关按钮的状态
	 * 
	 * @param toggleButton
	 * @param text
	 */
	private void setText(ToggleButton toggleButton, Odds odds) {
		String sOdds = odds.getResult() + " " + odds.getOdds();
		toggleButton.setText(sOdds);
		toggleButton.setTextOn(sOdds);
		toggleButton.setTextOff(sOdds);

		toggleButton.setTag(odds);
		if (odds.isChecked()) {
			if (!toggleButton.isChecked()) {
				toggleButton.setChecked(true);
			}
		} else {
			if (toggleButton.isChecked()) {
				toggleButton.setChecked(false);
			}
		}

		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Odds odds = (Odds) arg0.getTag();
				odds.setChecked(arg1);
			}
		});

		toggleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onRefreshListener.onRefresh();
			}
		});
	}

	/**
	 * 设置更多玩法Listener
	 * 
	 * @param onMoreListener
	 */
	public void setOnMoreListener(OnClickListener onMoreListener) {
		this.onMoreListener = onMoreListener;
	}

	/**
	 * 设置刷新Listener
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

}
