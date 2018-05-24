package com.gnommostudios.alertru.beacon_estimote_project;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.observation.region.Region;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.mgmtsdk.feature.settings.api.EstimotePackets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BeaconManager.BeaconRangingListener, BeaconManager.ServiceReadyCallback {

    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private BeaconRegion beaconRegion;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_fondo);

        EstimoteSDK.initialize(this, "christian-sellent-hotmail--hcu", "6492859b557684e4ec0cb3a212a55c42");

        beaconRegion = new BeaconRegion("Beacons with default Estimote UUID",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(this);
    }

    @Override
    public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
        if (beacons.size() > 0) {
            //Map<String, Double> map = new HashMap<>();
            Beacon nearlyBeacon = beacons.get(0);
            for (int i = 0; i < beacons.size(); i++) {
                Beacon beacon = beacons.get(i);
                if (Math.pow(10, (beacon.getMeasuredPower() - beacon.getRssi()) / 20.0) < Math.pow(10, (nearlyBeacon.getMeasuredPower() - nearlyBeacon.getRssi()) / 20.0)) {
                    nearlyBeacon = beacon;
                }
                //map.put(beacon.getMacAddress().toString(), Math.pow(10, (beacon.getMeasuredPower() - beacon.getRssi()) / 20.0));
                Log.i("Estimote", beacons.get(i).getMacAddress() + "");
            }

            //String nearlyBeacon = map.get()

            switch (nearlyBeacon.getMacAddress().toString()) {
                case "[E6:99:6A:7A:39:CA]": //Azul
                    relativeLayout.setBackgroundColor(Color.parseColor("#0000EE"));
                    break;
                case "[F4:50:04:B3:FA:B4]": //Morao
                    relativeLayout.setBackgroundColor(Color.parseColor("#EE00EE"));
                    break;
                case "[D3:94:A3:0B:22:C3]": //Verde
                    relativeLayout.setBackgroundColor(Color.parseColor("#009900"));
                    break;
                default:
                    relativeLayout.setBackgroundColor(Color.BLACK);
                    break;
            }

        } else {
            Log.i(TAG, "No hay estimotes cerca.");
        }
    }

    @Override
    public void onServiceReady() {
        beaconManager.setRangingListener(this);

        //beaconManager.setBackgroundScanPeriod(5000, 10000);

        beaconManager.startRanging(beaconRegion);
    }
}