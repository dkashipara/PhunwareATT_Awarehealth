package com.awarepoint.androidaccuracytest.LocationEngine.Services;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import com.awarepoint.androidaccuracytest.MainActivity;

import java.util.List;

/**
 * Created by ureyes on 1/11/2016.
 */
public class BleScanCallback extends ScanCallback {
    MainActivity mainActivity;

    public BleScanCallback(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        //Log.d("ble scan result", "callbackType: " + callbackType + " " + result.toString());

        long timestampNanos = result.getTimestampNanos();
        String mac = result.getDevice().getAddress();
        byte rssi = (byte) result.getRssi();
        byte[] scanBytes = result.getScanRecord().getBytes();
        // mainActivity.bleBeaconResult(mac,  timestampNanos, rssi,result.getScanRecord().getBytes());
        mainActivity.bleBeaconResult(mac, timestampNanos, rssi, scanBytes);
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        for (ScanResult sr : results) {
            Log.i("ScanResult - Results", sr.toString());
        }
    }

    @Override
    public void onScanFailed(int errorCode) {
        Log.e("Scan Failed", "Error Code: " + errorCode);
    }


}

