package org.luke.mesa.abs.animation.view.position;

import android.view.View;

import org.luke.mesa.abs.animation.abs.ViewAnimation;

public class TranslateXAnimation extends ViewAnimation {
    public TranslateXAnimation(long duration, View view, float to) {
        super(duration, view, to);
    }

    public TranslateXAnimation(View view, float to) {
        super(view, to);
    }

    @Override
    protected float getFrom(View view) {
        return view.getTranslationX();
    }

    @Override
    protected void apply(View view, float v) {
        view.setTranslationX(v);
    }
}
