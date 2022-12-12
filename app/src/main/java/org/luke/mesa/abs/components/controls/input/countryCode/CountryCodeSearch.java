package org.luke.mesa.abs.components.controls.input.countryCode;

import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.image.Image;
import org.luke.mesa.abs.components.controls.input.InputUtils;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.locale.Locale;
import org.luke.mesa.abs.locale.Localized;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;
import org.luke.mesa.data.property.StringProperty;

public class CountryCodeSearch extends HBox implements Styleable, Localized {
    private final App owner;
    private final GradientDrawable background;

    private final Image icon;
    private final EditText input;

    StringProperty value;

    public CountryCodeSearch(App owner) {
        super(owner);
        this.owner = owner;
        background = new GradientDrawable();
        setRadius(7);

        value = new StringProperty();

        setVerticalGravity(Gravity.CENTER_VERTICAL);

        icon = new Image(owner, R.drawable.magnify);
        icon.setHeight(26);

        ViewUtils.setMargin(icon, owner, 15, 10, 5, 10);

        input = new EditText(owner);
        input.setBackground(null);
        input.setTypeface(Font.DEFAULT.getFont());
        ViewUtils.spacer(owner, input);
        ViewUtils.setPaddingUnified(input, 10, owner);

        addView(icon);
        addView(input);

        InputUtils.bindToProperty(input, value);

        setBackground(background);
        applyStyle(owner.getStyle());
        applyLocale(owner.getLocale());
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setRadius(float radius) {
        background.setCornerRadius(ViewUtils.dipToPx(radius, owner));
    }

    public void setBackgroundColor(int color) {
        background.setColor(color);
    }

    @Override
    public void applyStyle(Style style) {
        setBackgroundColor(style.getBackgroundTertiary());
        icon.setColor(style.getChannelsDefault());
        input.setTextColor(style.getTextNormal());
        input.setHintTextColor(style.getTextMuted());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }

    @Override
    public void applyLocale(Locale locale) {
        input.setHint(locale.get("search"));
    }

    @Override
    public void applyLocale(Property<Locale> locale) {
        Localized.bindLocale(this, locale);
    }
}
