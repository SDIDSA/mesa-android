package org.luke.mesa.abs.components.layout.overlay;

import android.graphics.Color;
import android.widget.FrameLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.abs.Animation;
import org.luke.mesa.abs.animation.easing.Interpolator;

public abstract class Overlay extends FrameLayout {
    protected final App owner;

    private final ParallelAnimation show, hide;

    public Overlay(App owner) {
        super(owner);
        this.owner = owner;
        setAlpha(0);
        setBackgroundColor(Color.argb(192, 0, 0, 0));
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        show = new ParallelAnimation(400)
                .setInterpolator(Interpolator.EASE_OUT);

        show.addAnimation(new AlphaAnimation(this, 1));


        hide = new ParallelAnimation(400)
                .setInterpolator(Interpolator.EASE_OUT)
                .setOnFinished(() -> owner.removeOverlay(this));

        hide.addAnimation(new AlphaAnimation(400, this, 0));

        setOnClickListener(e -> hide());
    }

    public void show() {
        hide.stop();
        owner.addOverlay(this);
        show.start();
    }

    public void hide() {
        show.stop();
        hide.start();
    }

    public void addToShow(Animation animation) {
        show.addAnimation(animation);
    }

    public void addToHide(Animation animation) {
        hide.addAnimation(animation);
    }

    public abstract void applyInsets(Insets insets);
}
