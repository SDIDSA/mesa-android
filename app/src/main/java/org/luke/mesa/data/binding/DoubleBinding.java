package org.luke.mesa.data.binding;

import org.luke.mesa.abs.utils.functional.ObjectSupplier;
import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.observable.DoubleObservable;
import org.luke.mesa.data.observable.Observable;

public class DoubleBinding extends Binding<Double> implements DoubleObservable {
    public DoubleBinding(ObjectSupplier<Double> calc, Observable<?>... dependencies) {
        super(calc, dependencies);
    }

    public DoubleBinding add(DoubleObservable other) {
        return new DoubleBinding(() -> get() + other.get(), this, other);
    }

    public DoubleBinding subtract(DoubleObservable other) {
        return new DoubleBinding(() -> get() - other.get(), this, other);
    }

    public DoubleBinding multiply(DoubleObservable other) {
        return new DoubleBinding(() -> get() * other.get(), this, other);
    }

    public DoubleBinding divide(DoubleObservable other) {
        return new DoubleBinding(() -> get() / other.get(), this, other);
    }

    public BooleanBinding isEqualTo(DoubleObservable other) {
        return new BooleanBinding(() -> get() == other.get(), this, other);
    }

    public BooleanBinding isGreaterThan(DoubleObservable other) {
        return new BooleanBinding(() -> get() > other.get(), this, other);
    }

    public BooleanBinding isLesserThan(DoubleObservable other) {
        return new BooleanBinding(() -> get() < other.get(), this, other);
    }
}
