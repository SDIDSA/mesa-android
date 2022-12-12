package org.luke.mesa.abs.animation.abs;

import android.graphics.Color;

public abstract class ColorAnimation extends Animation {
    private int fromRed, fromGreen, fromBlue, fromAlpha;
    private int toRed, toGreen, toBlue, toAlpha;

    public ColorAnimation(long duration, int from, int to) {
        super(duration);
        setFrom(from);
        setTo(to);
    }

    public void setFrom(int from) {
        fromRed = Color.red(from);
        fromGreen = Color.green(from);
        fromBlue = Color.blue(from);
        fromAlpha = Color.alpha(from);
    }

    public void setTo(int to) {
        toRed = Color.red(to);
        toGreen = Color.green(to);
        toBlue = Color.blue(to);
        toAlpha = Color.alpha(to);
    }

    @Override
    public void update(float v) {
        int red = (int) (fromRed + (toRed - fromRed) * v);
        int green = (int) (fromGreen + (toGreen - fromGreen) * v);
        int blue = (int) (fromBlue + (toBlue - fromBlue) * v);
        int alpha = (int) (fromAlpha + (toAlpha - fromAlpha) * v);
        updateValue(Color.argb(alpha,red,green,blue));
    }

    public abstract void updateValue(int color);
}
