package com.moudle.savehumen.savehumen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moudle.savehumen.R;
import com.moudle.savehumen.service.MonitorService;
import com.moudle.savehumen.util.BackUpUtils;
import com.moudle.savehumen.util.Config;
import com.moudle.savehumen.util.RecordUtil;
import com.moudle.savehumen.util.TextEftUtil;
import com.moudle.savehumen.util.TimeMgr;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.update.BmobUpdateAgent;


public class MainActivity extends Activity
{

	private TextView tvBestScore;
	private TextView tvLastScore;
	private Button mStartBtn;
	private TextView mTimeText;
	// private boolean mIsStop = false; // 是否处于倒计时状态
	private TextEftUtil txtEftUtil;

	private Timer mTimer = new Timer();
	private TimerTask mTask;

	private class MyTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					TimeMgr.setTime();

					// 挑战成功
					if (TimeMgr.checkTime(BackUpUtils.getInstance(
							MainActivity.this).getSelectInt(
							TimeMgr.getSelState().ordinal())))
					{
						displaySuc(true);
					}

					// mTimeText.setText(TimeMgr.getTime() + "s");
					mTimeText.setText(RecordUtil
							.getDisplayFormatScoreByS(TimeMgr.getTime()));
				}
			});
		}
	}

	private void initResorces()
	{
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		txtEftUtil = TextEftUtil.createTextEftUtil(MainActivity.this,
				MainActivity.this.findViewById(R.id.mainAct),
				Config.MAIN_EFCTXT_NUM);

		mStartBtn = (Button) findViewById(R.id.btnStart);
		
		mTimeText = (TextView) findViewById(R.id.textTime);
		tvLastScore = (TextView) findViewById(R.id.tvLastScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);

		mStartBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				if (TimeMgr.getState() == TimeMgr.TimeState.TIME_NONE)
				{
					displaySelList();
				} else
				{
					if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
					{
						jumpToResultActivity();
					}
				}
			}
		});

	}

	private void displayScore()
	{
		tvLastScore.setText("Last: "
				+ RecordUtil.getDisplayFormatScore(RecordUtil
						.getLastScore(this)));
		tvBestScore.setText("Best: "
				+ RecordUtil.getDisplayFormatScore(RecordUtil
						.getBestScore(this)));
	}

	private void displayBtnType()
	{
		if (TimeMgr.getState() == TimeMgr.TimeState.TIME_NONE)
		{
			mStartBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.start_btn));
			mStartBtn.setText(getResources().getString(R.string.main_startBtn));
		} else if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
		{
			mStartBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.end_btn));
			mStartBtn.setText(getResources().getString(R.string.main_endBtn));
		}
	}

	private void displayTxtEft()
	{
		txtEftUtil.startTextEft();
	}

	/*
	 * 开始倒计时后，弹出选择框，并在点选任一选项后开始倒计时
	 */
	private void displaySelList()
	{
		new AlertDialog.Builder(MainActivity.this)
				.setTitle(getResources().getString(R.string.select_list_title))
				.setItems(
						getResources()
								.getStringArray(R.array.select_list_array),
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
//								mUpdateBtn.setEnabled(false);
								Toast.makeText(MainActivity.this,getResources().getString(R.string.select_list_toast)+ getResources().getStringArray(R.array.select_list_array)[which],
										Toast.LENGTH_LONG).show();

								TimeMgr.setSelState(which);

								startTimer();
							}
						}).show();
	}

	/*
	 * 挑战成功后展示toast 后期将改变背景颜色（成功界面）
	 */
	private void displaySuc(boolean isSet)
	{
		if (TimeMgr.getIsShowSuc())
		{
			return;
		}

		if (isSet)
		{
			Toast.makeText(MainActivity.this, "挑战成功", Toast.LENGTH_LONG).show();
			MainActivity.this.findViewById(R.id.main_imgBg).setBackgroundColor(
					getResources().getColor(Config.SUC_COLOR_ID));
			TimeMgr.setIsShowSuc(true);
		} else
		{
			// MainActivity.this.findViewById(R.id.main_imgBg).setBackgroundDrawable(getResources().getDrawable(R.drawable.list_background));
			MainActivity.this.findViewById(R.id.main_imgBg).setBackgroundColor(
					getResources().getColor(Config.FAL_COLOR_ID));
		}
	}

	/*
	 * 开始倒计时
	 */
	private void startTimer()
	{
		if (mTask != null)
		{
			return;
		}
		TimeMgr.resetTime();
		mTask = new MyTimerTask();
		mTimer.schedule(mTask, 500, 1000);
		TimeMgr.setState(TimeMgr.TimeState.TIME_ING);

		displayBtnType();
		displayTxtEft();
	}

	/*
	 * 结束倒计时
	 */
	private void shutDownTimer()
	{
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		if (null != mTimer && null != mTask)
		{
			mTask.cancel();
			mTimer.purge();
			mTask = null;
		}
	}

	/*
	 * 结束时跳转界面
	 */
	private void jumpToResultActivity()
	{
		shutDownTimer();
		MonitorService.stopMonitor(MainActivity.this);
		txtEftUtil.removeTxtFromLayout();

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		if (intent.resolveActivity(getPackageManager()) != null)
		{
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.fade_out);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		BmobUpdateAgent.setUpdateCheckConfig(true);
		initResorces();

		

	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		displayScore();
		displayBtnType();
		displaySuc(TimeMgr.checkTime(BackUpUtils.getInstance(MainActivity.this)
				.getSelectInt(TimeMgr.getSelState().ordinal())));
		
//		mUpdateBtn.setEnabled(true);
//		if(isNeedUpdate)
//		{
//			mUpdateBtn.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	protected void onPause()
	{
		if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
		{
			MonitorService.startMonitor(this);
		}
		super.onPause();
	}

	@Override
	public void onBackPressed()
	{
		if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
		{
			jumpToResultActivity();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
