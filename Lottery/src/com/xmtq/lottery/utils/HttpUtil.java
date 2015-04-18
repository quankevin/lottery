package com.xmtq.lottery.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {
	
	/**
	 * 向指定的url发送一个http post请求,使用httpClient请求
	 * @param strUrl
	 * @param params
	 * @return
	 * @throws NetException 
	 * @throws ServerException 
	 */
    public static String postHttpClient(String strUrl, Map<String, String> params, String body) {
    	Log.d("Debug","postHttpClient url:"+strUrl);
        DefaultHttpClient httpClient = null;
        HttpPost post = null;
        HttpEntity entity = null;
        try {
            httpClient = new DefaultHttpClient();
            post = new HttpPost(strUrl);
            
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3600*000); // 15000
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3600*1000); // 15000
            
            // 准备参数
            if(params != null){
            	List<NameValuePair> pairParams = new ArrayList<NameValuePair>();
            	for(String key : params.keySet()){
            		pairParams.add(new BasicNameValuePair(key,params.get(key)));
            	}
            	post.setEntity(new UrlEncodedFormEntity(pairParams,HTTP.UTF_8));
            }
           
            if(body != null){
            	post.setEntity(new StringEntity(body, HTTP.UTF_8));
            }
            
            HttpResponse httpResponse = httpClient.execute(post);
            //服务器成功返回响应
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");Log.d("Debug", result);
                return result;	
            } else {
            	// 服务器错误
            	Log.d("Debug","postHttpClient response SC_ERROR");
            	return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                }
            }
            if (post != null) {
            	post.abort();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }
    
    public static String getHttpClient(String url){
    	Log.d("Debug","getHttpClient url："+url);
		HttpResponse httpResponse;
		try {
			HttpGet httpRequest = new HttpGet(url);// 建立http get联机
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200){
    		    String result = EntityUtils.toString(httpResponse.getEntity());// 获取相应的字符串
    		    Log.d("Debug","getHttpClient response SC_OK");
    		    return result;
    		}else {
            	Log.d("Debug","getHttpClient response SC_ERROR:"+httpResponse.getStatusLine().getStatusCode());
            	return null;
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}// 发出http请求
    }
    
    public static String httpConntect(String strUrl) {
    	URL url;
		try {
			Log.d("Debug","httpConntect url:"+strUrl);
			url = new URL(strUrl);
			URLConnection connection =  url.openConnection();  
		    connection.connect();  
		    InputStream is = url.openStream(); 
		    
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String temp;
			while((temp = reader.readLine()) != null){
				sb.append(temp);
			}
			Log.d("Debug","httpConntect response:"+sb.toString());
			return sb.toString();
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
    }
    

    
    
}
