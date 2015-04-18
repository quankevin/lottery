package com.xmtq.lottery.activity;

import java.util.Date;
import java.util.List;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xmtq.lottery.R;
import com.xmtq.lottery.adapter.GameResuleDetailListAdapter;
import com.xmtq.lottery.bean.GameHistoryDateBean;
import com.xmtq.lottery.bean.RecomendHistoryBean;
import com.xmtq.lottery.bean.RecomendHistoryResponse;
import com.xmtq.lottery.network.HttpRequestAsyncTask;
import com.xmtq.lottery.network.HttpRequestAsyncTask.OnCompleteListener;
import com.xmtq.lottery.network.RequestMaker;
import com.xmtq.lottery.utils.DateUtil;
import com.xmtq.lottery.utils.ToastUtil;

/**
 * 赛果详情
 * 
 * @author Administrator
 * 
 */
public class GameResultActivity extends BaseActivity {

	private ListView game_result_detail_list;
	private ImageButton btn_back;
	private TextView tv_gametime;
	private TextView tv_game_week;
	private TextView tv_game_result;
	private GameHistoryDateBean mDateBean;

	// private List<GameHistoryDateBean> mHistoryDateBeansList = new
	// ArrayList<GameHistoryDateBean>();

	@Override
	public void setContentLayout() {
		setContentView(R.layout.game_result_detail);

	}

	@Override
	public void dealLogicBeforeInitView() {

		mDateBean = (GameHistoryDateBean) getIntent().getSerializableExtra(
				"mHistoryBean");
		if (mDateBean != null) {

			request(mDateBean);
		}
	}

	@Override
	public void initView() {
		tv_gametime = (TextView) findViewById(R.id.game_time);
		tv_game_week = (TextView) findViewById(R.id.game_week);
		tv_game_result = (TextView) findViewById(R.id.game_result);

		Date date = DateUtil.stringToDateFormat(mDateBean.getDate(),
				"yyyy-MM-dd");
		String dayOfWeek = DateUtil.getWeek(date);

		tv_game_week.setText(dayOfWeek);
		tv_gametime.setText(mDateBean.getDate());
		tv_game_result.setText(mDateBean.getHitcount() + "/"
				+ mDateBean.getCount());
		if(Integer.parseInt(mDateBean.getHitcount()) > 0){
			tv_game_result.setTextColor(getResources().getColor(R.color.text_gold));
		}

		game_result_detail_list = (ListView) findViewById(R.id.game_result_detail_list);
		btn_back = (ImageButton) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void dealLogicAfterInitView() {

	}

	@Override
	public void onClickEvent(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;

		default:
			break;
		}

	}

	private void request(GameHistoryDateBean mDateBean) {
		mLoadingDialog.show("数据加载中...");
		RequestMaker mRequestMaker = RequestMaker.getInstance();
		HttpRequestAsyncTask mAsyncTask = new HttpRequestAsyncTask();
		mAsyncTask.execute(mRequestMaker.getGameHistorySearch(mDateBean
				.getDate()));
		mAsyncTask.setOnCompleteListener(mOnCompleteListener);
	}

	private OnCompleteListener<RecomendHistoryResponse> mOnCompleteListener = new OnCompleteListener<RecomendHistoryResponse>() {

		@Override
		public void onComplete(RecomendHistoryResponse result,
				String resultString) {
			if (result != null) {
				if (result.errorcode.equals("0")) {
					RecomendHistoryResponse mResponse = result;
					List<RecomendHistoryBean> mHistoryBeansList = mResponse.mRecomendHistoryList;
					if (mHistoryBeansList != null) {
						GameResuleDetailListAdapter mAdapter = new GameResuleDetailListAdapter(
								GameResultActivity.this, mHistoryBeansList);
						game_result_detail_list.setAdapter(mAdapter);
					}
				} else {
					ToastUtil.showCenterToast(GameResultActivity.this,
							result.errormsg);
				}
			} else {
				ToastUtil.showCenterToast(GameResultActivity.this, "数据请求失败");
			}

			mLoadingDialog.dismiss();

		}
	};

}
