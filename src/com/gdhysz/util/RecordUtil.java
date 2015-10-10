package com.gdhysz.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Description:
 * @Author: WuRuiqiang
 * @CreateDate: 2015/3/23-21:25
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: [v1.0]
 */
public class RecordUtil {
    /**
     * 记录成绩信息的SharePreferences对应KEY值
     */
    final static String SCORE_SP_KEY = "score_key";
    /**
     * 上一次成绩
     */
    final static String LAST_SCORE_KEY = "last_score_key";
    /**
     * 最好成绩
     */
    final static String BEST_SCORE_KEY = "best_score_key";


    /**
     * 返回上一次成绩
     * @param context 上下文
     * @return 上一次成绩
     */
    public static int getLastScore(Context context) {
        return getScore(LAST_SCORE_KEY, context);
    }

    /**
     * 返回最好成绩
     * @param context 上下文
     * @return 最好成绩
     */
    public static int getBestScore(Context context) {
        return getScore(BEST_SCORE_KEY, context);
    }

    /**
     * 存储上一次成绩
     * @param context 上下文
     * @param score 分数
     */
    public static void setLastScore(Context context, int score) {
        setScore(LAST_SCORE_KEY, context, score);
    }

    /**
     * 存储最好成绩
     * @param context 上下文
     * @param score 分数
     */
    public static void setBestScore(Context context, int score) {
        setScore(BEST_SCORE_KEY, context, score);
    }

    /**
     * 把“秒”为单位的成绩转换为显示格式“**h**min**sec”成绩
     * @param score 以“秒”为单位的成绩
     * @return 显示格式的成绩
     */
    public static String getDisplayFormatScore(int score) {
        return score / 60 / 60 + "小时" + score / 60 % 60 + "分钟" + score % 60 + "秒";
    }
    
    /**
     * 把“秒”为单位的成绩转换为显示格式“**:**:**:”成绩
     * @param score 以“秒”为单位的成绩
     * @return 显示格式的成绩
     */
    public static String getDisplayFormatScoreByS(int score) {
    	if(score <= 0) {
    		return "00:00:00";
    	}
    	
    	final int ONE_HOURS_SEC = 3600;
    	final int ONE_MIN_SEC = 60;
    	
    	StringBuilder hoursStr = new StringBuilder(String.valueOf(score / ONE_HOURS_SEC));
    	StringBuilder minsStr = new StringBuilder(String.valueOf(score / ONE_MIN_SEC));
    	StringBuilder secsStr = new StringBuilder(String.valueOf(score % ONE_MIN_SEC));
    	
    	if(hoursStr.length() <= 1) {
    		hoursStr = hoursStr.insert(0, 0);
    	}
    	
    	if(minsStr.length() <= 1) {
    		minsStr = minsStr.insert(0, 0);
    	}
    	
    	if(secsStr.length() <= 1) {
    		secsStr = secsStr.insert(0, 0);
    	}
    	
        return hoursStr + ":" + minsStr + ":" + secsStr;
    }

    /**
     * 根据不同的KEY获取相应成绩
     * @param key 不同的成绩对应的KEY值
     * @param context 上下文
     * @return 成绩
     */
    public static int getScore(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SCORE_SP_KEY, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 将不同的成绩存入SharePreferences中.
     * @param key 不同成绩对应的KEY
     * @param context 上下文
     * @param score 要存入的成绩
     */
    public static void setScore(String key, Context context, int score) {
        SharedPreferences sp = context.getSharedPreferences(SCORE_SP_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, score);
        editor.apply();
    }
}
