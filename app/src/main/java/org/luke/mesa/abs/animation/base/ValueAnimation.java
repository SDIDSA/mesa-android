package org.luke.mesa.abs.animation.base;

import org.luke.mesa.abs.utils.functional.ObjectSupplier;

public abstract class ValueAnimation extends Animation {
    private float from, to;

    protected ObjectSupplier<Float> lateTo;
    protected ObjectSupplier<Float> lateFrom;

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

    public ValueAnimation() {
        super();
    }

    public ValueAnimation setLateTo(ObjectSupplier<Float> lateTo) {
        this.lateTo = lateTo;
        return this;
    }

    public ValueAnimation setLateFrom(ObjectSupplier<Float> lateFrom) {
        this.lateFrom = lateFrom;
        return this;
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

    public float getFrom() {
        return from;
    }

    @Override
    public void init() {
        if(lateFrom != null) {
            setFrom(lateFrom.get());
        }
        if (lateTo != null) {
            setTo(lateTo.get());
        }
        super.init();
    }

    @Override
    public void update(float v) {
        updateValue(from + (to - from) * v);
    }

    public abstract void updateValue(float v);
}
