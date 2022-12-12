package org.luke.mesa.abs.components;

import android.widget.FrameLayout;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;

public abstract class Page extends FrameLayout implements Styleable {
    protected App owner;

    public Page(App owner) {
        super(owner);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        this.owner = owner;
    }

    protected void setPadding(int padding) {
        ViewUtils.setPaddingUnified(this, padding, owner);
    }

    public abstract boolean onBack();

    public abstract void applyInsets(Insets insets);
}
