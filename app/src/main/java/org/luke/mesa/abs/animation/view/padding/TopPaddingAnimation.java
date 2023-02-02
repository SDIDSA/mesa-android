package org.luke.mesa.abs.animation.view.padding;

import android.view.View;

import org.luke.mesa.abs.animation.base.ViewAnimation;

public class TopPaddingAnimation extends ViewAnimation {
    private int left, right, bottom;

    public TopPaddingAnimation(long duration, View view, float to) {
        super(duration, view, to);
    }

    public TopPaddingAnimation(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        left = view.getPaddingLeft();
        right = view.getPaddingRight();
        bottom = view.getPaddingBottom();
        return view.getPaddingTop();
    }

    @Override
    protected void apply(View view, float v) {
        view.setPadding(left, (int) v, right, bottom);
    }
}
