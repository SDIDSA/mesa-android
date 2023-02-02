package org.luke.mesa.abs.components.controls.input;

import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.EditText;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.image.ColorIcon;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.locale.Locale;
import org.luke.mesa.abs.locale.Localized;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;
import org.luke.mesa.data.property.StringProperty;

public class SearchInput extends HBox implements Styleable, Localized {
    private final App owner;
    private final GradientDrawable background;

    private final ColorIcon icon;
    private final EditText input;

    private String hint = "";

    StringProperty value;

    public SearchInput(App owner) {
        super(owner);
        this.owner = owner;
        background = new GradientDrawable();
        setRadius(7);

        value = new StringProperty();

        setVerticalGravity(Gravity.CENTER_VERTICAL);

        icon = new ColorIcon(owner, R.drawable.magnify);
        icon.setHeight(24);

        ViewUtils.setMargin(icon, owner, 5, 7, 7, 7);

        input = new EditText(owner);
        input.setBackground(null);
        ViewUtils.setFont(input, new Font(14f));
        ViewUtils.spacer(owner, input);
        ViewUtils.setPaddingUnified(input, 12, owner);

        addView(input);
        addView(icon);

        InputUtils.bindToProperty(input, value);

        setBackground(background);
        applyStyle(owner.getStyle());
        applyLocale(owner.getLocale());
    }

    public void setHint(String hint) {
        this.hint = hint;
        input.setHint(owner.getLocale().get().get(hint));
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
        input.setHint(locale.get(hint));
    }

    @Override
    public void applyLocale(Property<Locale> locale) {
        Localized.bindLocale(this, locale);
    }
}
