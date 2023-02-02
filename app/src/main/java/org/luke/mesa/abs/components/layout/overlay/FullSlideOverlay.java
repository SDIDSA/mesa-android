package org.luke.mesa.abs.components.layout.overlay;


import android.view.View;

import androidx.core.graphics.Insets;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.image.ColorIcon;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.utils.ViewUtils;

public class FullSlideOverlay extends SlideOverlay {
    private final ColorIcon back;

    private final VBox root;
    public FullSlideOverlay(App owner) {
        super(owner);
        setHeightFactor(1.0);

        HBox top = new HBox(owner);
        back = new ColorIcon(owner, R.drawable.close);
        back.setHeight(36);
        back.setOnClick(this::hide);
        ViewUtils.setMarginUnified(back, owner, 10);
        ViewUtils.setPaddingUnified(back, 6, owner);
        top.addView(back);

        root = new VBox(owner);
        root.setSpacing(15);
        root.setPadding(15);
        ViewUtils.spacer(owner, root);

        list.addView(top);
        list.addView(root);

        background.setCornerRadius(0);

        applyStyle(owner.getStyle());
    }

    protected void addToRoot(View...views) {
        for(View v : views) {
            root.addView(v);
        }
    }

    @Override
    public void applySystemInsets(Insets insets) {
        list.setPadding(0, insets.top, 0, insets.bottom);
    }

    @Override
    public void applyInputInsets(boolean shown, Insets insets) {
        //IGNORE
    }

    @Override
    public void applyStyle(Style style) {
        if(back == null) {
            return;
        }
        back.setColor(style.getTextNormal());
        super.applyStyle(style);
    }
}
