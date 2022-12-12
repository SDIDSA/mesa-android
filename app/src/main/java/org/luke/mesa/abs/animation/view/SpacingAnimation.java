package org.luke.mesa.abs.animation.view;

import android.view.View;

import org.luke.mesa.abs.animation.abs.ViewAnimation;
import org.luke.mesa.abs.components.layout.linear.LinearBox;

public class SpacingAnimation extends ViewAnimation {

    public SpacingAnimation(long duration, View view, float to) {
        super(duration, view, to);
        check(view);
    }

    public SpacingAnimation(View view, float to) {
        super(view, to);
        check(view);
    }

    public void check(View view) {
        if(!(view instanceof LinearBox)) {
            throw new IllegalArgumentException("the spacing animation only works on LinearBox views");
        }
    }

    @Override
    protected float getFrom(View view) {
        return (float) ((LinearBox) view).getSpacing();
    }

    @Override
    protected void apply(View view, float v) {
        ((LinearBox) view).setSpacing(v);
    }
}
