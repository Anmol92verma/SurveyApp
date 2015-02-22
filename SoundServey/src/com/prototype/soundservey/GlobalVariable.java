package com.prototype.soundservey;

import android.content.Context;
import android.widget.Toast;

public class GlobalVariable {

	public static final String API_URL = "www.google.com";

	public interface ProgressListener {
		void transferred(long num);
	}

	public static String pos = "position";

	public static void showToast(Context ctx,String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
		
	}

}
