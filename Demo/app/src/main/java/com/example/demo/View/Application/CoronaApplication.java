package com.example.demo.View.Application;

import android.app.Application;
import android.content.Context;

import com.example.demo.Util.ApplicationManager;

public class CoronaApplication extends Application {
    private static CoronaApplication mInstance;
    public static volatile Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }

    public static synchronized CoronaApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ApplicationManager.prepareApplication(this);
    }


    public static Context getAppContext() {
        return applicationContext;
    }

}
