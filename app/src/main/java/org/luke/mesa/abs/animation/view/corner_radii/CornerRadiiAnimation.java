package org.luke.mesa.abs.animation.view.corner_radii;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import org.luke.mesa.abs.animation.base.ViewAnimation;

public class CornerRadiiAnimation extends ViewAnimation {
    public CornerRadiiAnimation(long duration, View view, float to) {
        super(duration, view, to);
        check(view);
    }

    public CornerRadiiAnimation(View view, float to) {
        super(view, to);
        check(view);
    }

    private void check(View view) {
        if(!(view.getBackground() instanceof GradientDrawable)) {
            throw new IllegalArgumentException("invalid background type...");
        }
    }

    @Override
    protected float getFrom(View view) {
        return ((GradientDrawable)view.getBackground()).getCornerRadius();
    }

    @Override
    protected void apply(View view, float v) {
        ((GradientDrawable)view.getBackground()).setCornerRadius(v);
    }
}
