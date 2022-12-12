package org.luke.mesa.abs.components.layout.overlay.country;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.CountryCode;
import org.luke.mesa.data.property.Property;

public class CountryCodeEntry extends HBox implements Styleable {
    private final Label name;
    private final Label code;

    public CountryCodeEntry(App owner) {
        super(owner);
        ViewUtils.setPaddingUnified(this, 15, owner);
        setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        setFocusable(true);
        setClickable(true);

        name = new Label(owner, "");
        name.setFont(Font.DEFAULT);
        ViewUtils.spacer(owner, name);

        code = new Label(owner, "");
        code.setFont(Font.DEFAULT);

        addView(name);
        addView(code);

        setBackground(new GradientDrawable());

        applyStyle(owner.getStyle());
    }

    public void load(CountryCode data) {
        name.setText(data.getName());
        code.setText(data.getCode());
    }

    @Override
    public void applyStyle(Style style) {
        name.setTextColor(style.getTextNormal());
        code.setTextColor(style.getTextMuted());

        setOnTouchListener((view, action) -> {
            switch (action.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setBackgroundColor(style.getBackgroundModifierHover());
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                case MotionEvent.ACTION_CANCEL:
                    setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
            return true;
        });
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
