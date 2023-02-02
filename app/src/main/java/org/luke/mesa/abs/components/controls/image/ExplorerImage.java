package org.luke.mesa.abs.components.controls.image;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.combine.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.position.TranslateXAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public class ExplorerImage extends LayerImage implements Styleable {
    private final SequenceAnimation bounce;

    public ExplorerImage(App owner) {
        super(owner,
                R.drawable.explorer_main_center,
                R.drawable.explorer_main_sides,
                R.drawable.explorer_trail_center,
                R.drawable.explorer_trail_sides
        );

        applyStyle(owner.getStyle());

        float ty = ViewUtils.dipToPx(5, owner);
        long duration = 800;
        ParallelAnimation bounceSent = new ParallelAnimation()
                .addAnimation(new TranslateYAnimation(getLayer(2), 0, ty))
                .addAnimation(new TranslateXAnimation(getLayer(2), 0, -ty));

        ParallelAnimation bounceReceived = new ParallelAnimation()
                .addAnimation(new TranslateYAnimation(getLayer(3), 0, ty))
                .addAnimation(new TranslateXAnimation(getLayer(3), 0, -ty));

        ParallelAnimation bounceAll = new ParallelAnimation()
                .addAnimation(new TranslateYAnimation(this, 0, -ty))
                .addAnimation(new TranslateXAnimation(this, 0, ty));

        ViewUtils.setPaddingUnified(this, 60, owner);
        setClipToPadding(false);
        setClipChildren(false);

        bounce = new SequenceAnimation(duration)
                .addAnimation(bounceReceived)
                .addAnimation(bounceSent)
                .addAnimation(bounceAll)
                .setDelay(-duration / 2)
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
        getLayer(2).setTranslationX(0);
        getLayer(3).setTranslationX(0);
    }

    @Override
    public void applyStyle(Style style) {
        //main center
        setColor(0, style.getTextNormal());
        //main sides
        setColor(1, style.getChannelsDefault());

        //trail center
        setColor(2, style.getTextMuted());
        //trail sides
        setColor(3, style.getTextMuted());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
