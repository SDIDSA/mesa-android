package org.luke.mesa.abs.components.controls.scratches;

import android.view.Gravity;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateXAnimation;
import org.luke.mesa.abs.components.controls.shape.Rectangle;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.utils.ViewUtils;

public class Loading extends HBox {
    private static final int count = 4;
    private final Rectangle[] rectangles;
    private ParallelAnimation loader;

    public Loading(App owner, double size) {
        super(owner);
        rectangles = new Rectangle[count];

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);

        setHorizontalGravity(Gravity.CENTER);

        setSpacing((int) size);

        for(int i = 0; i < count; i++) {
            rectangles[i] = new Rectangle(owner, size, size);
            rectangles[i].setRadius(size);
            addView(rectangles[i]);
        }

        float shift = ViewUtils.dipToPx((float) -(size * 2), owner);
        Runnable preLoad = () -> {
            for(Rectangle rect : rectangles) {
                rect.setTranslationX(shift);
            }
            rectangles[0].setAlpha(0);
            rectangles[count - 1].setAlpha(1);
        };

        preLoad.run();

        setTranslationX(-shift / 2);

        loader = new ParallelAnimation(500)
                .addAnimation(new AlphaAnimation(rectangles[0], 1))
                .addAnimation(new AlphaAnimation(rectangles[count - 1], 0))
                .setInterpolator(Interpolator.EASE_OUT).setOnFinished(() -> {
                    preLoad.run();
                    loader.start();
                });
        for(int i = 0; i < count; i++) {
            loader.addAnimation(new TranslateXAnimation(rectangles[i], 0));
        }
    }

    private boolean running = false;

    @Override
    protected void onAttachedToWindow() {
        if(running)
            loader.start();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if(running)
            loader.stop();
        super.onDetachedFromWindow();
    }

    public void startLoading() {
        running = true;
        loader.start();
    }

    public void stopLoading() {
        running = false;
        loader.stop();
    }

    public void setColor(int c) {
        for(Rectangle r : rectangles) {
            r.setFill(c);
        }
    }
}
