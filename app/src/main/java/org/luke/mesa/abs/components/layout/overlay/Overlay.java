package org.luke.mesa.abs.components.layout.overlay;

import android.graphics.Color;
import android.widget.FrameLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.ColorAnimation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.easing.Interpolator;

import java.util.ArrayList;

public abstract class Overlay extends FrameLayout {
    protected final App owner;

    private final ParallelAnimation show, hide;
    private final ArrayList<Runnable> onHidden;

    public Overlay(App owner) {
        super(owner);
        this.owner = owner;
        int shown = Color.argb(192, 0, 0, 0);
        int hidden = Color.argb(0, 0, 0, 0);
        setBackgroundColor(hidden);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


        show = new ParallelAnimation(400)
                .setInterpolator(Interpolator.EASE_OUT);

        show.addAnimation(new ColorAnimation(hidden, shown) {
            @Override
            public void updateValue(int color) {
                setBackgroundColor(color);
            }
        });

        onHidden = new ArrayList<>();

        hide = new ParallelAnimation(400)
                .setInterpolator(Interpolator.EASE_OUT)
                .setOnFinished(() -> {
                    owner.removeOverlay(this);
                    for(Runnable act : onHidden) {
                        act.run();
                    }
                });

        hide.addAnimation(new ColorAnimation(shown, hidden) {
            @Override
            public void updateValue(int color) {
                setBackgroundColor(color);
            }
        });

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

    public boolean isShowing() {
        return show.isRunning();
    }

    public boolean isHiding() {
        return hide.isRunning();
    }

    public void addToShow(Animation animation) {
        show.addAnimation(animation);
    }

    public void addToHide(Animation animation) {
        hide.addAnimation(animation);
    }

    public void addOnHidden(Runnable onHidden) {
        this.onHidden.add(onHidden);
    }

    public abstract void applySystemInsets(Insets insets);
    public abstract void applyInputInsets(boolean shown, Insets insets);
}
