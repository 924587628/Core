package com.baiyi.core.loader.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.baiyi.core.cache.Cache;
import com.baiyi.core.loader.BaseLoader;
import com.baiyi.core.loader.Loader;
import com.baiyi.core.loader.LoaderResult;
import com.baiyi.core.util.ApnUtils;
import com.baiyi.core.util.ContextUtil;
import com.baiyi.core.util.CoreLog;
import com.baiyi.core.util.DataTypeUtils;

/**
 * 
 * @author tangkun
 * 
 */
public abstract class BaseNetLoder extends BaseLoader implements Loader {
	private final static String TAG = BaseNetLoder.class.getName();
	// private static final int CONN_TIME_OUT = 30000;
	// private static final int READ_TIME_OUT = 90000;
	private static final int BUFFER_SIZE = 8192;
	// public static final String POST_IMAGE = "POST_IMAGE";
	public static final String POST_FORM = "POST_FORM";
	public static final String POST_DATA_Urlencoded = "POST_DATA_Urlencoded";
	public static final String POST_DATA = "POST_DATA";
	public static final String GET_IMAGE = "GET_IMAGE";
	public static final String Method_DELETE = "DELETE";
	public static final String Method_Get = "GET";
	public static final String Method_Post = "POST";

	private String method;

	private int maxRetryTime = 2;
	private int curRetryTime = 0;
	private Cache cache;
	// private int readTimeOut = READ_TIME_OUT;
	// private boolean useGizp = false;
	private String postData = null;
	private String imgName = null;
	private boolean isCanelLoadData;
	private boolean isCacheInMemor;
	private boolean isDisplayImg;
	private Context context = null;
	private String type = POST_DATA;
	private String urlName;
	private String fileName;

	private ArrayList<String[]> contentTextList = new ArrayList<String[]>();
	private ArrayList<byte[]> imgList = new ArrayList<byte[]>();

	public ArrayList<byte[]> getImgList() {
		return imgList;
	}

	/**
	 * 图片流数组-form 图片名称默认为 System.currentTimeMillis()+i+1.jpg
	 * 
	 * @param imgList
	 *            图片流数组
	 */
	public void setImgList(ArrayList<byte[]> imgList) {
		this.imgList = imgList;
	}

	public ArrayList<String[]> getContentTextList() {
		return contentTextList;
	}

	/**
	 * 文字数组-表单
	 * 
	 * @param contentTextList
	 *            文字数组
	 */
	public void setContentTextList(ArrayList<String[]> contentTextList) {
		this.contentTextList = contentTextList;
	}

	public BaseNetLoder(Context context) {
		this.context = context;
	}

