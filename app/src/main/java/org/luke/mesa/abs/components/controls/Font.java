package org.luke.mesa.abs.components.controls;

import android.graphics.Typeface;
import android.util.Log;

import org.luke.mesa.abs.App;

import java.util.HashMap;
import java.util.Objects;

public class Font {
    public static final int WEIGHT_NORMAL = 500, WEIGHT_BOLD = 700;

    public static final String DEFAULT_FAMILY = "Ubuntu";// Ubuntu
    public static final int DEFAULT_WEIGHT = WEIGHT_NORMAL;
    public static final boolean DEFAULT_POSTURE = false;
    public static final float DEFAULT_SIZE = 14;

    public static final Font DEFAULT = new Font();
    private static final HashMap<String, Typeface> base = new HashMap<>();
    private static final HashMap<Font, Typeface> cache = new HashMap<>();

    private String family;
    private float size;
    private int weight;
    private boolean posture;

    public Font(String family, float size, int weight, boolean posture) {
        this.family = family;
        this.size = size;
        this.weight = weight;
        this.posture = posture;
    }

    public Font(String family, float size, int weight) {
        this(family, size, weight, DEFAULT_POSTURE);
    }

    public Font(String family, float size) {
        this(family, size, DEFAULT_WEIGHT, DEFAULT_POSTURE);
    }

    public Font(String family) {
        this(family, DEFAULT_SIZE, DEFAULT_WEIGHT, DEFAULT_POSTURE);
    }

    public Font(float size) {
        this(DEFAULT_FAMILY, size, DEFAULT_WEIGHT, DEFAULT_POSTURE);
    }

    public Font(int weight) {
        this(DEFAULT_FAMILY, DEFAULT_SIZE, weight, DEFAULT_POSTURE);
    }

    public Font(float size, int weight) {
        this(DEFAULT_FAMILY, size, weight, DEFAULT_POSTURE);
    }

    private Font() {
        this(DEFAULT_FAMILY, DEFAULT_SIZE, DEFAULT_WEIGHT, DEFAULT_POSTURE);
    }

    public Font(float size, boolean posture) {
        this(DEFAULT_FAMILY, size, DEFAULT_WEIGHT, posture);
    }

    public Font(float size, boolean posture, int weight) {
        this(DEFAULT_FAMILY, size, weight, posture);
    }

    public static void init(App owner) {
        loadFont(DEFAULT_FAMILY, owner);
    }

    private static void loadFont(String name, App owner) {
        try {
            String path = "fonts/" + name + ".ttf";

            Log.i("font path", path);

            base.put(name, Typeface.createFromAsset(owner.getAssets(), path));
        } catch (Exception x) {
            Log.e("load font", "failed to load font ".concat(name), x);
        }
    }

    public Font setFamily(String family) {
        this.family = family;
        return this;
    }

    public Font setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public Font setPosture(boolean posture) {
        this.posture = posture;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(family, posture, size, weight);
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
        return Objects.equals(family, other.family) && posture == other.posture
                && Float.floatToIntBits(size) == Float.floatToIntBits(other.size) && weight == other.weight;
    }

    public Typeface getFont() {
        Typeface found = cache.get(this);

        if (found == null) {
            boolean bold = weight == WEIGHT_BOLD;
            boolean italic = posture;
            int style = bold && italic ? Typeface.BOLD_ITALIC :
                    (bold ? Typeface.BOLD :
                            (italic ? Typeface.ITALIC : Typeface.NORMAL));
            found = Typeface.create(base.get(family), style);
            cache.put(this, found);
        }

        return found;
    }

    public Font copy() {
        return new Font(family, size, weight, posture);
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
        return "Font [family=" + family + ", size=" + size + ", weight=" + weight + ", posture=" + posture + "]";
    }
}