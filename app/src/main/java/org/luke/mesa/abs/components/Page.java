package org.luke.mesa.abs.components;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class Page extends FrameLayout implements Styleable {
    private static final HashMap<Class<? extends Page>, Page> cache = new HashMap<>();
    protected App owner;

    public Page(App owner) {
        super(owner);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        this.owner = owner;
    }

    public static Page getInstance(App owner, Class<? extends Page> type) {
        Page found = cache.get(type);
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

    protected void setPadding(int padding) {
        ViewUtils.setPaddingUnified(this, padding, owner);
    }

    public abstract boolean onBack();

    public abstract void applyInsets(Insets insets);
}
