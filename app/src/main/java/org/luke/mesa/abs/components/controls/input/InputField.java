package org.luke.mesa.abs.components.controls.input;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.Animation;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.abs.ValueAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.animation.view.scale.ScaleX;
import org.luke.mesa.abs.animation.view.scale.ScaleXYAnimation;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.image.Image;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.observable.StringObservable;
import org.luke.mesa.data.property.Property;
import org.luke.mesa.data.property.StringProperty;

public class InputField extends FrameLayout implements Input, Styleable {
    protected final EditText input;
    protected final App owner;
    private final HBox preInput;
    private final GradientDrawable background;
    private final StringProperty value;

    private final Label prompt;
    private final Label errorLabel;
    private final FrameLayout prompts;
    private final ParallelAnimation focus, unfocus;
    private final ParallelAnimation showError, hideError;
    private final Animation timer;

    private final String key;
    private final Image clear;
    private boolean success = false;
    private boolean error = false;
    private Image showPassword = null;
    private boolean passShown = false;

    public InputField(App owner, String promptText) {
        this(owner, promptText, promptText);
    }

    public InputField(App owner, String promptText, String key) {
        super(owner);
        this.key = key;
        this.owner = owner;
        background = new GradientDrawable();
        setRadius(7);

        value = new StringProperty();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        prompt = new Label(owner, promptText);
        prompt.setFont(new Font(14f, Font.WEIGHT_BOLD));
        prompt.setMaxLines(1);
        prompt.setLines(1);

        errorLabel = new Label(owner, "");
        errorLabel.setFont(new Font(14f, Font.WEIGHT_BOLD));
        errorLabel.setMaxLines(1);
        errorLabel.setLines(1);

        int errorBy = -ViewUtils.dipToPx(30, owner);
        errorLabel.setTranslationY(-errorBy);
        errorLabel.setAlpha(0);

        showError = new ParallelAnimation(400)
                .addAnimation(new AlphaAnimation(errorLabel, 1))
                .addAnimation(new TranslateYAnimation(errorLabel, 0))
                .addAnimation(new AlphaAnimation(prompt, 0))
                .addAnimation(new TranslateYAnimation(prompt, errorBy))
                .setInterpolator(Interpolator.EASE_OUT);

        hideError = new ParallelAnimation(400)
                .addAnimation(new AlphaAnimation(errorLabel, 0))
                .addAnimation(new TranslateYAnimation(errorLabel, -errorBy))
                .addAnimation(new AlphaAnimation(prompt, 1))
                .addAnimation(new TranslateYAnimation(prompt, 0))
                .setInterpolator(Interpolator.EASE_OUT);

        input = new EditText(owner);
        input.setLayoutParams(params);
        input.setTypeface(Font.DEFAULT.getFont());
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        input.setShowSoftInputOnFocus(true);
        ViewUtils.setPadding(input, 0, 25, 0, 13, owner);
        input.setBackground(null);
        input.setMaxLines(1);
        input.setLines(1);
        input.setSingleLine(true);

        preInput = new HBox(owner);
        preInput.setVerticalGravity(Gravity.CENTER);
        preInput.addView(input);
        ViewUtils.setPadding(preInput, 15, 0, 15, 0, owner);

        prompts = new FrameLayout(owner);
        prompts.setPivotX(ViewUtils.dipToPx(15, owner));
        prompts.setPivotY(-ViewUtils.dipToPx(20, owner));
        prompts.setAlpha(.5f);
        prompts.setClickable(false);
        prompts.setFocusable(false);
        ViewUtils.setPadding(prompts, 15, 20, 5, 20, owner);

        prompts.addView(prompt);
        prompts.addView(errorLabel);

        addView(preInput);
        addView(prompts);

        timer = new ValueAnimation(2000, 0, 1) {
            @Override
            public void updateValue(float v) {
                //do nothing
            }
        }.setFps(5).setOnFinished(() -> {
            showError.stop();
            hideError.start();
        });

        focus = new ParallelAnimation(200)
                .addAnimation(new ScaleX(prompts, .75f))
                .addAnimation(new ScaleXYAnimation(prompts, .75f))
                .addAnimation(new AlphaAnimation(prompts, 1f))
                .setInterpolator(Interpolator.EASE_OUT);

        unfocus = new ParallelAnimation(200)
                .addAnimation(new ScaleX(prompts, 1))
                .addAnimation(new ScaleXYAnimation(prompts, 1))
                .addAnimation(new AlphaAnimation(prompts, .5f))
                .setInterpolator(Interpolator.EASE_OUT);

        input.setOnFocusChangeListener((view, focused) -> {
            if (getValue().length() > 0)
                return;

            if (focused) {
                unfocus.stop();
                focus.start();
            } else {
                focus.stop();
                unfocus.start();
            }
        });

        clear = new Image(owner, R.drawable.clear);
        clear.setHeight(20);
        clear.setWidth(20);
        clear.setVisibility(INVISIBLE);
        clear.setOnClick(() -> setValue(""));

        valueProperty().addListener((obs, ov, nv) -> {
            clear.setVisibility(nv.isEmpty() ? INVISIBLE : VISIBLE);
            clearError();
            if (ov.isEmpty() && !nv.isEmpty()) {
                unfocus.stop();
                focus.start();
            }
        });

        addPost(clear);

        InputUtils.bindToProperty(input, value);

        preInput.setBackground(background);
        applyStyle(owner.getStyle());
    }

