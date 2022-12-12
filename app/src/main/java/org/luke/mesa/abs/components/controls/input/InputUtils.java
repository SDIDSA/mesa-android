package org.luke.mesa.abs.components.controls.input;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.luke.mesa.abs.utils.functional.StringConsumer;
import org.luke.mesa.data.property.StringProperty;

public class InputUtils {
    public static void setChangeListener(EditText input, StringConsumer onChange) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence ov, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence nv, int i, int i1, int i2) {
                onChange.accept(String.valueOf(nv));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public static void bindToProperty(EditText input, StringProperty property) {
        setChangeListener(input, property::set);
    }
}
