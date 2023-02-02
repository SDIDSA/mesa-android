package org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent;

import android.widget.FrameLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.EntryDisp;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

public abstract class EntryContent extends VBox {
    private static final HashMap<Class<? extends EntryContent>, EntryContent> cache = new HashMap<>();

    public EntryContent(App owner) {
        super(owner);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public static Collection<EntryContent> getCachedContents() {
        return cache.values();
    }

    public static EntryContent getInstance(App owner, Class<? extends EntryContent> type) {
        EntryContent found = cache.get(type);
        if (found == null) {
            try {
                found = type.getConstructor(App.class).newInstance(owner);
                cache.put(type, found);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            if (found.getParent() != null) {
                EntryDisp parent = ((EntryDisp) found.getParent());
                if (parent.getLoaded() != found)
                    parent.removeView(found);
            }
        }
        if (type.isInstance(found)) {
            return type.cast(found);
        } else {
            return null;
        }
    }

    public abstract void applyInsets(Insets insets);

    public static void clearCache() {
        cache.clear();
    }
}
