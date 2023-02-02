package org.luke.mesa.abs.utils;

import android.util.Log;

public class ErrorHandler {
    public static void handle(Throwable throwable, String action) {
        Log.e(throwable.getClass().getSimpleName(), action);
        //throwable.printStackTrace();
    }
}
