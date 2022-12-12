package org.luke.mesa.data.binding.boolean_type;


import org.luke.mesa.abs.utils.functional.ObjectSupplier;
import org.luke.mesa.data.binding.Binding;
import org.luke.mesa.data.observable.BooleanObservable;
import org.luke.mesa.data.observable.Observable;

public class BooleanBinding extends Binding<Boolean> implements BooleanObservable {

    public BooleanBinding(ObjectSupplier<Boolean> calc, Observable<?>... dependencies) {
        super(calc, dependencies);
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
