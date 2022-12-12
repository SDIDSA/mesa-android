package org.luke.mesa.data.observable;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.binding.IntegerBinding;
import org.luke.mesa.data.binding.string_type.StringBinding;

public interface StringObservable extends Observable<String> {
    StringBinding concat(StringObservable obs);
    BooleanBinding isEmpty();
    IntegerBinding lengthProperty();
}
