package org.luke.mesa.data.observable;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;

public interface BooleanObservable extends Observable<Boolean> {
    BooleanBinding or(BooleanObservable other);
    BooleanBinding and(BooleanObservable other);
    BooleanBinding not();
    BooleanBinding or(boolean other);
    BooleanBinding and(boolean other);
}
