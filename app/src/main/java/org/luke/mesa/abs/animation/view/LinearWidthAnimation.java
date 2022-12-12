package org.luke.mesa.abs.animation.view;

import android.view.View;
import android.widget.LinearLayout;

import org.luke.mesa.abs.animation.abs.ViewAnimation;

public class LinearWidthAnimation extends ViewAnimation {
    private int height;
    public LinearWidthAnimation(long duration, View view, float to) {
        super(duration, view, to);
        check(view);
    }

    public LinearWidthAnimation(View view, float to) {
        super(view, to);
        check(view);
    }

    public void check(View view) {
        if(!(view.getLayoutParams() instanceof LinearLayout.LayoutParams)) {
            throw new IllegalArgumentException("can't use linear width animation on non linear children");
        }
    }

    @Override
    protected float getFrom(View view) {
        height = view.getLayoutParams().height;
        return view.getLayoutParams().width;
    }

    @Override
    protected void apply(View view, float v) {
        view.setLayoutParams(new LinearLayout.LayoutParams((int) v, height));
    }
}
