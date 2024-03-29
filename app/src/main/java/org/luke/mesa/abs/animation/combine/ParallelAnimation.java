package org.luke.mesa.abs.animation.combine;

import org.luke.mesa.abs.animation.base.Animation;

import java.util.ArrayList;

public class ParallelAnimation extends Animation {
    private final ArrayList<Animation> animations;

    public ParallelAnimation() {
        this(200);
    }

    public ParallelAnimation(long duration) {
        super(duration);

        animations = new ArrayList<>();
    }

    public ParallelAnimation addAnimation(Animation animations) {
        this.animations.add(animations);
        return this;
    }

    public ParallelAnimation removeAnimation(Animation animations) {
        this.animations.remove(animations);
        return this;
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    @Override
    public void init() {
        for (Animation a : animations) {
            a.init();
        }
        super.init();
    }

    @Override
    public void update(float v) {
        for (Animation a : animations) {
            a.update(v);
        }
    }
}
