package org.luke.mesa.app.pages.session.sessionFragments;

import android.widget.FrameLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.app.pages.session.SessionPage;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

public abstract class SessionFragment extends FrameLayout {
    private static final HashMap<Class<? extends SessionFragment>, SessionFragment> cache = new HashMap<>();
    protected App owner;

    public SessionFragment(App owner) {
        super(owner);
        this.owner = owner;
    }

    public static Collection<SessionFragment> getCachedFragments() {
        return cache.values();
    }

    public static SessionFragment getInstance(App owner, Class<? extends SessionFragment> type) {
        SessionFragment found = cache.get(type);
        if (found == null) {
            try {
                found = type.getConstructor(App.class).newInstance(owner);
                cache.put(type, found);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            if (found.getParent() != null) {
                SessionPage page = ((SessionPage) found.getParent());
                if (page.getLoaded() != found) {
                    SessionFragment finalFound = found;
                    Platform.runLater(() -> page.removeView(finalFound));
                }
            }

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

    public void applyInsets(Insets insets) {
        //LEAVE EMPTY
    }
}
