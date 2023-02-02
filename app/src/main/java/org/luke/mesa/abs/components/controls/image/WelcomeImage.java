package org.luke.mesa.abs.components.controls.image;

import android.graphics.Color;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.combine.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public class WelcomeImage extends LayerImage implements Styleable {
    private final SequenceAnimation bounce;

    public WelcomeImage(App owner) {
        super(owner,
                R.drawable.welcome_received_frame,
                R.drawable.welcome_received_content,
                R.drawable.welcome_phone_frame,
                R.drawable.welcome_phone_screen,
                R.drawable.welcome_sent_frame,
                R.drawable.welcome_sent_content,
                R.drawable.welcome_logo
        );

        applyStyle(owner.getStyle());

        float ty = ViewUtils.dipToPx(6, owner);
        float a = .6f;
        long duration = 1200;
        ParallelAnimation bounceSent = new ParallelAnimation()
                .addAnimation(new TranslateYAnimation(getLayer(4), 0, ty))
                .addAnimation(new TranslateYAnimation(getLayer(5), 0, ty))
                .addAnimation(new AlphaAnimation(getLayer(4), 1, a))
                .addAnimation(new AlphaAnimation(getLayer(5), 1, a));

        ParallelAnimation bounceReceived = new ParallelAnimation()
                .addAnimation(new TranslateYAnimation(getLayer(0), 0, ty))
                .addAnimation(new TranslateYAnimation(getLayer(1), 0, ty))
                .addAnimation(new AlphaAnimation(getLayer(0), 1, a))
                .addAnimation(new AlphaAnimation(getLayer(1), 1, a));

        bounce = new SequenceAnimation(duration)
                .addAnimation(bounceReceived)
                .addAnimation(bounceSent)
                .setDelay(-duration * 3 / 4)
                .setInterpolator(Interpolator.EASE_BOTH)
                .setCycleCount(Animation.INDEFINITE)
                .setAutoReverse(true);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == VISIBLE) {
            reset();
            bounce.start();
        } else {
            bounce.stop();
            reset();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    private void reset() {
        getLayer(0).setAlpha(1f);
        getLayer(1).setAlpha(1f);
        getLayer(4).setAlpha(1f);
        getLayer(5).setAlpha(1f);

        getLayer(0).setTranslationY(0);
        getLayer(1).setTranslationY(0);
        getLayer(4).setTranslationY(0);
        getLayer(5).setTranslationY(0);
    }

    @Override
    public void applyStyle(Style style) {
        //received frames
        setColor(0, style.getTextNormal());
        //received content
        setColor(1, style.getBackgroundPrimary());

        //phone frames
        setColor(2, style.getHeaderSecondary());
        //phone screen
        setColor(3, style.getBackgroundSecondary());

        //sent frames
        setColor(4, style.getAccent());
        //sent content
        setColor(5, Color.WHITE);

        //logo
        setColor(6, style.getHeaderSecondary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
