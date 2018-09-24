package com.dannycheung.canvaslayout.dom.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.dannycheung.canvaslayout.dom.config.engine.IImageEngine;
import com.dannycheung.canvaslayout.dom.config.engine.IRunnableEngine;
import com.facebook.soloader.SoLoader;

/**
 * Created by dannycheung on 2017/10/27.
 */

// image
// runnable
// layout
// resource



public class Configuration {

    private static Context context;

    public static Configuration createDefault(Context context) {

        SoLoader.init(context, false);

        Configuration.context = context;
        Configuration configuration = new Configuration();
        //
        int[] size = getScreenSize(context);
        // density
        configuration.density = size[0] / 750f * 2;
        configuration.oridensity = context.getResources().getDisplayMetrics().density;
        configuration.screenWidthDp = (int) (size[0] / configuration.density);
        configuration.screenHeightDp = (int) (size[1] / configuration.density);
        return configuration;
    }

    public Configuration setNetImageEngine(IImageEngine.INetImageEngine netImageEngine) {
        this.netImageEngine = netImageEngine;
        return this;
    }

    public Configuration setRunnableEngine(IRunnableEngine runnableEngine) {
        this.runnableEngine = runnableEngine;
        return this;
    }

    private static int[] getScreenSize(Context context) {
        int[] result = new int[2];
//        android.content.res.Configuration config = context.getResources().getConfiguration(); //获取设置的配置信息
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        switch (config.orientation) {
//            default:
//            case android.content.res.Configuration.ORIENTATION_LANDSCAPE:
//                result[0] = metrics.heightPixels;
//                result[1] = metrics.widthPixels;
//                break;
//            case android.content.res.Configuration.ORIENTATION_PORTRAIT:
//                result[0] = metrics.widthPixels;
//                result[1] = metrics.heightPixels;
//                break;
//        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        result[0] = Math.min(metrics.widthPixels, metrics.heightPixels);
        result[1] = Math.max(metrics.widthPixels, metrics.heightPixels);
        return result;
    }

    public float density;
    public float oridensity;
    public int screenWidthDp;
    public int screenHeightDp;
    public IImageEngine.INetImageEngine netImageEngine;
    public IRunnableEngine runnableEngine;



    public Context getContext() {
        return context;
    }

    private Configuration() {

    }
}
