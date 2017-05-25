package com.awarepoint.androidaccuracytest.SyncData;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.MapTiles;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.MobileConfiguration.SvgMap;
import com.awarepoint.androidaccuracytest.R;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;
import com.awarepoint.androidaccuracytest.androidsvg.SVG;
import com.awarepoint.androidaccuracytest.androidsvg.SVGParseException;
import com.google.gson.Gson;

import java.util.List;



/**
 * Created by ureyes on 3/29/2016.
 */
public class SyncMapImage {
    int siteId;

    List<Area> areaFloorsList;
    int totalTiles = 0;
    int tilesComplete = 0;

    int imagesProcessed = 0;

    DatabaseManager databaseManager;
    MainActivity mainActivity;

    private String ApplianceIP;
    private String baseUrl = "https://awarehealthapi.ahealth.awarepoint.com";


    public SyncMapImage(MainActivity mActivity) {
        this.mainActivity = mActivity;
        this.databaseManager = mActivity.databaseManager;
        this.siteId = mActivity.databaseManager.getSiteId();
        this.ApplianceIP = mActivity.databaseManager.getApplianceIP();

        mainActivity.createSyncDialogProgress("Config3 SYNC");
    }



    public void executeSynAreaIdMapImage(List<Area> areaList) {
        areaFloorsList = areaList;

        mainActivity.progressDialog.setMax(areaList.size());
        mainActivity.progressDialog.setProgress(0);

        for (int li = 0; li < areaList.size(); li++) {

            Area area = areaList.get(li);

            if (area.getType().equals("FLOOR")) {

                SyncAreaIdMapImage syncAreaIdMapImage = new SyncAreaIdMapImage(areaList.get(li).getAreaId());
                syncAreaIdMapImage.execute();
            }
        }
    }


    public class SyncAreaIdMapImage extends AsyncTask<Void, Integer, String> {
        long areaId = 0;
        String url;

        public SyncAreaIdMapImage(long aId) {
            areaId = aId;
        }


        @Override
        protected String doInBackground(Void... params) {

            try {
                publishProgress(imagesProcessed);
                String url_mapps=baseUrl + "/api/map?operation=byAreaId&areaId="+areaId;
                return  mainActivity.sh.makeServiceCall(url_mapps, ServiceHandler.GET);
            } catch (Exception err) {
                mainActivity.fragmentHeader.fragmentMenupw.processComplete(true);
                Log.e(mainActivity.TAG, err.getMessage());
                err.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String responseText) {
            super.onPostExecute(responseText);
            Gson gson = new Gson();
            SvgMap map = gson.fromJson(responseText, SvgMap.class);


            String graphicsdata = map.getSvgMap();

            try {
                SVG svg = SVG.getFromString(graphicsdata);

            } catch (SVGParseException e) {
                e.printStackTrace();
            }

            Resources res = mainActivity.getResources();
            Drawable drawable = res.getDrawable(R.drawable.phunwareimage50);

            Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
        byte[] graphicByte = mainActivity.databaseHandler.mapTilesHandler.convertBitmapToBytes(bm);
            Log.d("IMAGE","widht "+bm.getWidth()+"height "+bm.getHeight() +" graphic bytes "+graphicByte);

                mainActivity.databaseHandler.mapTilesHandler.addRecordMapTiles(new MapTiles(siteId, areaId, graphicByte, 0, 0, 0));
             imagesProcessed++;
            if (imagesProcessed == areaFloorsList.size()) {
                Log.d("SYNCMAPTILES","Image processed ");
                mainActivity.fragmentHeader.fragmentMenupw.processComplete(false);
            }
        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mainActivity.progressDialog.setProgress(values[0]);
        }
    }

}
