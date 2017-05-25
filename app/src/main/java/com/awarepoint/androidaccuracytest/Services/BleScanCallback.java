package com.awarepoint.androidaccuracytest.Services;

import android.annotation.TargetApi;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.util.Log;

import com.awarepoint.androidaccuracytest.MainActivity;

import java.util.List;

/**
 * Created by ureyes on 1/11/2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BleScanCallback extends ScanCallback {
    MainActivity mainActivity;

    public BleScanCallback(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        Log.d("ble scan result", "callbackType: " + callbackType + " " + result.toString());

        long timestampNanos = result.getTimestampNanos();
        String mac = result.getDevice().getAddress();
        byte rssi = (byte) result.getRssi();

       // mainActivity.bleBeaconResult(mac,  timestampNanos, rssi);

    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        for (ScanResult sr : results) {
            Log.i("ScanResult - Results", sr.toString());
        }
    }

    @Override
    public void onScanFailed(int errorCode)
    {
        Log.e("Scan Failed", "Error Code: " + errorCode);
    }


}