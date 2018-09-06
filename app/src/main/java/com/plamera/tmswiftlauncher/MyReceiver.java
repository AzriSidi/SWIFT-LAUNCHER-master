package com.plamera.tmswiftlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.plamera.tmswiftlauncher.Device.DeviceService;

public class MyReceiver extends BroadcastReceiver{
    String TAG = "MyReceiver";
    DeviceService deviceService;
    Toast mToast;
    HomeScreen homeScreen = new HomeScreen();

    @Override
    public void onReceive(Context context, Intent intent) {
        deviceService = new DeviceService(context);
        Log.d(TAG,"MyReceiver Start");

        try {
            Global.getMessage = intent.getStringExtra("dataMessage");
            Global.getTask = intent.getStringExtra("dataTask");
            Global.getQueue = intent.getStringExtra("dataQueue");
            Global.getLoginStatus = intent.getStringExtra("loginStatus");
            Global.getServerStatus = intent.getStringExtra("connectServer");

            if(Global.getMessage == null){
                Global.getMessage = "";
            }else if(Global.getTask == null){
                Global.getTask = "";
            }else if(Global.getQueue == null){
                Global.getQueue = "";
            }else if(Global.getLoginStatus == null){
                Global.getLoginStatus = "";
            }

            Log.d(TAG,"Message: "+Global.getMessage);
            Log.d(TAG,"Task: "+Global.getTask);
            Log.d(TAG,"Queue: "+Global.getQueue);
            Log.d(TAG,"LoginStatus: "+Global.getLoginStatus);
            Log.d(TAG,"CheckConnectServer: "+Global.getServerStatus);

            switch (Global.getLoginStatus) {
                case "LOGOUT":
                    deviceService.stopSwift();
                    deviceService.logOut();
                    deviceService.disableBroadcastReceiver();
                    break;
                case "LOGOUTFAIL":
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(context, "Logout Failed", Toast.LENGTH_SHORT);
                    mToast.show();
                    break;
                default:
                    break;
            }

            homeScreen.displayReceiver();
        }catch (NullPointerException ex){
            Log.d(TAG,"NullPointerException: "+ex);
        }
    }
}