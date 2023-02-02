package org.luke.mesa.abs.components.layout.overlay;

import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.base.ValueAnimation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.abs.utils.functional.ObjectSupplier;
import org.luke.mesa.data.property.Property;

public abstract class SlideOverlay extends Overlay implements Styleable {
    protected final VBox list;
    protected final GradientDrawable background;
    protected int height;
    private float initTY, initPY, lastY, velocity;
    private long lastTime;

    public SlideOverlay(App owner) {
        super(owner);

        background = new GradientDrawable();
        int cr = ViewUtils.dipToPx(10, owner);
        background.setCornerRadii(new float[]{
                cr, cr,
                cr, cr,
                0, 0,
                0, 0
        });

        list = new VBox(owner);
        list.setBackground(background);
        list.setTranslationY(owner.getScreenHeight());

        list.setOnClickListener(e -> {
            //consume
        });

        addView(list);



        addToShow(new TranslateYAnimation(list, 0)
                .setLateTo(() -> (float) (owner.getScreenHeight() - height))
                .setLateFrom(() -> (float) owner.getScreenHeight()));

        addToHide(new TranslateYAnimation(list, 0)
                .setLateTo(() -> (float) owner.getScreenHeight()));


        Animation releaseShow = new TranslateYAnimation(300, list, 0)
                .setLateTo(() -> (float) (owner.getScreenHeight() - height)).setInterpolator(Interpolator.EASE_OUT);

        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initTY = event.getRawY();
                    initPY = list.getTranslationY();
                    lastY = initTY;
                    break;
                case MotionEvent.ACTION_UP:
                    float fdy = event.getRawY() - initTY;
                    if (Math.abs(fdy) < 10) {
                        v.performClick();
                    } else {
                        long dy = System.currentTimeMillis() - lastTime;
                        if (dy > 300) {
                            velocity = 0;
                        }
                        if (velocity > 10) {
                            hide();
                        } else if (velocity < -10) {
                            releaseShow.start();
                        } else {
                            int min = owner.getScreenHeight() - height;
                            int max = owner.getScreenHeight();
                            int mid = (max + min) / 2;
                            if (list.getTranslationY() > mid) {
                                hide();
                            } else {
                                releaseShow.start();
                            }
                        }
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    float nty = event.getRawY();
                    velocity = nty - lastY;
                    float dy = nty - initTY;
                    float ny = initPY + dy;
                    lastY = nty;
                    list.setTranslationY(Math.max(ny, owner.getScreenHeight() - height));
                    lastTime = System.currentTimeMillis();
                    break;
            }
            return true;
        });

        applyStyle(owner.getStyle());
    }

    protected void setHeight(int height) {
        this.height = height;
        LayoutParams params = new LayoutParams(owner.getScreenWidth(), height);
        list.setLayoutParams(params);
    }

    protected void setHeightFactor(double factor) {
        setHeight((int) (owner.getScreenHeight() * factor));
    }

    @Override
    public void applyStyle(Style style) {
        background.setColor(style.getBackgroundMobilePrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
