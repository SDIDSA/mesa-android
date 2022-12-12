package org.luke.mesa.abs.components.layout.overlay.country;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.functional.ObjectConsumer;
import org.luke.mesa.data.CountryCode;

import java.util.List;

public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeHolder> {
    private final App owner;
    private final List<CountryCode> allData;
    private List<CountryCode> data;

    private ObjectConsumer<CountryCode> onAction;

    public CountryCodeAdapter(App owner, List<CountryCode> allData) {
        this.owner = owner;
        this.allData = allData;
        this.data = allData;
    }

    public void setOnAction(ObjectConsumer<CountryCode> onAction) {
        this.onAction = onAction;
    }

    @NonNull
    @Override
    public CountryCodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryCodeHolder holder = new CountryCodeHolder(new CountryCodeEntry(owner));
        holder.setOnAction(code -> {
            if (onAction != null) {
                onAction.accept(code);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryCodeHolder holder, int position) {
        CountryCode code = data.get(position);
        holder.load(code);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void search(String pattern) {
        if (pattern.isBlank()) {
            data = allData;
            notifyDataSetChanged();
        } else {
            CountryCode.searchAmong(allData, pattern, res -> {
                data = res;
                notifyDataSetChanged();
            });
        }
    }
}
