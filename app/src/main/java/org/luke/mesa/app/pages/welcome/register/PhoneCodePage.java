package org.luke.mesa.app.pages.welcome.register;

import android.graphics.Color;

import androidx.core.graphics.drawable.DrawableCompat;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.api.Auth;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.phoneCode.PhoneCode;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.welcome.WelcomePageWithBack;

public class PhoneCodePage extends WelcomePageWithBack implements Styleable {
    private final Label enter;
    private final Label sub;

    private final PhoneCode code;

    private final Button next;
    private final Button resend;

    public PhoneCodePage(App owner) {
        super(owner);

        enter = new Label(owner, "enter_phone_code");
        enter.setFont(new Font(24f, Font.WEIGHT_BOLD));
        enter.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ViewUtils.setMarginTop(enter, owner, 25);

        sub = new Label(owner, "sms_sent");
        ViewUtils.setMarginTop(sub, owner, 15);

        code = new PhoneCode(owner, 6);
        code.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        ViewUtils.setMarginTop(code, owner, 40);

        next = new Button(owner, "verify");
        next.setFont(new Font(16, Font.WEIGHT_BOLD));
        ViewUtils.setMarginTop(next, owner, 20);

        resend = new Button(owner, "resend_code");
        resend.setFont(new Font(16, Font.WEIGHT_BOLD));
        ViewUtils.setMarginTop(resend, owner, 10);

        addView(enter);
        addView(sub);
        addView(code);
        addView(next);
        addView(resend);

        back.setOnClick(() -> previousInto(PreRegister.class));

        next.disabledProperty().bind(code.valueProperty().lengthProperty().isLesserThan(code.getCount()));

        resend.setOnClick(() -> {
            resend.startLoading();
            Platform.runAfter(resend::stopLoading, 20000);
        });

        next.setOnClick(() -> {
            next.startLoading();

            Auth.verifyPhoneOwn(owner.getString(PreRegister.PENDING_PHONE), code.getValue(), res -> {
                if (res.has("err")) {
                    next.stopLoading();
                    Input.applyError(res, this);
                } else {
                    code.triggerSuccess("phone_verified");
                    owner.putData(PreRegister.PHONE_CODE, code.getValue());
                    Platform.runAfter(() -> nextInto(Register.class), 1000);
                }
            });
        });

        DrawableCompat.wrap(getBackground());
        applyStyle(owner.getStyle());
    }

    @Override
    public void init() {
        super.init();
        next.stopLoading();
        code.setValue("");

        hide(back, enter, sub, code, next, resend);
        Platform.runAfter(() -> new SequenceAnimation(600)
                .addAnimation(parallelAnimation(-50, .7f, back, enter, sub))
                .addAnimation(parallelAnimation(0, .7f, code))
                .addAnimation(parallelAnimation(50, .7f, next))
                .addAnimation(parallelAnimation(50, .7f, resend))
                .setDelay(-400)
                .setInterpolator(Interpolator.OVERSHOOT).start(), 300);
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);

        enter.setTextColor(style.getHeaderPrimary());
        sub.setTextColor(style.getHeaderSecondary());
        next.setBackgroundColor(style.getAccent());
        next.setTextFill(Color.WHITE);
        resend.setBackgroundColor(style.getSecondaryButtonBack());
        resend.setTextFill(Color.WHITE);

        setBackgroundColor(style.getBackgroundMobilePrimary());
    }
}
