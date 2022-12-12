package org.luke.mesa.data.observable;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.binding.DoubleBinding;

public interface DoubleObservable extends Observable<Double> {
    DoubleBinding add(DoubleObservable other);

    DoubleBinding subtract(DoubleObservable other);

    DoubleBinding multiply(DoubleObservable other);

    DoubleBinding divide(DoubleObservable other);

    BooleanBinding isEqualTo(DoubleObservable other);

    BooleanBinding isGreaterThan(DoubleObservable other);

    BooleanBinding isLesserThan(DoubleObservable other);
}
