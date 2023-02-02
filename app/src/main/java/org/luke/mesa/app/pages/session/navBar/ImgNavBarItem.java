package org.luke.mesa.app.pages.session.navBar;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewOutlineProvider;

import androidx.annotation.DrawableRes;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.base.ValueAnimation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.components.controls.image.ColorIcon;
import org.luke.mesa.abs.components.controls.image.Image;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public class ImgNavBarItem extends NavBarItem implements Styleable {
    private final Image image;
    private final Animation focus, unfocus;

    public ImgNavBarItem(App owner) {
        this(owner, Integer.MIN_VALUE, 27f);
    }

    public ImgNavBarItem(App owner, float size) {
        this(owner, Integer.MIN_VALUE, size);
    }

    public ImgNavBarItem(App owner, @DrawableRes int resId) {
        this(owner, resId, 27f);
    }

    public ImgNavBarItem(App owner, @DrawableRes int resId, float size) {
        super(owner);
        if (resId == Integer.MIN_VALUE) {
            image = new Image(owner);
            GradientDrawable back = new GradientDrawable();
            back.setCornerRadius(ViewUtils.dipToPx(size, owner));
            image.setBackground(back);
            image.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
            image.setClipToOutline(true);
        } else {
            image = new ColorIcon(owner, resId);
        }

        image.setClickable(false);
        image.setFocusable(false);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        image.setLayoutParams(params);

        addView(image);
        applyStyle(owner.getStyle());

        float focusSize = size * 1.2f;
        focus = new ParallelAnimation(300)
                .addAnimation(new AlphaAnimation(image, 1f))
                .addAnimation(new ValueAnimation(size, focusSize) {
                    @Override
                    public void updateValue(float v) {
                        applySize(v);
                    }
                }).setInterpolator(Interpolator.EASE_OUT);

        unfocus = new ParallelAnimation(300)
                .addAnimation(new AlphaAnimation(image, .5f))
                .addAnimation(new ValueAnimation(focusSize, size) {
                    @Override
                    public void updateValue(float v) {
                        applySize(v);
                    }
                }).setInterpolator(Interpolator.EASE_OUT);

        unselect();
    }

    private void applySize(float size) {
        float padding = (HEIGHT - size) / 2;
        ViewUtils.setPadding(this, 0, padding, 0, padding, owner);
        image.setSize((int) size);
    }

    @Override
    public void select() {
        unfocus.stop();
        focus.start();
    }

    @Override
    public void unselect() {
        focus.stop();
        unfocus.start();
    }

    public void setBitmap(BitmapDrawable drawable) {
        image.setImageDrawable(drawable);
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);

        if (image instanceof ColorIcon) {
            ((ColorIcon) image).setColor(style.getTextNormal());
        }
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
