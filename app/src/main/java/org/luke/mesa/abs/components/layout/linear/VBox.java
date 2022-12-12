package org.luke.mesa.abs.components.layout.linear;

import android.widget.LinearLayout;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.ViewUtils;

public class VBox extends LinearBox{
    public VBox(App owner) {
        super(owner);
        setOrientation(LinearLayout.VERTICAL);
    }
}
