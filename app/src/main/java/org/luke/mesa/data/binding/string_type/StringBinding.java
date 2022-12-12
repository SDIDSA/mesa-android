package org.luke.mesa.data.binding.string_type;

import org.luke.mesa.abs.utils.functional.ObjectSupplier;
import org.luke.mesa.data.binding.Binding;
import org.luke.mesa.data.binding.IntegerBinding;
import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.observable.Observable;
import org.luke.mesa.data.observable.StringObservable;

public class StringBinding extends Binding<String> implements StringObservable {
    public StringBinding(ObjectSupplier<String> calc, Observable<?>... dependencies) {
        super(calc, dependencies);
    }

    public BooleanBinding isEmpty() {
        return new BooleanBinding(() -> get().isEmpty(), this);
    }

    public StringBinding concat(StringObservable other) {
        return new StringBinding(() -> get().concat(other.get()), this, other);
    }
    public IntegerBinding lengthProperty() {
        return new IntegerBinding(() -> get().length(), this);
    }
}
