package org.luke.mesa.app.pages.welcome;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.Animation;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateXAnimation;
import org.luke.mesa.abs.animation.view.scale.ScaleXYAnimation;
import org.luke.mesa.abs.components.Page;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public class Welcome extends Page {
    Animation running = null;
    private WelcomePage loaded;

    public Welcome(App owner) {
        super(owner);

        loaded = WelcomePage.getInstance(owner, WelcomeMain.class);
        if(loaded != null) loaded.init();
        addView(loaded);

        applyStyle(owner.getStyle());
    }

    public void nextInto(Class<? extends WelcomePage> pageType) {
        navigateInto(pageType, 1);
    }

    public void previousInto(Class<? extends WelcomePage> pageType) {
        navigateInto(pageType, -1);
    }

    private void navigateInto(Class<? extends WelcomePage> pageType, int direction) {
        if (running != null && running.isRunning())
            return;

        new Thread(() -> {
            WelcomePage page = WelcomePage.getInstance(owner, pageType);

            if (page == null) return;
            if (running != null && running.isRunning()) return;

            Platform.runLater(() -> {
                page.applyInsets(owner.getInsets());

                WelcomePage old = loaded;

                loaded = page;
                page.init();
                page.setAlpha(1);
                page.setScaleX(1);
                page.setScaleY(1);
                page.setTranslationX(0);
                removeView(page);
                addView(page, 0);
                running = new ParallelAnimation(500)
                        .addAnimation(new AlphaAnimation(old, 0))
                        .addAnimation(new ScaleXYAnimation(old, .8f))
                        .addAnimation(new TranslateXAnimation(old, ViewUtils.dipToPx(-100 * direction, owner)))
                        .setInterpolator(Interpolator.EASE_OUT).setOnFinished(() -> removeView(old)).start();
            });
        }).start();
    }

    @Override
    public boolean onBack() {
        return loaded.goBack();
    }

    @Override
    public void applyInsets(Insets insets) {
        if (loaded != null) {
            loaded.applyInsets(insets);
        }
    }

    @Override
    public void applyStyle(Style style) {
        owner.setBackgroundColor(style.getBackgroundMobilePrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
