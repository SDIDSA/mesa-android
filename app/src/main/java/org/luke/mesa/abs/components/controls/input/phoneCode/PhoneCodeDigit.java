package org.luke.mesa.abs.components.controls.input.phoneCode;

import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public class PhoneCodeDigit extends FrameLayout implements Styleable {
    protected final Label input;
    private final App owner;
    private final GradientDrawable background;

    public PhoneCodeDigit(App owner) {
        super(owner);
        this.owner = owner;
        background = new GradientDrawable();
        setBackground(background);
        setRadius(7);

        input = new Label(owner, "");
        input.setLayoutParams(new LayoutParams(ViewUtils.dipToPx(35, owner), ViewUtils.dipToPx(45, owner)));
        input.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        input.setGravity(Gravity.CENTER);
        input.setFont(new Font(22f));
        input.setBackground(null);

        addView(input);

        applyStyle(owner.getStyle());
    }

    public void setRadius(float radius) {
        background.setCornerRadius(ViewUtils.dipToPx(radius, owner));
    }

    public void setPaddingLeft(float val) {
        ViewUtils.setPadding(this, val, 0,0,0, owner);
    }

    public void setPaddingRight(float val) {
        ViewUtils.setPadding(this, 0, 0,val,0, owner);
    }

    public void setRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        float tlp = ViewUtils.dipToPx(topLeft, owner);
        float trp = ViewUtils.dipToPx(topRight, owner);
        float brp = ViewUtils.dipToPx(bottomRight, owner);
        float blp = ViewUtils.dipToPx(bottomLeft, owner);
        background.setCornerRadii(new float[] {
            tlp,tlp, trp,trp, brp,brp, blp,blp
        });
    }

    public void setBackgroundColor(int color) {
        background.setColor(color);
    }

    public void setValue(String value) {
       input.setText(value);
    }

    @Override
    public void applyStyle(Style style) {
        setBackgroundColor(style.getBackgroundTertiary());
        input.setTextColor(style.getTextNormal());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
