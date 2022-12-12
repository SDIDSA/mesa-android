package org.luke.mesa.abs.components.controls.input.phone_email;

import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.Animation;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.LinearWidthAnimation;
import org.luke.mesa.abs.animation.view.SpacingAnimation;
import org.luke.mesa.abs.api.API;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.InputField;
import org.luke.mesa.abs.components.controls.input.countryCode.CountryCodeInput;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.components.layout.linear.VBox;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.CountryCode;
import org.luke.mesa.data.binding.Bindings;
import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.observable.BooleanObservable;
import org.luke.mesa.data.observable.StringObservable;

public class EmailPhoneInput extends VBox implements Input {
    private final InputField input;
    private final RegisterTypes types;

    private Animation addedToShow = null;
    private Animation addedToHide = null;

    private final BooleanObservable empty;

    private final StringObservable value;

    private final CountryCodeInput country;

    public EmailPhoneInput(App owner) {
        super(owner);
        setSpacing(20);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        types = new RegisterTypes(owner);

        input = new InputField(owner, "email");
        input.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        ViewUtils.spacer(owner, input);

        int countryShown = ViewUtils.dipToPx(110, owner);
        country = new CountryCodeInput(owner, "Country Code");
        country.setLayoutParams(new FrameLayout.LayoutParams(0, FrameLayout.LayoutParams.WRAP_CONTENT));

        HBox inputCont = new HBox(owner);
        inputCont.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        inputCont.addView(country);
        inputCont.addView(input);

        ParallelAnimation hide = new ParallelAnimation(300)
                .setInterpolator(Interpolator.EASE_OUT);
        hide.addAnimation(new LinearWidthAnimation(300, country, 0));
        hide.addAnimation(new AlphaAnimation(300, country, 0));
        hide.addAnimation(new SpacingAnimation(300, inputCont, 0));

        ParallelAnimation show = new ParallelAnimation(300)
                .setInterpolator(Interpolator.EASE_OUT);
        show.addAnimation(new LinearWidthAnimation(300, country, countryShown));
        show.addAnimation(new AlphaAnimation(300, country, 1));
        show.addAnimation(new SpacingAnimation(300, inputCont, 10));

        types.typeProperty().addListener((obs, ov, nv) -> {
            if (nv == ov) {
                return;
            }

            input.setPromptText(nv.getKey());
            input.setInputType(nv.getInputType());

            if (nv.equals(RegisterType.EMAIL)) {
                if(addedToHide == null) {
                    addedToHide = types.generateAnimation();
                    hide.addAnimation(addedToHide);
                }
                show.stop();
                hide.start();
            } else {
                if(addedToShow == null) {
                    addedToShow = types.generateAnimation();
                    show.addAnimation(addedToShow);
                }
                hide.stop();
                show.start();

                if(country.getCountryCode() == null) {
                    TelephonyManager tm = owner.getSystemService(TelephonyManager.class);
                    CountryCode.searchAmong(owner, tm.getSimCountryIso(), list -> {
                        country.setValue(list.get(0).getName());
                    });
                }
            }
        });

        BooleanObservable isPhone = types.typeProperty().isEqualTo(RegisterType.PHONE);
        BooleanBinding emailCheck = input.valueProperty().isEmpty();
        BooleanBinding phoneCheck = emailCheck.or(country.valueProperty().isEmpty());
        empty =  Bindings.whenBoolean(isPhone).then(phoneCheck).otherwise(emailCheck);

        value = Bindings.whenString(isPhone).then(country.countryCodeProperty().concat(input.valueProperty())).otherwise(input.valueProperty());

        addView(types);
        addView(inputCont);
    }

    public BooleanObservable emptyProperty() {
        return empty;
    }

    public RegisterType getType() {
        return types.getType();
    }

    @Override
    public void setValue(String value) {
        input.setValue(value);
    }

    public CountryCode getCountryCode() {
        return country.getCountryCode();
    }

    @Override
    public void setError(String error, String plus) {
        input.setError(error, plus);
        country.setError(" ");
    }

    @Override
    public void setError(String error) {
        input.setError(error);
        country.setError(" ");
    }

    @Override
    public void clearError() {
        input.clearError();
        country.clearError();
    }

    @Override
    public String getValue() {
        return value.get();
    }

    @Override
    public StringObservable valueProperty() {
        return value;
    }

    @Override
    public String getKey() {
        return getType() == RegisterType.PHONE ? "phone" : "email";
    }
}
