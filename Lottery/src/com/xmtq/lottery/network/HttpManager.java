package com.xmtq.lottery.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.Proxy;
import android.text.TextUtils;

import com.xmtq.lottery.utils.NetUtil;

public class HttpManager {
	private static final int RESPONSE_TIME_OUT = 10 * 1000;
	private static final int REQUEST_TIME_OUT = 10 * 1000;
	private static final int DEFAULT_MAX_CONNECTIONS = 30;
	public static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;

	public static final int DEFAULT_SOCKET_TIMEOUT_SHORT = 10 * 1000;

	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	private static DefaultHttpClient sHttpClient;

	final static HttpParams httpParams = new BasicHttpParams();
	static {

		ConnManagerParams.setTimeout(httpParams, 1000);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(10));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpClientParams.setRedirecting(httpParams, true);
		HttpProtocolParams.setUserAgent(httpParams, "Android client");
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			schemeRegistry.register(new Scheme("https", sf, 443));
		} catch (Exception ex) {
		}

		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);
		sHttpClient = new DefaultHttpClient(manager, httpParams);
		BasicCookieStore cookieStore = new BasicCookieStore();
		sHttpClient.setCookieStore(cookieStore);
		CookieSpecFactory csf = new CookieSpecFactory() {
			public CookieSpec newInstance(HttpParams params) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin)
							throws MalformedCookieException {

					}
				};
			}
		};
		sHttpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME_OUT);
		sHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				RESPONSE_TIME_OUT);
		sHttpClient.getCookieSpecs().register("oschina", csf);
		sHttpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				"oschina");
		sHttpClient.getParams().setParameter(
				CookieSpecPNames.SINGLE_COOKIE_HEADER, true);
	}

	public static void longTimeOut() {
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT);
		ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
	}

	public static void shortTimeOut() {
		HttpConnectionParams.setSoTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT_SHORT);
		HttpConnectionParams.setConnectionTimeout(httpParams,
				DEFAULT_SOCKET_TIMEOUT_SHORT);
		ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT_SHORT);
	}

	public static CookieStore getCookieStore() {

		return sHttpClient.getCookieStore();
	}

	private HttpManager() {
	}

	public static HttpResponse execute(HttpGet get) throws IOException {
		System.out.println("-------------HttpManager get-------------");
		return sHttpClient.execute(get);
	}

	public static HttpResponse execute(HttpPost post) throws IOException {
		System.out.println("-------------HttpManager post-------------");
		return sHttpClient.execute(post);
	}

	public static HttpResponse execute(HttpUriRequest post) throws IOException {
		System.out
				.println("-------------HttpManager HttpUriRequest post-------------");
		return sHttpClient.execute(post);
	}

	public static HttpResponse execute(HttpHead head) throws IOException {
		return sHttpClient.execute(head);
	}

	public static HttpResponse executeHost(HttpHost host, HttpGet get)
			throws IOException {
		return sHttpClient.execute(host, get);
	}

	public static HttpResponse execute(Context context, HttpGet get)
			throws IOException {
		if (!NetUtil.isNetworkAvailable(context) && isWapNetwork()) {
			setWapProxy();
			return sHttpClient.execute(get);
		}

		final HttpHost host = (HttpHost) sHttpClient.getParams().getParameter(
				ConnRouteParams.DEFAULT_PROXY);
		if (host != null) {
			sHttpClient.getParams().removeParameter(
					ConnRouteParams.DEFAULT_PROXY);
		}

		return sHttpClient.execute(get);
	}

	private static void setSinaWapProxy() {
		final HttpHost para = (HttpHost) sHttpClient.getParams().getParameter(
				ConnRouteParams.DEFAULT_PROXY);
		if (para != null) {
			sHttpClient.getParams().removeParameter(
					ConnRouteParams.DEFAULT_PROXY);
		}
		String host = Proxy.getDefaultHost();
		int port = Proxy.getDefaultPort();
		HttpHost httpHost = new HttpHost(host, port);
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
	}

	public static HttpResponse execute(Context context, HttpUriRequest post)
			throws IOException {
		if (!NetUtil.isNetworkAvailable(context) && isWapNetwork()) {
			setSinaWapProxy();
		}
		return sHttpClient.execute(post);
	}

	public static HttpResponse execute(Context context, HttpPost post)
			throws IOException {
		if (!NetUtil.isNetworkAvailable(context) && isWapNetwork()) {
			setWapProxy();
			return sHttpClient.execute(post);
		}

		final HttpHost host = (HttpHost) sHttpClient.getParams().getParameter(
				ConnRouteParams.DEFAULT_PROXY);
		if (host != null) {
			sHttpClient.getParams().removeParameter(
					ConnRouteParams.DEFAULT_PROXY);
		}
		return sHttpClient.execute(post);
	}

	private static boolean isWapNetwork() {
		final String proxyHost = android.net.Proxy.getDefaultHost();
		return !TextUtils.isEmpty(proxyHost);
	}

	private static void setWapProxy() {
		final HttpHost host = (HttpHost) sHttpClient.getParams().getParameter(
				ConnRouteParams.DEFAULT_PROXY);
		if (host == null) {
			final String host1 = Proxy.getDefaultHost();
			int port = Proxy.getDefaultPort();
			HttpHost httpHost = new HttpHost(host1, port);
			sHttpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
					httpHost);
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
