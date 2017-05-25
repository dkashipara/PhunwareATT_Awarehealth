package com.awarepoint.androidaccuracytest.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseHandler;
import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Sites;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.SitesHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.R;
import com.awarepoint.androidaccuracytest.SyncData.LocationEngine.SyncBeaconConfig;
import com.awarepoint.androidaccuracytest.SyncData.SyncLocationEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ureyes on 3/22/2016.
 */
public class FragmentHeader extends Fragment {
    public View mfragmentView;

    public FragmentMenu_phunware fragmentMenupw;
    MainActivity mainActivity;
    public int SpinnerSiteSelectedPosition;
    public int SpinnerApplianceSelectedPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mfragmentView = inflater.inflate(R.layout.layout_main_header, null);
        mainActivity = ((MainActivity) getActivity());
        fragmentMenupw = (FragmentMenu_phunware) getChildFragmentManager().findFragmentById(R.id.fragment_menu_phunware);

        buttonClicksHandler();



        return mfragmentView;
    }




    public void loadSites(final DatabaseManager dbManager) {

        final SitesHandler sitesHandler = new SitesHandler(dbManager);

        List<Sites> sitesList = sitesHandler.checkTableApplianceSites();

        List<String> siteNameList = new ArrayList<String>();
        siteNameList.add(0, "Select Site...");

        for (Sites sites : sitesList) {
            siteNameList.add(sites.getSiteName());
        }
        mainActivity.databaseHandler = new DatabaseHandler((mainActivity).databaseManager);
        mainActivity.fragmentContent.fragmentRoomTransition.loadCampus(mainActivity.databaseManager);
    }


    public boolean checkSyncValidationInput() {

        return true;
    }


    public void buttonClicksHandler() {

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {


                    final Button btnSendDataLE = (Button) mfragmentView.findViewById(R.id.btn_sendLEdata);
                    btnSendDataLE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            btnSendDataLE.setEnabled(false);

                            SyncLocationEngine syncLocationEngine = new SyncLocationEngine(mainActivity);
                            syncLocationEngine.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    });


                    final Button btnSyncAllConf = (Button) mfragmentView.findViewById(R.id.btn_sync_config);
                    btnSyncAllConf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean correct = checkSyncValidationInput();

                            if (correct) {
                                mainActivity.databaseHandler.cleanDatabaseBeacons();

                                btnSyncAllConf.setEnabled(false);

                                SyncBeaconConfig syncBeaconConfig = new SyncBeaconConfig(mainActivity);
                                syncBeaconConfig.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }


                        }
                    });


                } catch (Exception err) {
                    Log.e(mainActivity.TAG, err.getMessage());
                    err.printStackTrace();
                }

            }
        });


    }

    public void processComplete(boolean isFailed) {

        try {
            Button btnSyncConfig = (Button) mfragmentView.findViewById(R.id.btn_sync_config);
            btnSyncConfig.setEnabled(true);
            mainActivity.progressDialog.dismiss();

            if (isFailed) {
                if (mainActivity.sh == null)
                    Toast.makeText(mainActivity, "Token not Granted, Request Token..", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(mainActivity, "Process failed, please try again", Toast.LENGTH_SHORT).show();

            }

     } catch (Exception err) {
            Log.e(mainActivity.TAG, err.getMessage());
        }

    }


}
