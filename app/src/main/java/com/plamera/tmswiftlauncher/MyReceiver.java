package com.plamera.tmswiftlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.plamera.tmswiftlauncher.Device.DeviceService;

public class MyReceiver extends BroadcastReceiver{
    String TAG = "MyReceiver";
    Toast mToast;
    DeviceService deviceService;
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

            Log.d(TAG,"Message: "+Global.getMessage);
            Log.d(TAG,"Task: "+Global.getTask);
            Log.d(TAG,"Queue: "+Global.getQueue);
            Log.d(TAG,"LoginStatus: "+Global.getLoginStatus);
            Log.d(TAG,"CheckConnectServer: "+Global.getServerStatus);
            if(Global.getLoginStatus.contains("LOGOUT")){
                deviceService.stopSwift();
                deviceService.logOut();
            }else if(Global.getLoginStatus.equals("LOGOUT_FAIL")){
                if (mToast != null) mToast.cancel();
                mToast = Toast.makeText(context, "Logout Failed", Toast.LENGTH_SHORT);
                mToast.show();
                homeScreen.pd.dismiss();
            }
            homeScreen.displayReceiver();
        }catch (NullPointerException ex){
            Log.d(TAG,"NullPointerException: "+ex);
        }
    }
}