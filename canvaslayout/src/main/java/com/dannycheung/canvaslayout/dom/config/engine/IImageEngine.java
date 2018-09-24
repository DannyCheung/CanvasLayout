package com.dannycheung.canvaslayout.dom.config.engine;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class IImageEngine {

    public interface INetImageEngine {
        void load(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config config, INetImageCallback callback);
        Bitmap loadSync(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config config);
    }

    public interface INetImageCallback {
        void onResponse(String url, Bitmap bitmap, boolean isImmediate);
        void onErrorResponse(String url, int statusCode);
    }

}
