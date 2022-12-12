package org.luke.mesa.abs.components.layout.overlay.country;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.luke.mesa.abs.utils.functional.ObjectConsumer;
import org.luke.mesa.data.CountryCode;

public class CountryCodeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final CountryCodeEntry entry;
    private CountryCode data;

    private ObjectConsumer<CountryCode> onAction;
    public CountryCodeHolder(CountryCodeEntry entry) {
        super(entry);
        entry.setOnClickListener(this::onClick);
        this.entry = entry;
    }

    public void load(CountryCode data) {
        this.data = data;
        entry.load(data);
    }

    @Override
    public void onClick(View view) {
        if(onAction != null && data != null) {
            onAction.accept(data);
        }
    }

    public void setOnAction(ObjectConsumer<CountryCode> onAction) {
        this.onAction = onAction;
    }
}
