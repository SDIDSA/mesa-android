package org.luke.mesa.app.pages.session.overlays.createServer;

import android.graphics.Color;
import android.view.Gravity;
import android.widget.ScrollView;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.controls.text.font.FontWeight;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.components.layout.overlay.FullSlideOverlay;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.utils.ViewUtils;

public class CreateServer extends FullSlideOverlay {
    private final Label header;
    private final Label subHeader;


    private final Label haveInvite;
    private final Button join;
    public CreateServer(App owner) {
        super(owner);

        header = new Label(owner, "create_server");
        header.setFont(new Font(22f, FontWeight.BOLD));

        subHeader = new Label(owner, "create_server_sub");
        subHeader.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        subHeader.setFont(new Font(13f));
        subHeader.setLineSpacing(5);
        subHeader.setAlpha(0.7f);

        VBox center = new VBox(owner);
        center.setSpacing(10);
        center.setGravity(Gravity.CENTER);
        center.addView(header);
        center.addView(subHeader);

        ScrollView centerCont = new ScrollView(owner);
        centerCont.addView(center);
        ViewUtils.spacer(owner, centerCont);

        haveInvite = new Label(owner, "have_invite");
        haveInvite.setFont(new Font(18, FontWeight.MEDIUM));

        join = new Button(owner, "join_server");
        join.setFont(new Font(14, FontWeight.BOLD));

        VBox bottom = new VBox(owner);
        bottom.setSpacing(10);
        bottom.setGravity(Gravity.CENTER);
        bottom.addView(haveInvite);
        bottom.addView(join);

        addToRoot(centerCont, bottom);

        applyStyle(owner.getStyle());
    }

    @Override
    public void applySystemInsets(Insets insets) {
        list.setPadding(0, insets.top, 0, insets.bottom);
    }

    @Override
    public void applyStyle(Style style) {
        if(header == null) return;
        header.setTextColor(style.getTextNormal());
        subHeader.setTextColor(style.getTextNormal());

        haveInvite.setTextColor(style.getTextNormal());
        join.setBackgroundColor(style.getSecondaryButtonBack());
        join.setTextFill(Color.WHITE);
        super.applyStyle(style);
    }
}
