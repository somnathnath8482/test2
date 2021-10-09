package com.example.artixspy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity{
    private EditText str;
    private Button chenge,hide;
    private TextView display;
    private AdView mAdView;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        str = findViewById(R.id.str);
        display = findViewById(R.id.display);
        chenge = findViewById(R.id.chenge);
        hide = findViewById(R.id.hide);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {
        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {
        }
    });

    mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);


        hide.setOnClickListener(v -> {
            PackageManager p = getPackageManager();
            ComponentName componentName = new ComponentName(getApplicationContext(), MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
            p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            /*PackageManager p = getPackageManager();
ComponentName componentName = new ComponentName(this, com.apps.MainActivity.class);
p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);*/
        });
        chenge.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                return false;
            }
        });

        chenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chengeline();


            }
        });


    }

    private void chengeline() {
    final String line;
        boolean bool;
        line = str.getText().toString().trim();
        int len = str.length();
        char carray[] = line.toCharArray();

        for (int i = 0; i < len; i++) {
            bool = Character.isLowerCase(carray[i]);
            if (bool == true)
                carray[i] = Character.toUpperCase(carray[i]);
            else
                carray[i] = Character.toLowerCase(carray[i]);
        }


        String chenged = String.valueOf(carray);
        display.setText(chenged);
        display.setVisibility(View.VISIBLE);
    }


}