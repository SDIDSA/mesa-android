package org.luke.mesa.abs.components.controls.input.phone_email;

import android.graphics.drawable.GradientDrawable;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.Animation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.position.TranslateXAnimation;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

public class RegisterTypes extends FrameLayout implements Styleable {
    private final App owner;
    private final GradientDrawable background;
    private final Button selected;

    private final RegisterTypeButton phone, email;
    private RegisterTypeButton selectedButton;

    private final Property<RegisterType> type;

    public RegisterTypes(App owner) {
        super(owner);
        this.owner = owner;
        background = new GradientDrawable();
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setRadius(7);
        ViewUtils.setPaddingUnified(this,4, owner);

        type = new Property<>();

        selected = new RegisterTypeButton(owner, RegisterType.HIDDEN);
        selected.setAlpha(.5f);
        Button hidden = new RegisterTypeButton(owner, RegisterType.HIDDEN);

        phone = new RegisterTypeButton(owner, RegisterType.PHONE);
        email = new RegisterTypeButton(owner, RegisterType.EMAIL);

        HBox underButtons = new HBox(owner);
        underButtons.addView(selected);
        underButtons.addView(hidden);

        HBox buttons = new HBox(owner);
        buttons.addView(email);
        buttons.addView(phone);

        addView(underButtons);
        addView(buttons);

        phone.setOnClick(() -> select(phone, email));
        email.setOnClick(() -> select(email, phone));

        setBackground(background);

        select(email, phone);
        applyStyle(owner.getStyle());
    }

    private void select(RegisterTypeButton select, RegisterTypeButton unselect) {
        selectedButton = select;
        type.set(select.getType());
        select.select();
        unselect.unselect();
    }

    public Animation generateAnimation() {
        int to = selectedButton.getLeft();
        if(((HBox) selectedButton.getParent()).indexOfChild(selectedButton) == 1) {
            to = ((HBox) selectedButton.getParent()).getWidth() - selected.getWidth();
        }

        return new TranslateXAnimation(300, selected, to)
                .setInterpolator(Interpolator.EASE_OUT);
    }

    public RegisterType getType() {
        return type.get();
    }

    public void setRadius(float radius) {
        setRadius(radius, background);
    }

    public void setRadius(float radius, GradientDrawable back) {
        back.setCornerRadius(ViewUtils.dipToPx(radius, owner));
    }

    public Property<RegisterType> typeProperty() {
        return type;
    }

    @Override
    public void applyStyle(Style style) {
        background.setColor(style.getBackgroundTertiary());
        selected.setBackgroundColor(style.getTextMuted());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }

    private static class RegisterTypeButton extends Button implements Styleable {
        private final RegisterType type;
        boolean selected = false;

        public RegisterTypeButton(App owner, RegisterType type) {
            super(owner, type.getKey());
            this.type = type;
            ViewUtils.spacer(owner, this);
            ViewUtils.setPaddingUnified(this, 10, owner);
            setFont(new Font(14, Font.WEIGHT_BOLD));
            setRadius(5);
            setDimOnTouch(false);
            applyStyle(owner.getStyle());
        }

        public RegisterType getType() {
            return type;
        }

        public void unselect() {
            selected = false;
            setAlpha(.6f);
        }

        public void select() {
            selected = true;
            setAlpha(1.0f);
        }

        @Override
        public void applyStyle(Style style) {
            setTextFill(style.getTextNormal());
        }

        @Override
        public void applyStyle(Property<Style> style) {
            Styleable.bindStyle(this, style);
        }
    }
}
