package org.luke.mesa.abs.components.controls.image;

import android.graphics.Color;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.Animation;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.abs.SequenceAnimation;
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
                .addAnimation(new TranslateYAnimation(getLayer(4), ty).setOverrideFrom(0))
                .addAnimation(new TranslateYAnimation(getLayer(5), ty).setOverrideFrom(0))
                .addAnimation(new AlphaAnimation(getLayer(4), a).setOverrideFrom(1))
                .addAnimation(new AlphaAnimation(getLayer(5), a).setOverrideFrom(1));

        ParallelAnimation bounceReceived = new ParallelAnimation()
                .addAnimation(new TranslateYAnimation(getLayer(0), ty).setOverrideFrom(0))
                .addAnimation(new TranslateYAnimation(getLayer(1), ty).setOverrideFrom(0))
                .addAnimation(new AlphaAnimation(getLayer(0), a).setOverrideFrom(1))
                .addAnimation(new AlphaAnimation(getLayer(1), a).setOverrideFrom(1));

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
            bounce.start();
        } else {
            bounce.stop();
        }
        super.onWindowVisibilityChanged(visibility);
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
