package org.luke.mesa.abs.utils;

import android.os.Handler;
import android.os.Looper;

public class Platform {
    private static Handler handler;

    public static void runLater(Runnable r) {
        if (handler == null) handler = new Handler(Looper.getMainLooper());
        handler.post(r);
    }

    public static void runAfter(Runnable r, long after) {
        new Thread(() -> {
            try {
                Thread.sleep(after);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runLater(r);
        }).start();
    }
}
