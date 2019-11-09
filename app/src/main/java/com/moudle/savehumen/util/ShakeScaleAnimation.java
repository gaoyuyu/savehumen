package com.moudle.savehumen.util;

import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.Transformation;

/**
 * @Description:
 * @Author: WuRuiqiang
 * @CreateDate: 2015/4/14-0:00
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: [v1.0]
 */
public class ShakeScaleAnimation extends Animation {
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        setDuration(1200);
        setRepeatCount(1);
        setInterpolator(new CycleInterpolator(1));
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Log.v(ShakeScaleAnimation.class.getSimpleName(), "This interpolatedTime is : ---" + interpolatedTime);
        Matrix matrix = t.getMatrix();
        if (interpolatedTime <= 0.25) {
            matrix.preTranslate(-interpolatedTime * 30, 0);
        } else if (interpolatedTime > 0.25 && interpolatedTime <= 0.75) {
            matrix.preTranslate(interpolatedTime * 60, 0);
            matrix.postTranslate(-30, 0);
        } else {
            matrix.preTranslate(-interpolatedTime * 30, 0);
            matrix.postTranslate(30, 0);
        }

    }
}
