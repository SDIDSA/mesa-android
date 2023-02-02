package org.luke.mesa.abs.components.controls.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import org.luke.mesa.abs.App;
import org.luke.mesa.abs.utils.DiskImageCache;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.functional.ImageConsumer;

import java.io.IOException;
import java.net.URL;

public class ImageProxy {
    private static final LruCache<String, Bitmap> memoryCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 1024) / 8) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };
    private static DiskImageCache diskCache;

    public static void init(App owner) {
        diskCache = new DiskImageCache(owner, "bitmaps", Bitmap.CompressFormat.JPEG, 80);
    }

    private static String makeKey(String url) {
        StringBuilder sb = new StringBuilder();

        for (int i = Math.max(0, url.length() - 20); i < url.length(); i++) {
            char c = url.charAt(i);
            if (Character.isDigit(c) || Character.isLetter(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }

        return sb.toString();
    }

    public static void getImage(String url, ImageConsumer onResult) {
        new Thread(() -> {
            String key = makeKey(url);
            Bitmap found = get(key);
            if (found == null) {
                try {
                    found = download(url);
                    put(key, found);
                } catch (Exception x) {
                    ErrorHandler.handle(x, "downloading image at " + url);
                }
            }
            Bitmap finalFound = found;
            Platform.runLater(() -> {
                try {
                    onResult.accept(finalFound);
                } catch (Exception e) {
                    ErrorHandler.handle(e, "downloading image at " + url);
                }
            });

        }).start();
    }

    private static Bitmap get(String key) {
        Bitmap found = memoryCache.get(key);
        if (found == null) {
            found = diskCache.getBitmap(key);
        }
        return found;
    }

    private static void put(String key, Bitmap bitmap) {
        if (memoryCache.get(key) == null) {
            memoryCache.put(key, bitmap);
        }

        if (diskCache.getBitmap(key) == null) {
            diskCache.put(key, bitmap);
        }

    }

    private static Bitmap download(String url) {
        try {
            return BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            ErrorHandler.handle(e, "download image at " + url);
            return null;
        }
    }

}
