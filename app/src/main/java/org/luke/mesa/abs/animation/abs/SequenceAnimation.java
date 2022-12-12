package org.luke.mesa.abs.animation.abs;

import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.utils.Threaded;

import java.util.ArrayList;

public class SequenceAnimation extends Animation {
    private final ArrayList<Animation> animations;
    private Animation running = null;
    private long delay = 0;

    public SequenceAnimation(long duration) {
        super(duration);
        animations = new ArrayList<>();
    }

    public SequenceAnimation setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public SequenceAnimation addAnimation(Animation animation) {
        animation.setDuration(getDuration());
        this.animations.add(animation);
        return this;
    }

    public SequenceAnimation removeAnimation(Animation animation) {
        this.animations.remove(animation);
        return this;
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Animation> T start() {
        new Thread(() -> {
            for (int i = 0; i < animations.size() - 1; i++) {
                Animation current = animations.get(i);
                Animation next = animations.get(i + 1);
                current.start();
                running = current;
                Threaded.sleep(getDuration() + delay);
                next.start();
                running = next;
            }
        }).start();
        return (T) this;
    }

    @Override
    public void stop() {
        if (running != null)
            running.stop();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Animation> T setInterpolator(Interpolator interpolator) {
        for (Animation animation : animations) {
            animation.setInterpolator(interpolator);
        }
        return (T) this;
    }

    @Override
    public void update(float v) {
        //IGNORE
    }

}
