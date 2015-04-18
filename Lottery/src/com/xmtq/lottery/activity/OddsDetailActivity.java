package com.xmtq.lottery.activity;

import java.util.List;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.OddsGridviewAdapter;
import com.xmtq.lottery.bean.GameCanBetBean;
import com.xmtq.lottery.bean.Odds;
import com.xmtq.lottery.widget.MyGridView;

/**
 * 赔率详情
 * 
 * @author Administrator
 * 
 */
public class OddsDetailActivity extends BaseActivity {

	private TextView match_team;
	private TextView host_team;
	private GameCanBetBean gameCanBetBean;
	private int position;
	private ToggleButton sp_odds_win;
	private ToggleButton sp_odds_draw;
	private ToggleButton sp_odds_lose;
	private ToggleButton rq_odds_win;
	private ToggleButton rq_odds_draw;
	private ToggleButton rq_odds_lose;
	private MyGridView bfGridView;
	private MyGridView jqGridView;
	private MyGridView bqGridView;
	private Button button_cancle;
	private Button button_ok;

	@Override
	public void setContentLayout() {
		setContentView(R.layout.odds_detail);

	}

	@Override
	public void dealLogicBeforeInitView() {
		gameCanBetBean = (GameCanBetBean) getIntent().getSerializableExtra(
				"GameCanBetBean");
		position = getIntent().getIntExtra("position", 0);
	}

	@Override
	public void initView() {
		match_team = (TextView) findViewById(R.id.match_team);
		host_team = (TextView) findViewById(R.id.host_team);
		sp_odds_win = (ToggleButton) findViewById(R.id.sp_odds_win);
		sp_odds_draw = (ToggleButton) findViewById(R.id.sp_odds_draw);
		sp_odds_lose = (ToggleButton) findViewById(R.id.sp_odds_lose);
		rq_odds_win = (ToggleButton) findViewById(R.id.rq_odds_win);
		rq_odds_draw = (ToggleButton) findViewById(R.id.rq_odds_draw);
		rq_odds_lose = (ToggleButton) findViewById(R.id.rq_odds_lose);
		bfGridView = (MyGridView) findViewById(R.id.bf_gridview);
		jqGridView = (MyGridView) findViewById(R.id.jq_gridview);
		bqGridView = (MyGridView) findViewById(R.id.bq_gridview);
		button_cancle = (Button) findViewById(R.id.button_cancle);
		button_ok = (Button) findViewById(R.id.button_ok);

		button_cancle.setOnClickListener(this);
		button_ok.setOnClickListener(this);

		initOddsView();
	}

	@Override
	public void dealLogicAfterInitView() {

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.button_cancle:
			finish();
			break;

		case R.id.button_ok:
			saveData();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化赔率详情
	 */
	private void initOddsView() {
		if (gameCanBetBean != null) {
			match_team.setText(gameCanBetBean.getMatchTeam() + "(主)");
			host_team.setText(gameCanBetBean.getHostTeam() + "(客)");

			// 胜负平
			List<Odds> spOddsList = gameCanBetBean.getSpOddsList();
			if (spOddsList.size() > 0) {
				for (int i = 0; i < spOddsList.size(); i++) {
					Odds odds = spOddsList.get(i);
					if (odds.getResult().equals("胜")) {
						setText(sp_odds_win, odds);
					} else if (odds.getResult().equals("平")) {
						setText(sp_odds_draw, odds);
					} else if (odds.getResult().equals("负")) {
						setText(sp_odds_lose, odds);
					}
				}
			}

			// 让球胜负平
			List<Odds> rqOddsList = gameCanBetBean.getRqOddsList();
			if (rqOddsList.size() > 0) {
				for (int i = 0; i < rqOddsList.size(); i++) {
					Odds odds = rqOddsList.get(i);
					if (odds.getResult().equals("胜")) {
						setText(rq_odds_win, odds);
					} else if (odds.getResult().equals("平")) {
						setText(rq_odds_draw, odds);
					} else if (odds.getResult().equals("负")) {
						setText(rq_odds_lose, odds);
					}
				}
			}

			// 比分
			List<Odds> bfOddsList = gameCanBetBean.getBfOddsList();
			if (bfOddsList.size() > 0) {
				OddsGridviewAdapter adapter = new OddsGridviewAdapter(this,
						bfOddsList);
				bfGridView.setAdapter(adapter);
			}

			// 进球
			List<Odds> jqOddsList = gameCanBetBean.getJqOddsList();
			if (bfOddsList.size() > 0) {
				OddsGridviewAdapter adapter = new OddsGridviewAdapter(this,
						jqOddsList);
				jqGridView.setAdapter(adapter);
			}

			// 半全场
			List<Odds> bqOddsList = gameCanBetBean.getBqOddsList();
			if (bfOddsList.size() > 0) {
				OddsGridviewAdapter adapter = new OddsGridviewAdapter(this,
						bqOddsList);
				bqGridView.setAdapter(adapter);
			}
		}
	}

	/**
	 * 保存数据，回传到上个Activity
	 */
	private void saveData() {
		Intent intent = new Intent();
		intent.putExtra("GameCanBetBean", gameCanBetBean);
		intent.putExtra("position", position);
		setResult(RESULT_OK, intent);

		finish();
	}

	/**
	 * 设置开关按钮状态
	 * 
	 * @param toggleButton
	 * @param text
	 */
	private void setText(ToggleButton toggleButton, Odds odds) {
		String text = odds.getResult() + " " + odds.getOdds();
		toggleButton.setText(text);
		toggleButton.setTextOn(text);
		toggleButton.setTextOff(text);

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
