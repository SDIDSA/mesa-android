package org.luke.mesa.data.property;

import org.luke.mesa.data.binding.boolean_type.BooleanBinding;
import org.luke.mesa.data.observable.ChangeListener;
import org.luke.mesa.data.observable.Observable;

import java.util.ArrayList;
import java.util.Objects;

public class Property<T> implements Observable<T> {
    private T value;

    private Observable<T> boundTo;
    private boolean bound;

    private ChangeListener<T> onBoundChanged;

    private final ArrayList<ChangeListener<? super T>> listeners = new ArrayList<>();

    public Property() {
    }

    public Property(T value) {
        this.value = value;
    }

    public boolean isBound() {
        return bound;
    }

    public void bind(Observable<T> bindTo) {
        if(bound) {
            throw new IllegalStateException("you can't bind this property because it's already bound");
        }
        boundTo = bindTo;

        onBoundChanged = (obs, ov, nv) -> set(nv);

        boundTo.addListener(onBoundChanged);
        bound = true;
    }

    public void unbind() {
        if(bound) {
            boundTo.removeListener(onBoundChanged);
            boundTo = null;
            bound = false;
        }
    }

    @Override
    public void set(T value) {
        T ov = this.value;
        this.value = value;
        if(!Objects.equals(ov, value)) {
            for (ChangeListener<? super T> listener : listeners) {
                listener.changed(this, ov, value);
            }
        }
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void addListener(ChangeListener<? super T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        listeners.remove(listener);
    }

   public BooleanBinding isEqualTo(Observable<T> other) {
        return new BooleanBinding(() -> Objects.equals(get(), other.get()), this, other);
    }

    public BooleanBinding isEqualTo(T other) {
        return new BooleanBinding(() -> Objects.equals(get(), other), this);
    }
}
