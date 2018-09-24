package com.dannycheung.canvaslayout.dom.config.engine;

public interface IRunnableEngine {
    void post(Runnable runnable);
    void postDelay(Runnable runnable, long delay);
    void removeCallbacks(Runnable runnable);
    void postMainThread(Runnable runnable);
    void postMainThreadDelay(Runnable runnable, long delay);
    void removeCallbacksMainThread(Runnable runnable);

}
