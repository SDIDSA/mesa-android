package org.luke.mesa.abs.animation.easing;

public class EaseInOutInterpolator implements Interpolator {
    @Override
    public float interpolate(float v) {
        return v * v * (3.0f - 2.0f * v);
    }
}
