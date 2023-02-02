package org.luke.mesa.abs.components.controls.image;

import android.view.ViewGroup;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.ViewUtils;

public class Image extends androidx.appcompat.widget.AppCompatImageView {
    protected final App owner;
    private Runnable onClick;

    public Image(App owner) {
        super(owner);
        this.owner = owner;

        setAdjustViewBounds(true);
        setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

        setOnClickListener(e -> fire());

        setFocusable(false);
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
        setFocusable(true);
    }

    public void setHeight(float height) {
        getLayoutParams().height = ViewUtils.dipToPx(height, owner);
        requestLayout();
    }

    public void setWidth(float width) {
        getLayoutParams().width = ViewUtils.dipToPx(width, owner);
        requestLayout();
    }

    public void setSize(float size) {
        setWidth(size);
        setHeight(size);
    }

    public void fire() {
        if (onClick != null)
            onClick.run();
    }
}
