package org.luke.mesa.abs.animation.view.padding;

import android.view.View;

import org.luke.mesa.abs.animation.base.ViewAnimation;

public class RightPaddingAnimation extends ViewAnimation {
    private int left, top, bottom;
    public RightPaddingAnimation(long duration, View view, float to) {
        super(duration, view, to);
    }

    public RightPaddingAnimation(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        left = view.getPaddingLeft();
        top = view.getPaddingTop();
        bottom = view.getPaddingBottom();
        return view.getPaddingRight();
    }

    @Override
    protected void apply(View view, float v) {
        view.setPadding(left,top, (int) v,bottom);
    }
}
