package org.luke.mesa.app.pages.welcome;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.animation.view.scale.ScaleXYAnimation;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.utils.ViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

public abstract class WelcomePage extends FrameLayout {
    private static final HashMap<Class<? extends WelcomePage>, WelcomePage> cache = new HashMap<>();

    protected final App owner;
    protected VBox root;
    protected FrameLayout preRoot;
    private Welcome welcome;

    public WelcomePage(App owner) {
        super(owner);
        this.owner = owner;

        preRoot = new FrameLayout(owner);

        root = new VBox(owner);
        root.setHorizontalGravity(Gravity.CENTER);
        ViewUtils.setPaddingUnified(root, 12, owner);
        root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        root.setClipToPadding(false);

        preRoot.addView(root);
        super.addView(preRoot);
    }

    public static Collection<WelcomePage> getCachedPages() {
        return cache.values();
    }

    public static WelcomePage getInstance(App owner, Class<? extends WelcomePage> type) {
        WelcomePage found = cache.get(type);
        if (found == null) {
            try {
                found = type.getConstructor(App.class).newInstance(owner);
                cache.put(type, found);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            if (found.getParent() != null)
                ((ViewGroup) found.getParent()).removeView(found);
        }
        if (type.isInstance(found)) {
            return type.cast(found);
        } else {
            return null;
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    private Welcome getWelcome() {
        if (welcome == null)
            welcome = (Welcome) owner.getLoaded();

        return welcome;
    }

    public void nextInto(Class<? extends WelcomePage> pageType) {
        getWelcome().nextInto(pageType);
    }

    public void previousInto(Class<? extends WelcomePage> pageType) {
        getWelcome().previousInto(pageType);
    }

    public abstract boolean goBack();

    public void hide() {

    }

    public void init() {
        //For subclasses to implement
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == VISIBLE) {
            init();
        } else {
            hide();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    protected void hide(View... views) {
        for (View view : views) {
            hide(view);
        }
    }

    protected void hide(View view) {
        view.setAlpha(0);
    }

    protected ParallelAnimation parallelAnimation(float fromY, float fromScale, View... views) {
        ParallelAnimation res = new ParallelAnimation();

        for (View view : views) {
            res.addAnimation(parallelAnimation(view, fromY, fromScale));
        }

        return res;
    }

    protected ParallelAnimation parallelAnimation(View view, float fromY, float fromScale) {
        view.setTranslationY(ViewUtils.dipToPx(fromY, owner));
        view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        return new ParallelAnimation()
                .addAnimation(new AlphaAnimation(view, 1f).setOverrideFrom(0))
                .addAnimation(new TranslateYAnimation(view, 0).setOverrideFrom(fromY))
                .addAnimation(new ScaleXYAnimation(view, 1).setOverrideFrom(fromScale));
    }

    @Override
    public void addView(View child) {
        root.addView(child);
    }

    public void applyInsets(Insets insets) {
        owner.applyInsets(preRoot, insets);
    }
}
