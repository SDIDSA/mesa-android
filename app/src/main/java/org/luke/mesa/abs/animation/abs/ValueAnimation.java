package org.luke.mesa.abs.animation.abs;

public abstract class ValueAnimation extends Animation {
    private float from, to;

    public ValueAnimation(long duration, float from, float to) {
        super(duration);
        this.from = from;
        this.to = to;
    }

    public ValueAnimation(float from, float to) {
        super();
        this.from = from;
        this.to = to;
    }

    public void setFrom(float from) {
        this.from = from;
    }

    public void setTo(float to) {
        this.to = to;
    }

    public float getTo() {
        return to;
    }

    @Override
    public void update(float v) {
        updateValue(from + (to - from) * v);
    }

    public abstract void updateValue(float v);
}