    public void setTextMinWidth(int width) {
        prompts.setLayoutParams(new LayoutParams(ViewUtils.dipToPx(width, owner), ViewGroup.LayoutParams.WRAP_CONTENT));
        preInput.setLayoutParams(new LayoutParams(ViewUtils.dipToPx(width, owner), ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    public void addPost(Image icon) {
        icon.setSize(35);
        ViewUtils.setPaddingUnified(icon, 6, owner);
        preInput.addView(icon);
    }

    public HBox getPreInput() {
        return preInput;
    }

    public void addPre(View view) {
        preInput.addView(view, 0);
    }

    public void removeClear() {
        preInput.removeView(clear);
    }

    public void setHidden(boolean hidden) {
        if (hidden) {
            input.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPassword = new Image(owner, R.drawable.show_password);
            showPassword.setColor(Color.WHITE);
            addPost(showPassword);

            showPassword.setOnClick(() -> {
                int selection = input.getSelectionStart();
                if (passShown) {
                    showPassword.setImageResource(R.drawable.show_password);
                    input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passShown = false;
                } else {
                    showPassword.setImageResource(R.drawable.hidden_password);
                    input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passShown = true;
                }
                input.setSelection(selection);
            });

            applyStyle(owner.getStyle().get());
        }
    }

    public StringObservable valueProperty() {
        return value;
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        if (input.getText().length() == 0 && !isFocused()) {
            unfocus.stop();
            focus.start();
        }
        input.setText(value);
        input.setSelection(input.getText().length());
    }

    public void setInputType(int inputType) {
        input.setInputType(inputType);
    }

    public void setRadius(float radius) {
        background.setCornerRadius(ViewUtils.dipToPx(radius, owner));
    }

    public void setBackgroundColor(int color) {
        background.setColor(color);
    }

    public void setBorderColor(int color) {
        background.setStroke(2, color);
    }

    public void setPromptText(String key) {
        prompt.setKey(key);
    }

    public String getKey() {
        return key;
    }

    @Override
    public void setError(String errorKey, String plus) {
        hideError.stop();

        showError.start();
        timer.start();

        error = true;
        errorLabel.setKey(errorKey);

        if (plus != null)
            errorLabel.addParam(0, plus);

        applyStyle(owner.getStyle());
    }

    public void setSuccess(String successKey, String plus) {
        hideError.stop();

        showError.start();
        timer.start();

        success = true;
        errorLabel.setKey(successKey);

        if (plus != null)
            errorLabel.addParam(0, plus);

        applyStyle(owner.getStyle());
    }

    public void setError(String errorKey) {
        setError(errorKey, null);
    }

    public void setSuccess(String successKey) {
        setSuccess(successKey, null);
    }

    public void clearError() {
        showError.stop();
        timer.stop();

        hideError.start();

        error = false;
        success = false;
        applyStyle(owner.getStyle());
    }

    @Override
    public void applyStyle(Style style) {
        setBackgroundColor(style.getBackgroundTertiary());
        setBorderColor(error ? style.getTextDanger() : success ? style.getTextPositive() : Color.TRANSPARENT);
        input.setTextColor(style.getTextNormal());
        @ColorInt int textColor = error ? style.getTextDanger() : success ? style.getTextPositive() : style.getChannelsDefault();
        prompt.setTextColor(textColor);
        errorLabel.setTextColor(textColor);

        clear.setColor(style.getChannelsDefault());

        if (showPassword != null) showPassword.setColor(style.getChannelsDefault());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
