package org.luke.mesa.app.pages.welcome;

import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.combine.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.LinearWidthAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.api.Auth;
import org.luke.mesa.abs.api.Session;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.InputField;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.controls.text.font.FontWeight;
import org.luke.mesa.abs.components.layout.overlay.country.CountryCodeOverlay;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.DataUtils;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.session.SessionPage;
import org.luke.mesa.data.CountryCode;
import org.luke.mesa.data.SessionManager;
import org.luke.mesa.data.property.Property;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Login extends WelcomePageWithBack implements Styleable {
    public static final String REGISTERED_EMAIL_PHONE = "registered_email_phone";

    private final Button login;

    private final Label welcomeBack;
    private final Label etsy;

    private final InputField email_phone;
    private final InputField password;

    private final Label countryCode;
    private final CountryCodeOverlay countryCodeOverlay;
    private final Property<CountryCode> countryCodeProperty;
    private CountryCode lastVal = null;

    public Login(App owner) {
        super(owner);

        welcomeBack = new Label(owner, "welcome_back");
        welcomeBack.setFont(new Font(32f, FontWeight.BOLD));
        ViewUtils.setMarginTop(welcomeBack, owner, 15);

        etsy = new Label(owner, "etsya");
        ViewUtils.setMarginTop(etsy, owner, 10);

        countryCodeProperty = new Property<>();
        countryCodeOverlay = new CountryCodeOverlay(owner);
        countryCodeOverlay.setOnCountryCode(countryCodeProperty::set);

        int countryCodeWidth = ViewUtils.dipToPx(70, owner);

        countryCode = new Label(owner, "");
        countryCode.setFont(new Font(16f));
        countryCode.setFocusable(true);
        countryCode.setClickable(true);
        countryCode.setOnClickListener(e -> countryCodeOverlay.show());
        countryCode.setLayoutParams(new FrameLayout.LayoutParams(countryCodeWidth, FrameLayout.LayoutParams.MATCH_PARENT));

        ViewUtils.setPadding(countryCode, 0, 25, 0, 13, owner);

        FrameLayout preCountryCode = new FrameLayout(owner);
        preCountryCode.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT));
        preCountryCode.addView(countryCode);
        preCountryCode.setAlpha(0);
        preCountryCode.setTranslationY(countryCodeWidth / 4f);

        countryCodeProperty.addListener((obs, ov, nv) -> {
            if (nv != null) lastVal = nv;
            if(nv != null) {
                countryCode.setKey(nv.getShortName() + "  " + nv.getCode());
            }
        });

        ParallelAnimation showCode = new ParallelAnimation(400)
                .addAnimation(new LinearWidthAnimation(preCountryCode, countryCodeWidth))
                .addAnimation(new AlphaAnimation(preCountryCode, 1))
                .addAnimation(new TranslateYAnimation(preCountryCode, 0))
                .setInterpolator(Interpolator.EASE_OUT);
        ParallelAnimation hideCode = new ParallelAnimation(400)
                .addAnimation(new LinearWidthAnimation(preCountryCode, 0))
                .addAnimation(new AlphaAnimation(preCountryCode, 0))
                .addAnimation(new TranslateYAnimation(preCountryCode, countryCodeWidth / 4f))
                .setInterpolator(Interpolator.EASE_OUT);

        email_phone = new InputField(owner, "email_phone");
        email_phone.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        email_phone.addPre(preCountryCode);

        String allowedChars = "0123456789 +-()";
        email_phone.valueProperty().addListener((obs, ov, nv) -> {
            CountryCode cc = countryCodeProperty.get();
            String coc = cc == null ? "" : cc.getCode();
            boolean number = (digitCount(nv) + (nv.startsWith("+") ? 0 : digitCount(coc))) > 6;
            for (int i = 0; i < nv.length() && number; i++) {
                char c = nv.charAt(i);
                if (allowedChars.indexOf(c) == -1) {
                    number = false;
                }
            }

            if (number) {
                if (nv.startsWith("+")) {
                    ArrayList<CountryCode> sorted = new ArrayList<>(DataUtils.readCountryCodes(owner));
                    sorted.sort((c1, c2) -> Integer.compare(c2.getCode().length(), c1.getCode().length()));

                    for (CountryCode code : sorted) {
                        if (nv.startsWith(code.getCode())) {
                            countryCodeProperty.set(code);
                            String stripped = nv.replace(code.getCode(), "").trim();
                            email_phone.setValue(stripped);

                            hideCode.stop();
                            showCode.start();
                            break;
                        }
                    }
                    return;
                }

                TelephonyManager tm = owner.getSystemService(TelephonyManager.class);
                CountryCode.searchAmong(owner, tm.getSimCountryIso(), list -> {
                    if (list.isEmpty()) return;

                    if (lastVal == null) countryCodeProperty.set(list.get(0));
                    else countryCodeProperty.set(lastVal);

                    hideCode.stop();
                    if (!showCode.isRunning()) showCode.start();
                });
            } else {
                showCode.stop();
                if (!hideCode.isRunning()) hideCode.start();
                countryCodeProperty.set(null);
            }
        });

        ViewUtils.setMarginTop(email_phone, owner, 30);

        password = new InputField(owner, "password");
        password.setHidden(true);
        password.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ViewUtils.setMarginTop(password, owner, 10);

        login = new Button(owner, "login");
        login.setFont(new Font(16, FontWeight.BOLD));
        ViewUtils.setMarginTop(login, owner, 30);

        addView(welcomeBack);
        addView(etsy);
        addView(email_phone);
        addView(password);
        addView(login);

        login.setOnClick(() -> {
            String emailPhone = (countryCodeProperty.get() == null ? "" : countryCode.getText()) + email_phone.getValue();

            Phonenumber.PhoneNumber number = null;
            boolean isValid = false;
            try {
                CountryCode cc = countryCodeProperty.get();
                String code = cc == null ? "" : cc.getCode();
                number = PhoneNumberUtil.getInstance().parse(emailPhone, code);
                isValid = PhoneNumberUtil.getInstance().isValidNumber(number);
            } catch (NumberParseException e) {
                e.printStackTrace();
            }

            if (isValid) {
                emailPhone = PhoneNumberUtil.getInstance().format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            }

            Input.clearError(this);
            boolean err = false;
            if (email_phone.getValue().isBlank()) {
                email_phone.setError("This field is required");
                err = true;
            }

            if (password.getValue().isBlank()) {
                password.setError("This field is required");
                err = true;
            }

            if (err) return;
            login.startLoading();

            Auth.auth(emailPhone, password.getValue(), res -> {
                if (res.has("err")) {
                    Input.applyError(res, this);
                    login.stopLoading();
                } else {
                    try {
                        JSONObject user = res.getJSONObject("user");
                        SessionManager.storeSession(res.getString("token"), owner, user.getString("id"));
                        onSuccess(user);
                    } catch (JSONException | URISyntaxException x) {
                        ErrorHandler.handle(x, "login");
                    }
                }
            });
        });

        back.setOnClick(() -> previousInto(WelcomeMain.class));

        applyStyle(owner.getStyle());
    }

    private void onSuccess(JSONObject user) {
        Session.getServers(servers -> {
            try {
                JSONArray servarr = servers.getJSONArray("servers");
                owner.putServers(servarr);
                owner.putData("user", user);

                owner.loadPage(SessionPage.class);
            } catch (JSONException x) {
                ErrorHandler.handle(x, "get servers");
            }
        });
    }

    @Override
    public void hide() {
        hide(back, welcomeBack, etsy, email_phone, password, login);
    }

    @Override
    public void init() {
        super.init();

        login.stopLoading();

        String registered = owner.getString(REGISTERED_EMAIL_PHONE);
        if (registered != null) {
            email_phone.setValue(registered);
            owner.putData(REGISTERED_EMAIL_PHONE, null);
        }

        new SequenceAnimation(600)
                .addAnimation(parallelAnimation(-50, .7f, welcomeBack, etsy, back))
                .addAnimation(parallelAnimation(0, .7f, email_phone))
                .addAnimation(parallelAnimation(0, .7f, password))
                .addAnimation(parallelAnimation(50, .7f, login))
                .setDelay(-400)
                .setInterpolator(Interpolator.OVERSHOOT).start();
    }

    private int digitCount(CharSequence val) {
        int count = 0;
        for (int i = 0; i < val.length(); i++) {
            count += Character.isDigit(val.charAt(i)) ? 1 : 0;
        }
        return count;
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);

        welcomeBack.setTextColor(style.getTextNormal());
        etsy.setTextColor(style.getTextNormal());
        countryCode.setTextColor(style.getTextNormal());
        login.setBackgroundColor(style.getAccent());
        login.setTextFill(Color.WHITE);
    }
}
