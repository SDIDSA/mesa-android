package org.luke.mesa.abs.components.controls.text.font;

import android.graphics.Typeface;
import android.util.Log;

import org.luke.mesa.abs.App;

import java.util.HashMap;
import java.util.Objects;

public class Font {
    public static final String DEFAULT_FAMILY = "Ubuntu";
    public static final FontWeight DEFAULT_WEIGHT = FontWeight.NORMAL;
    public static final boolean DEFAULT_ITALIC = false;
    public static final float DEFAULT_SIZE = 14;

    public static final Font DEFAULT = new Font();
    private static final HashMap<String, Typeface> base = new HashMap<>();
    private static final HashMap<Font, Typeface> cache = new HashMap<>();

    private String family;
    private float size;
    private FontWeight weight;
    private boolean italic;

    public Font(String family, float size, FontWeight weight, boolean italic) {
        this.family = family;
        this.size = size;
        this.weight = weight;
        this.italic = italic;
    }

    public Font(String family, float size, FontWeight weight) {
        this(family, size, weight, DEFAULT_ITALIC);
    }

    public Font(String family, float size) {
        this(family, size, DEFAULT_WEIGHT, DEFAULT_ITALIC);
    }

    public Font(String family) {
        this(family, DEFAULT_SIZE, DEFAULT_WEIGHT, DEFAULT_ITALIC);
    }

    public Font(float size) {
        this(DEFAULT_FAMILY, size, DEFAULT_WEIGHT, DEFAULT_ITALIC);
    }

    public Font(FontWeight weight) {
        this(DEFAULT_FAMILY, DEFAULT_SIZE, weight, DEFAULT_ITALIC);
    }

    public Font(float size, FontWeight weight) {
        this(DEFAULT_FAMILY, size, weight, DEFAULT_ITALIC);
    }

    private Font() {
        this(DEFAULT_FAMILY, DEFAULT_SIZE, DEFAULT_WEIGHT, DEFAULT_ITALIC);
    }

    public Font(float size, boolean italic) {
        this(DEFAULT_FAMILY, size, DEFAULT_WEIGHT, italic);
    }

    public Font(float size, boolean italic, FontWeight weight) {
        this(DEFAULT_FAMILY, size, weight, italic);
    }

    public static void init(App owner) {
        loadFont(DEFAULT_FAMILY, owner);
    }

    private static void loadFont(String name, App owner) {
        for (FontVar var : FontVar.values()) {
            try {
                String path = "fonts/" + name + "-" + var.name.replace(" ", "") + ".ttf";

                Log.i("font path", path);

                base.put(name + " " + var.name, Typeface.createFromAsset(owner.getAssets(), path));
            } catch (Exception x) {
                Log.e("load font", "failed to load font ".concat(name), x);
            }
        }
    }

    public Font setFamily(String family) {
        this.family = family;
        return this;
    }

    public Font setWeight(FontWeight weight) {
        this.weight = weight;
        return this;
    }

    public Font setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(family, italic, size, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Font other = (Font) obj;
        return Objects.equals(family, other.family) && italic == other.italic
                && Float.floatToIntBits(size) == Float.floatToIntBits(other.size) && weight == other.weight;
    }

    public Typeface getFont() {
        Typeface found = cache.get(this);

        if (found == null) {
            found = Typeface.create(base.get(family + " " + FontVar.getVar(weight, italic)), Typeface.NORMAL);
            cache.put(this, found);
        }

        return found;
    }

    public Font copy() {
        return new Font(family, size, weight, italic);
    }

    public float getSize() {
        return size;
    }

    public Font setSize(float size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return "Font [family=" + family + ", size=" + size + ", weight=" + weight + ", italic=" + italic + "]";
    }

    private enum FontVar {
        LIGHT("Light", FontWeight.LIGHT, false),
        REGULAR("Regular", FontWeight.NORMAL, false),
        MEDIUM("Medium", FontWeight.MEDIUM, false),
        BOLD("Bold", FontWeight.BOLD, false),
        LIGHT_ITALIC("Light Italic", FontWeight.LIGHT, true),
        ITALIC("Italic", FontWeight.NORMAL, true),
        MEDIUM_ITALIC("Medium Italic", FontWeight.MEDIUM, true),
        BOLD_ITALIC("Bold Italic", FontWeight.BOLD, true);

        final String name;
        final FontWeight weight;
        final boolean italic;
        FontVar(String name, FontWeight weight, boolean italic) {
            this.name = name;
            this.weight = weight;
            this.italic = italic;
        }

        static String getVar(FontWeight weight, boolean italic) {
            for(FontVar var : values()) {
                if(weight == var.weight && italic == var.italic) return var.name;
            }

            return REGULAR.name;
        }
    }
}