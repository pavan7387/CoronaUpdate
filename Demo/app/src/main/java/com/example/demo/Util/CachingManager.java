package com.example.demo.Util;

import android.app.Application;
import android.content.Context;

public class CachingManager {

    /*App Context*/
    //Save Application Context
    public static void saveAppContext(Application context) {
        //Get ApplicationCache
        ApplicationCache applicationCache = ApplicationCache.getInstance();

        //Save Application Context
        applicationCache.setAppContext(context);
    }

    //Get Application Context
    public static Context getAppContext() {
        //Get ApplicationCache
        ApplicationCache applicationCache = ApplicationCache.getInstance();

        //Get Application Context
        return (applicationCache.getAppContext());
    }

    //Remove Cache
    public static void removeApplicationCache() {
        //Get ApplicationCache
        ApplicationCache applicationCache = ApplicationCache.getInstance();

        //Remove Application Cache
        applicationCache.removeApplicationCache();
    }

}


