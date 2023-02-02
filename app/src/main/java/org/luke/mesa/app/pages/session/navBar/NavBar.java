package org.luke.mesa.app.pages.session.navBar;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;

import org.json.JSONException;
import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.components.controls.image.ImageProxy;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.sessionFragments.SessionFriends;
import org.luke.mesa.app.pages.session.sessionFragments.main.SessionMain;
import org.luke.mesa.data.beans.User;
import org.luke.mesa.data.property.Property;

public class NavBar extends FrameLayout implements Styleable {
    private final View topBorder;
    private final GradientDrawable background;

    private final Animation show, hide;
    private final HBox root;
    private boolean shown = true;

    public NavBar(App owner) {
        super(owner);
        root = new HBox(owner);
        root.setGravity(Gravity.CENTER);
        background = new GradientDrawable();
        setBackground(background);

        setClipToPadding(false);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;

        root.setLayoutParams(layoutParams);
        setLayoutParams(layoutParams);

        ImgNavBarItem chats = new ImgNavBarItem(owner, R.drawable.icon_inv, 30)
                .setOnAction(SessionMain.class);
        ImgNavBarItem friends = new ImgNavBarItem(owner, R.drawable.friend, 25)
                .setOnAction(SessionFriends.class);
        ImgNavBarItem search = new ImgNavBarItem(owner, R.drawable.search, 19);
        ImgNavBarItem notifications = new ImgNavBarItem(owner, R.drawable.bell, 19);
        ImgNavBarItem user = new ImgNavBarItem(owner);

        Platform.runAfter(() -> NavBarItem.select(chats), 100);

        chats.setAlpha(1f);
        try {
            User.getForId(
                    owner.getJsonObject("user").getString("id"),
                    u -> ImageProxy.getImage(u.getAvatar(), img ->
                            user.setBitmap(new BitmapDrawable(owner.getResources(), img))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        topBorder = new View(owner);
        LayoutParams blp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewUtils.dipToPx(1, owner));
        blp.gravity = Gravity.TOP;
        topBorder.setLayoutParams(blp);

        root.addView(chats);
        root.addView(friends);
        root.addView(search);
        root.addView(notifications);
        root.addView(user);

        addView(root);
        addView(topBorder);

        show = new TranslateYAnimation(400, this, 0)
                .setInterpolator(Interpolator.EASE_OUT);
        hide = new TranslateYAnimation(400, this, 0)
                .setLateTo(this::getFloatHeight)
                .setInterpolator(Interpolator.EASE_OUT);

        setElevation(5);

        applyStyle(owner.getStyle());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = root.getChildCount();
        int total = getWidth();
        int unit = total / count;

        for (int i = 0; i < count; i++) {
            View v = root.getChildAt(i);
            if (v.getWidth() == unit) continue;
            ViewGroup.LayoutParams p = v.getLayoutParams();
            p.width = unit;
            v.setLayoutParams(p);
        }
    }

    public void show() {
        shown = true;
        hide.stop();
        show.start();
    }

    public void hide() {
        shown = false;
        show.stop();
        hide.start();
    }

    public void toggle() {
        if (shown) {
            hide();
        } else {
            show();
        }
    }

    @Override
    public boolean isShown() {
        return shown;
    }

    private float getFloatHeight() {
        return getHeight();
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        background.setColor(color);
    }

    @Override
    public void applyStyle(Style style) {
        setBackgroundColor(style.getBackgroundFloating());
        topBorder.setBackgroundColor(style.getBackgroundPrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
