package org.luke.mesa.abs.components.controls.input.countryCode;

import android.text.InputType;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.input.InputField;
import org.luke.mesa.abs.components.layout.overlay.country.CountryCodeOverlay;
import org.luke.mesa.data.CountryCode;
import org.luke.mesa.data.binding.string_type.StringBinding;
import org.luke.mesa.data.observable.StringObservable;
import org.luke.mesa.data.property.Property;

public class CountryCodeInput extends InputField {
    private final CountryCodeOverlay countryCodeOverlay;
    private final Property<CountryCode> countryCode;

    public CountryCodeInput(App owner, String promptText) {
        super(owner, promptText);
        input.setFocusable(false);
        input.setClickable(false);

        input.setOnDragListener((v, event) -> {
            if(event.getClipData() != null && event.getClipData().getItemCount() == 1) {
                setValue(event.getClipData().getItemAt(0).getText().toString());
            }
            return true;
        });

        countryCode = new Property<>();

        setFocusable(true);
        setClickable(true);

        countryCodeOverlay = new CountryCodeOverlay(owner);
        countryCodeOverlay.setOnCountryCode(countryCode::set);

        InputMethodManager imm = (InputMethodManager) owner.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);

        OnClickListener onClick = v -> {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            countryCodeOverlay.show();
        };

        countryCode.addListener((obs, ov, nv) -> super.setValue(nv == null ? "" : nv.getShortName() + "  " + nv.getCode()));

        input.setOnClickListener(onClick);
        setOnClickListener(onClick);

        removeClear();
        setInputType(InputType.TYPE_CLASS_PHONE);
    }

    public CountryCode getCountryCode() {
        return countryCode.get();
    }

    public StringObservable countryCodeProperty() {
        return new StringBinding(() -> countryCode.get() == null ? "" : countryCode.get().getCode(), countryCode);
    }

    @Override
    public void setValue(String value) {
        if (value == null || value.isEmpty()) {
            countryCode.set(null);
        } else {
            CountryCode.searchAmong(owner, value, res -> {
                if (!res.isEmpty()) {
                    countryCode.set(res.get(0));
                }
            });

        }
    }
}
