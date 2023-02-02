package org.luke.mesa.abs.animation.view.padding;

import android.view.View;

import org.luke.mesa.abs.animation.base.ViewAnimation;

public class LeftPaddingAnimation extends ViewAnimation {
    private int top, right,bottom;
    public LeftPaddingAnimation(long duration, View view, float to) {
        super(duration, view, to);
    }

    public LeftPaddingAnimation(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        top = view.getPaddingTop();
        right = view.getPaddingRight();
        bottom = view.getPaddingBottom();
        return view.getPaddingLeft();
    }

    @Override
    protected void apply(View view, float v) {
        view.setPadding((int) v, top,right,bottom);
    }
}
