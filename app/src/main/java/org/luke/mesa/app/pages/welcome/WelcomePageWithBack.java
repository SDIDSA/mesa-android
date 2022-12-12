package org.luke.mesa.app.pages.welcome;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.image.Image;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public abstract class WelcomePageWithBack extends WelcomePage implements Styleable {
    protected final Image back;

    public WelcomePageWithBack(App owner) {
        super(owner);

        HBox top = new HBox(owner);
        back = new Image(owner, R.drawable.arrow);
        back.setHeight(38);
        ViewUtils.setPaddingUnified(back, 5, owner);
        top.addView(back);

        addView(top);
    }

    @Override
    public boolean goBack() {
        back.fire();
        return true;
    }

    @Override
    public void applyStyle(Style style) {
        back.setColor(style.getHeaderPrimary());
        setBackgroundColor(style.getBackgroundMobilePrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
