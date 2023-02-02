package org.luke.mesa.app.pages.welcome;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateXAnimation;
import org.luke.mesa.abs.components.Page;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.Threaded;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

import java.util.concurrent.atomic.AtomicReference;

public class Welcome extends Page {
    Animation running = null;
    private WelcomePage loaded;

    public Welcome(App owner) {
        super(owner);

        WelcomePage.clearCache();
        nextInto(WelcomeMain.class);

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

        owner.hideKeyboard();

        new Thread(() -> {
            AtomicReference<WelcomePage> page = new AtomicReference<>();
            WelcomePage old = loaded;
            if (old != null)
                running = new ParallelAnimation(500)
                        .addAnimation(new AlphaAnimation(old, 0))
                        .addAnimation(new TranslateXAnimation(old, ViewUtils.dipToPx(-100 * direction, owner)))
                        .setInterpolator(Interpolator.EASE_OUT).setOnFinished(() -> removeView(old)).start();

            new Thread(() -> {
                Threaded.sleep(250);
                while (page.get() == null) {
                    Threaded.sleep(10);
                }
                Platform.runLater(() -> {
                    page.get().setAlpha(1);
                    page.get().hide();
                    removeView(loaded);
                    addView(loaded, 0);
                });
            }).start();

            page.set(WelcomePage.getInstance(owner, pageType));

            if (page.get() == null) return;

            Platform.runLater(() -> {
                loaded = page.get();
                loaded.applyInsets(owner.getSystemInsets());
                loaded.setAlpha(0);
                loaded.setScaleX(1);
                loaded.setScaleY(1);
                loaded.setTranslationX(0);
            });
        }).start();
    }

    public WelcomePage getLoaded() {
        return loaded;
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
