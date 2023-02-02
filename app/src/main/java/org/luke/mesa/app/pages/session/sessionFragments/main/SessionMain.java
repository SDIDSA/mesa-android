package org.luke.mesa.app.pages.session.sessionFragments.main;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.sessionFragments.SessionFragment;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.MainLeft;
import org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent.EntryContent;

public class SessionMain extends SessionFragment {
    private final MainLeft left;

    public SessionMain(App owner) {
        super(owner);
        left = new MainLeft(owner);
        addView(left);
    }

    public void loadLeftEntryContent(Class<? extends EntryContent> contentType) {
        left.loadEntryContent(contentType);
    }

    @Override
    public void applyInsets(Insets insets) {
        super.applyInsets(insets);

        ViewUtils.setPadding(this, 0, ViewUtils.pxToDip(insets.top, owner), 0, 0, owner);
    }
}
