package com.example.demo.Util;

import android.app.Application;

public class ApplicationCache {

    /**
     * Singleton Class
     */
    private static ApplicationCache applicationCache = null;

    private final int MAX_BASE_CATEGORIES = 30;

    //Constructor
    private ApplicationCache()
    {

    }

    //Get Instance
    public static synchronized ApplicationCache getInstance()
    {
        if(applicationCache == null)
        {
            applicationCache = new ApplicationCache();
        }

        return applicationCache;
    }


    //Application Context
    private Application appContext;

    private int [] categoryResourceId;


    public Application getAppContext() {
        return appContext;
    }

    public void setAppContext(Application appContext) {
        this.appContext = appContext;
    }

    //Releasing Resources
    public void removeApplicationCache()
    {
        applicationCache = null;
    }

}

