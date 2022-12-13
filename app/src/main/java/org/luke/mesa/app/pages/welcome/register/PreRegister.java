package org.luke.mesa.app.pages.welcome.register;

import android.graphics.Color;

import androidx.core.graphics.drawable.DrawableCompat;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.api.Auth;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.phone_email.EmailPhoneInput;
import org.luke.mesa.abs.components.controls.input.phone_email.RegisterType;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.welcome.WelcomeMain;
import org.luke.mesa.app.pages.welcome.WelcomePageWithBack;

public class PreRegister extends WelcomePageWithBack implements Styleable {
    public static final String PENDING_REGISTER_TYPE = "pending_type";
    public static final String PENDING_PHONE = "pending_phone";
    public static final String PHONE_CODE = "phone_code";
    public static final String PENDING_EMAIL = "pending_email";
    public static final String PENDING_USERNAME = "pending_username";
    public static final String PENDING_PASSWORD = "pending_password";

    private final Label enter;

    private final EmailPhoneInput input;

    private final Button next;

    private final PhoneNumberUtil phoneUtil;

    public PreRegister(App owner) {
        super(owner);

        enter = new Label(owner, "enter_phone_email");
        enter.setFont(new Font(24f, Font.WEIGHT_BOLD));
        ViewUtils.setMarginTop(enter, owner, 35);

        next = new Button(owner, "next");
        next.setFont(new Font(16, Font.WEIGHT_BOLD));
        ViewUtils.setMarginTop(next, owner, 40);

        input = new EmailPhoneInput(owner);
        ViewUtils.setMarginTop(input, owner, 30);

        next.disabledProperty().bind(input.emptyProperty());

        addView(enter);
        addView(input);
        addView(next);

        phoneUtil = PhoneNumberUtil.getInstance();

        next.setOnClick(() -> {
            Input.clearError(this);

            owner.putData(PENDING_REGISTER_TYPE, input.getType());

            next.startLoading();
            if (input.getType() == RegisterType.PHONE) {
                Phonenumber.PhoneNumber number = null;
                boolean isValid = false;
                try {
                    String code = input.getCountryCode().getCode();
                    number = phoneUtil.parse(input.getValue(), code);
                    isValid = phoneUtil.isValidNumber(number);
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }

                if (!isValid) {
                    input.setError("phone_invalid");
                    next.stopLoading();
                } else {
                    String pending = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                    Auth.phoneOwn(pending, res -> {
                        next.stopLoading();
                        if (res.has("err")) {
                            Input.applyError(res, this);
                        } else {
                            owner.putData(PENDING_PHONE, pending);
                            nextInto(PhoneCodePage.class);
                        }
                    });
                }
            } else {
                String email = input.getValue();
                Auth.emailOwn(email, res -> {
                    next.stopLoading();
                    if (res.has("err")) {
                        Input.applyError(res, this);
                    } else {
                        owner.putData(PENDING_EMAIL, email);
                        nextInto(Register.class);
                    }
                });
            }
        });

        back.setOnClick(() -> previousInto(WelcomeMain.class));

        DrawableCompat.wrap(getBackground());

        applyStyle(owner.getStyle());
    }

    @Override
    public void hide() {
        hide(back, enter, input, next);
    }

    @Override
    public void init() {
        new SequenceAnimation(600)
                .addAnimation(parallelAnimation(-50, .7f, back, enter))
                .addAnimation(parallelAnimation(0, .7f, input))
                .addAnimation(parallelAnimation(50, .7f, next))
                .setDelay(-400)
                .setInterpolator(Interpolator.OVERSHOOT).start();
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);

        enter.setTextColor(style.getHeaderPrimary());
        next.setBackgroundColor(style.getAccent());
        next.setTextFill(Color.WHITE);

        setBackgroundColor(style.getBackgroundMobilePrimary());
    }
}
