package com.gdhysz.util;

import java.util.ArrayList;
import java.util.List;

import com.gdhysz.savehumen.R;
import com.gdhysz.util.TimeMgr.TimeState;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextEftUtil {
	
	private static Context mContext;
	private static View mView;
	private static TextEftUtil mInstance;
	// anim：在res/anim里设置即可
    private static final int[] mAnimIDs;
	private static final int[] mTxtColors;
	static {
		mAnimIDs = new int[] {
			R.anim.alpha_scale,
			R.anim.alpha_scale2,
			R.anim.alpha_scale3,
			R.anim.alpha_scale4,
			R.anim.alpha_scale5,
		};
		mTxtColors = new int[] {
			R.color.darkorange,
			R.color.navy,
			R.color.lightsteelblue,
			R.color.chocolate,
			R.color.indianred,
			R.color.aquamarine,
		};
	}
	
	private List<TextView> mViews;
	
	// 每个 view 动画的监听事件
	AnimationListener mAsListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			if(TimeMgr.getState() == TimeState.TIME_NONE) {
				return;
			}
			
			for(TextView txtView : mViews) {
				Animation anim = txtView.getAnimation();
				if(anim != null) {
					if(anim.hasEnded()) {
						mInstance.addTxtViewToLayout(mView, txtView);
					}
				}
				else {
					mInstance.addTxtViewToLayout(mView, txtView);
				}
			}
		}
	};
	
	public TextEftUtil() {
		mViews = new ArrayList<TextView>();
	}
	
	public static synchronized void setInstance(Context context, View view) {
		if(mInstance == null) {
			mInstance = new TextEftUtil();
		}
		mContext = context;
		mView = view;
    }

    public static TextEftUtil createTextEftUtil(Context context, View view, int num) {
		TextEftUtil.setInstance(context, view);
		TextView txtView; 
		for(int i=0; i<num; i++) {
			txtView = new TextView(context);
			txtView.setTypeface(Typeface.MONOSPACE);
            mInstance.mViews.add(txtView);
		}
		return mInstance;
	}
	
	public void startTextEft() {
		for(TextView txtView : mInstance.mViews) {
			mInstance.addTxtViewToLayout(mView, txtView);
		}
	}
	
	public int[] getPos(boolean isHor) {
		if(mView == null || !(mView instanceof View)) {
			return new int[] {};
		}
		
		int viewWidth = mView.getWidth();
		int viewHeight = mView.getHeight();
		int endPosX = 0;
		if(isHor) {
			endPosX = viewWidth/3;
		}
		else {
			endPosX = viewWidth-50;
		}
		int x = 0 + (int) (Math.random() * endPosX);
		int y = viewHeight/5 + (int) (Math.random() * viewHeight/3);
		
		return new int[] {x, y};
	}
	
	/*
	 * 	在view里添加text
	 *  设置控件所在的位置posX, posY，并且不改变宽高
	 *  绝对位置
	 */
	public void addTxtViewToLayout(View view, TextView txtView) {
		// 2/5的概率，是水平还是竖直
		boolean isHor = (int)(Math.random() * 6) > 3;
		int[] pos = getPos(isHor);
		int posX = pos[0];
		int posY = pos[1];
		
		RelativeLayout relView = (RelativeLayout) view;
		MarginLayoutParams margin = new MarginLayoutParams(relView.getLayoutParams());
		if(!isHor) {
			margin.setMargins(posX, posY, 0, 0);
		}
		else {
			// 50 应该改为run中获取1个字符的长度，改
			margin.setMargins(posX, posY, mView.getWidth()-posX-50, 0);
		}
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		if(txtView.getParent() != null) {
			txtView.setLayoutParams(layoutParams);
			txtView.setVisibility(View.VISIBLE);
		}
		else {
			relView.addView(txtView, layoutParams);
		}
		
		mInstance.loadAnimation(txtView);
		txtView.setText(BackUpUtils.getInstance(mContext).getCorStr(R.array.remind_str));
		txtView.setTextSize(25);
	}
	
	public void removeTxtFromLayout() {
		for(TextView txtView : mInstance.mViews) {
			txtView.clearAnimation();
			txtView.setVisibility(View.GONE);
		}
	}
	
	private void loadAnimation(TextView txtView) {
		int range = (int) (Math.random() * mAnimIDs.length);
		int animID = mAnimIDs[range];
        // int colorRange = (int) (Math.random() * mTxtColors.length);
		Animation animation = AnimationUtils.loadAnimation(mContext, animID);
        animation.setAnimationListener(mAsListener);
        txtView.startAnimation(animation);
		txtView.setTextColor(mContext.getResources().getColor(mTxtColors[range]));
	}
	
}
