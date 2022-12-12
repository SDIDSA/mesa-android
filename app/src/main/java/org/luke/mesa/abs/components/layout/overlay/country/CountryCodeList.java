package org.luke.mesa.abs.components.layout.overlay.country;

import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.DataUtils;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.abs.utils.functional.ObjectConsumer;
import org.luke.mesa.data.CountryCode;

public class CountryCodeList extends RecyclerView {
    private final CountryCodeAdapter adapter;

    private ObjectConsumer<CountryCode> onAction;
    public CountryCodeList(App owner) {
        super(owner);

        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(true);
        ViewUtils.setMarginTop(this, owner, 15);

        LinearLayoutManager lm = new LinearLayoutManager(owner);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(lm);

        adapter = new CountryCodeAdapter(owner, DataUtils.readCountryCodes(owner));
        adapter.setOnAction(code -> {
            if(onAction != null) {
                onAction.accept(code);
            }
        });
        setAdapter(adapter);

        setClipToPadding(false);
    }

    public void setOnAction(ObjectConsumer<CountryCode> onAction) {
        this.onAction = onAction;
    }

    public void search(String pattern) {
        adapter.search(pattern);
    }
}
