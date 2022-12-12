package org.luke.mesa.abs.components.controls.input.date;

import android.text.InputType;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.input.InputField;
import org.luke.mesa.abs.components.layout.overlay.date_time.DateTimeOverlay;
import org.luke.mesa.data.date_time.Date;

public class DateInput extends InputField {
    private final DateTimeOverlay dateOverlay;

    public DateInput(App owner, String promptText, String key) {
        super(owner, promptText, key);
        input.setFocusable(false);
        input.setClickable(false);

        setFocusable(true);
        setClickable(true);

        dateOverlay = new DateTimeOverlay(owner);

        InputMethodManager imm = (InputMethodManager) owner.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);

        OnClickListener onClick = v -> {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            dateOverlay.show();
        };

        dateOverlay.dateProperty().addListener((obs, ov, nv) -> setValue(nv.toString()));

        input.setOnClickListener(onClick);
        setOnClickListener(onClick);

        removeClear();
        setInputType(InputType.TYPE_CLASS_PHONE);
    }

    public Date getDate() {
        return dateOverlay.getDate();
    }

    public void setDate(Date date) {
        dateOverlay.setDate(date);
    }
}
