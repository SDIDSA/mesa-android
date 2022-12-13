package org.luke.mesa.abs.animation.easing;

public class EaseIn implements Interpolator {
    @Override
    public float interpolate(float v) {
        return v * v * v * v;
    }
}
