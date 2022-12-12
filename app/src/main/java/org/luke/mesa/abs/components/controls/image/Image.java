package org.luke.mesa.abs.components.controls.image;

import android.content.ClipData;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.ViewUtils;

public class Image extends androidx.appcompat.widget.AppCompatImageView {
    private final App owner;
    private Runnable onClick;

    public Image(App owner, int id) {
        super(owner);
        this.owner = owner;

        setAdjustViewBounds(true);
        setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        setOnClickListener(e -> {
            if (onClick != null) {
                onClick.run();
            }
        });

        setFocusable(false);
        setImageResource(id);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
        setFocusable(true);
    }

    public void setHeight(int height) {
        getLayoutParams().height = ViewUtils.dipToPx(height, owner);
        requestLayout();
    }

    public void setWidth(int width) {
        getLayoutParams().width = ViewUtils.dipToPx(width, owner);
        requestLayout();
    }

    public void setSize(int size) {
        setWidth(size);
        setHeight(size);
    }

    public void setColor(int color) {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void fire() {
        if (onClick != null)
            onClick.run();
    }
}
