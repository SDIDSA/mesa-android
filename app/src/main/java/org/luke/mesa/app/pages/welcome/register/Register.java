package org.luke.mesa.app.pages.welcome.register;

import android.graphics.Color;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.api.Auth;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.InputField;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.controls.text.transformationMethods.AllCaps;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.welcome.WelcomePageWithBack;

public class Register extends WelcomePageWithBack implements Styleable {
    private final Label header;

    private final Label pau;
    private final Label ycactl;
    private final Label pass_hint;

    private final Button next;

    private final InputField username;
    private final InputField password;

    public Register(App owner) {
        super(owner);

        header = new Label(owner, "register");
        header.setFont(new Font(24, Font.WEIGHT_BOLD));
        ViewUtils.setMarginTop(header, owner, 10);

        pau = new Label(owner, "pau");
        pau.setFont(new Font(14, Font.WEIGHT_BOLD));
        pau.setTransformationMethod(new AllCaps());
        pau.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ViewUtils.setMargin(pau, owner, 3, 25, 0, 0);

        ycactl = new Label(owner, "ycactl");
        ycactl.setFont(new Font(12f));
        ycactl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ViewUtils.setMargin(ycactl, owner, 3, 7, 0, 0);

        pass_hint = new Label(owner, "pass_hint");
        pass_hint.setFont(new Font(12f));
        pass_hint.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ViewUtils.setMargin(pass_hint, owner, 3, 7, 0, 0);

        username = new InputField(owner, "wsecy", "username");
        username.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ViewUtils.setMarginTop(username, owner, 10);

        password = new InputField(owner, "password");
        password.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        password.setHidden(true);
        ViewUtils.setMarginTop(password, owner, 20);

        next = new Button(owner, "next");
        next.setFont(new Font(16, Font.WEIGHT_BOLD));


        ViewUtils.setMarginTop(next, owner, 20);

        addView(header);
        addView(pau);
        addView(username);
        addView(ycactl);
        addView(password);
        addView(pass_hint);
        addView(next);

        back.setOnClick(() -> previousInto(PreRegister.class));

        next.setOnClick(() -> {
            Input.clearError(this);

            String uname = username.getValue();
            String pword = password.getValue();
            Auth.usernameOwn(uname, pword, res -> {
                next.stopLoading();
                if (res.has("err")) {
                    Input.applyError(res, this);
                } else {
                    owner.putData(PreRegister.PENDING_USERNAME, uname);
                    owner.putData(PreRegister.PENDING_PASSWORD, pword);

                    nextInto(BirthdayPage.class);
                }
            });
        });

        applyStyle(owner.getStyle());
    }

    @Override
    public void hide() {
        hide(back, header, pau, username, ycactl, password, pass_hint, next);
    }

    @Override
    public void init() {
        new SequenceAnimation(600)
                .addAnimation(parallelAnimation(-50, .7f, back, header))
                .addAnimation(parallelAnimation(0, .7f, pau, username, ycactl))
                .addAnimation(parallelAnimation(0, .7f, password, pass_hint))
                .addAnimation(parallelAnimation(50, .7f, next))
                .setDelay(-400)
                .setInterpolator(Interpolator.OVERSHOOT).start();
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);

        header.setTextColor(style.getHeaderPrimary());
        pau.setTextColor(style.getChannelsDefault());
        ycactl.setTextColor(style.getChannelsDefault());
        pass_hint.setTextColor(style.getChannelsDefault());
        next.setBackgroundColor(style.getAccent());
        next.setTextFill(Color.WHITE);

        setBackgroundColor(style.getBackgroundMobilePrimary());
    }
}
