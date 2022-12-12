package org.luke.mesa.data.observable;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;

public interface Observable<T> {
    void set(T value);
    T get();
    void addListener(ChangeListener<? super T> listener);
    void removeListener(ChangeListener<? super T> listener);

    BooleanBinding isEqualTo(Observable<T> other);
    BooleanBinding isEqualTo(T other);
}
