package org.luke.mesa.abs.components.layout.overlay.date_time;

import android.view.Gravity;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.shape.Rectangle;
import org.luke.mesa.abs.components.layout.linear.HBox;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.date_time.Date;
import org.luke.mesa.data.observable.ChangeListener;
import org.luke.mesa.data.property.Property;

public class DatePicker extends FrameLayout implements Styleable {
    private final App owner;

    private final HBox dateFields;

    private final DateTimeField dayField;
    private final DateTimeField monthField;
    private final DateTimeField yearField;

    private final Property<Date> value;

    private final Rectangle selection;

    public DatePicker(App owner) {
        super(owner);
        this.owner = owner;

        value = new Property<>();

        dateFields = new HBox(owner);
        dateFields.setGravity(Gravity.CENTER);

        monthField = new DateTimeField(owner);
        for (int i = 1; i <= 12; i++) {
            monthField.addValues("month_" + i);
        }

        dayField = new DateTimeField(owner);
        for (int i = 1; i <= 31; i++) {
            dayField.addValues("" + i);
        }

        yearField = new DateTimeField(owner);
        for (int i = 1960; i <= 2022; i++) {
            yearField.addValues("" + i);
        }

        dateFields.addView(dayField);
        dateFields.addView(monthField);
        dateFields.addView(yearField);

        ChangeListener<String> listener = (obs, ov, nv) -> value.set(Date.of(
                Integer.parseInt(yearField.getValue()),
                Integer.parseInt(monthField.getValue().split("_")[1]),
                Integer.parseInt(dayField.getValue())));

        dayField.valueProperty().addListener(listener);
        monthField.valueProperty().addListener(listener);
        yearField.valueProperty().addListener(listener);

        value.addListener((obs, ov, nv) -> {
            yearField.setValue(String.valueOf(nv.getYear()));
            monthField.setValue("month_" + nv.getMonth());
            dayField.setValue(String.valueOf(nv.getDay()));
        });

        selection = new Rectangle(owner);
        selection.setHeight(DateTimeField.PRE_UNIT);
        selection.setTranslationY(ViewUtils.dipToPx(DateTimeField.PRE_UNIT, owner));
        selection.setRadius(7);

        addView(selection);
        addView(dateFields);

        applyStyle(owner.getStyle());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        selection.setWidth(ViewUtils.pxToDip(dateFields.getWidth(), owner));
    }

    public Date getValue() {
        return value.get();
    }

    public void setValue(Date value) {
        this.value.set(value);
    }

    public Property<Date> valueProperty() {
        return value;
    }

    @Override
    public void applyStyle(Style style) {
        selection.setFill(style.getBackgroundTertiary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
