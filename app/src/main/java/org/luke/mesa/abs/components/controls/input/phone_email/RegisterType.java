package org.luke.mesa.abs.components.controls.input.phone_email;

import android.text.InputType;

public enum RegisterType {
    HIDDEN("", InputType.TYPE_CLASS_TEXT),
    PHONE("phone", InputType.TYPE_CLASS_PHONE),
    EMAIL("email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

    private String key;
    private int inputType;
    RegisterType(String key, int inputType) {
        this.key = key;
        this.inputType = inputType;
    }

    public String getKey() {
        return key;
    }

    public int getInputType() {
        return inputType;
    }
}
