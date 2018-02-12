package com.venson.create_user;

import android.content.Context;
import android.widget.Toast;

public class Common {
	// Android官方模擬器連結本機web server可以直接使用 http://10.0.2.2
//	public static String URL = "http://192.168.196.202:8080/Ex_TextToJson_Web";
	public final static String URL = "http://10.0.2.2:8080/EasyGo_MySQL_Web/";
	public static void showToast(Context context, int messageResId) {
		Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
	}
	public final static String PREF_FILE = "preference";

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
