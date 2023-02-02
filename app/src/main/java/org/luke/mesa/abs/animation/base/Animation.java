package org.luke.mesa.abs.animation.base;

import android.view.View;

import androidx.annotation.NonNull;

import org.luke.mesa.abs.animation.combine.ParallelAnimation;
import org.luke.mesa.abs.animation.combine.SequenceAnimation;
import org.luke.mesa.abs.animation.easing.Interpolator;
import org.luke.mesa.abs.animation.easing.Linear;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.Threaded;

public abstract class Animation {
    public static final int INDEFINITE = -1;

    public static float timeScale = 1.0f;

    private float fps = 60f;
    private Interpolator interpolator = new Linear();
    private long duration;
    private long lastUpdate;

    private Runnable onFinished;
    private float progress;
    private Thread th;

    private boolean autoReverse = false;
    private int cycleCount = 1;

    protected Animation(long duration) {
        this.duration = (long) (duration * timeScale);
    }

    protected Animation() {
        this(0);
    }

    public void init() {

    }

    public <T extends Animation> T start() {
        stop();
        init();
        return start(1);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Animation> T start(int rep) {
        th = new Thread() {
            public void run() {
                final long start = System.nanoTime();
                while (!Thread.currentThread().isInterrupted()) {
                    long now = System.nanoTime();
                    if (now - lastUpdate >= 0x3b9aca00 / fps) {
                        progress = (now - start) / (float) (duration * 0xf4240);
                        boolean reversed = (rep % 2 == 0 && autoReverse);
                        float to = reversed ? 0 : 1;
                        final float fp = Math.min(Math.max(reversed ? 1 - progress : progress, 0), 1);
                        if ((reversed && fp <= 0) || (!reversed && fp >= 1.0f)) {
                            Platform.runLater(() -> update(interpolator.interpolate(to)));
                            if (onFinished != null) {
                                Platform.runLater(onFinished);
                            }
                            if (rep < cycleCount || cycleCount == INDEFINITE) {
                                Animation.this.start(rep + 1);
                            }
                            return;
                        } else {
                            Platform.runLater(() -> update(interpolator.interpolate(fp)));
                        }
                        lastUpdate = now;
                    }else {
                        Threaded.sleep(1);
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
    public <T extends Animation> T setFps(float fps) {
        this.fps = fps;
        return (T) this;
    }

    public long getDuration() {
        return duration;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setDuration(long duration) {
        this.duration = duration;
        return (T) this;
    }

    public Runnable getOnFinished() {
        return onFinished;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
        return (T) this;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    @SuppressWarnings("unchecked")
    public <T extends Animation> T setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
        return (T) this;
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
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
        sb.append(getClass().getSimpleName());

        if (this instanceof ParallelAnimation) {
            ParallelAnimation pa = (ParallelAnimation) this;
            View target = null;

            for (Animation sa : pa.getAnimations()) {
                if (sa instanceof ViewAnimation) {
                    target = ((ViewAnimation) sa).getView();
                }
            }

            if (target != null) {
                sb.append("\ttarget : ").append(target.getClass().getSimpleName());
            }

            for (Animation sa : pa.getAnimations()) {
                sb.append(sa.toString(indent + 1));
            }
        }

        if (this instanceof SequenceAnimation) {
            SequenceAnimation pa = (SequenceAnimation) this;
            for (Animation sa : pa.getAnimations()) {
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
