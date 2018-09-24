package com.dannycheung.canvaslayout.dom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.dannycheung.canvaslayout.dom.config.Configuration;

/**
 * Created by dannycheung on 2017/10/27.
 */

public class VitualDom {

//    public static final float LINE = 0.5f;
//    public static final float LINE_BOLD = 0.75f;
//    public static final float LINE_BOLD_X2 = 1.5f;
    private static boolean ENABLE_ASYNC = false;
    public static boolean GLOBAL_DEBUG = false;
//    private static Bitmap.Config defaultConfig = Bitmap.Config.RGB_565;

    private static Configuration config;

    public static void setConfig(Configuration config) {
        VitualDom.config = config;
    }

    public static float getDensity() {
        return VitualDom.config != null ? VitualDom.config.density : 1;
    }
//
    public static float getOriginDensity() {
        return VitualDom.config != null ? VitualDom.config.oridensity : 1;
    }
//
//    public static float getWidthDp() {
//        return VitualDom.config != null ? VitualDom.config.screenWidthDp : 0;
//    }
//
//    public static float getHeightDp() {
//        return VitualDom.config != null ? VitualDom.config.screenHeightDp : 0;
//    }

//    public static void loadImage(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Configuration.INetImageCallback callback) {
//        loadImage(url, maxWidth, maxHeight, scaleType, defaultConfig,callback);
//    }
//
//    public static void loadImage(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config config, Configuration.INetImageCallback callback) {
//        if (VitualDom.config != null && VitualDom.config.netImageEngine != null) {
//            VitualDom.config.netImageEngine.load(url, maxWidth, maxHeight, scaleType,config, callback);
//        } else {
//            if (callback != null) {
//                callback.onErrorResponse(url, -1);
//            }
//        }
//    }

//    public static Bitmap loadImageSync(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType) {
//        return loadImageSync(url, maxWidth, maxHeight, scaleType,defaultConfig);
//    }
//
//    public static Bitmap loadImageSync(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config config) {
//        Bitmap bitmap = null;
//        if (VitualDom.config != null && VitualDom.config.netImageEngine != null) {
//            bitmap = VitualDom.config.netImageEngine.loadSync(url, maxWidth, maxHeight, scaleType,config);
//        }
//        return bitmap;
//    }
//
//    public static Drawable getDrawable(String resName) {
//        Context context = config.getContext();
//        int resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
//        return getDrawable(resId);
//    }
//
//    public static Drawable getDrawable(int resId) {
//        Context context = config.getContext();
//        Drawable drawable = context.getResources().getDrawable(resId);
//        if (drawable != null) {
//            drawable.setBounds(0, 0,
//                    (int) (drawable.getIntrinsicWidth() / getOriginDensity() * getDensity()),
//                    (int) (drawable.getIntrinsicHeight() / getOriginDensity() * getDensity()));
//        }
//        return drawable;
//    }
//
//    public static Resources getResource() {
//        return config != null ? config.getContext().getResources() : null;
//    }
//
    public static void postRunnable(Runnable runnable) {
        boolean hasRunEngine = VitualDom.config != null && VitualDom.config.runnableEngine != null;
        if (hasRunEngine && ENABLE_ASYNC) {
            VitualDom.config.runnableEngine.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void postRunnableDelay(Runnable runnable, long delay) {
        boolean hasRunEngine = VitualDom.config != null && VitualDom.config.runnableEngine != null;
        if (hasRunEngine) {
            VitualDom.config.runnableEngine.postDelay(runnable, delay);
        } else {
            runnable.run();
        }
    }

    public static void postMainThread(Runnable runnable) {
        boolean hasRunEngine = VitualDom.config != null && VitualDom.config.runnableEngine != null;
        if (hasRunEngine && ENABLE_ASYNC) {
            VitualDom.config.runnableEngine.postMainThread(runnable);
        } else {
            runnable.run();
        }
    }

    public static void postMainThreadDelay(Runnable runnable, long delay) {
        boolean hasRunEngine = VitualDom.config != null && VitualDom.config.runnableEngine != null;
        if (hasRunEngine) {
            VitualDom.config.runnableEngine.postMainThreadDelay(runnable, delay);
        } else {
            runnable.run();
        }
    }

    public static void removeCallbacks(Runnable runnable) {
        boolean hasRunEngine = VitualDom.config != null && VitualDom.config.runnableEngine != null;
        if (hasRunEngine) {
            VitualDom.config.runnableEngine.removeCallbacks(runnable);
        }
    }

    public static void removeCallbacksMainThread(Runnable runnable) {
        boolean hasRunEngine = VitualDom.config != null && VitualDom.config.runnableEngine != null;
        if (hasRunEngine) {
            VitualDom.config.runnableEngine.removeCallbacksMainThread(runnable);
        }
    }
}
