package org.luke.mesa.data.property;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.observable.BooleanObservable;

public class BooleanProperty extends Property<Boolean> implements BooleanObservable {
    public BooleanProperty(boolean b) {
        super(b);
    }

    public BooleanProperty() {
        super();
    }

    public BooleanBinding or(BooleanObservable other) {
        return new BooleanBinding(() -> get() || other.get(), this, other);
    }

    public BooleanBinding and(BooleanObservable other) {
        return new BooleanBinding(() -> get() && other.get(), this, other);
    }

    public BooleanBinding not() {
        return new BooleanBinding(() -> !get(), this);
    }

    public BooleanBinding or(boolean other) {
        return new BooleanBinding(() -> get() || other, this);
    }

    public BooleanBinding and(boolean other) {
        return new BooleanBinding(() -> get() && other, this);
    }
}
