package org.luke.mesa.data.property;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.binding.IntegerBinding;
import org.luke.mesa.data.binding.string_type.StringBinding;
import org.luke.mesa.data.observable.StringObservable;

public class StringProperty extends Property<String> implements StringObservable {
    public StringProperty() {
        super("");
    }
    public StringProperty(String b) {
        super(b);
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
