package org.luke.mesa.abs.animation.easing;

public interface Interpolator {
    Interpolator LINEAR = new LinearInterpolator();
    Interpolator EASE_IN = new EaseInInterpolator();
    Interpolator EASE_OUT = new EaseOutInterpolator();
    Interpolator EASE_BOTH = new EaseInOutInterpolator();

    Interpolator ANTICIPATEOVERSHOOT = new SplineInterpolator(0.68f, -0.6f, 0.32f, 1.6f);
    Interpolator ANTICIPATE = new SplineInterpolator(0.36f, 0f, 0.66f, -0.56f);
    Interpolator OVERSHOOT = new SplineInterpolator(0.34f, 1.56f, 0.64f, 1);

    float interpolate(float v);
}
