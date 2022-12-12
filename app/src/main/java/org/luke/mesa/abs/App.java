package org.luke.mesa.abs;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.luke.mesa.abs.components.Page;
import org.luke.mesa.abs.components.controls.Font;
import org.luke.mesa.abs.components.layout.overlay.Overlay;
import org.luke.mesa.abs.locale.Locale;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.data.property.Property;

import java.util.HashMap;

import io.socket.client.Socket;

public class App extends AppCompatActivity {
    private static final String MAIN_SOCKET = "main_socket";
    private final HashMap<String, Object> data = new HashMap<>();

    public Style dark, light;
    public Style dark_auto, light_auto;
    public Locale fr_fr, en_us;

    private FrameLayout root;
    private Page loaded;

    private Property<Style> style;
    private Property<Locale> locale;
    private Overlay loadedOverlay;

    private Insets insets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dark = new Style(this, "dark", true);
        light = new Style(this, "light", false);
        dark_auto = dark.clone();
        light_auto = light.clone();

        fr_fr = new Locale(this, "fr_FR");
        en_us = new Locale(this, "en_US");

        Font.init(this);

        style = new Property<>(isDarkMode(getResources().getConfiguration()) ? dark_auto : light_auto);
        locale = new Property<>(en_us);

        root = new FrameLayout(this);
        setContentView(root);

        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, ins) -> {
            this.insets = ins.getInsets(WindowInsetsCompat.Type.systemBars());

            if (loaded != null) {
                loaded.applyInsets(insets);
            }

            if (loadedOverlay != null) {
                loadedOverlay.applyInsets(insets);
            }

            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (style.get().isAuto())
            Platform.runLater(() -> setStyle(isDarkMode(newConfig) ? dark_auto : light_auto));
    }

    public void loadPage(Page page) {
        if (loaded != null) {
            root.removeView(loaded);
        }
        loaded = page;

        page.applyInsets(insets);

        root.addView(page, 0);
    }

    public void setBackgroundColor(int color) {
        root.setBackgroundColor(color);
    }

    public Page getLoaded() {
        return loaded;
    }

    public void addOverlay(Overlay overlay) {
        removeOverlay(overlay);
        root.addView(overlay);
        overlay.requestLayout();

        overlay.applyInsets(insets);

        loadedOverlay = overlay;
    }

    public Insets getInsets() {
        return insets;
    }

    public void applyInsets(View view, Insets insets) {
        if (insets != null && view != null) {
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        }
    }

    public void removeOverlay(Overlay overlay) {
        root.removeView(overlay);

        loadedOverlay = null;
    }

    public void log(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    public void showKeyboard(View input) {
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, 0);
    }

    public void hideKeyboard(View input) {
        input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (loadedOverlay != null) {
            loadedOverlay.hide();
        } else if (loaded == null || !loaded.onBack()) {
            super.onBackPressed();
        }
    }

    public int getScreenHeight() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);

        return size.y;
    }

    public int getScreenWidth() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);

        return size.x;
    }

    public void applyStyle(Style style) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        WindowInsetsControllerCompat controllerCompat = new WindowInsetsControllerCompat(window, root);
        controllerCompat.setAppearanceLightStatusBars(style.isLight());
        controllerCompat.setAppearanceLightNavigationBars(style.isLight());
    }

    public boolean isDarkMode(Configuration newConfig) {
        return (newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    public Property<Style> getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style.set(style);
    }

    public Property<Locale> getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    public void putData(String key, Object value) {
        data.put(key, value);
    }

    public <T> T getTypedData(String key, Class<T> type) {
        Object object = data.get(key);
        if(object != null && type.isInstance(object)) {
            return type.cast(object);
        }
        return null;
    }

    public String getString(String key) {
        return getTypedData(key, String.class);
    }

    public Socket getMainSocket() {
        return getTypedData(MAIN_SOCKET, Socket.class);
    }
}
