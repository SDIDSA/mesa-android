package org.luke.mesa.abs.utils;

import org.luke.mesa.abs.utils.functional.BooleanSupplier;

public class Threaded {
    private Threaded() {

    }

    public static void waitWhile(BooleanSupplier condition) {
        while(condition.get().booleanValue()) {
            sleep(5);
        }
    }

    public static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException x) {
            Thread.currentThread().interrupt();
        }
    }

    public static Thread runAfter(int duration, Runnable action) {
        Thread t = new Thread(()-> {
            try {
                Thread.sleep(duration);
                Platform.runLater(action);
            } catch (InterruptedException x) {
                Thread.currentThread().interrupt();
            }
        });

        t.start();

        return t;
    }
}