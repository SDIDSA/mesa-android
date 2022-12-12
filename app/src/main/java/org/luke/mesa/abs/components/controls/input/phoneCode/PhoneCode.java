package org.luke.mesa.abs.components.controls.input.phoneCode;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.SpacingAnimation;
import org.luke.mesa.abs.animation.view.corner_radii.CornerRadiiAnimation;
import org.luke.mesa.abs.animation.view.corner_radii.LeftCornerRadiiAnimation;
import org.luke.mesa.abs.animation.view.corner_radii.RightCornerRadiiAnimation;
import org.luke.mesa.abs.animation.view.padding.LeftPaddingAnimation;
import org.luke.mesa.abs.animation.view.padding.RightPaddingAnimation;
import org.luke.mesa.abs.components.controls.input.Input;
import org.luke.mesa.abs.components.controls.input.InputUtils;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.observable.StringObservable;
import org.luke.mesa.data.property.Property;
import org.luke.mesa.data.property.StringProperty;

public class PhoneCode extends FrameLayout implements Styleable, Input {
    private final App owner;
    private final EditText et;
    private final StringProperty value;
    private final GradientDrawable border;

    private final InfoLabel errorLabel;
    private final InfoLabel successLabel;

    private final int count;

    public PhoneCode(App owner, int count) {
        super(owner);
        this.owner = owner;
        this.count = count;
        border = new GradientDrawable();
        setBackground(border);
        setRadius(7);

        errorLabel = new InfoLabel(owner, Style::getTextDanger);

        successLabel = new InfoLabel(owner, Style::getTextPositive);

        value = new StringProperty();

        HBox root = new HBox(owner);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        root.setHorizontalGravity(Gravity.CENTER);

        PhoneCodeDigit[] digits = new PhoneCodeDigit[count];
        for (int i = 0; i < count; i++) {
            PhoneCodeDigit digit = new PhoneCodeDigit(owner);
            digit.setRadius(0);
            digits[i] = digit;
            root.addView(digit);
        }
        digits[0].setRadius(7, 0, 0, 7);
        digits[count - 1].setRadius(0, 7, 7, 0);
        int targetSpacing = ViewUtils.dipToPx(15, owner);
        digits[0].setPaddingLeft(targetSpacing);
        digits[count - 1].setPaddingRight(targetSpacing);

        et = new EditText(owner);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        et.setBackground(null);
        et.setTextColor(Color.TRANSPARENT);
        et.setCursorVisible(false);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(count)});

        root.setOnClickListener(e -> owner.showKeyboard(et));

        InputUtils.setChangeListener(et, nv -> {
            StringBuilder normalized = new StringBuilder();
            for (char c : nv.toCharArray()) {
                if (Character.isDigit(c)) {
                    normalized.append(c);
                }
            }
            String nov = normalized.toString();
            if (nov.equals(nv)) {
                value.set(nov);
            } else {
                et.setText(nov);
                et.setSelection(nov.length());
            }
        });

        ParallelAnimation space = new ParallelAnimation(400)
                .addAnimation(new SpacingAnimation(root, 0)
                        .setLateTo(() -> {
                            int digitSize = digits[1].getWidth() * count;
                            int fullWidth = root.getWidth();
                            float remaining = fullWidth - digitSize;
                            return ViewUtils.pxToDip(remaining / (count - 1), owner);
                        }))
                .addAnimation(new LeftPaddingAnimation(digits[0], 0))
                .addAnimation(new RightPaddingAnimation(digits[count - 1], 0))
                .setInterpolator(Interpolator.EASE_OUT);

        int radii = ViewUtils.dipToPx(7, owner);
        space.addAnimation(new RightCornerRadiiAnimation(digits[0], radii));
        space.addAnimation(new LeftCornerRadiiAnimation(digits[count - 1], radii));
        for (int i = 1; i < count - 1; i++) {
            space.addAnimation(new CornerRadiiAnimation(digits[i], radii));
        }

        ParallelAnimation unspace = new ParallelAnimation(400)
                .addAnimation(new SpacingAnimation(root, 0))
                .addAnimation(new LeftPaddingAnimation(digits[0], targetSpacing))
                .addAnimation(new RightPaddingAnimation(digits[count - 1], targetSpacing))
                .setInterpolator(Interpolator.EASE_OUT);

        unspace.addAnimation(new RightCornerRadiiAnimation(digits[0], 0));
        unspace.addAnimation(new LeftCornerRadiiAnimation(digits[count - 1], 0));
        for (int i = 1; i < count - 1; i++) {
            unspace.addAnimation(new CornerRadiiAnimation(digits[i], 0));
        }

        et.setOnFocusChangeListener((v, f) -> {
            if (!f)
                owner.hideKeyboard(et);
        });

        value.addListener((obs, ov, nv) -> {
            for (int i = 0; i < nv.length(); i++) {
                digits[i].setValue(Character.toString(nv.charAt(i)));
            }
            for (int i = nv.length(); i < count; i++) {
                digits[i].setValue("");
            }
        });

        value.lengthProperty().addListener((obs, ov, nv) -> {
            if (nv == count || nv == 0) {
                space.stop();
                if (!unspace.isRunning())
                    unspace.start();
            } else {
                unspace.stop();
                if (!space.isRunning())
                    space.start();
            }
        });

        setClipToPadding(false);

        ViewUtils.setPadding(this, 0, 0, 0, 40, owner);

        addView(et);
        addView(errorLabel);
        addView(successLabel);
        addView(root);

        root.setOnCreateContextMenuListener((menu, view, menuInfo) -> {
            menu.add("paste");
            menu.getItem(0).setOnMenuItemClickListener(item -> {
                ClipboardManager clipboard = getSystemService(owner, ClipboardManager.class);
                if (clipboard != null && clipboard.getPrimaryClip().getItemCount() > 0) {
                    CharSequence txt = clipboard.getPrimaryClip().getItemAt(0).getText();
                    et.setText(txt);
                    et.setSelection(et.getText().length());
                }
                return true;
            });
        });
        applyStyle(owner.getStyle());
    }

    public void setRadius(float radius) {
        border.setCornerRadius(ViewUtils.dipToPx(radius, owner));
    }

    public void triggerSuccess(String successKey) {
        successLabel.trigger(successKey);
    }

    @Override
    public void setError(String errorKey, String plus) {
        errorLabel.trigger(errorKey, plus);
    }

    public void setError(String errorKey) {
        errorLabel.trigger(errorKey);
    }

    public void clearError() {
        errorLabel.clear();
    }

    @Override
    public String getValue() {
        return value.get();
    }

    @Override
    public void setValue(String value) {
        et.setText("");
    }

    @Override
    public StringObservable valueProperty() {
        return value;
    }

    @Override
    public String getKey() {
        return "phone_code";
    }

    public int getCount() {
        return count;
    }

    @Override
    public void applyStyle(Style style) {
        errorLabel.setTextColor(style.getTextDanger());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
