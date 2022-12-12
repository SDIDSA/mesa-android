package org.luke.mesa.abs.components.layout.linear;

import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.DoubleProperty;

public class LinearBox extends LinearLayout {
    private final App owner;
    private final DoubleProperty spacing;

    public LinearBox(App owner) {
        super(owner);
        this.owner = owner;

        spacing = new DoubleProperty(0.0);
        spacing.addListener((obs, ov, nv) -> applySpacing(nv));
    }

    private void applySpacing(double spacing) {
        int rs = ViewUtils.dipToPx(spacing, owner);
        GradientDrawable divider = (GradientDrawable) ResourcesCompat.getDrawable(owner.getResources(), R.drawable.divider_shape,null);
        if(divider==null) {
            return;
        }
        if(this instanceof HBox) {
            divider.setSize(rs, 1);
        } else {
            divider.setSize(1, rs);
        }
        setDividerDrawable(divider);
        setShowDividers(SHOW_DIVIDER_MIDDLE);
    }

    public void setSpacing(double spacing) {
        this.spacing.set(spacing);
    }

    public DoubleProperty spacingProperty() {
        return spacing;
    }

    public App getOwner() {
        return owner;
    }

    public void setPadding(int padding) {
        ViewUtils.setPaddingUnified(this, padding, owner);
    }

    public double getSpacing() {
        return spacing.get();
    }
}
