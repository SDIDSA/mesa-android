package org.luke.mesa.app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.app.pages.welcome.Welcome;
import org.luke.mesa.data.property.Property;

public class Mesa extends App implements Styleable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPage(Welcome.class);
        applyStyle(getStyle());
    }

    @Override
    public void applyStyle(Style style) {
        super.applyStyle(style);
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}