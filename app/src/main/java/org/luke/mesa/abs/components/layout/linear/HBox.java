package org.luke.mesa.abs.components.layout.linear;

import android.widget.LinearLayout;

import org.luke.mesa.abs.App;

public class HBox extends LinearBox {
    public HBox(App owner) {
        super(owner);
        setOrientation(LinearLayout.HORIZONTAL);
    }
}
