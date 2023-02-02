package org.luke.mesa.app.pages.welcome.register;

import android.graphics.Color;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.combine.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.api.Auth;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.date.DateInput;
import org.luke.mesa.abs.components.controls.input.phone_email.RegisterType;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.controls.text.font.FontWeight;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.abs.utils.functional.JsonConsumer;
import org.luke.mesa.app.pages.welcome.Login;
import org.luke.mesa.app.pages.welcome.WelcomePage;
import org.luke.mesa.app.pages.welcome.WelcomePageWithBack;
import org.luke.mesa.data.date_time.Date;

import java.util.concurrent.atomic.AtomicReference;

public class BirthdayPage extends WelcomePageWithBack implements Styleable {
    private final Label header;
    private final DateInput birthdate;
    private final Button next;

    public BirthdayPage(App owner) {
        super(owner);

        header = new Label(owner, "enter_birthday");
        header.setFont(new Font(24, FontWeight.BOLD));
        ViewUtils.setMarginTop(header, owner, 10);

        birthdate = new DateInput(owner, "birth_date", "birth_date");
        birthdate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ViewUtils.setMarginTop(birthdate, owner, 40);

        birthdate.setDate(Date.of(2000, 1, 1));

        next = new Button(owner, "create_account");
        next.setFont(new Font(16, FontWeight.BOLD));

        ViewUtils.setMarginTop(next, owner, 30);

        addView(header);
        addView(birthdate);
        addView(next);

        back.setOnClick(() -> previousInto(Register.class));

        next.setOnClick(() -> {
            Input.clearError(this);

            next.startLoading();

            AtomicReference<String> email_phone = new AtomicReference<>();
            JsonConsumer onResult = res -> {
                if (res.has("err")) {
                    next.stopLoading();
                    for (WelcomePage page : WelcomePage.getCachedPages()) {
                        if (!Input.applyError(res, page).isEmpty() && page != this) {
                            previousInto(page.getClass());
                            break;
                        }
                    }
                } else {
                    owner.putData(Login.REGISTERED_EMAIL_PHONE, email_phone.get());
                    birthdate.setSuccess("jalm");
                    Platform.runAfter(() -> {
                        nextInto(Login.class);
                        birthdate.clearError();
                        next.stopLoading();
                    }, 2000);
                }
            };

            String username = owner.getString(PreRegister.PENDING_USERNAME);
            String password = owner.getString(PreRegister.PENDING_PASSWORD);
            String bdate = birthdate.getDate().toString();
            RegisterType type = owner.getTypedData(PreRegister.PENDING_REGISTER_TYPE, RegisterType.class);
            switch (type) {
                case PHONE:
                    String phone = owner.getString(PreRegister.PENDING_PHONE);
                    email_phone.set(phone);
                    Auth.registerPhone(phone, username, password, bdate, owner.getString(PreRegister.PHONE_CODE), onResult);
                    break;
                case EMAIL:
                    String email = owner.getString(PreRegister.PENDING_EMAIL);
                    email_phone.set(email);
                    Auth.registerEmail(email, username, password, bdate, onResult);
                    break;
            }
            //TODO save birthday and proceed
        });

        applyStyle(owner.getStyle());
    }

    @Override
    public void hide() {
        hide(back, header, birthdate, next);
    }

    @Override
    public void init() {
        new SequenceAnimation(600)
                .addAnimation(parallelAnimation(-50, .7f, back, header))
                .addAnimation(parallelAnimation(0, .7f, birthdate))
                .addAnimation(parallelAnimation(50, .7f, next))
                .setDelay(-400)
                .setInterpolator(Interpolator.OVERSHOOT).start();
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);

        header.setTextColor(style.getHeaderPrimary());


        next.setBackgroundColor(style.getAccent());
        next.setTextFill(Color.WHITE);
    }
}
