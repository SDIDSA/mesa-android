package org.luke.mesa.data.binding;

import org.luke.mesa.data.binding.boolean_type.BooleanWhen;
import org.luke.mesa.data.binding.string_type.StringWhen;
import org.luke.mesa.data.observable.BooleanObservable;

public class Bindings {
    public static BooleanWhen whenBoolean(BooleanObservable condition) {
        return new BooleanWhen(condition);
    }

    public static StringWhen whenString(BooleanObservable condition) {
        return new StringWhen(condition);
    }
}
