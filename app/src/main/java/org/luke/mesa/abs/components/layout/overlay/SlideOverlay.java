package org.luke.mesa.abs.components.layout.overlay;

import android.util.Log;
import android.view.MotionEvent;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public abstract class SlideOverlay extends Overlay implements Styleable {
    protected final VBox list;
    private int height;

    private float initTY, initPY, lastY, velocity;

    private long lastTime;

    public SlideOverlay(App owner) {
        super(owner);

        list = new VBox(owner);
        list.setTranslationY(owner.getScreenHeight());

        list.setOnClickListener(e -> {
            //consume
        });

        addView(list);

        addToShow(new TranslateYAnimation(list, 0)
                .setLateTo(() -> (float) (owner.getScreenHeight() - height)));

        addToHide(new TranslateYAnimation(list, 0)
                .setLateTo(() -> (float) owner.getScreenHeight()));


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
                        if(dy > 300) {
                            velocity = 0;
                        }
                        if (velocity > 10) {
                            hide();
                        } else if (velocity < -10) {
                            show();
                        } else {
                            int min = owner.getScreenHeight() - height;
                            int max = owner.getScreenHeight();
                            int mid = (max + min) / 2;
                            if (list.getTranslationY() > mid) {
                                hide();
                            } else {
                                show();
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
        ViewUtils.setPadding(list, 15, 15, 15, 0, owner);
    }

    protected void setHeightFactor(double factor) {
        setHeight((int) (owner.getScreenHeight() * factor));
    }

    @Override
    public void applyStyle(Style style) {
        list.setBackgroundColor(style.getBackgroundMobilePrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
