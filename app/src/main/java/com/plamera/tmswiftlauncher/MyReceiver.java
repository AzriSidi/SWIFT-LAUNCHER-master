package com.plamera.tmswiftlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver{
    String TAG = "MyReceiver";
    HomeScreen homeScreen = new HomeScreen();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"MyReceiver Start");
        try {
            Global.getMessage = intent.getStringExtra("dataMessage");
            Global.getTask = intent.getStringExtra("dataTask");
            Global.getQueue = intent.getStringExtra("dataQueue");
            Global.getLoginStatus = intent.getStringExtra("loginStatus");
            Global.getServerStatus = intent.getStringExtra("connectServer");

            Log.d(TAG,"Message: "+Global.getMessage);
            Log.d(TAG,"Task: "+Global.getTask);
            Log.d(TAG,"Queue: "+Global.getQueue);
            Log.d(TAG,"LoginStatus: "+Global.getLoginStatus);
            Log.d(TAG,"CheckConnectServer: "+Global.getServerStatus);

            homeScreen.displayReceiver();
        }catch (NullPointerException ex){
            Log.d(TAG,"NullPointerException: "+ex);
        }
    }
}