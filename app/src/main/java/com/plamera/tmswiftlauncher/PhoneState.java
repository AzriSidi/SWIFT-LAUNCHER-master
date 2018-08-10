package com.plamera.tmswiftlauncher;

import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneState extends PhoneStateListener {

    TextView signalHome,signalMain;
    Activity activity;
    ImageView iconSignal,iconData;
    private TelephonyManager telephonyManager;

    public PhoneState(Activity activity, Context context){
        this.activity = activity;
        signalMain = activity.findViewById(R.id.textView11);
        signalHome = activity.findViewById(R.id.textView7);
        iconSignal = activity.findViewById(R.id.signalLevel);
        iconData = activity.findViewById(R.id.dataActivity);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public void onDataActivity(int direction) {
        super.onDataActivity(direction);



        String dataState;

        switch (direction) {
            case TelephonyManager.DATA_ACTIVITY_NONE:
                dataState = " DATA_ACTIVITY_NONE";
                if(activity instanceof MainActivity||activity instanceof HomeScreen) iconData.setImageResource((R.drawable.data_activity_none));
                break;
            case TelephonyManager.DATA_ACTIVITY_IN:
                dataState = " DATA_ACTIVITY_IN";
                if(activity instanceof MainActivity||activity instanceof HomeScreen) iconData.setImageResource((R.drawable.data_activity_in));
                break;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                dataState = " DATA_ACTIVITY_OUT";
                if(activity instanceof MainActivity||activity instanceof HomeScreen) iconData.setImageResource((R.drawable.data_activity_out));
                break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                dataState = " DATA_ACTIVITY_INOUT";
                if(activity instanceof MainActivity||activity instanceof HomeScreen) iconData.setImageResource((R.drawable.data_activity_inout));
                break;
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                dataState = " DATA_ACTIVITY_DORMANT";
                if(activity instanceof MainActivity||activity instanceof HomeScreen) iconData.setImageResource((R.drawable.data_activity_dormant));
                break;
            default:
                dataState = " UNKNOWN " + direction;
                break;


        }

        Log.d("PhoneState", "onDataActivity ->" + dataState);
/*
        if(activity instanceof MainPageActivity)
            dataMain.setText(dataState);
*/
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);

        Log.d("PhoneState ", "onSignalStrengthsChanged []");

        int dbmLevel = 0;
        int asuLevel = 0;

        String readSignalStrength;

        if (getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_CDMA]) == 0) {

            dbmLevel = signalStrength.getCdmaDbm();

        } else if (getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_EVDO_0]) == 0 ||
                getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_EVDO_A]) == 0 ||
                getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_EVDO_B]) == 0) {

            dbmLevel = signalStrength.getEvdoDbm();

        } else if (signalStrength.isGsm()) {
            if (signalStrength.getGsmSignalStrength() != 99) {

                asuLevel = signalStrength.getGsmSignalStrength();
                dbmLevel = signalStrength.getGsmSignalStrength() * 2 - 113;

            } else {

                dbmLevel = signalStrength.getGsmSignalStrength();
            }
        }

        if (asuLevel==0) {
            readSignalStrength = "Signal Strength  " + dbmLevel + " dBm";
        }else{
            readSignalStrength = "Signal Strength  " + dbmLevel + " dBm"+ " " +asuLevel+" ASU";
        }


        if (getNetwork().compareTo(NETWORK_TYPES[TelephonyManager.NETWORK_TYPE_LTE]) == 0) dbmLevel=+15;

        if (dbmLevel >= -70)
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_100));

        if (dbmLevel >= -75 && dbmLevel < -70 )
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_80));

        if (dbmLevel >= -85 && dbmLevel < -75)
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_60));

        if (dbmLevel >= -90 && dbmLevel < -85)
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_40));

        if (dbmLevel > -95 && dbmLevel < -90)
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_20));

        if (dbmLevel > -99 && dbmLevel < -95)
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_0));

        if (dbmLevel == -99)
            if(activity instanceof MainActivity||activity instanceof HomeScreen) iconSignal.setImageResource((R.drawable.signal_not_available));


        if(activity instanceof MainActivity){
            signalMain.setText(readSignalStrength);
        }else if (activity instanceof HomeScreen){
            signalHome.setText(readSignalStrength);
        }
    }

    private static final String[] NETWORK_TYPES = {
            "",  // 0  - NETWORK_TYPE_UNKNOWN
            "GPRS",     // 1  - NETWORK_TYPE_GPRS
            "EDGE",     // 2  - NETWORK_TYPE_EDGE
            "UMTS",     // 3  - NETWORK_TYPE_UMTS
            "CDMA",     // 4  - NETWORK_TYPE_CDMA
            "EVDO_0",   // 5  - NETWORK_TYPE_EVDO_0
            "EVDO_A",   // 6  - NETWORK_TYPE_EVDO_A
            "1xRTT",    // 7  - NETWORK_TYPE_1xRTT
            "HSDPA",    // 8  - NETWORK_TYPE_HSDPA
            "HSUPA",    // 9  - NETWORK_TYPE_HSUPA
            "HSPA",     // 10 - NETWORK_TYPE_HSPA
            "IDEN",     // 11 - NETWORK_TYPE_IDEN
            "EVDO_B",   // 12 - NETWORK_TYPE_EVDO_B
            "LTE",      // 13 - NETWORK_TYPE_LTE
            "EHRPD",    // 14 - NETWORK_TYPE_EHRPD
            "HSPA+",    // 15 - NETWORK_TYPE_HSPAP
    };

    public String getNetwork() {
        return getTelephonyNetworkType();
    }

    public String getCarier() {
        return getCarierName();
    }

    private String getCarierName() {
        String networkOperatorName = telephonyManager.getNetworkOperatorName();

        if(networkOperatorName.equals("")){
            networkOperatorName = "Not Available";
        }
        return networkOperatorName;
    }

    private String getTelephonyNetworkType() {

        int networkType = telephonyManager.getNetworkType();

        if (networkType < NETWORK_TYPES.length)
            return NETWORK_TYPES[telephonyManager.getNetworkType()];

        return "Unrecognized: " + networkType;
    }

}
