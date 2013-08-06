package com.Localization;

import android.content.Context;
import android.widget.Toast;

public class ErrorReporting {
	private static Context context;
	public static void initialize(Context c) {
		context = c;
	}
	
	public static boolean maybeReportError(String e) {
		if (context != null) {
			Toast.makeText(context, e, Toast.LENGTH_SHORT).show();
			return true;
		} else {
			return false;
		}
	}
}
