package org.luke.mesa.abs.animation.abs;

import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.utils.Threaded;

import java.util.ArrayList;

public class SequenceAnimation extends Animation {
    private final ArrayList<Animation> animations;
    private Animation running = null;
    private long delay = 0;
    private Thread runner;

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
    protected void init() {
        for(Animation animation : animations) {
            animation.init();
        }
        super.init();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Animation> T start() {
        runner = new Thread(() -> {
            for (int i = 0; i < animations.size() - 1 && !Thread.currentThread().isInterrupted(); i++) {
                Animation current = animations.get(i);
                Animation next = animations.get(i + 1);
                current.start();
                running = current;
                Threaded.sleep(getDuration() + delay);
                next.start();
                running = next;
            }

        });
        runner.start();
        return (T) this;
    }

    @Override
    public void stop() {
        if (runner != null && runner.isAlive())
            runner.interrupt();

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
    @SuppressWarnings("unchecked")
    public <T extends Animation> T setAutoReverse(boolean autoReverse) {
        for (Animation animation : animations) {
            animation.setAutoReverse(autoReverse);
        }
        return (T) this;
    }

    @Override
    public <T extends Animation> T setCycleCount(int cycleCount) {
        for (Animation animation : animations) {
            animation.setCycleCount(cycleCount);
        }
        return (T) this;
    }

    @Override
    public void update(float v) {
        //IGNORE
    }

}
