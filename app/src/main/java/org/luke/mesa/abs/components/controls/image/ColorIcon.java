package org.luke.mesa.abs.components.controls.image;

import android.graphics.PorterDuff;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.ViewUtils;

public class ColorIcon extends Image {
    private Runnable onClick;

    public ColorIcon(App owner, int id) {
        super(owner);
        if (id != Integer.MIN_VALUE)
            setImageResource(id);
    }

    public ColorIcon(App owner) {
        this(owner, Integer.MIN_VALUE);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
        setFocusable(true);
    }

    public void setColor(int color) {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void fire() {
        if (onClick != null)
            onClick.run();
    }
}
