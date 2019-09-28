package com.yumetsuki.chatapp;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
    }

}
