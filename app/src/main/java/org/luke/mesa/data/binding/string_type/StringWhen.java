package org.luke.mesa.data.binding.string_type;

import org.luke.mesa.data.observable.BooleanObservable;
import org.luke.mesa.data.observable.StringObservable;
import org.luke.mesa.data.observable.Observable;
import org.luke.mesa.data.property.StringProperty;

import java.util.ArrayList;

public class StringWhen  {
    private final BooleanObservable condition;
    private StringObservable then;
    private StringObservable otherwise;

    private final ArrayList<Observable<?>> dependencies = new ArrayList<>();

    public StringWhen(BooleanObservable condition) {
        this.condition = condition;
        dependencies.add(condition);
    }

    public StringWhen then(StringObservable then) {
        this.then = then;
        dependencies.add(then);
        return this;
    }

    public StringWhen then(String then) {
        this.then = new StringProperty(then);
        return this;
    }

    public StringBinding otherwise(StringObservable otherwise) {
        this.otherwise = otherwise;
        dependencies.add(otherwise);

        return generateBinding();
    }

    public StringBinding otherwise(String otherwise) {
        this.otherwise = new StringProperty(otherwise);
        return generateBinding();
    }

    private StringBinding generateBinding() {
        return new StringBinding(() -> condition.get() ? then.get() : otherwise.get(), dependencies.toArray(new Observable[0]));
    }
}
