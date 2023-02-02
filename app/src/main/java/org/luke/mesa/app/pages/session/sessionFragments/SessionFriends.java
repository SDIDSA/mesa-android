package org.luke.mesa.app.pages.session.sessionFragments;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.utils.ViewUtils;

public class SessionFriends extends SessionFragment {
    public SessionFriends(App owner) {
        super(owner);

        addView(new Label(owner, "friends and shit"));
    }

    @Override
    public void applyInsets(Insets insets) {
        super.applyInsets(insets);

        ViewUtils.setPadding(this, 0, ViewUtils.pxToDip(insets.top, owner), 0, 0, owner);
    }
}
