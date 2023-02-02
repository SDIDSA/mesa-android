package org.luke.mesa.abs.style;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.ColorInt;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.Assets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class Style {
    private int accent;
    private final HashMap<String, Integer> colors;
    private final boolean dark;
    private boolean auto;

    private Style(HashMap<String, Integer> colors, @ColorInt int accent, boolean dark) {
        this.colors = new HashMap<>(colors);
        this.accent = accent;
        this.dark = dark;
        auto = true;
    }

    public Style(App owner, String styleName, boolean dark) {
        this.dark = dark;
        colors = new HashMap<>();
        try {
            JSONObject data = new JSONObject(Objects.requireNonNull(Assets.readAsset(owner, "themes/" + styleName + ".json")));
            Iterator<String> keys = data.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                colors.put(key, parseColor(data.getString(key)));
            }

            //Log.i("style", data.toString(4));
        } catch (JSONException x) {
            Log.e("init style", "failed to load style ".concat(styleName), x);
        }

        accent = parseColor("#5865F2FF");
    }

    @ColorInt
    private static int parseColor(String rgba) {
        char[] chars = rgba.toCharArray();

        String argb = "#";

        argb += chars[7];
        argb += chars[8];

        argb += chars[1];
        argb += chars[2];
        argb += chars[3];
        argb += chars[4];
        argb += chars[5];
        argb += chars[6];

        return Color.parseColor(argb);
    }

    public boolean isAuto() {
        return auto;
    }

    public boolean isDark() {
        return dark;
    }

    public boolean isLight() {
        return !dark;
    }

    @ColorInt
    public int getAccent() {
        return accent;
    }

    public void setAccent(@ColorInt int accent) {
        this.accent = accent;
    }

    public Style clone() {
        return new Style(colors, accent, dark);
    }

    @ColorInt
    public int getHeaderPrimary() {
        return colors.get("headerPrimary");
    }

    @ColorInt
    public int getHeaderSecondary() {
        return colors.get("headerSecondary");
    }

    @ColorInt
    public int getTextNormal() {
        return colors.get("textNormal");
    }

    @ColorInt
    public int getTextMuted() {
        return colors.get("textMuted");
    }

    @ColorInt
    public int getTextLink() {
        return colors.get("textLink");
    }

    @ColorInt
    public int getTextLinkLowSaturation() {
        return colors.get("textLinkLowSaturation");
    }

    @ColorInt
    public int getTextPositive() {
        return colors.get("textPositive");
    }

    @ColorInt
    public int getTextWarning() {
        return colors.get("textWarning");
    }

    @ColorInt
    public int getTextDanger() {
        return colors.get("textDanger");
    }

    @ColorInt
    public int getTextBrand() {
        return colors.get("textBrand");
    }

    @ColorInt
    public int getInteractiveNormal() {
        return colors.get("interactiveNormal");
    }

    @ColorInt
    public int getInteractiveHover() {
        return colors.get("interactiveHover");
    }

    @ColorInt
    public int getInteractiveActive() {
        return colors.get("interactiveActive");
    }

    @ColorInt
    public int getInteractiveMuted() {
        return colors.get("interactiveMuted");
    }

    @ColorInt
    public int getBackgroundPrimary() {
        return colors.get("backgroundPrimary");
    }

    @ColorInt
    public int getBackgroundSecondary() {
        return colors.get("backgroundSecondary");
    }

    @ColorInt
    public int getBackgroundSecondaryAlt() {
        return colors.get("backgroundSecondaryAlt");
    }

    @ColorInt
    public int getBackgroundTertiary() {
        return colors.get("backgroundTertiary");
    }

    @ColorInt
    public int getBackgroundAccent() {
        return colors.get("backgroundAccent");
    }

    @ColorInt
    public int getBackgroundFloating() {
        return colors.get("backgroundFloating");
    }

    @ColorInt
    public int getBackgroundNestedFloating() {
        return colors.get("backgroundNestedFloating");
    }

    @ColorInt
    public int getBackgroundMobilePrimary() {
        return colors.get("backgroundMobilePrimary");
    }

    @ColorInt
    public int getBackgroundMobileSecondary() {
        return colors.get("backgroundMobileSecondary");
    }

    @ColorInt
    public int getBackgroundModifierHover() {
        return colors.get("backgroundModifierHover");
    }

    @ColorInt
    public int getBackgroundModifierActive() {
        return colors.get("backgroundModifierActive");
    }

    @ColorInt
    public int getBackgroundModifierSelected() {
        return colors.get("backgroundModifierSelected");
    }

    @ColorInt
    public int getBackgroundModifierAccent() {
        return colors.get("backgroundModifierAccent");
    }

    @ColorInt
    public int getInfoPositiveText() {
        return colors.get("infoPositiveText");
    }

    @ColorInt
    public int getInfoWarningText() {
        return colors.get("infoWarningText");
    }

    @ColorInt
    public int getInfoDangerText() {
        return colors.get("infoDangerText");
    }

    @ColorInt
    public int getInfoHelpBackground() {
        return colors.get("infoHelpBackground");
    }

    @ColorInt
    public int getInfoHelpForeground() {
        return colors.get("infoHelpForeground");
    }

    @ColorInt
    public int getInfoHelpText() {
        return colors.get("infoHelpText");
    }

    @ColorInt
    public int getStatusWarningText() {
        return colors.get("statusWarningText");
    }

    @ColorInt
    public int getScrollbarThinThumb() {
        return colors.get("scrollbarThinThumb");
    }

    @ColorInt
    public int getScrollbarThinTrack() {
        return colors.get("scrollbarThinTrack");
    }

    @ColorInt
    public int getScrollbarAutoThumb() {
        return colors.get("scrollbarAutoThumb");
    }

    @ColorInt
    public int getScrollbarAutoTrack() {
        return colors.get("scrollbarAutoTrack");
    }

    @ColorInt
    public int getScrollbarAutoScrollbarColorThumb() {
        return colors.get("scrollbarAutoScrollbarColorThumb");
    }

    @ColorInt
    public int getScrollbarAutoScrollbarColorTrack() {
        return colors.get("scrollbarAutoScrollbarColorTrack");
    }

    @ColorInt
    public int getLogoPrimary() {
        return colors.get("logoPrimary");
    }

    @ColorInt
    public int getControlBrandForeground() {
        return colors.get("controlBrandForeground");
    }

    @ColorInt
    public int getControlBrandForegroundNew() {
        return colors.get("controlBrandForegroundNew");
    }

    @ColorInt
    public int getBackgroundMentioned() {
        return colors.get("backgroundMentioned");
    }

    @ColorInt
    public int getBackgroundMentionedHover() {
        return colors.get("backgroundMentionedHover");
    }

    @ColorInt
    public int getBackgroundMessageHover() {
        return colors.get("backgroundMessageHover");
    }

    @ColorInt
    public int getChannelsDefault() {
        return colors.get("channelsDefault");
    }

    @ColorInt
    public int getChanneltextareaBackground() {
        return colors.get("channeltextareaBackground");
    }

    @ColorInt
    public int getActivityCardBackground() {
        return colors.get("activityCardBackground");
    }

    @ColorInt
    public int getTextboxMarkdownSyntax() {
        return colors.get("textboxMarkdownSyntax");
    }

    @ColorInt
    public int getDeprecatedCardBg() {
        return colors.get("deprecatedCardBg");
    }

    @ColorInt
    public int getDeprecatedCardEditableBg() {
        return colors.get("deprecatedCardEditableBg");
    }

    @ColorInt
    public int getDeprecatedStoreBg() {
        return colors.get("deprecatedStoreBg");
    }

    @ColorInt
    public int getDeprecatedQuickswitcherInputBackground() {
        return colors.get("deprecatedQuickswitcherInputBackground");
    }

    @ColorInt
    public int getDeprecatedQuickswitcherInputPlaceholder() {
        return colors.get("deprecatedQuickswitcherInputPlaceholder");
    }

    @ColorInt
    public int getDeprecatedTextInputBg() {
        return colors.get("deprecatedTextInputBg");
    }

    @ColorInt
    public int getDeprecatedTextInputBorder() {
        return colors.get("deprecatedTextInputBorder");
    }

    @ColorInt
    public int getDeprecatedTextInputBorderHover() {
        return colors.get("deprecatedTextInputBorderHover");
    }

    @ColorInt
    public int getDeprecatedTextInputBorderDisabled() {
        return colors.get("deprecatedTextInputBorderDisabled");
    }

    @ColorInt
    public int getDeprecatedTextInputPrefix() {
        return colors.get("deprecatedTextInputPrefix");
    }

    @ColorInt
    public int getCloseIconActive() {
        return colors.get("closeIconActive");
    }

    @ColorInt
    public int getCountryCodeItemHover() {
        return colors.get("countryCodeItemHover");
    }

    @ColorInt
    public int getSecondaryButtonBack() {
        return colors.get("secondaryButtonBack");
    }

    @ColorInt
    public int getLinkButtonText() {
        return colors.get("linkButtonText");
    }

    @ColorInt
    public int getCountryCodeItemText() {
        return colors.get("countryCodeItemText");
    }

    @ColorInt
    public int getCountryNameItemText() {
        return colors.get("countryNameItemText");
    }

    @ColorInt
    public int getSessionWindowBorder() {
        return colors.get("sessionWindowBorder");
    }

    @ColorInt
    public int getBackgroundLayer() {
        return colors.get("backgroundLayer");
    }
}
