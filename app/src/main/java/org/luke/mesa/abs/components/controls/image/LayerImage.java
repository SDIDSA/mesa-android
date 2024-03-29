package org.luke.mesa.abs.components.controls.image;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.luke.mesa.abs.App;

import java.util.ArrayList;

public class LayerImage extends FrameLayout {
    private final ArrayList<ColorIcon> layers;

    public LayerImage(App owner, int... layerIds) {
        super(owner);

        layers = new ArrayList<>();

        for (int layerId : layerIds) {
            addLayer(owner, layerId);
        }
    }

    public void setOnClick(Runnable onClick) {
        for(ColorIcon layer : layers) {
            layer.setOnClick(onClick);
        }
    }

    public ColorIcon getLayer(int index) {
        return layers.get(index);
    }

    public void addLayer(App owner, int res) {
        ColorIcon layer = new ColorIcon(owner, res);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        layer.setLayoutParams(params);
        addView(layer);
        layers.add(layer);
    }

    public void setColor(int layer, int color) {
        layers.get(layer).setColor(color);
    }
}
