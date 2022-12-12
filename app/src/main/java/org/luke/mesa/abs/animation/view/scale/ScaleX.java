package org.luke.mesa.abs.animation.view.scale;

import android.view.View;

import org.luke.mesa.abs.animation.abs.ViewAnimation;

public class ScaleX extends ViewAnimation {
    public ScaleX(long duration, View view, float to) {
        super(duration, view, to);
    }

    public ScaleX(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        return view.getScaleX();
    }

    @Override
    protected void apply(View view, float v) {
        view.setScaleX(v);
    }
}
