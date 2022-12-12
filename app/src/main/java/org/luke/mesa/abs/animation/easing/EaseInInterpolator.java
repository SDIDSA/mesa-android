package org.luke.mesa.abs.animation.easing;

public class EaseInInterpolator implements Interpolator {
    @Override
    public float interpolate(float v) {
        return v * v * v * v;
    }
}
