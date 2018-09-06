package com.plamera.tmswiftlauncher.Device;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.plamera.tmswiftlauncher.Global;
import com.plamera.tmswiftlauncher.MainActivity;
import com.plamera.tmswiftlauncher.MyReceiver;
import com.plamera.tmswiftlauncher.TrackLogService;

import java.io.File;
import java.util.List;

public class DeviceService {
    Context context;
    Intent intent;
    String TAG;
    String swfitPackage = "my.com.tm.swift";
    String swiftClass = "my.com.tmrnd.swift.LocationUpdateService";

    public DeviceService(Context context) {
        this.context = context;
        TAG = context.getClass().getSimpleName();
    }

    public void startSwift(){
        Log.d(TAG,"SwiftService: startSwift");
        try {
            intent = new Intent();
            intent.setComponent(new ComponentName(swfitPackage,swiftClass));
            intent.putExtra("staffID", Global.usernameBB);
            intent.putExtra("password", Global.passwordBB);
            intent.putExtra("imei", Global.IMEIPhone);
            intent.putExtra("imsi", Global.IMSIsimCardPhone);
            intent.putExtra("firmVer", Global.frmVersion);
            intent.putExtra("serverStatus", Global.loginServer);
            intent.putExtra("loginType", Global.UserType);
            intent.putExtra("token", Global.getToken);
            enableBroadcastReceiver();
            context.startService(intent);
        }catch (Exception ex) {
            Log.e(TAG,"BroadcastExeception: "+ex.toString());
        }
    }

    public void stopSwift(){
        Log.d(TAG,"SwiftService: stopSwift");
        intent = new Intent();
        intent.setComponent(new ComponentName(swfitPackage,swiftClass));
        context.stopService(intent);

        List<ApplicationInfo> packages;

        PackageManager pm;
        pm = context.getPackageManager();

        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);


        for (ApplicationInfo packageInfo : packages) {
            if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;
            if(packageInfo.packageName.equals("com.plamera.tmswiftlauncher")) continue;
            if(packageInfo.packageName.equals("com.example.tm_demo")) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            Log.d(TAG + "-KILL",packageInfo.packageName);
        }
    }

    public void enableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(context, MyReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.d(TAG,"SwiftService: enableBroadcast");
    }

    public void disableBroadcastReceiver(){
        ComponentName receiver = new ComponentName(context, MyReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Log.d(TAG,"SwiftService: disableBroadcast");
    }

    public void clearCacheSwift(Context mContext) {
            Log.d(TAG, "- Clear Cache Start");

            try {
                 Context kContext = mContext.createPackageContext("my.com.tm.swift",
                         Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);

                File cachDir = kContext.getCacheDir();
                Log.d(TAG, "dir " + cachDir.getPath());

                if (cachDir != null && cachDir.isDirectory()) {

                    Log.v("Trim", "can read " + cachDir.canRead());
                    String[] fileNames = cachDir.list();
                    //Iterate for the fileName and delete
                }
            } catch (Exception ex) {
                Log.e(TAG,  "- Clear Cache Exception");
            }
    }

    public void startTrackLog(){
        try {
            intent = new Intent(context, TrackLogService.class);
            context.startService(intent);
        }catch (Exception ex){
            Log.e(TAG,"Exeception: "+ex.toString());
        }
    }

    public boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (swiftClass.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startAgent(){
        try {
            intent = new Intent();
            intent.setComponent(new ComponentName("org.wso2.emm.agent", "org.wso2.emm.agent.BroadcastService"));
            context.startService(intent);
        }catch (Exception ex){
            Log.d(TAG,"broadcastExeception: "+ex.toString());
        }
    }

    public void logoutState(String logout){
        Log.d(TAG,"logoutState: "+logout);
        try {
            intent = new Intent();
            intent.setAction("com.plamera.LOGOUT_STATE");
            intent.putExtra("logoutState", logout);
            context.sendBroadcast(intent);

            Log.d(TAG, "SwiftService: "+isMyServiceRunning());
            if(isMyServiceRunning()){
                Log.d(TAG, "SwiftService: Already running");
                startSwift();
            }else {
                Log.d(TAG, "SwiftService: Not running");
                startSwift();
            }
        }catch (Exception e) {
            Log.e(TAG,"logoutState: "+e.toString());
        }
    }

    public void logOut(){
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        Global.status = "Offline";
    }
}