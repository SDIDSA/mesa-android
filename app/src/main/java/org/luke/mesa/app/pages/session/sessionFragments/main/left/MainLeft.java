package org.luke.mesa.app.pages.session.sessionFragments.main.left;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent.EntryContent;

public class MainLeft extends HBox {
    private final EntryList entries;
    private final EntryDisp disp;

    private final App owner;


    public MainLeft(App owner) {
        super(owner);
        this.owner = owner;

        ViewUtils.setPadding(this, 0, 0, 7, 0, owner);

        entries = new EntryList(owner);
        disp = new EntryDisp(owner);

        addView(entries);
        addView(disp);
    }

    public void loadEntryContent(Class<? extends EntryContent> contentType) {
        disp.load(contentType);
    }
}
