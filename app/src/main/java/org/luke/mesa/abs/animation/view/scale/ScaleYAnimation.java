package org.luke.mesa.abs.animation.view.scale;

import android.view.View;

import org.luke.mesa.abs.animation.base.ViewAnimation;

public class ScaleYAnimation extends ViewAnimation {
    public ScaleYAnimation(long duration, View view, float to) {
        super(duration, view, to);
    }

    public ScaleYAnimation(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        return view.getScaleY();
    }

    @Override
    protected void apply(View view, float v) {
        view.setScaleY(v);
    }
}
