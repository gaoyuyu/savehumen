package com.gdhysz.util;

import android.content.Context;

import com.gdhysz.savehumen.R;

public class BackUpUtils {
	private static BackUpUtils mInstance;
	private static Context mContext;
	private final int [] SELECT_LIST_INT;
	
	public static synchronized BackUpUtils getInstance(Context context) {
		mContext = context;
		if(mInstance == null) {
			mInstance = new BackUpUtils();
		}
		
		return mInstance;
	}
	
	public BackUpUtils() {
		SELECT_LIST_INT = mContext.getResources().getIntArray(R.array.select_list_int);	
		
	}
	
	public int getSelectInt(int id) {
		return SELECT_LIST_INT[id];
	}
	
	public String getCorStr(int resID) {
		String str = null;
		String[] mLeastStrs = mContext.getResources().getStringArray(resID);
		int randNum = (int) (Math.random() * mLeastStrs.length);
		str = mLeastStrs[randNum];
		return str;
	}
	
}