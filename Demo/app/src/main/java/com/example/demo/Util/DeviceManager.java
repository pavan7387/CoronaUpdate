package com.example.demo.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;

public class DeviceManager {

    //Check Network Connection
    public static boolean isNetworkAvailable()
    {
        //Get Application Context
        Context appContext = CachingManager.getAppContext();

        //Get ConnectivityManager
        ConnectivityManager connectivityManager = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get NetworkInfo
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ((networkInfo == null || !networkInfo.isConnected()) ? false : true);
    }

    public static void openSettings(Activity activityContext, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activityContext.getPackageName(), null);
        intent.setData(uri);
        activityContext.startActivityForResult(intent, requestCode);
    }

}
