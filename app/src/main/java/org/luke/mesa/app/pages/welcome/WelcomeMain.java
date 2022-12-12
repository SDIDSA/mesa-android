package org.luke.mesa.app.pages.welcome;

import android.graphics.Color;
import android.view.Gravity;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.image.Image;
import org.luke.mesa.abs.components.controls.image.WelcomeImage;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.welcome.register.PreRegister;
import org.luke.mesa.data.property.Property;

public class WelcomeMain extends WelcomePage implements Styleable {
    private final WelcomeImage center;
    private final HBox top;
    private final Image mesa;
    private final Label welcome;
    private final Label join;
    private final Button login;
    private final Button register;

    public WelcomeMain(App owner) {
        super(owner);

        welcome = new Label(owner, "welcome");
        welcome.setFont(new Font(24, Font.WEIGHT_BOLD));

        join = new Label(owner, "join_over");
        join.setFont(new Font(14f));
        join.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        register = new Button(owner, "register");
        register.setFont(new Font(16, Font.WEIGHT_BOLD));
        login = new Button(owner, "login");
        login.setFont(new Font(16, Font.WEIGHT_BOLD));

        center = new WelcomeImage(owner);
        ViewUtils.spacer(owner, center);
        top = new HBox(owner);
        top.setVerticalGravity(Gravity.CENTER);

        mesa = new Image(owner, R.drawable.mesa);
        mesa.setHeight(24);

        top.addView(mesa);

        ViewUtils.setMarginTop(top, owner, 30);
        ViewUtils.setMarginTop(center, owner, 40);
        ViewUtils.setMarginTop(welcome, owner, 20);
        ViewUtils.setMarginTop(join, owner, 15);
        ViewUtils.setMarginTop(register, owner, 30);
        ViewUtils.setMarginTop(login, owner, 12);

        addView(top);
        addView(center);
        addView(welcome);
        addView(join);
        addView(register);
        addView(login);

        register.setOnClick(() -> nextInto(PreRegister.class));
        //register.setOnClick(() -> nextInto(getRegister()));
        login.setOnClick(() -> nextInto(Login.class));
        //login.setOnClick(() -> nextInto(BirthdayPage.class));

        applyStyle(owner.getStyle());
    }

    @Override
    public void init() {
        hide(top, center, welcome, join, register, login);
        SequenceAnimation sa = new SequenceAnimation(600)
                .addAnimation(parallelAnimation(-50, .7f, top, center))
                .addAnimation(parallelAnimation(0, .7f, welcome, join))
                .addAnimation(parallelAnimation(50, .7f, register))
                .addAnimation(parallelAnimation(50, .7f, login))
                .setDelay(-400)
                .setInterpolator(Interpolator.OVERSHOOT);

        Platform.runAfter(sa::start, 300);
    }

    @Override
    public boolean goBack() {
        return false;
    }

    @Override
    public void applyStyle(Style style) {
        mesa.setColor(style.getHeaderSecondary());

        welcome.setTextColor(style.getHeaderPrimary());
        join.setTextColor(style.getHeaderSecondary());

        register.setBackgroundColor(style.getAccent());
        register.setTextFill(Color.WHITE);

        login.setBackgroundColor(style.getSecondaryButtonBack());
        login.setTextFill(Color.WHITE);

        setBackgroundColor(style.getBackgroundMobilePrimary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
