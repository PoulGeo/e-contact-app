package com.george.econtactdemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class LaunchActivity extends AppCompatActivity {
    ProgressBar progressBar;
    LocationManager locationManager;
    WifiManager wifiManager;
    private int mProgressStatus = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        progressBar = (ProgressBar) findViewById(R.id.progress);


        final Button btnLaunch = (Button) findViewById(R.id.button3);
        final Button eggrafh = (Button) findViewById(R.id.eggrafh);

        btnLaunch.setVisibility(View.INVISIBLE);
        btnLaunch.setEnabled(false);

        eggrafh.setVisibility(View.INVISIBLE);
        eggrafh.setEnabled(false);


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        if (!wifiManager.isWifiEnabled()) {
            showWifiAlert();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(20);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnLaunch.setVisibility(View.VISIBLE);
                        btnLaunch.setEnabled(true);
                        eggrafh.setEnabled(true);
                        eggrafh.setVisibility(View.VISIBLE);


                    }
                });
            }
        }).start();


        // me to parakatw zhtas permission apo ton user me parathyro dialogou

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION}, 123);


    }

    public void launch(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void showWifiAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("To Internet απαιτείται");
        alertDialog.setMessage("Αν δεν είναι ενεργοποιημένο πατήστε ενεργοποίηση.");
        alertDialog.setPositiveButton("Ενεργοποιηση", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                wifiManager.setWifiEnabled(true);
            }
        });

        alertDialog.setNegativeButton("Απορριψη", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    public void eggrafh(View view) {
        Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
