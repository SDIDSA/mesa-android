package org.luke.mesa.abs.components.layout.overlay.date_time;

import android.graphics.Color;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.controls.button.Button;
import org.luke.mesa.abs.components.layout.overlay.SlideOverlay;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.date_time.Date;
import org.luke.mesa.data.property.Property;

public class DateTimeOverlay extends SlideOverlay {
    private final DatePicker date;

    private final Button done;

    public DateTimeOverlay(App owner) {
        super(owner);

        done = new Button(owner, "done");
        done.setFont(new Font(16, Font.WEIGHT_BOLD));

        date = new DatePicker(owner);

        list.addView(ViewUtils.spacer(owner));
        list.addView(date);
        list.addView(ViewUtils.spacer(owner));
        list.addView(done);

        done.setOnClick(this::hide);

        setHeightFactor(.5);
        applyStyle(owner.getStyle());
    }

    public Date getDate() {
        return date.getValue();
    }

    public void setDate(Date date) {
        this.date.setValue(date);
    }

    public Property<Date> dateProperty() {
        return date.valueProperty();
    }

    @Override
    public void applyInsets(Insets insets) {
        ViewUtils.setMargin(done, owner, 0, 15, 0, 15 + ViewUtils.pxToDip(insets.bottom, owner));
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);
        if (done == null) return;
        done.setTextFill(Color.WHITE);
        done.setBackgroundColor(style.getSecondaryButtonBack());
    }
}
