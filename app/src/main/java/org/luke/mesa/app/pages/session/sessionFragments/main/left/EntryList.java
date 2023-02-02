package org.luke.mesa.app.pages.session.sessionFragments.main.left;

import android.util.Log;

import androidx.annotation.DrawableRes;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.scratches.Orientation;
import org.luke.mesa.abs.components.controls.scratches.Separator;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.overlays.createServer.CreateServer;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent.DMEntryContent;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent.EntryContent;
import org.luke.mesa.data.property.Property;

public class EntryList extends VBox implements Styleable {
    private final App owner;

    private final Separator separator;

    public EntryList(App owner) {
        super(owner);
        this.owner = owner;

        setSpacing(10);
        setLayoutParams(new LayoutParams(ViewUtils.dipToPx(ListEntry.ITEM_SIZE_DP + 28, owner), LayoutParams.MATCH_PARENT));
        ViewUtils.setPadding(this, 0, 10, 0, 0, owner);

        separator = new Separator(owner, Orientation.HORIZONTAL, 22);

        CreateServer createServerOverlay = new CreateServer(owner);

        addItem(R.drawable.message, DMEntryContent.class);
        addView(separator);
        addItem(R.drawable.plus, createServerOverlay::show);
        addItem(R.drawable.discover, () -> {});

        Log.i("servers", owner.getServers().toString());

        applyStyle(owner.getStyle());
    }

    public void addItem(@DrawableRes int res, Runnable onAction) {
        ListEntry item = new ListEntry(owner);
        item.setOnAction(onAction);
        item.setIcon(res);
        addView(item);
    }

    public void addItem(@DrawableRes int res, Class<? extends EntryContent> onAction) {
        ListEntry item = new ListEntry(owner);
        item.setOnAction(onAction);
        item.setIcon(res);
        addView(item);
    }

    @Override
    public void applyStyle(Style style) {
        separator.setColor(style.getTextMuted());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
