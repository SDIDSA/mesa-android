package org.luke.mesa.abs.animation.view.scale;

import android.view.View;

import org.luke.mesa.abs.animation.abs.ViewAnimation;

public class ScaleY extends ViewAnimation {
    public ScaleY(long duration, View view, float to) {
        super(duration, view, to);
    }

    public ScaleY(View view, float to) {
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