	/**
	 * 默认2次
	 * 
	 * @param maxRetryTime
	 *            timeout后执行次数
	 */
	public void setMaxRetryTime(int maxRetryTime) {
		this.maxRetryTime = maxRetryTime;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getUrlName() {
		return urlName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 接口名称
	 * 
	 * @param urlName
	 */
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public String getType() {
		return type;
	}

	/**
	 * 请求 method
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 缓存图片
	 * 
	 * @param cache
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * post数据流
	 * 
	 * @param postData
	 */
	public void setPostData(String postData) {
		this.postData = postData;
	}

	// protected void setUseGizp(boolean useGizp) {
	// this.useGizp = useGizp;
	// }
	protected void setisCanelLoadData(boolean isCanelLoadData) {
		this.isCanelLoadData = isCanelLoadData;
	}

	public boolean isDisplayImg() {
		return isDisplayImg;
	}

	public boolean isCacheInMemor() {
		return isCacheInMemor;
	}

	public void setCacheInMemor(boolean isCacheInMemor) {
		this.isCacheInMemor = isCacheInMemor;
	}

	/**
	 * Gprs下是否显示图片
	 * 
	 * @param isDisplayImg
	 */
	public void setDisplayImg(boolean isDisplayImg) {
		this.isDisplayImg = isDisplayImg;
	}

	public Context getContext() {
		return context;
	}

	/** Http头 */
	private ArrayList<Pair<String, String>> headers;

	private void checkHeaderisNull() {
		if (headers == null) {
			headers = new ArrayList<Pair<String, String>>(3);
		}
	}

	/**
	 * 添加头
	 * 
	 * @param name
	 * @param value
	 */
	public void addRequestHeader(String name, String value) {
		checkHeaderisNull();
		addPairToList(new Pair<String, String>(name, value));
	}

	private void addPairToList(Pair<String, String> p) {
		for (Pair<String, String> pp : headers) {
			if (p.key.equals(pp.key)) {
				pp.value = p.value;
				return;
			}
		}
		headers.add(p);
	}

	/**
	 * 设置请求链接、参数
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		setTag((Object) url);
		if (url == null) {
			throw new IllegalArgumentException("url is null");
		}
	}

	// public void setReadTimeOut(int readTimeOut) {
	// this.readTimeOut = readTimeOut;
	// }

	protected abstract Object covert(byte[] bytes);

	private LoaderResult loadDataFromUrl(String url) {
		CoreLog.i(TAG, "=访问地址=>" + url);
		byte[] data = null;
		boolean isTimeout = true;
		curRetryTime = 0;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
		if (ni == null) {
			return sendErrorMsg(Result_Code_NotNet, "网络故障，请修复你的网络连接");
		}
		if (!ContextUtil.isNetWorking(context)) {
			return sendErrorMsg(Result_Code_NotNet, "网络故障，请修复你的网络连接");
		}
		if (isCanelLoadData && !isDisplayImg) {
			return sendErrorMsg(-1, "");
		}
		boolean isCMWAP = ApnUtils.isCMWAP(context);
		int network_type = ni.getType();
		// 重试机制
		while (curRetryTime < maxRetryTime && isTimeout) {

			// long conn_cost = 0;
			CoreLog.e("start----", "~~~~~~~" + isCancel());
			if (isCancel())
				return sendErrorMsg(Result_Code_Cannel, "");

			int code = 0;
			HttpURLConnection connection = null;
			isTimeout = false;
			// long startTime = System.currentTimeMillis();
			try {

				if (network_type == ConnectivityManager.TYPE_MOBILE) {
					/**
					 * 当前网络移动网络
					 */
					if (ApnUtils.isCMWAP(context) || ApnUtils.isuniWAP(context)) {
						String burl = url;
						if (isCMWAP) {
							burl = cmwapUrl(burl);
						}
						URL _url = new URL(burl);
						connection = (HttpURLConnection) _url.openConnection();
						connection.setDoInput(true);
					} else if (ApnUtils.isctWAP(context)) {
						URL _url = new URL(url);
						Proxy p = new Proxy(java.net.Proxy.Type.HTTP,
								new InetSocketAddress("10.0.0.200", 80));
						connection = (HttpURLConnection) _url.openConnection(p);
					} else {
						URL _url = new URL(url);
						connection = (HttpURLConnection) _url.openConnection();
						connection.setDoInput(true);
					}
				} else {
					/**
					 * WIFI或者其它类型网络
					 */
					URL _url = new URL(url);
					connection = (HttpURLConnection) _url.openConnection();
					connection.setDoInput(true);
				}
				connection.setInstanceFollowRedirects(true);
				connection.setConnectTimeout(30000);
				connection.setReadTimeout(30000);
				HttpURLConnection.setFollowRedirects(true);
				if (isCMWAP) {
					String host = url.substring(7, url.indexOf('/', 7));
					connection.setRequestProperty("X-Online-Host", host);
					connection.setRequestProperty("Connection", "Keep-Alive");
				}
				// connection.setRequestProperty("Contet-Type","application/x-www-form-urlencoded");
				if (imgList.size() > 0 || contentTextList.size() > 0) {
					dealRequestHeaders(connection);
				} else {
					dealRequestHeader(connection);
				}
				// connection.setRequestMethod("POST");

				connection.connect();
				// if(postData!=null){
				// DataOutputStream out=new
				// DataOutputStream(connection.getOutputStream());
				// out.writeBytes(postData);
				// out.flush();
				// out.close();
				// }

				code = connection.getResponseCode();
				CoreLog.i(TAG, "code=" + code);
				// conn_cost = System.currentTimeMillis();
				int length = connection.getContentLength();
				long curByteNum = 0;
				if (code == 200 || code == 201) {
					InputStream input = connection.getInputStream();
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					byte[] buffer = new byte[BUFFER_SIZE];
					int count = 0;
					while ((count = input.read(buffer)) != -1) {
						if (isCancel()) {
							output.close();
							input.close();
							return sendErrorMsg(Result_Code_Cannel, "");
						}
						curByteNum += count;
						output.write(buffer, 0, count);
						if (length > 0)
							sendProcessMsg(curByteNum, length);
					}
					output.flush();
					data = output.toByteArray();
					output.close();
					input.close();
				} else {
					sendErrorMsg(code, "请求服务器出错，代码" + code);
				}
				// long end = System.currentTimeMillis();
				// CoreLog.e(TAG, "接口名称:" + urlName +",请求时间:"+(conn_cost -
				// startTime)+"毫秒,下载时间："+(end-conn_cost)+"毫秒,数据大小:"+curByteNum);
			} catch (Exception ex) {
				if (curRetryTime >= maxRetryTime) {
					if (ex instanceof SocketTimeoutException) {
						isTimeout = true;
						CoreLog.i(TAG, "读取数据超时,请点击重试");
						// ex.printStackTrace();
						return sendErrorMsg(Reuslt_Code_ReadTimeOut,
								"读取数据超时,请点击重试");
						// }
					} else if (ex instanceof ConnectTimeoutException) {
						isTimeout = true;
						CoreLog.i(TAG, "连接超时,请点击重试");
						// ex.printStackTrace();
						return sendErrorMsg(Result_Code_NetTimeOut,
								"连接超时,请点击重试");

						// }
					} else {
						CoreLog.i(TAG, "发生意外：");
						// ex.printStackTrace();
						return sendErrorMsg(Result_Code_NotNet,
								"网络故障，请修复你的网络连接");
					}
				}
			} finally {
				curRetryTime++;
				if (connection != null)
					connection.disconnect();
			}
		}
		CoreLog.e("exit----", "~~~~~~~" + isCancel());
		if (isCancel()) {
			return sendErrorMsg(-1, "用户退出");
		}
		LoaderResult visitResult = new LoaderResult();
		visitResult.setCode(1);
		visitResult.setResult(data);
		return visitResult;
	}

	/**
	 * 从一个链接中获取主机部分
	 * 
	 * @param url
	 * @return
	 */
	public static String getUrlHost(String url) {
		URI u = URI.create(url);
		return u.getHost();
	}

	public final String cmwapUrl(String url) {
		int contentBeginIdx = url.indexOf('/', 7);
		StringBuffer urlStringBuffer = new StringBuffer("http://10.0.0.172:80");
		urlStringBuffer.append(url.substring(contentBeginIdx));
		return urlStringBuffer.toString();
	}

	/**
	 * 以下代码用于兼容Ophone
	 * 
	 * @param c
	 * @return
	 */
	public static boolean prepareInternetApn(Context c, String apnname) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo().isConnected()
				&& cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}

		NetworkInfo[] netInfos = cm.getAllNetworkInfo();
		String in;
		for (NetworkInfo ni : netInfos) {
			in = getNetworkInterfaceName(ni);
			if (ni.isConnected() && apnname.equals(in)) {
				return true;
			}
		}
		cm.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, apnname);
		for (NetworkInfo ni : netInfos) {
			in = getNetworkInterfaceName(ni);
			if (ni.isConnected() && apnname.equals(in)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过反射获取Ophone的getAptype函数
	 * 
	 * @param ni
	 * @return
	 */
	private static String getNetworkInterfaceName(NetworkInfo ni) {
		try {
			Class<?> c = ni.getClass();
			Class<?>[] cc = new Class[0];
			Method getApType = c.getMethod("getApType", cc);
			if (getApType == null) {
				return null;
			}
			Object[] o = new Object[0];
			return (String) getApType.invoke(ni, o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	StringBuffer buffer = new StringBuffer();

	public void dealRequestHeaders(HttpURLConnection wapconn)
			throws IOException {
		/*** 设置头属性 */
		wapconn.setRequestProperty("Http-version", "HTTP/1.1");
		// wapconn.setRequestProperty("Accept-Encoding", "gzip");
		// wapconn.setRequestProperty("Accept-Encoding", "identity");
		if (!DataTypeUtils.isEmpty(headers)) {
			for (Pair<String, String> p : headers) {
				wapconn.setRequestProperty(p.key, p.value);
			}
		}
		/** "设置请求方法" */
		wapconn.setRequestMethod(method);
		if (Method_Post.equals(method)) {
			String twoHyphens = "--";
			String boundary = UUID.randomUUID().toString();
			// String boundary = "---------------------------123821742118716";
			String lineEnd = "\r\n";
			if (type.equals(POST_FORM)) {
				wapconn.setRequestProperty("Connection", "Keep-Alive");
//				wapconn.setRequestProperty("User-Agent",
//						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
//				wapconn.setRequestProperty("Accept-Language",
//						"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//				wapconn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//				wapconn.setRequestProperty("Accept",
//						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				wapconn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
			}
			if (type.equals(POST_FORM)) {
				// ==================================================================================================================
				wapconn.setDoOutput(true);
				DataOutputStream dos = new DataOutputStream(
						wapconn.getOutputStream());
				for (int i = 0; i < imgList.size(); i++) {
					StringBuffer sb = new StringBuffer();
					sb.append(lineEnd + twoHyphens + boundary + lineEnd);
					String imgName = System.currentTimeMillis() + i + ".jpg";
					sb.append("Content-Disposition: form-data; name=\"")
							.append(getFileName()).append("\"; filename=\"")
							.append(imgName).append("\"").append(lineEnd);
					sb.append("Content-Type: image/jpeg" + lineEnd + lineEnd);
					dos.writeBytes(sb.toString());
					byte[] data = imgList.get(i);
					dos.write(data, 0, data.length);

					// log............打应
					buffer.append(sb.toString());
					buffer.append(data.toString());
					buffer.append(lineEnd);
				}

				// ====================================================================================================================
				if (contentTextList != null) {
					for (String[] parsm : contentTextList) {

						String key = parsm[0];
						String value = parsm[1];
						dos.writeBytes(twoHyphens + boundary + lineEnd);
						buffer.append(twoHyphens + boundary + lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\""
								+ key + "\"" + lineEnd + lineEnd);
						buffer.append("Content-Disposition: form-data; name=\""
								+ key + "\"" + lineEnd + lineEnd);
						dos.write(value.getBytes("UTF-8"));
						buffer.append(value);
						dos.writeBytes(lineEnd);
						buffer.append(lineEnd);
					}
				}
				dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens
						+ lineEnd);
				buffer.append(lineEnd + twoHyphens + boundary + twoHyphens
						+ lineEnd);
				CoreLog.d(TAG, buffer.toString());
				dos.flush();
			} else {
				OutputStream os = wapconn.getOutputStream();
				os.write(postData.getBytes());
				os.flush();
				os.close();
			}
		}

	}

	public void dealRequestHeaderss(HttpURLConnection wapconn)
			throws IOException {
		if (method.equals(Method_Post)) {
			return;
		}
		/*** 设置头属性 */
		wapconn.setRequestProperty("Http-version", "HTTP/1.1");
		wapconn.setRequestMethod(method);
		wapconn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		wapconn.setRequestProperty("Content-Length", ""
				+ postData.getBytes().length);
		wapconn.setRequestMethod(method);

		wapconn.setDoOutput(true);
		OutputStream os = wapconn.getOutputStream();
		String post = new String(postData);
		CoreLog.i(TAG, "application/x-www-form-urlencoded : " + post);
		os.write(postData.getBytes());
		os.flush();
		os.close();
	}

	public void dealRequestHeader(HttpURLConnection wapconn) throws IOException {
		/*** 设置头属性 */
		wapconn.setRequestProperty("Http-version", "HTTP/1.1");
		if (!DataTypeUtils.isEmpty(headers)) {
			for (Pair<String, String> p : headers) {
				wapconn.setRequestProperty(p.key, p.value);
			}
		}

		StringBuffer logBuffer = new StringBuffer();
		/** "设置请求方法" */
		wapconn.setRequestMethod(method);
		if (Method_Post.equals(method)) {
			String twoHyphens = "--";
			String boundary = UUID.randomUUID().toString();
			String lineEnd = "\r\n";
			if (type.equals(POST_FORM)) {
				wapconn.setRequestProperty("Connection", "Keep-Alive");
				wapconn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
			} else if (type.equals(POST_DATA_Urlencoded)) {
				/*** 设置头属性 */
				wapconn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				wapconn.setRequestProperty("Content-Length",
						"" + postData.getBytes().length);

			} else {
				// wapconn.setRequestProperty("Content-Type",
				// "application/x-www-form-urlencoded");
				wapconn.setRequestProperty("Content-Type", "application/json");
				// wapconn.setRequestProperty("Content-Type",
				// "application/json");
				wapconn.setRequestProperty("Content-Length",
						"" + postData.getBytes().length);
			}
			if (type.equals(POST_FORM)) {
				// ==================================================================================================================
				DataOutputStream dos = new DataOutputStream(
						wapconn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(twoHyphens + boundary + lineEnd);
				sb.append("Content-Disposition: form-data; name=\"")
						.append("pic").append("\"; filename=\"")
						.append(imgName).append("\"").append(lineEnd);
				sb.append("Content-Type: image/jpeg" + lineEnd);
				sb.append(lineEnd);
				dos.writeBytes(sb.toString());
				dos.write(postData.getBytes(), 0, postData.getBytes().length);
				dos.writeBytes(lineEnd);
				logBuffer.append(sb.toString() + lineEnd);

				// ====================================================================================================================
				if (contentTextList != null) {
					for (String[] parsm : contentTextList) {

						String key = parsm[0];
						String value = parsm[1];
						dos.writeBytes(twoHyphens + boundary + lineEnd);
						logBuffer.append(twoHyphens + boundary + lineEnd);
						dos.writeBytes("Content-Disposition: form-data; name=\""
								+ key + "\"" + lineEnd + lineEnd);
						logBuffer
								.append("Content-Disposition: form-data; name=\""
										+ key + "\"" + lineEnd + lineEnd);
						dos.write(value.getBytes("UTF-8"));
						dos.writeBytes(lineEnd);
						logBuffer.append(value + lineEnd);
					}
				}
				CoreLog.d(TAG, logBuffer.toString());
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				dos.flush();
			} else {
				if (DataTypeUtils.isEmpty(postData)) {
					return;
				}
				wapconn.setDoOutput(true);
				OutputStream os = wapconn.getOutputStream();
				String post = new String(postData);
				CoreLog.i(TAG, post);
				os.write(postData.getBytes());
				os.flush();
				os.close();
			}
		} else if (Method_DELETE.equals(getMethod())) {

		} else {

		}

	}

	private LoaderResult loadDataFromCache(String url) {
		LoaderResult result = null;
		Object o = null;
		if (isCanelLoadData && !isDisplayImg) {
			result = new LoaderResult();
			result.setCode(1);
			result.setResult(o);
			return result;
		}
		o = cache.get(url);
		if (isCancel()) {
			result = new LoaderResult();
			result.setCode(1);
			result.setResult(o);
			return result;
		}
		if (isCacheInMemor && o == null) {
			result = new LoaderResult();
			result.setCode(1);
			result.setResult(o);
			return result;
		}
		if (o == null) {
			result = loadDataFromUrl(url);
			if (isCancel()) {
				return result;
			}
			if (result.getCode() < 0) {
				return result;
			}
			o = result.getResult();
			if (o != null) {
				cache.put(url, o);
				if (isCancel()) {
					return result;
				}
			}
			return result;
		}
		result = new LoaderResult();
		result.setCode(1);
		result.setResult(o);
		return result;
	}

	@Override
	protected LoaderResult onVisitor() {
		LoaderResult result = null;
		if (getTag() == null) {
			return sendErrorMsg(-1, "错误的URL");
		}
		String url = getTag().toString();
		if (isCacheInMemor && cache != null) {
			return loadDataFromCache(getTag().toString());
		}
		if (cache != null) {
			result = loadDataFromCache(getTag().toString());
		} else {
			result = loadDataFromUrl(getTag().toString());
		}
		if (result.getCode() < 0) {
			return result;
		}

		if (result.getResult() != null) {
			Object o = covert((byte[]) result.getResult());
			if (o == null) {
				if (cache != null) {
					result = loadDataFromUrl(url);
					if (result == null) {
						return sendCompleteMsg();
					}
					if (result.getCode() < 0) {
						return result;
					}
					if (result.getResult() != null) {
						o = covert((byte[]) result.getResult());
					}
				}
			}
			setResult(o);
			return sendCompleteMsg();
		}
		result.setTag(getTag());
		return result;
	}

}
