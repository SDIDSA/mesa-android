package org.luke.mesa.abs.style;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.Assets;

import java.util.HashMap;
import java.util.Iterator;

public class Style {
    private int accent;
    private final HashMap<String, Integer> colors;
    private final boolean dark;
    private boolean auto;

    private Style(HashMap<String, Integer> colors, int accent, boolean dark) {
        this.colors = (HashMap<String, Integer>) colors.clone();
        this.accent = accent;
        this.dark = dark;
        auto = true;
    }

    public Style(App owner, String styleName, boolean dark) {
        this.dark = dark;
        colors = new HashMap<>();
        try {
            JSONObject data = new JSONObject(Assets.readAsset(owner, "themes/" + styleName + ".json"));
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
        //accent = parseColor("#887799FF");
    }

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

    public int getAccent() {
        return accent;
    }

    public void setAccent(int accent) {
        this.accent = accent;
    }

    public Style clone() {
        return new Style(colors, accent, dark);
    }

    public int getHeaderPrimary() {
        return colors.get("headerPrimary");
    }

    public int getHeaderSecondary() {
        return colors.get("headerSecondary");
    }

    public int getTextNormal() {
        return colors.get("textNormal");
    }

    public int getTextMuted() {
        return colors.get("textMuted");
    }

    public int getTextLink() {
        return colors.get("textLink");
    }

    public int getTextLinkLowSaturation() {
        return colors.get("textLinkLowSaturation");
    }

    public int getTextPositive() {
        return colors.get("textPositive");
    }

    public int getTextWarning() {
        return colors.get("textWarning");
    }

    public int getTextDanger() {
        return colors.get("textDanger");
    }

    public int getTextBrand() {
        return colors.get("textBrand");
    }

    public int getInteractiveNormal() {
        return colors.get("interactiveNormal");
    }

    public int getInteractiveHover() {
        return colors.get("interactiveHover");
    }

    public int getInteractiveActive() {
        return colors.get("interactiveActive");
    }

    public int getInteractiveMuted() {
        return colors.get("interactiveMuted");
    }

    public int getBackgroundPrimary() {
        return colors.get("backgroundPrimary");
    }

    public int getBackgroundSecondary() {
        return colors.get("backgroundSecondary");
    }

    public int getBackgroundSecondaryAlt() {
        return colors.get("backgroundSecondaryAlt");
    }

    public int getBackgroundTertiary() {
        return colors.get("backgroundTertiary");
    }

    public int getBackgroundAccent() {
        return colors.get("backgroundAccent");
    }

    public int getBackgroundFloating() {
        return colors.get("backgroundFloating");
    }

    public int getBackgroundNestedFloating() {
        return colors.get("backgroundNestedFloating");
    }

    public int getBackgroundMobilePrimary() {
        return colors.get("backgroundMobilePrimary");
    }

    public int getBackgroundMobileSecondary() {
        return colors.get("backgroundMobileSecondary");
    }

    public int getBackgroundModifierHover() {
        return colors.get("backgroundModifierHover");
    }

    public int getBackgroundModifierActive() {
        return colors.get("backgroundModifierActive");
    }

    public int getBackgroundModifierSelected() {
        return colors.get("backgroundModifierSelected");
    }

    public int getBackgroundModifierAccent() {
        return colors.get("backgroundModifierAccent");
    }

    public int getInfoPositiveText() {
        return colors.get("infoPositiveText");
    }

    public int getInfoWarningText() {
        return colors.get("infoWarningText");
    }

    public int getInfoDangerText() {
        return colors.get("infoDangerText");
    }

    public int getInfoHelpBackground() {
        return colors.get("infoHelpBackground");
    }

    public int getInfoHelpForeground() {
        return colors.get("infoHelpForeground");
    }

    public int getInfoHelpText() {
        return colors.get("infoHelpText");
    }

    public int getStatusWarningText() {
        return colors.get("statusWarningText");
    }

    public int getScrollbarThinThumb() {
        return colors.get("scrollbarThinThumb");
    }

    public int getScrollbarThinTrack() {
        return colors.get("scrollbarThinTrack");
    }

    public int getScrollbarAutoThumb() {
        return colors.get("scrollbarAutoThumb");
    }

    public int getScrollbarAutoTrack() {
        return colors.get("scrollbarAutoTrack");
    }

    public int getScrollbarAutoScrollbarColorThumb() {
        return colors.get("scrollbarAutoScrollbarColorThumb");
    }

    public int getScrollbarAutoScrollbarColorTrack() {
        return colors.get("scrollbarAutoScrollbarColorTrack");
    }

    public int getLogoPrimary() {
        return colors.get("logoPrimary");
    }

    public int getControlBrandForeground() {
        return colors.get("controlBrandForeground");
    }

    public int getControlBrandForegroundNew() {
        return colors.get("controlBrandForegroundNew");
    }

    public int getBackgroundMentioned() {
        return colors.get("backgroundMentioned");
    }

    public int getBackgroundMentionedHover() {
        return colors.get("backgroundMentionedHover");
    }

    public int getBackgroundMessageHover() {
        return colors.get("backgroundMessageHover");
    }

    public int getChannelsDefault() {
        return colors.get("channelsDefault");
    }

    public int getChanneltextareaBackground() {
        return colors.get("channeltextareaBackground");
    }

    public int getActivityCardBackground() {
        return colors.get("activityCardBackground");
    }

    public int getTextboxMarkdownSyntax() {
        return colors.get("textboxMarkdownSyntax");
    }

    public int getDeprecatedCardBg() {
        return colors.get("deprecatedCardBg");
    }

    public int getDeprecatedCardEditableBg() {
        return colors.get("deprecatedCardEditableBg");
    }

    public int getDeprecatedStoreBg() {
        return colors.get("deprecatedStoreBg");
    }

    public int getDeprecatedQuickswitcherInputBackground() {
        return colors.get("deprecatedQuickswitcherInputBackground");
    }

    public int getDeprecatedQuickswitcherInputPlaceholder() {
        return colors.get("deprecatedQuickswitcherInputPlaceholder");
    }

    public int getDeprecatedTextInputBg() {
        return colors.get("deprecatedTextInputBg");
    }

    public int getDeprecatedTextInputBorder() {
        return colors.get("deprecatedTextInputBorder");
    }

    public int getDeprecatedTextInputBorderHover() {
        return colors.get("deprecatedTextInputBorderHover");
    }

    public int getDeprecatedTextInputBorderDisabled() {
        return colors.get("deprecatedTextInputBorderDisabled");
    }

    public int getDeprecatedTextInputPrefix() {
        return colors.get("deprecatedTextInputPrefix");
    }

    public int getCloseIconActive() {
        return colors.get("closeIconActive");
    }

    public int getCountryCodeItemHover() {
        return colors.get("countryCodeItemHover");
    }

    public int getSecondaryButtonBack() {
        return colors.get("secondaryButtonBack");
    }

    public int getLinkButtonText() {
        return colors.get("linkButtonText");
    }

    public int getCountryCodeItemText() {
        return colors.get("countryCodeItemText");
    }

    public int getCountryNameItemText() {
        return colors.get("countryNameItemText");
    }

    public int getSessionWindowBorder() {
        return colors.get("sessionWindowBorder");
    }

    public int getBackgroundLayer() {
        return colors.get("backgroundLayer");
    }
}
