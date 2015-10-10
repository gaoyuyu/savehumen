package com.gdhysz.util;

public class TimeMgr {
	
	public enum TimeState {
		TIME_NONE,	// 非挑战
		TIME_ING,	// 挑战中
		// TIME_END,	// 挑战结束
	};
	
	public enum SelectState {
		SELECT_TEST,
		SELECT_ONE_MIN,
		SELECT_THIRTY_MIN,
		SELECT_ONE_HOUR,
		SELECT_CUSTOM,		// 自定义
		SELECT_INFI, 		// 无极限挑战
	}
	
	// 倒计时状态
	private static TimeState mState = TimeState.TIME_NONE;
	// 选择挑战的模式
	private static SelectState mSelState = SelectState.SELECT_TEST;
	// 是否挑战成功，若为null则选择无极限挑战
	private static boolean mIsSuccess = false;
	// 是否展示suc效果
	private static boolean mIsShowSuc = false;
	// 正在倒计时的秒数
	private static int n_time = 0;
		
	public static void resetTime() {
		n_time = 0;
	}
		
	public static synchronized void setTime() {
		if(n_time < 0) {
			n_time = 0;
		}
		else {
			++n_time;
		}
	}
		
	public static int getTime() {
		if(n_time < 0) {
			n_time = 0;
		}
		
		return n_time;
	}
	
	/*
	 * 根据所选挑战时间进行核对
	 */
	public static boolean checkTime(int selectTime) {
		if(getIsSuccess()) {
			// 不展示toast也不用去继续比较，同时不影响挑战结果
			return true;
		}
		
		boolean isSuc = n_time >= selectTime;
		setIsSuccess(isSuc);
		return getIsSuccess();
	}
	
	//////////////////////////
	
	public static void setState(TimeState state) {
		mState = state;
	}
		
	public static TimeState getState() {
		return mState;
	}
	
	public static void setSelState(int state) {
		mSelState = SelectState.values()[state];
	}
	
	public static SelectState getSelState() {
		return mSelState;
	}
	
	public static void setIsSuccess(boolean isSuc) {
		mIsSuccess = isSuc;
	}
	
	public static boolean getIsSuccess() {
		return mIsSuccess;
	}
	
	public static void setIsShowSuc(boolean isShow) {
		mIsShowSuc = isShow;
	}
	
	public static boolean getIsShowSuc() {
		return mIsShowSuc;
	}
	
}
