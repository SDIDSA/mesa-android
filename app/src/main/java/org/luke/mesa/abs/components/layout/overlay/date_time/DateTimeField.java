package org.luke.mesa.abs.components.layout.overlay.date_time;

import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.abs.ValueAnimation;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;
import org.luke.mesa.data.property.StringProperty;

import java.util.ArrayList;
import java.util.Collections;

public class DateTimeField extends FrameLayout implements Styleable {
    public static final int PRE_UNIT = 45;
    private final int UNIT;
    private final ArrayList<String> values;
    private final Option[] options = new Option[5];
    private final StringProperty value;
    private int pos = 0;
    private float initTY, initPY;

    public DateTimeField(App owner) {
        super(owner);
        UNIT = ViewUtils.dipToPx(PRE_UNIT, owner);
        values = new ArrayList<>();

        value = new StringProperty();

        for (int i = 0; i < options.length; i++) {
            options[i] = new Option(owner);
            addView(options[i]);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UNIT * 3);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);
        setForegroundGravity(Gravity.CENTER);

        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initTY = event.getRawY();
                    initPY = pos;
                    break;
                case MotionEvent.ACTION_UP:
                    new ValueAnimation(200, pos, (int) ((pos + (float) UNIT / (pos >= 0 ? 2 : -2)) / UNIT) * UNIT) {
                        @Override
                        public void updateValue(float v) {
                            setPos((int) v);
                        }
                    }.start();
                    performClick();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float ny = event.getRawY();
                    float dy = ny - initTY;
                    float np = initPY + dy;
                    setPos((int) np);
                    break;
            }
            return true;
        });

        setClipToPadding(false);
        ViewUtils.setPadding(this, 0, 2 * UNIT, 0, 2 * UNIT, owner);
    }

    private void setPos(int pos) {
        this.pos = pos;
        layout();
    }

    public void addValues(String... values) {
        Collections.addAll(this.values, values);
        layout();
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        for (int i = 0; i < values.size(); i++) {
            String val = values.get(i);
            if (val.equals(value) && !value.equals(getValue())) {
                this.value.set(value);
                setPos(-i * UNIT);
                break;
            }
        }
    }

    public StringProperty valueProperty() {
        return value;
    }

    private void layout() {
        int element = -pos / UNIT;
        int ty = pos % UNIT;

        for (int i = 0; i < options.length; i++) {
            int valueIndex = element + i - 2;
            valueIndex = (valueIndex % values.size() + values.size()) % values.size();
            if (valueIndex >= 0 && valueIndex < values.size()) {
                options[i].setKey(values.get(valueIndex));
            }
            int ety = UNIT * (i - 2) + ty;
            int distance = Math.abs(ety);
            float factor = 1 - ((float) distance / UNIT) * .5f;
            options[i].setAlpha(factor);
            options[i].setScaleX(factor * .5f + .5f);
            options[i].setScaleY(factor * .5f + .5f);
            options[i].setTranslationY(ety);
        }
        value.set(options[2].getKey());
    }

    @Override
    public void applyStyle(Style style) {
        for (Option option : options) {
            option.setTextColor(style.getTextNormal());
        }
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }

    private static class Option extends FrameLayout {
        private final Label label;

        public Option(App owner) {
            super(owner);
            int UNIT = ViewUtils.dipToPx(PRE_UNIT, owner);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, UNIT);
            params.gravity = Gravity.CENTER;
            setLayoutParams(params);

            setForegroundGravity(Gravity.CENTER);

            label = new Label(owner, "");
            label.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            label.setFont(new Font(20f));
            label.setSingleLine(true);
            label.setLayoutGravity(Gravity.CENTER);

            addView(label);
        }

        public void setKey(String key) {
            label.setKey(key);
        }

        public String getKey() {
            return label.getKey();
        }

        public void setTextColor(@ColorInt int color) {
            label.setTextColor(color);
        }
    }
}
