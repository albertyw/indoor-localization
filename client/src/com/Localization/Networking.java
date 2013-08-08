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

import android.os.Looper;
import android.util.Log;

public class Networking {
	public static void postData(final String server, final String data) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Looper.prepare();
			    try {
					 // Create a new HttpClient and Post Header
				    HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(server);

			        // Add your data
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("data", data));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			       
				        // Execute HTTP Post Request
				        httpclient.execute(httppost);
			        
			    } catch (Exception e) {
			        Log.d(C.TAG, "Exception while making HTTP Request: " + e.getMessage());
			        ErrorReporting.maybeReportError("Can't connect.");
			    }
			        
			}
		});
		t.start();
	   
	} 
}
