package com.gdhysz.util;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockReceiver extends DeviceAdminReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("--->DeviceAdminReceiver !!", "接收");
		super.onReceive(context, intent);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		Log.i("--->DeviceAdminReceiver !!", "激活");
		super.onEnabled(context, intent);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		Log.i("--->DeviceAdminReceiver !!", "取消");
		super.onDisabled(context, intent);
	}

}