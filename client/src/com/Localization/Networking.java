package com.Localization;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Looper;
import android.util.Log;

public class Networking {
	private static AsyncHttpClient client = new AsyncHttpClient();

	private static double lastTime = 0.0;
	private static int requests = 0;

	public static void postData(final String server, final String data) {
		if (client == null) {
			client = new AsyncHttpClient();
		}
		RequestParams params = new RequestParams();
		params.put("data", data);
		client.post(server, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				++requests;
				if (lastTime +1000.0<System.currentTimeMillis()) {
					Log.d(C.TAG, "Requests in last seconds: " + requests);
					requests = 0;
					lastTime = System.currentTimeMillis();

				}

			}
			
			@Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				ErrorReporting.maybeReportError("Packet dropped due to: " + arg0.getMessage());
			}
		});
	} 
}
