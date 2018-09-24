package com.dannycheung.canvaslayoutdemo;

import android.app.Application;

import com.dannycheung.canvaslayout.dom.VitualDom;
import com.dannycheung.canvaslayout.dom.config.Configuration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VitualDom.setConfig(Configuration.createDefault(this));
    }
}
