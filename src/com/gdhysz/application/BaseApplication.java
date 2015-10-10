package com.gdhysz.application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import android.app.Application;
import android.util.Log;

public class BaseApplication extends Application
{
	public static String APPID = "48be8626becb357738efc503f1770001";

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化BmobSDK
		Bmob.initialize(this, APPID);
//		BmobUpdateAgent.initAppVersion(getApplicationContext());
//		Log.i(BMOB_TAG, "初始化AppVersion表");

	}
}
