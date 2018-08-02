package com.greylabs.itunesalbums.tools;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class FunAnimator {
    private View viewToAnimate;
    private Long duration = 400L;

    public FunAnimator(View viewToAnimate) {
        this.viewToAnimate = viewToAnimate;
    }

    public void startAnimator() {
        scaleView(viewToAnimate, duration, 0L, 1f, 1.06f, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                scaleView(viewToAnimate, duration, 0L, 1.06f, 1f, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startAnimator();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void scaleView(View v, Long duration, Long offset, Float startScale, Float endScale, Animation.AnimationListener listener) {
        ScaleAnimation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true);
        anim.setDuration(duration);
        anim.setStartOffset(offset);

        anim.setAnimationListener(listener);
        v.startAnimation(anim);
    }
}
