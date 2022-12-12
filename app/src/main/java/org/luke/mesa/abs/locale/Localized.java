package org.luke.mesa.abs.locale;

import org.luke.mesa.data.observable.ChangeListener;
import org.luke.mesa.data.observable.Observable;
import org.luke.mesa.data.property.Property;

import java.lang.ref.WeakReference;

public interface Localized {

	void applyLocale(Locale locale);

	void applyLocale(Property<Locale> locale);

	static void bindLocale(Localized node, Property<Locale> locale) {
		bindLocaleWeak(node, locale);
	}

	private static void bindLocaleWeak(Localized node, Property<Locale> locale) {
		node.applyLocale(locale.get());
		WeakReference<Localized> weakNode = new WeakReference<>(node);
		ChangeListener<Locale> listener = new ChangeListener<>() {
			@Override
			public void changed(Observable<? extends Locale> obs, Locale ov, Locale nv) {
				if (weakNode.get() != null) {
					if (nv != ov) {
						weakNode.get().applyLocale(nv);
					}
				} else {
					locale.removeListener(this);
				}
			}
		};
		locale.addListener(listener);
	}
}
