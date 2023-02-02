package org.luke.mesa.app.pages.session;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateXAnimation;
import org.luke.mesa.abs.components.Page;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.navBar.NavBar;
import org.luke.mesa.app.pages.session.navBar.NavBarItem;
import org.luke.mesa.app.pages.session.sessionFragments.SessionFragment;
import org.luke.mesa.data.property.Property;

public class SessionPage extends Page {
    private final NavBar navBar;

    private Animation running = null;
    private SessionFragment loaded = null;

    public SessionPage(App owner) {
        super(owner);

        navBar = new NavBar(owner);
        addView(navBar);

        SessionFragment.clearCache();

        setOnClickListener(e -> navBar.toggle());

        applyStyle(owner.getStyle());
    }

    public void navigateInto(Class<? extends SessionFragment> pageType, NavBarItem o, NavBarItem n) {
        HBox parent = (HBox) n.getParent();
        int ind = parent.indexOfChild(n);
        int oldInd = parent.indexOfChild(o);

        if (ind > oldInd) {
            nextInto(pageType);
        } else {
            previousInto(pageType);
        }
    }

    public void nextInto(Class<? extends SessionFragment> pageType) {
        navigateInto(pageType, 1);
    }

    public void previousInto(Class<? extends SessionFragment> pageType) {
        navigateInto(pageType, -1);
    }

    private void navigateInto(Class<? extends SessionFragment> fragmentType, int direction) {
        if (running != null && running.isRunning())
            running.stop();

        new Thread(() -> {
            SessionFragment old = loaded;
            if (old != null && fragmentType.isInstance(old)) return;
            SessionFragment nw = SessionFragment.getInstance(owner, fragmentType);
            if (nw == null) return;

            loaded = nw;

            Platform.runLater(() -> {
                loaded.applyInsets(owner.getSystemInsets());

                float fromX = getWidth() * direction;
                loaded.setTranslationX(fromX / 2);
                loaded.setAlpha(0);
                addView(loaded);
                ParallelAnimation trans = new ParallelAnimation(400)
                        .addAnimation(new TranslateXAnimation(loaded, fromX / 2, 0))
                        .addAnimation(new AlphaAnimation(loaded, 0, 1))
                        .setInterpolator(Interpolator.EASE_OUT);
                if (old != null) {
                    trans.addAnimation(new TranslateXAnimation(old, 0, -fromX / 2));
                    trans.addAnimation(new AlphaAnimation(old, 1, 0));
                    trans.setOnFinished(() -> removeView(old));
                }
                running = trans;
                running.start();
            });
        }).start();
    }

    public SessionFragment getLoaded() {
        return loaded;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void applyInsets(Insets insets) {
        ViewUtils.setPadding(navBar, 0, 0, 0, ViewUtils.pxToDip(insets.bottom, owner), owner);
        if (loaded != null) {
            loaded.applyInsets(insets);
        }
    }

    @Override
    public void applyStyle(Style style) {
        owner.setBackgroundColor(style.getBackgroundTertiary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
