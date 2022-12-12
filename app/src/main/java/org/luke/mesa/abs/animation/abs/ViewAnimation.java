package org.luke.mesa.abs.animation.abs;

import android.view.View;

import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.functional.ObjectSupplier;

public abstract class ViewAnimation extends ValueAnimation {
    private final View view;

    private ObjectSupplier<Float> lateTo;
    public ViewAnimation(long duration, View view,float to) {
        super(duration, 0, to);
        this.view = view;
    }

    public ViewAnimation(View view,float to) {
        super(0, to);
        this.view = view;
    }

    public Animation setLateTo(ObjectSupplier<Float> lateTo) {
        this.lateTo = lateTo;
        return this;
    }

    public void init() {
        setFrom(getFrom(view));
        if(lateTo != null) {
            setTo(lateTo.get());
        }
        super.init();
    }

    public View getView() {
        return view;
    }

    @Override
    public void updateValue(float v) {
        if(view.isAttachedToWindow()) {
            apply(view, v);
        } else {
            apply(view, getTo());
            stop();
            if (getOnFinished() != null) {
                Platform.runLater(getOnFinished());
            }
        }
    }

    protected abstract float getFrom(View view);

    protected abstract void apply(View view, float v);
}
