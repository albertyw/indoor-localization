package com.Localization;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ErrorReporting {
	private static Context context;
	private static Handler handler;
	public static void initialize(Handler h) {
		handler = h;
	}
	
	public static boolean maybeReportError(String e) {
		if (handler != null) {
			Bundle b = new Bundle();
			b.putString("error", e);
		
			Message m = new Message();
			m.setData(b);
			handler.sendMessage(m);
			return true;
		} else {
			return false;
		}
	}
}
