package org.luke.mesa.abs;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.base.ColorAnimation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.animation.view.scale.ScaleXYAnimation;
import org.luke.mesa.abs.components.Page;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.image.ImageProxy;
import org.luke.mesa.abs.components.layout.overlay.Overlay;
import org.luke.mesa.abs.locale.Locale;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.Threaded;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.app.pages.SplashScreen;
import org.luke.mesa.data.property.Property;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.socket.client.Socket;

public class App extends AppCompatActivity {
    private static final String MAIN_SOCKET = "main_socket";
    private static final String SERVERS = "servarr";
    private final HashMap<String, Object> data = new HashMap<>();

    public Style dark, light;
    public Style dark_auto, light_auto;
    public Locale fr_fr, en_us;
    Animation running = null;
    private FrameLayout root;
    private Page loaded;
    private Property<Style> style;
    private Property<Locale> locale;
    private Overlay loadedOverlay;
    private Insets systemInsets;
    private Insets inputInsets;
    private SplashScreen splash;

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    protected void postInit() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageProxy.init(this);

        root = new FrameLayout(this);
        splash = new SplashScreen(this);
        root.addView(splash);
        setContentView(root);

        WindowCompat.setDecorFitsSystemWindows(this.getWindow(), false);

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, ins) -> {
            this.systemInsets = ins.getInsets(WindowInsetsCompat.Type.systemBars());

            boolean inputShown = ins.isVisible(WindowInsetsCompat.Type.ime());
            if (inputShown) {
                inputInsets = ins.getInsets(WindowInsetsCompat.Type.ime());
                if (loadedOverlay != null)
                    loadedOverlay.applyInputInsets(true, inputInsets);
            } else {
                if (loadedOverlay != null)
                    loadedOverlay.applyInputInsets(false, null);
            }

            if (loaded != null) {
                loaded.applyInsets(systemInsets);
            }

            if (loadedOverlay != null) {
                loadedOverlay.applySystemInsets(systemInsets);
            }

            return WindowInsetsCompat.CONSUMED;
        });

        new Thread(() -> {
            dark = new Style(this, "dark", true);
            light = new Style(this, "light", false);
            dark_auto = dark.clone();
            light_auto = light.clone();

            fr_fr = new Locale(this, "fr_FR");
            en_us = new Locale(this, "en_US");

            Font.init(this);

            style = new Property<>(isDarkMode(getResources().getConfiguration()) ? dark_auto : light_auto);
            locale = new Property<>(en_us);

            Platform.runLater(() -> style.addListener((obs, ov, nv) -> applyStyle()));

            postInit();
        }).start();

    }

    public void loadPage(Class<? extends Page> pageType) {
        hideKeyboard();
        Thread init = new Thread(() -> {
            if (running != null && running.isRunning())
                return;

            if (loaded != null && pageType.isInstance(loaded))
                return;

            AtomicReference<Page> page = new AtomicReference<>();
            View old = loaded;
            if (old != null)
                running = new ParallelAnimation(500)
                        .addAnimation(new AlphaAnimation(old, 0))
                        .addAnimation(new TranslateYAnimation(old, ViewUtils.dipToPx(-30, this)))
                        .addAnimation(new ScaleXYAnimation(old, .8f))
                        .setInterpolator(Interpolator.EASE_OUT).setOnFinished(() -> root.removeView(old)).start();

            new Thread(() -> {
                Threaded.sleep(250);
                while (page.get() == null) {
                    Threaded.sleep(10);
                }
                Platform.runLater(() -> {
                    page.get().setAlpha(1);
                    root.addView(page.get(), 0);
                    page.get().applyStyle(style.get());
                });
            }).start();

            page.set(Page.getInstance(this, pageType));

            if (page.get() == null) return;

            Platform.runLater(() -> {
                loaded = page.get();
                loaded.applyInsets(this.getSystemInsets());
                loaded.setAlpha(0);
                loaded.setScaleX(1);
                loaded.setScaleY(1);
                loaded.setTranslationY(0);
            });
        });

        if (splash.getParent() == root) {
            new ParallelAnimation(300)
                    .addAnimation(new AlphaAnimation(splash, 0))
                    .setInterpolator(Interpolator.EASE_OUT).setOnFinished(() -> {
                        root.removeView(splash);
                        init.start();
                    }).start();
        } else {
            init.start();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Page> T getLoaded() {
        return (T) loaded;
    }

    public void addOverlay(Overlay overlay) {
        removeOverlay(overlay);
        root.addView(overlay);
        overlay.requestLayout();

        overlay.applySystemInsets(systemInsets);

        loadedOverlay = overlay;
    }

    public Insets getSystemInsets() {
        return systemInsets;
    }

    public Insets getInputInsets() {
        return inputInsets;
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

    public void hideKeyboard() {
        View currentFocus;
        if ((currentFocus = getWindow().getCurrentFocus()) != null) currentFocus.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (loadedOverlay != null) {
            loadedOverlay.hide();
        } else if (loaded == null || !loaded.onBack()) {
            moveTaskToBack(false);
            //super.onBackPressed();
        }
    }

    public int getScreenHeight() {
        return root.getHeight();
    }

    public int getScreenWidth() {
        return root.getWidth();
    }

    public Property<Style> getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style.set(style);
    }

    public void setBackgroundColor(int color) {
        int trans = adjustAlpha(color, 0.005f);
        Window win = getWindow();
        ColorAnimation anim = new ColorAnimation(300, getWindow().getStatusBarColor(), color) {
            @Override
            public void updateValue(int color) {
                root.setBackgroundColor(color);
                win.setStatusBarColor(trans);
                win.setNavigationBarColor(trans);
            }
        };
        anim.start();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (style.get().isAuto())
            Platform.runLater(() -> setStyle(isDarkMode(newConfig) ? dark_auto : light_auto));
    }

    public void applyStyle() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        WindowInsetsControllerCompat controllerCompat = new WindowInsetsControllerCompat(window, root);
        controllerCompat.setAppearanceLightStatusBars(style.get().isLight());
        controllerCompat.setAppearanceLightNavigationBars(style.get().isLight());
    }

    public boolean isDarkMode(Configuration newConfig) {
        return (newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
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
        if (object != null && type.isInstance(object)) {
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

    public void putMainSocket(Socket socket) {
        putData(MAIN_SOCKET, socket);
    }

    public void putServers(JSONArray servarr) {
        data.put(SERVERS, servarr);
    }

    public JSONObject getJsonObject(String key) {
        return getTypedData(key, JSONObject.class);
    }

    public JSONArray getJsonArray(String key) {
        return getTypedData(key, JSONArray.class);
    }

    public JSONArray getServers() {
        return getJsonArray(SERVERS);
    }
}
