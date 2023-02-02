package org.luke.mesa.app.pages.session.sessionFragments.main.left;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.ColorAnimation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.corner_radii.CornerRadiiAnimation;
import org.luke.mesa.abs.animation.view.scale.ScaleYAnimation;
import org.luke.mesa.abs.components.controls.image.ColorIcon;
import org.luke.mesa.abs.components.controls.image.Image;
import org.luke.mesa.abs.components.controls.shape.Rectangle;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.sessionFragments.SessionFragment;
import org.luke.mesa.app.pages.session.sessionFragments.main.SessionMain;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent.EntryContent;
import org.luke.mesa.data.property.Property;

import java.util.Objects;

public class ListEntry extends HBox implements Styleable {
    public static final float ITEM_SIZE_DP = 48;
    private static ListEntry selected;
    private final GradientDrawable background;
    private final Rectangle thumb;
    private final FrameLayout center;
    private final App owner;
    private final Image icon;
    private ParallelAnimation select;
    private ParallelAnimation unselect;
    private Runnable onAction;
    private boolean selectable = false;

    public ListEntry(App owner) {
        super(owner);
        this.owner = owner;
        setSpacing(6);
        ViewUtils.setPadding(this, 0, 0, 14, 0, owner);

        setGravity(Gravity.CENTER);

        thumb = new Rectangle(owner);
        thumb.setSize(8, ITEM_SIZE_DP * 0.9);
        thumb.setRadius(4);
        thumb.setTranslationX(-ViewUtils.dipToPx(4, owner));

        center = new FrameLayout(owner);
        int sizePx = ViewUtils.dipToPx(ITEM_SIZE_DP, owner);
        center.setLayoutParams(new LayoutParams(sizePx, sizePx));

        background = new GradientDrawable();
        center.setBackground(background);

        icon = new ColorIcon(owner);
        ViewUtils.setMarginUnified(icon, owner, ITEM_SIZE_DP * .25f);
        icon.setSize(ITEM_SIZE_DP * .5f);
        icon.setClickable(false);
        icon.setFocusable(false);

        center.addView(icon);

        addView(thumb);
        addView(center);

        setOnClickListener(e -> select(this));

        applyStyle(owner.getStyle());

        unselect();
    }

    private static void select(ListEntry item) {
        if (item.onAction != null) {
            item.onAction.run();
        }

        if (item.selectable) {
            ListEntry old = selected;
            if (old == item) return;
            if (old != null) old.unselect();

            selected = item;
            selected.select();
        }
    }

    public void setIcon(@DrawableRes int res) {
        icon.setImageResource(res);
    }

    public void setOnAction(Class<? extends EntryContent> contentType) {
        selectable = true;
        applyStyle(owner.getStyle().get());
        setOnAction(() -> loadContent(contentType));
    }

    private void loadContent(Class<? extends EntryContent> contentType) {
        SessionFragment frag = SessionFragment.getInstance(owner, SessionMain.class);
        ((SessionMain) Objects.requireNonNull(frag)).loadLeftEntryContent(contentType);
    }

    public void setOnAction(Runnable onAction) {
        this.onAction = onAction;
        applyStyle(owner.getStyle().get());
    }

    public void select() {
        unselect.stop();
        select.start();
    }

    public void unselect() {
        select.stop();
        unselect.start();
    }

    @Override
    public void applyStyle(Style style) {
        thumb.setFill(style.getTextNormal());
        background.setColor(style.getBackgroundPrimary());

        select = new ParallelAnimation(400)
                .addAnimation(new CornerRadiiAnimation(center, ViewUtils.dipToPx(ITEM_SIZE_DP, owner) / 3.5f))
                .addAnimation(new ColorAnimation(style.getBackgroundPrimary(), style.getAccent()) {
                    @Override
                    public void updateValue(int color) {
                        background.setColor(color);
                    }
                })
                .addAnimation(new AlphaAnimation(icon, 1))
                .addAnimation(new AlphaAnimation(thumb, 1))
                .addAnimation(new ScaleYAnimation(thumb, 1))
                .setInterpolator(Interpolator.EASE_OUT);

        unselect = new ParallelAnimation(400)
                .addAnimation(new CornerRadiiAnimation(center, ViewUtils.dipToPx(ITEM_SIZE_DP, owner) / 2.0f))
                .addAnimation(new ColorAnimation(style.getAccent(), style.getBackgroundPrimary()) {
                    @Override
                    public void updateValue(int color) {
                        background.setColor(color);
                    }
                })
                .addAnimation(new AlphaAnimation(icon, 0.5f))
                .addAnimation(new AlphaAnimation(thumb, 0))
                .addAnimation(new ScaleYAnimation(thumb, 0.5f))
                .setInterpolator(Interpolator.EASE_OUT);

        if (icon instanceof ColorIcon)
            ((ColorIcon) icon).setColor(selectable ? style.getTextNormal() : style.getTextPositive());


        setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (icon instanceof ColorIcon && !selectable) {
                        background.setAlpha(200);
                        icon.setAlpha(1f);
                        ((ColorIcon) icon).setColor(Color.WHITE);
                        background.setColor(style.getTextPositive());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (icon instanceof ColorIcon && !selectable) {
                        background.setAlpha(255);
                        icon.setAlpha(0.5f);
                        ((ColorIcon) icon).setColor(style.getTextPositive());
                        background.setColor(style.getBackgroundPrimary());
                    }
                    view.performClick();
                    break;
            }
            return true;
        });
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
