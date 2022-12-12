package org.luke.mesa.abs.animation.abs;

import android.view.View;

import androidx.annotation.NonNull;

import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.easing.LinearInterpolator;
import org.luke.mesa.abs.utils.Platform;

public abstract class Animation {
    public static float timeScale = 1.0f;

    private float fps = 60f;
    private Interpolator interpolator = new LinearInterpolator();
    private long duration;
    private long lastUpdate;

    private Runnable onFinished;
    private float progress;
    private Thread th;

    Animation(long duration) {
        this.duration = (long) (duration * timeScale);
    }

    Animation() {
        this(0);
    }

    protected void init() {

    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T start() {
        stop();
        th = new Thread() {
            public void run() {
                init();
                final long start = System.nanoTime();
                while (!Thread.currentThread().isInterrupted()) {
                    long now = System.nanoTime();
                    if (now - lastUpdate >= 0x3b9aca00 / fps) {
                        progress = (now - start) / (float) (duration * 0xf4240);
                        final float fp = progress;
                        if (fp >= 1.0f) {
                            Platform.runLater(() -> update(interpolator.interpolate(1.0f)));
                            if (onFinished != null) {
                                Platform.runLater(onFinished);
                            }
                            return;
                        } else {
                            Platform.runLater(() -> update(interpolator.interpolate(fp)));
                        }
                        lastUpdate = now;
                    }
                }
            }
        };
        th.start();
        return (T) this;
    }

    public void stop() {
        if (isRunning()) {
            th.interrupt();
        }
    }

    public boolean isRunning() {
        return th != null && th.isAlive();
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setFps(float fps) {
        this.fps = fps;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setDuration(long duration) {
        this.duration = duration;
        return (T) this;
    }

    public long getDuration() {
        return duration;
    }

    public Runnable getOnFinished() {
        return onFinished;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return (T) this;
    }

    public abstract void update(float v);

    private String toString(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append('\n');
        for(int i = 0; i < indent; i++) {
            sb.append('\t');
        }
        sb.append(getClass().getSimpleName());

        if(this instanceof ParallelAnimation) {
            ParallelAnimation pa = (ParallelAnimation) this;
            View target = null;

            for(Animation sa : pa.getAnimations()) {
                if(sa instanceof ViewAnimation) {
                    target = ((ViewAnimation) sa).getView();
                }
            }

            if(target != null) {
                sb.append("\ttarget : " + target.getClass().getSimpleName());
            }

            for(Animation sa : pa.getAnimations()) {
                sb.append(sa.toString(indent + 1));
            }
        }

        if(this instanceof SequenceAnimation) {
            SequenceAnimation pa = (SequenceAnimation) this;
            for(Animation sa : pa.getAnimations()) {
                sb.append(sa.toString(indent + 1));
            }
        }
        return sb.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return toString(0);
    }
}
