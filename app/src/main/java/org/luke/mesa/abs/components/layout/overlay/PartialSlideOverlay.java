package org.luke.mesa.abs.components.layout.overlay;

import androidx.core.graphics.Insets;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.animation.base.Animation;
import org.luke.mesa.abs.animation.base.ValueAnimation;
import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.view.position.TranslateYAnimation;
import org.luke.mesa.abs.utils.ViewUtils;
import org.luke.mesa.abs.utils.functional.ObjectSupplier;

public abstract class PartialSlideOverlay extends SlideOverlay {
    private final Animation showKeyboard;
    private final Animation hideKeyboard;

    public PartialSlideOverlay(App owner, double heightFactor) {
        super(owner);

        ObjectSupplier<Integer> inputShownHeight = () -> owner.getScreenHeight() + (owner.getSystemInsets().bottom / 2) - (owner.getInputInsets() == null ? 0 : (owner.getInputInsets().bottom + owner.getSystemInsets().top));
        ObjectSupplier<Integer> inputHiddenHeight = () -> (int) (owner.getScreenHeight() * heightFactor);

        showKeyboard = new ParallelAnimation(300)
                .addAnimation(new TranslateYAnimation(list, 0)
                        .setLateTo(() -> (float) owner.getSystemInsets().top))
                .addAnimation(new ValueAnimation() {
                    @Override
                    public void updateValue(float v) {
                        setHeight((int) v);
                    }
                }.setLateTo(() -> (float) inputShownHeight.get())
                        .setLateFrom(() -> (float) inputHiddenHeight.get()))
                .setInterpolator(Interpolator.EASE_OUT);

        hideKeyboard = new ParallelAnimation(300)
                .addAnimation(new TranslateYAnimation(list, 0)
                        .setLateTo(() -> owner.getScreenHeight() - (float) inputHiddenHeight.get()))
                .addAnimation(new ValueAnimation() {
                    @Override
                    public void updateValue(float v) {
                        setHeight((int) v);
                    }
                }.setLateTo(() -> (float) inputHiddenHeight.get())
                        .setLateFrom(() -> (float) inputShownHeight.get()))
                .setInterpolator(Interpolator.EASE_OUT);

        addOnHidden(() -> {
            list.setTranslationY(owner.getScreenHeight());
            setHeight(inputHiddenHeight.get());
        });

        setHeightFactor(heightFactor);
    }

    @Override
    public void setHeightFactor(double heightFactor) {
        super.setHeightFactor(heightFactor);
    }

    @Override
    public void show() {
        owner.hideKeyboard();
        showKeyboard.stop();
        hideKeyboard.stop();
        super.show();
    }

    @Override
    protected void setHeight(int height) {
        super.setHeight(height);
        ViewUtils.setPadding(list, 15, 15, 15, 0, owner);
    }

    @Override
    public void applyInputInsets(boolean shown, Insets insets) {
        if (isShowing() || isHiding()) {
            return;
        }
        if (shown) {
            hideKeyboard.stop();
            showKeyboard.start();
        } else {
            showKeyboard.stop();
            hideKeyboard.start();
        }
    }
}
