package org.luke.mesa.abs.components.layout.overlay.country;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.components.controls.input.SearchInput;
import org.luke.mesa.abs.components.layout.overlay.PartialSlideOverlay;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.functional.ObjectConsumer;
import org.luke.mesa.data.CountryCode;

public class CountryCodeOverlay extends PartialSlideOverlay implements Styleable {
    private ObjectConsumer<CountryCode> onCountryCode;

    private final CountryCodeList codeList;
    public CountryCodeOverlay(App owner) {
        super(owner, .7);

        SearchInput search = new SearchInput(owner);
        search.setHint("search");

        codeList = new CountryCodeList(owner);
        codeList.setOnAction(code -> {
            if(onCountryCode != null) {
                hide();
                onCountryCode.accept(code);
            }
        });

        search.valueProperty().addListener((obs, ov, nv) -> codeList.search(nv));

        list.addView(search);
        list.addView(codeList);

        applyStyle(owner.getStyle());
    }

    public void setOnCountryCode(ObjectConsumer<CountryCode> onCountryCode) {
        this.onCountryCode = onCountryCode;
    }

    @Override
    public void applySystemInsets(Insets insets) {
        codeList.setPadding(0,0,0,insets.bottom);
    }
}
