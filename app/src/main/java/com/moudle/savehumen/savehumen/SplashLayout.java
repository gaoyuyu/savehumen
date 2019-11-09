package com.moudle.savehumen.savehumen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.moudle.savehumen.R;


/**
 * @author gaoyuyu
 * time: 2019/9/10 0010 11:14
 */
public class SplashLayout extends ConstraintLayout {

    private AppCompatImageView splashBubble1;
    private AppCompatImageView splashBubble2;
    private AppCompatImageView splashIcon;
    private AppCompatTextView splashTv;
    private AppCompatImageView splashBubble3;
    private AppCompatImageView splashBubble4;
    private SplashActivity activity;
    private AnimatorSet animatorSet = null;

    private void assignViews() {
        splashBubble1 = (AppCompatImageView) findViewById(R.id.splash_bubble_1);
        splashBubble2 = (AppCompatImageView) findViewById(R.id.splash_bubble_2);
        splashIcon = (AppCompatImageView) findViewById(R.id.splash_icon);
        splashTv = (AppCompatTextView) findViewById(R.id.splash_tv);
        splashBubble3 = (AppCompatImageView) findViewById(R.id.splash_bubble_3);
        splashBubble4 = (AppCompatImageView) findViewById(R.id.splash_bubble_4);
    }

    public SplashLayout(Context context) {
        super(context);
    }

    public SplashLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplashLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SplashLayout attach(SplashActivity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        assignViews();
        ObjectAnimator iconXAnim = ObjectAnimator.ofFloat(splashIcon, "scaleX", 0f, 1f, 1f);
        ObjectAnimator iconYAnim = ObjectAnimator.ofFloat(splashIcon, "scaleY", 0f, 1f, 1f);
        ObjectAnimator tvXAnim = ObjectAnimator.ofFloat(splashTv, "scaleX", 0f, 1f, 1f);
        ObjectAnimator tvYAnim = ObjectAnimator.ofFloat(splashTv, "scaleY", 0f, 1f, 1f);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(iconXAnim, iconYAnim, tvXAnim, tvYAnim);
        animatorSet.setDuration(2000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                activity.loadData();
            }
        });
    }

    /**
     * 圆形气泡动画
     */
    private void bubbleAnim() {
        ObjectAnimator b1x = ObjectAnimator.ofFloat(splashBubble1, "translationX", 0f, 100f, 0f);
        ObjectAnimator b1y = ObjectAnimator.ofFloat(splashBubble1, "translationY", splashBubble1.getTranslationY(), splashBubble1.getTranslationY() + 50, splashBubble1.getTranslationY() - 30);

        ObjectAnimator b2sx = ObjectAnimator.ofFloat(splashBubble2, "scaleX", 0.8f, 1f);
        ObjectAnimator b2sy = ObjectAnimator.ofFloat(splashBubble2, "scaleY", 0.8f, 1f);

        ObjectAnimator b3x = ObjectAnimator.ofFloat(splashBubble3, "translationX", splashBubble3.getTranslationX() + 80, splashBubble3.getTranslationX());

        ObjectAnimator b4sx = ObjectAnimator.ofFloat(splashBubble4, "scaleX", 0.8f, 1f);
        ObjectAnimator b4sy = ObjectAnimator.ofFloat(splashBubble4, "scaleY", 0.8f, 1f);

        b1x.setRepeatCount(ValueAnimator.INFINITE);
        b1x.setRepeatMode(ValueAnimator.REVERSE);
        b1y.setRepeatCount(ValueAnimator.INFINITE);
        b1y.setRepeatMode(ValueAnimator.REVERSE);
        b2sx.setRepeatCount(ValueAnimator.INFINITE);
        b2sx.setRepeatMode(ValueAnimator.REVERSE);
        b2sy.setRepeatCount(ValueAnimator.INFINITE);
        b2sy.setRepeatMode(ValueAnimator.REVERSE);

        b3x.setRepeatCount(ValueAnimator.INFINITE);
        b3x.setRepeatMode(ValueAnimator.REVERSE);

        b4sx.setRepeatCount(ValueAnimator.INFINITE);
        b4sx.setRepeatMode(ValueAnimator.REVERSE);
        b4sy.setRepeatCount(ValueAnimator.INFINITE);
        b4sy.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(b1x, b1y, b2sx, b2sy, b3x, b4sx, b4sy);
        animatorSet.setDuration(5000);
        animatorSet.start();

    }

    public void playAnim() {
        if (animatorSet != null && !animatorSet.isRunning()) {
            animatorSet.start();
        }
        bubbleAnim();
    }
}
