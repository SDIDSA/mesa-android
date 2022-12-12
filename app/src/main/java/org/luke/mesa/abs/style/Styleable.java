package org.luke.mesa.abs.style;

import org.luke.mesa.data.observable.ChangeListener;
import org.luke.mesa.data.observable.Observable;
import org.luke.mesa.data.property.Property;

import java.lang.ref.WeakReference;

public interface Styleable {
    void applyStyle(Style style);
    void applyStyle(Property<Style> style);

    static void bindStyle(Styleable node, Property<Style> style) {
        bindStyleWeak(node, style);
    }

    private static void bindStyleWeak(Styleable node, Property<Style> style) {
        node.applyStyle(style.get());
        WeakReference<Styleable> weakNode = new WeakReference<>(node);
        ChangeListener<Style> listener = new ChangeListener<>() {
            @Override
            public void changed(Observable<? extends Style> obs, Style ov, Style nv) {
                if (weakNode.get() != null) {
                    if (nv != ov) {
                        weakNode.get().applyStyle(nv);
                    }
                } else {
                    style.removeListener(this);
                }
            }
        };
        style.addListener(listener);
    }
}
