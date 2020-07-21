package com.example.demo.Util;

import android.app.Application;

public class ApplicationManager {
    //Prepare Application
    public static void prepareApplication(Application appContext)
    {
        //Save Application Context in Cache
        CachingManager.saveAppContext(appContext);

    }

}
