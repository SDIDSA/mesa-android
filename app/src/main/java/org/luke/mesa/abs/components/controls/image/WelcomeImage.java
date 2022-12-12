package org.luke.mesa.abs.components.controls.image;

import android.content.ClipData;
import android.graphics.Color;
import android.view.View;

import org.luke.mesa.R;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.data.property.Property;

public class WelcomeImage extends LayerImage implements Styleable {
    public WelcomeImage(App owner) {
        super(owner,
                R.drawable.welcome_received_frame,
                R.drawable.welcome_received_content,
                R.drawable.welcome_phone_frame,
                R.drawable.welcome_phone_screen,
                R.drawable.welcome_sent_frame,
                R.drawable.welcome_sent_content,
                R.drawable.welcome_logo
        );

        applyStyle(owner.getStyle());
    }

    @Override
    public void applyStyle(Style style) {
        //received frames
        setColor(0, style.getTextNormal());
        //received content
        setColor(1, style.getBackgroundPrimary());

        //phone frames
        setColor(2, style.getHeaderSecondary());
        //phone screen
        setColor(3, style.getBackgroundSecondary());

        //sent frames
        setColor(4, style.getAccent());
        //sent content
        setColor(5, Color.WHITE);

        //logo
        setColor(6, style.getHeaderSecondary());
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
