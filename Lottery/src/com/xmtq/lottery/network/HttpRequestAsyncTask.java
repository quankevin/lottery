package com.xmtq.lottery.network;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.xmtq.lottery.Consts;
import com.xmtq.lottery.bean.BaseResponse;
import com.xmtq.lottery.utils.LogUtil;

public class HttpRequestAsyncTask extends
		AsyncTask<Request, Void, BaseResponse> {

	private String resultString;
	private OnCompleteListener onCompleteListener;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected BaseResponse doInBackground(Request... params) {

		BaseResponse object = null;
		try {
			Request request = params[0];
			/**
			 * 获取BaseUrl
			 */
			String urlString = Consts.host;
			if (urlString == null) {
				resultString = "NoUrl";
				return null;
			}

			HttpResponse httpResponse;
			HttpPost httpPost = null;

			/**
			 * POST请求方式
			 */

			httpPost = new HttpPost(urlString);
			httpPost.setEntity(new StringEntity(request.getBody(), "UTF-8"));
			HttpManager.shortTimeOut();
			httpResponse = HttpManager.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				LogUtil.log("getStatusCode:"
						+ httpResponse.getStatusLine().getStatusCode());
				if (null != httpPost) {
					httpPost.abort();
				}

			} else {
				resultString = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8");

				LogUtil.log("resultString:" + resultString);
				object = request.getXmlParser().parse(resultString);
			}
		} catch (ClientProtocolException e) {
			resultString = "timeover";
			e.printStackTrace();
		} catch (ParseException e) {
			resultString = "wrong";
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
	}

	@Override
	protected void onPostExecute(BaseResponse result) {
		super.onPostExecute(result);
		if (null != onCompleteListener) {
			onCompleteListener.onComplete(result, resultString);
		}
	}

	public interface OnCompleteListener<T extends BaseResponse> {
		public void onComplete(T result, String resultString);
	}

	public OnCompleteListener getOnCompleteListener() {
		return onCompleteListener;
	}

	public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}
}
