package org.luke.mesa.abs.components.controls.input.phoneCode;

import android.view.Gravity;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.base.ValueAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.AlphaAnimation;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.components.controls.text.font.Font;
import org.luke.mesa.abs.components.controls.text.Label;
import org.luke.mesa.abs.components.controls.text.font.FontWeight;
import org.luke.mesa.abs.style.Style;
import org.luke.mesa.abs.style.Styleable;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.data.property.Property;

import java.util.function.Function;

public class InfoLabel extends Label implements Styleable {
    private final ParallelAnimation show, hide;
    private final Animation timer;
    private final Function<Style, Integer> colorSupplier;

    public InfoLabel(App owner, Function<Style, Integer> colorSupplier) {
        super(owner, "");
        this.colorSupplier = colorSupplier;

        setFont(new Font(14f, FontWeight.BOLD));
        setMaxLines(1);
        setLayoutGravity(Gravity.CENTER);

        int errorBy = ViewUtils.dipToPx(50, owner);
        setAlpha(0);

        show = new ParallelAnimation(300)
                .addAnimation(new AlphaAnimation(this, 1))
                .addAnimation(new TranslateYAnimation(this, errorBy))
                .setInterpolator(Interpolator.EASE_OUT);

        hide = new ParallelAnimation(300)
                .addAnimation(new AlphaAnimation(this, 0))
                .addAnimation(new TranslateYAnimation(this, 0))
                .setInterpolator(Interpolator.EASE_IN);

        timer = new ValueAnimation(2000, 0, 1) {
            @Override
            public void updateValue(float v) {
                //do nothing
            }
        }.setFps(5).setOnFinished(() -> {
            show.stop();
            hide.start();
        });

        applyStyle(owner.getStyle());
    }

    public void trigger(String key, String plus) {
        hide.stop();

        setKey(key);

        if (plus != null)
            addParam(0, plus);

        show.start();
        timer.start();
    }

    public void trigger(String key) {
        trigger(key, null);
    }

    public void clear() {
        show.stop();
        timer.stop();

        hide.start();
    }

    @Override
    public void applyStyle(Style style) {
        setTextColor(colorSupplier.apply(style));
    }

    @Override
    public void applyStyle(Property<Style> style) {
        Styleable.bindStyle(this, style);
    }
}
