package com.gnommostudios.alertru.beacon_estimote_project.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estimote.coresdk.recognition.packets.Beacon;
import com.gnommostudios.alertru.beacon_estimote_project.R;

import java.text.DecimalFormat;
import java.util.List;

public class BeaconListAdapter extends ArrayAdapter {

    private List<Beacon> elements;
    private Activity context;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public BeaconListAdapter(Activity context, List<Beacon> elements) {
        super(context, R.layout.element_list, elements);

        this.context = context;
        this.elements = elements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.element_list, null);
        Beacon element = elements.get(position);

        TextView uuid = convertView.findViewById(R.id.txt_uuid);
        TextView major = convertView.findViewById(R.id.txt_maj);
        TextView minor = convertView.findViewById(R.id.txt_min);
        TextView mac = convertView.findViewById(R.id.txt_mac);
        TextView rssi = convertView.findViewById(R.id.txt_rssi);
        TextView distanceTxt = convertView.findViewById(R.id.txt_distance);

        uuid.setText("UUID: " + element.getProximityUUID());
        major.setText("Major: " + element.getMajor());
        minor.setText("Minor: " + element.getMinor());
        mac.setText("M.A.C.: " + element.getMacAddress());
        rssi.setText("Rssi: " + element.getRssi());
        distanceTxt.setText("Distance: " + decimalFormat.format(Math.pow(10.0, (element.getMeasuredPower() - element.getRssi()) / 20.0)) + "m");

        return convertView;
    }

    public void updateBeaconList(List<Beacon> beacons) {
        elements.clear();
        elements.addAll(beacons);
        notifyDataSetChanged();
    }
}
