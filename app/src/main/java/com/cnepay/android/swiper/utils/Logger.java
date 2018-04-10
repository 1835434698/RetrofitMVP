package com.cnepay.android.swiper.utils;

import android.util.Log;

import com.cnepay.android.swiper.MainApp;


public class Logger {

	public final static boolean isDebug = true;

	public final static int ALL = 6;
	public final static int VERBOSE = 5;
	public final static int DEBUG = 4;
	public final static int INFO = 3;
	public final static int WARN = 2;
	public final static int ERROR = 1;

	public static int LOG_LEVEL = (MainApp.WEB_ENVIRONMENT.equalsIgnoreCase(MainApp.WEB_ENVIRONMENT_PRODUCT) && !MainApp.PRODUCT_DEBUG)? WARN:ALL;

	public static void v(String tag, String msg) {
		if (LOG_LEVEL > VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL > DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL > INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL > WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL > ERROR) {
			Log.e(tag, msg);
		}
	}
}
