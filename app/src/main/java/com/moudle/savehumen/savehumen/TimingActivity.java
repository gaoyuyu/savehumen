package com.moudle.savehumen.savehumen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.moudle.savehumen.R;
import com.moudle.savehumen.util.LockReceiver;
import com.moudle.savehumen.util.ScreenListener;
import com.moudle.savehumen.util.TimeSection;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimingActivity extends Activity implements OnClickListener
{
	private EditText dateEdit;
	private EditText startEdit,endEdit;
	private Calendar calendar;
	private int yearSeted,monthSeted,daySeted;
	private static final String TAG="TimingActivity";
	private TimeSection timeSection;
	private Button doneButton;
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;
	private boolean isFromBtn = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timing);
		
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
				
				long currentTimeMillis = System.currentTimeMillis();
				
				Log.i(TAG, "onUserPresent-start-->"+timeSection.getStartTime());
				Log.i(TAG, "onUserPresent-end-->"+timeSection.getEndTime());
				Log.i(TAG, "onUserPresent-System.currentTimeMillis()-->"+System.currentTimeMillis());
				if(isFromBtn)
				{
					if(currentTimeMillis <timeSection.getStartTime() || currentTimeMillis > timeSection.getEndTime())
					{
						Toast.makeText(TimingActivity.this, "挑战成功",Toast.LENGTH_SHORT).show();
						Log.i(TAG, "挑战成功");
					}
					else
					{
						Toast.makeText(TimingActivity.this, "挑战失败",Toast.LENGTH_SHORT).show();
						Log.i(TAG, "挑战失败");
					}
				}
				isFromBtn = false;
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

	private void setListener()
	{
		startEdit.setOnClickListener(this);
		endEdit.setOnClickListener(this);
		dateEdit.setOnClickListener(this);
		doneButton.setOnClickListener(this);
	}

	private void initView()
	{
		//初始化TimeSection存2个时间节点
		timeSection = TimeSection.getInstance();
		timeSection.setStartTime(-1);
		timeSection.setEndTime(-1);
		//initView
		dateEdit = (EditText)findViewById(R.id.dateEdit);
		startEdit = (EditText)findViewById(R.id.start);
		endEdit = (EditText)findViewById(R.id.end);
		doneButton = (Button)findViewById(R.id.timingChallengeBtn);
		//初始化date
		calendar = Calendar.getInstance();
		yearSeted= calendar.get(Calendar.YEAR);
		monthSeted = calendar.get(Calendar.MONTH);
		daySeted = calendar.get(Calendar.DAY_OF_MONTH);
		dateEdit.setText(yearSeted+"-"+(monthSeted+1)+"-"+daySeted);
		//初始化2个时间节点
		startEdit.setText("00 : 00");
		endEdit.setText("00 : 00");
	}
	
	public void lockTheScreen()
	{
		mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mComponentName = new ComponentName(TimingActivity.this,
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
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.dateEdit:
			showDatePickerDialog(v);
			break;
		case R.id.start:
			showTimePickerDialog(R.id.start,v);
			break;
		case R.id.end:
			showTimePickerDialog(R.id.end,v);
			break;
		case R.id.timingChallengeBtn:
			lockTheScreen();
			isFromBtn = true;
			break;

		default:
			break;
		}
		
	}
	private void showDatePickerDialog(final View v)
	{
		LinearLayout datePickerLayout = (LinearLayout) TimingActivity.this.getLayoutInflater().inflate(R.layout.datepicker, null);
		final DatePicker datePicker = (DatePicker)datePickerLayout.findViewById(R.id.datePicker1);
		AlertDialog datePickerDialog = new AlertDialog.Builder(TimingActivity.this)
		.setTitle("设置")
		.setView(datePickerLayout)
		.setPositiveButton("完成",  new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				yearSeted= datePicker.getYear();
				monthSeted = datePicker.getMonth();
				daySeted = datePicker.getDayOfMonth();
				EditText ed = (EditText)v;
				ed.setText(yearSeted+"-"+(monthSeted+1)+"-"+daySeted);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
	private void showTimePickerDialog(final int id,final View v)
	{
		// TODO Auto-generated method stub
		LinearLayout timePickerLayout = (LinearLayout)TimingActivity.this.getLayoutInflater().inflate(R.layout.timepicker, null);
		final TimePicker timePicker = (TimePicker)timePickerLayout.findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
		AlertDialog datePickerDialog = new AlertDialog.Builder(TimingActivity.this)
		.setTitle("设置")
		.setView(timePickerLayout)
		.setPositiveButton("完成",  new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				int currentHour = timePicker.getCurrentHour();
				int currentMin = timePicker.getCurrentMinute();
				EditText ed = (EditText)v;
				String currentTime = ((currentHour >= 0 && currentHour <= 9)?"0"+currentHour:currentHour)+"："+((currentMin >= 0 && currentMin <= 9)?"0"+currentMin:currentMin);
				ed.setText(currentTime);
				calendar.set(yearSeted, monthSeted, daySeted,currentHour,currentMin);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Log.i(TAG,"TimeInMillis is ："+calendar.getTimeInMillis()+"-------"+simpleDateFormat.format(calendar.getTime()));
				Log.i(TAG, timeSection.toString());
				if(id == R.id.start)
				{
					timeSection.setStartTime(calendar.getTimeInMillis());
					Log.i(TAG, "start："+timeSection.toString());
				}
				
				if(id == R.id.end)
				{
					timeSection.setEndTime(calendar.getTimeInMillis());
					Log.i(TAG, "end："+timeSection.toString());
				}
				
				if(timeSection.getStartTime() != -1 && timeSection.getEndTime() != -1)
				{
					Log.i(TAG, " timeSection 初始化完毕");
					
					if(timeSection.getStartTime() > timeSection.getEndTime())
					{
						Log.i(TAG, "start > end ");
						startEdit.setTextColor(Color.RED);
						endEdit.setTextColor(Color.RED);
						Toast.makeText(TimingActivity.this, "亲，开始时间不能大于结束时间哦~", Toast.LENGTH_SHORT).show();
					}else if( timeSection.getStartTime() == timeSection.getEndTime())
					{
						Log.i(TAG, "start = end ");
						startEdit.setTextColor(Color.RED);
						endEdit.setTextColor(Color.RED);
						Toast.makeText(TimingActivity.this, "这位亲，不能填2个相同的时间哦~", Toast.LENGTH_SHORT).show();
					}
					else
					{
						startEdit.setTextColor(Color.BLACK);
						endEdit.setTextColor(Color.BLACK);
					}
				}
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
}

