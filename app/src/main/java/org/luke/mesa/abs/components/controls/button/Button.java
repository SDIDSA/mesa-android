package org.luke.mesa.abs.components.controls.button;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.scratches.Loading;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.BooleanProperty;

public class Button extends FrameLayout {
    private final App owner;
    private final GradientDrawable background;
    private final Loading loading;
    private final BooleanProperty disabled;
    Label label;
    private Runnable onClick;
    private boolean dimOnTouch = true;

    public Button(App owner, String text) {
        super(owner);
        this.owner = owner;
        background = new GradientDrawable();
        setRadius(7);
        ViewUtils.setPaddingUnified(this, 15, owner);

        disabled = new BooleanProperty(false);

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        label = new Label(owner, text);
        label.setLayoutGravity(Gravity.CENTER);

        loading = new Loading(owner, 8);

        addView(label);

        loading.setAlpha(0);
        loading.setColor(Color.WHITE);

        setOnTouchListener((view, event) -> {
            if (!isClickable()) {
                return true;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (dimOnTouch)
                        background.setAlpha(150);
                    break;
                case MotionEvent.ACTION_UP:
                    background.setAlpha(255);
                    view.performClick();
                    break;
            }
            return true;
        });

        setOnClickListener(e -> {
            if (onClick != null) {
                onClick.run();
            }
        });

        disabled.addListener((obs, ov, nv) -> {
            setEnabled(!nv);
            setAlpha(nv ? .6f : 1f);
        });

        setClickable(true);
        setFocusable(false);
        setBackground(background);
    }

    public BooleanProperty disabledProperty() {
        return disabled;
    }

    public void setDimOnTouch(boolean dimOnTouch) {
        this.dimOnTouch = dimOnTouch;
    }

    public void startLoading() {
        if (indexOfChild(loading) == -1)
            addView(loading, 0);

        label.setAlpha(0);
        loading.setAlpha(1);
        loading.startLoading();
        setClickable(false);
    }

    public void stopLoading() {
        if (indexOfChild(loading) != -1)
            removeView(loading);

        loading.stopLoading();
        label.setAlpha(1);
        loading.setAlpha(0);
        setClickable(true);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
        setFocusable(true);
    }

    public void setRadius(float radius) {
        background.setCornerRadius(ViewUtils.dipToPx(radius, owner));
    }

    public void setBackgroundColor(int color) {
        background.setColor(color);
    }

    public void setTextFill(int color) {
        label.setTextColor(color);
    }

    public void setFont(Font font) {
        label.setFont(font);
    }
}