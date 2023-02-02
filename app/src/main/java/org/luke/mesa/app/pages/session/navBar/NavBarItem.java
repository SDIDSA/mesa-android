package org.luke.mesa.app.pages.session.navBar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.SessionPage;
import org.luke.mesa.app.pages.session.sessionFragments.SessionFragment;

public abstract class NavBarItem extends FrameLayout implements Styleable {
    public static final float HEIGHT = 55;
    private static NavBarItem selected = null;
    protected final App owner;
    private Runnable onAction;
    private RippleDrawable ripple;
    private boolean selectable = false;

    public NavBarItem(App owner) {
        super(owner);
        this.owner = owner;
        ViewUtils.spacer(owner, this);

        setClickable(true);
        setFocusable(true);

        setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ripple.setHotspot(event.getX(), event.getY());
                    setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    setPressed(false);
                    break;
            }
            return true;
        });

        setOnClickListener(e -> select(this));

        ripple = new RippleDrawable(ColorStateList.valueOf(Color.TRANSPARENT), getBackground(), new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{Color.BLACK}));
        setBackground(ripple);
    }

    public static void select(NavBarItem item) {
        if (item.onAction != null) {
            item.onAction.run();
        }

        if (item.selectable) {
            NavBarItem old = selected;
            if (old == item) return;
            if (old != null) old.unselect();

            selected = item;
            selected.select();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends NavBarItem> T setOnAction(Runnable onAction) {
        this.onAction = onAction;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends NavBarItem> T setOnAction(Class<? extends SessionFragment> fragType) {
        selectable = true;
        this.onAction = () -> loadFragment(fragType);
        return (T) this;
    }

    private void loadFragment(Class<? extends SessionFragment> pageType) {
        ((SessionPage) owner.getLoaded()).navigateInto(pageType, selected, this);
    }

    public abstract void select();

    public abstract void unselect();

    @Override
    public void applyStyle(Style style) {
        setRippleColor(App.adjustAlpha(style.getTextMuted(), .7f));
    }

    public void setRippleColor(int touchColor) {
        ripple.setColor(ColorStateList.valueOf(touchColor));
    }
}
