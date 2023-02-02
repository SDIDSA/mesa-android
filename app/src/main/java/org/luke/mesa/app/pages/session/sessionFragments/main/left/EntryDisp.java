package org.luke.mesa.app.pages.session.sessionFragments.main.left;

import android.graphics.drawable.GradientDrawable;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent.EntryContent;
import org.luke.mesa.data.property.Property;

public class EntryDisp extends FrameLayout implements Styleable {
    private final App owner;

    private final GradientDrawable background;

    public EntryDisp(App owner) {
        super(owner);
        this.owner = owner;

        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        background = new GradientDrawable();
        int cr = ViewUtils.dipToPx(10, owner);
        background.setCornerRadii(new float[]{
                cr, cr,
                cr, cr,
                0, 0,
                0, 0
        });
        setBackground(background);

        applyStyle(owner.getStyle());
    }

    private EntryContent loaded;
    public void load(Class<? extends EntryContent> contentType) {
        new Thread(() -> {
            loaded = EntryContent.getInstance(owner, contentType);
            Platform.runLater(() -> {
                removeAllViews();
                loaded.applyInsets(owner.getSystemInsets());
                addView(loaded);
            });
        }).start();
    }

    public EntryContent getLoaded() {
        return loaded;
    }

    @Override
    public void applyStyle(Style style) {
        background.setColor(style.getBackgroundPrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
