package org.luke.mesa.app.pages.session.sessionFragments.main.left.entryContent;

import android.graphics.Color;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.image.ExplorerImage;
import org.luke.mesa.abs.components.controls.input.SearchInput;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.controls.text.font.FontWeight;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.navBar.NavBarItem;
import org.luke.mesa.data.property.Property;

public class DMEntryContent extends EntryContent implements Styleable {
    private final Label header;
    private final Label noDms;

    private final Button addFriends;

    public DMEntryContent(App owner) {
        super(owner);
        setSpacing(15);

        VBox top = new VBox(owner);
        top.setPadding(15);
        top.setSpacing(15);

        header = new Label(owner, "dms");
        header.setFont(new Font(18f, FontWeight.BOLD));

        SearchInput search = new SearchInput(owner);
        search.setHint("conv_search");

        ExplorerImage explore = new ExplorerImage(owner);
        ViewUtils.spacer(owner, explore);

        noDms = new Label(owner, "no_dms");
        noDms.setFont(new Font(16f, FontWeight.MEDIUM));
        noDms.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        addFriends = new Button(owner, "add_friends");

        VBox bottom = new VBox(owner);
        bottom.setPadding(20);
        bottom.setSpacing(15);

        ViewUtils.setMargin(addFriends, owner, 15, 0, 15, 0);

        top.addView(header);
        top.addView(search);

        bottom.addView(noDms);
        bottom.addView(addFriends);

        addView(top);
        addView(explore);
        addView(bottom);

        applyStyle(owner.getStyle());
    }

    @Override
    public void applyInsets(Insets insets) {
        App owner = getOwner();
        ViewUtils.setPadding(this, 0, 0, 0, ViewUtils.pxToDip(insets.bottom, owner) + NavBarItem.HEIGHT, owner);
    }

    @Override
    public void applyStyle(Style style) {
        header.setTextColor(style.getTextNormal());
        noDms.setTextColor(style.getTextNormal());

        addFriends.setBackgroundColor(style.getAccent());
        addFriends.setTextFill(Color.WHITE);
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
