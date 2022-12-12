package org.luke.mesa.abs.animation.view.position;

import android.view.View;

import org.luke.mesa.abs.animation.abs.ViewAnimation;

public class TranslateYAnimation extends ViewAnimation {
    public TranslateYAnimation(long duration, View view, float to) {
        super(duration, view, to);
    }

    public TranslateYAnimation(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        return view.getTranslationY();
    }

    @Override
    protected void apply(View view, float v) {
        view.setTranslationY(v);
    }
}
