package org.luke.mesa.app.pages;

import android.graphics.Color;
import android.view.Gravity;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.image.ColorIcon;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.utils.ViewUtils;

public class SplashScreen extends VBox {
    private final ColorIcon icon;
    private final ColorIcon subIcon;

    public SplashScreen(App owner) {
        super(owner);

        setGravity(Gravity.CENTER);

        icon = new ColorIcon(owner, R.drawable.icon);
        icon.setSize(100);
        subIcon = new ColorIcon(owner, R.drawable.mesa);
        subIcon.setSize(80);

        addView(ViewUtils.spacer(owner));
        addView(icon);
        addView(ViewUtils.spacer(owner));
        addView(subIcon);

        ViewUtils.setPaddingUnified(this, 30, owner);

        applyStyle(owner);
    }

    private void applyStyle(App owner) {
        int color = Color.parseColor("#DCDDDE");
        icon.setColor(color);
        subIcon.setColor(color);

        int back = Color.parseColor("#202225");
        owner.setBackgroundColor(back);
    }

}
