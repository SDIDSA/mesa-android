package org.luke.mesa.abs.components.controls.text;

import android.util.TypedValue;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.locale.Locale;
import org.luke.mesa.abs.locale.Localized;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

import java.util.ArrayList;

public class Label extends androidx.appcompat.widget.AppCompatTextView implements Localized {
    private final App owner;
    private String key;
    private ArrayList<String> params = new ArrayList<>();

    public Label(App owner, String key) {
        super(owner);
        this.owner = owner;
        this.key = key;

        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        setFont(Font.DEFAULT);
        applyLocale(owner.getLocale());
    }

    public App getOwner() {
        return owner;
    }

    public void setLayoutGravity(int gravity) {
        ((FrameLayout.LayoutParams) getLayoutParams()).gravity = gravity;
    }

    public Label setFont(Font font) {
        setTypeface(font.getFont());
        setTextSize(TypedValue.COMPLEX_UNIT_SP, font.getSize());
        return this;
    }

    public void setKey(String key) {
        this.key = key;
        applyLocale(owner.getLocale().get());
    }

    public String getKey() {
        return key;
    }

    public void addParam(int i, String param) {
        if (i >= params.size()) {
            params.add(param);
        } else {
            params.set(i, param);
        }
        applyLocale(owner.getLocale().get());
    }

    public void setLineSpacing(float spacing) {
        setLineSpacing(ViewUtils.dipToPx(spacing, owner), 1);
    }

    @Override
    public void applyLocale(Locale locale) {
        if (key != null && !key.isEmpty()) {
            String val = locale.get(key);
            for (int i = 0; i < params.size(); i++) {
                String param = params.get(i);
                param = (param.charAt(0) == '&' && param.length() > 1) ? locale.get(param.substring(1)) : param;
                val = val.replace("{$" + i + "}", param);
            }
            setText(val);
        } else {
            setText("");
        }
    }

    @Override
    public void applyLocale(Property<Locale> locale) {
        Localized.bindLocale(this, locale);
    }
}
