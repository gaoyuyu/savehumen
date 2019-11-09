package com.moudle.savehumen.savehumen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.moudle.savehumen.R;
import com.moudle.savehumen.util.Config;
import com.moudle.savehumen.util.RecordUtil;
import com.moudle.savehumen.util.ScreenShotUtils;
import com.moudle.savehumen.util.ShowConStrUtil;
import com.moudle.savehumen.util.TimeMgr;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ResultActivity extends Activity implements OnClickListener {
	
	private ShowConStrUtil mTeaseUtil;
	private TextView mTextTime;
	private TextView mConStr;
	private TextView mEndDate;
	private Button mShareBtn;
	private static final String TAG="ResultActivity";
	private static final String BAIDU_APK_DOWNLOAD_URL="http://shouji.baidu.com/software/item?docid=7724040&from=as";
	private Handler handler  = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// 分享成功（失败）之后的操作
//			Toast.makeText(ResultActivity.this, "Res Share", Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}
		
	};
	
	private void initResources() {
		String endDayStr = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_WEEKDAY);
		String endTimeStr = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
		
		mTeaseUtil = ShowConStrUtil.createEmptyWorkingUtil(this, TimeMgr.getTime());
		
		mTextTime = (TextView) findViewById(R.id.textFinalTime);
		mConStr = (TextView) findViewById(R.id.textConStr);
		mEndDate = (TextView) findViewById(R.id.textEndDate);
		mShareBtn = (Button)findViewById(R.id.shareBtn);

        int bestScore = RecordUtil.getBestScore(this);
        int lastScore = RecordUtil.getLastScore(this);
        if (bestScore < TimeMgr.getTime()) {
            RecordUtil.setBestScore(this, TimeMgr.getTime());
        }
        if (lastScore != TimeMgr.getTime()) {
        	RecordUtil.setLastScore(this, TimeMgr.getTime());
        }
		
        // mTextTime.setText(TimeMgr.getTime() + "s");
        mTextTime.setText(RecordUtil.getDisplayFormatScore(TimeMgr.getTime()));
		mConStr.setText(mTeaseUtil.getTeaseStr());
		mEndDate.setText(getResources().getString(R.string.endStr) + endDayStr + endTimeStr);
		mShareBtn.setOnClickListener(this);
	}
	
	private void resetConfig() {
		TimeMgr.setIsSuccess(false);
		TimeMgr.setIsShowSuc(false);
	}
	
	private void displayEnd() {
		View bgView = ResultActivity.this.findViewById(R.id.result_imgBg);
		if(TimeMgr.getIsSuccess()) {
			bgView.setBackgroundColor(getResources().getColor(Config.SUC_COLOR_ID));
			resetConfig();
		}
		else {
			// bgView.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_background));
			bgView.setBackgroundColor(getResources().getColor(Config.FAL_COLOR_ID));
		}
	}
	
	
	///////////////////////////////////////
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		initResources();
		displayEnd();
		TimeMgr.resetTime();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.shareBtn:
			String shareTime = mTextTime.getText().toString();

			showShare(shareTime,view);
			break;

		default:
			break;
		}
	}
	

	private void showShare(String shareTime,final View view)
	{
		//截取屏幕并存在sd卡
		ScreenShotUtils.getScreenShotBitmap(this, view);
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("你能hold住吗？");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(BAIDU_APK_DOWNLOAD_URL);
		// text是分享文本，所有平台都需要这个字段
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl("http://10.163.7.91:8080/scrawl/publish/10.jpg");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("哥能在【扔掉手机APP】坚持"+shareTime+"，你能hold住吗？");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(this.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://10.163.7.91:8080/scrawl");
		oks.setText("哥能在【扔掉手机APP】坚持"+shareTime+"，你能hold住吗？");
		oks.setImagePath(ScreenShotUtils.getSDCardPath()+"/Demo/ScreenImages"+"/screenshot.png");
//		oks.setImageUrl("http://img2.cache.netease.com/tech/2015/4/7/2015040709103419de7.jpg");
		Log.i(TAG, ScreenShotUtils.getSDCardPath()+"/Demo/ScreenImages"+"/screenshot.png");
		
		//设置自定义回调
		oks.setCallback(new OneKeyShareCallBack());
		
		// 启动分享GUI
		oks.show(this);
	}
	
	/**
	 * OneKeyShareCallBack
	 * 所有社交平台的回调，这个是子线程，不是主线程，所有UI处理等操作必须使用Handler操作 
	 * @author Gao
	 */
	public class OneKeyShareCallBack implements PlatformActionListener
	{

		@Override
		public void onCancel(Platform arg0, int arg1)
		{
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2)
		{
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
		}

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2)
		{
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(1);
			
		}
		
	}
}
