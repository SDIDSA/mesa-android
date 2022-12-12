package org.luke.mesa.abs.animation.easing;

public class EaseOutInterpolator implements Interpolator {
    @Override
    public float interpolate(float v) {
        return (float) (1 - Math.pow(1 - v, 4));
    }
}
