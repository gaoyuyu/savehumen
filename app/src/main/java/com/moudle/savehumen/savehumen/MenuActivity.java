package com.moudle.savehumen.savehumen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.moudle.savehumen.R;
import com.moudle.savehumen.util.LockReceiver;
import com.moudle.savehumen.util.ScreenListener;

import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

public class MenuActivity extends Activity implements OnClickListener
{

	private Button normalBtn;
	private Button timingBtn;
	private Button infiniteBtn;
	private Button mUpdateBtn;
	private UpdateResponse ur;
	private boolean isNeedUpdate = false;
	private static final String BMOB_TAG="Bmob Log";
	private static final String TAG="MenuActivity";
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;
	private boolean isFromInfiniteBtn = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		initView();
		setListener();

		ScreenListener screenListener = new ScreenListener(getApplicationContext());
		screenListener.begin(new ScreenListener.ScreenStateListener()
		{
			@Override
			public void onUserPresent()
			{
				// TODO Auto-generated method stub
				Log.i(TAG, "onUserPresent");
				if(isFromInfiniteBtn)
				{
					Log.i(TAG, "无限挑战失败");
				}
				isFromInfiniteBtn = false;
			}

			@Override
			public void onScreenOn()
			{
				// TODO Auto-generated method stub
				Log.i(TAG, "onScreenOn");
			}

			@Override
			public void onScreenOff()
			{
				// TODO Auto-generated method stub
				Log.i(TAG, "onScreenOff");
			}
		});

	}
	private void initView()
	{
		normalBtn = (Button)findViewById(R.id.normalBtn);
		timingBtn = (Button)findViewById(R.id.timingBtn);
		mUpdateBtn = (Button) findViewById(R.id.btnUpdate);
		infiniteBtn = (Button) findViewById(R.id.infiniteBtn);
	}
	private void setListener()
	{
		normalBtn.setOnClickListener(this);
		timingBtn.setOnClickListener(this);
		// 点击按钮进行手动更新操作
		mUpdateBtn.setOnClickListener(this);

		infiniteBtn.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
//		mUpdateBtn.setEnabled(true);
//		if(isNeedUpdate)
//		{
//			mUpdateBtn.setVisibility(View.VISIBLE);
//		}
		super.onResume();
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.normalBtn:
			redirectActivity(MainActivity.class);
			break;
		case R.id.timingBtn:
//			redirectActivity(TimingActivity.class);
			Toast.makeText(MenuActivity.this,"该功能开发中，敬请期待！",Toast.LENGTH_SHORT).show();
			break;
		case R.id.infiniteBtn:
			lockTheScreen();
			isFromInfiniteBtn = true;
//			Message msg =  new Message();
//			msg.what = 1;
//			lockHandler.sendMessage(msg);
			break;
		case R.id.btnUpdate:
			BmobUpdateAgent.forceUpdate(MenuActivity.this);
			break;

		default:
			break;
		}
	}
	public void redirectActivity(Class<?> cls)
	{
		Intent intent = new Intent(MenuActivity.this,cls);
		startActivity(intent);
	}

	public void lockTheScreen()
	{
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(MenuActivity.this,
				LockReceiver.class);
		// 判断是否有权限
		if (mDevicePolicyManager.isAdminActive(mComponentName))
		{
			mDevicePolicyManager.lockNow();
		} else
		{
			activeManager();
		}
	}
	private void activeManager()
	{
		// 激活设备管理器获取权限
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "传说中的一键锁屏");
		startActivityForResult(intent, 1);
		// mDevicePolicyManager.lockNow();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		mDevicePolicyManager.lockNow();

		super.onActivityResult(requestCode, resultCode, data);
	}
}
