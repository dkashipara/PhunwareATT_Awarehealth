package com.awarepoint.androidaccuracytest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.Database.DatabaseHandler;
import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.LocationEngine.Domain.Log4jHelper;
import com.awarepoint.androidaccuracytest.LocationEngine.Domain.MessageType;
import com.awarepoint.androidaccuracytest.LocationEngine.Sensors.AccelerometerListener;
import com.awarepoint.androidaccuracytest.LocationEngine.Sensors.AccelerometerManager;
import com.awarepoint.androidaccuracytest.LocationEngine.Services.BleScanCallback;
import com.awarepoint.androidaccuracytest.Services.ServiceHandler;
import com.awarepoint.androidaccuracytest.SyncData.LocationEngine.SyncBeaconConfig;
import com.awarepoint.androidaccuracytest.fragments.FragmentContent;
import com.awarepoint.androidaccuracytest.fragments.FragmentHeader;
import com.awarepoint.androidaccuracytest.iBeaconMask.Beacon;
import com.awarepoint.androidaccuracytest.iBeaconMask.IBeacon;

import com.awarepoint.locationengine.configuration.ConfigurationHandler;
import com.awarepoint.locationengine.listener.MobileLocationChangeListener;
import com.awarepoint.locationengine.mobile.application.MobileApplication;
import com.awarepoint.locationengine.outputlocation.api.mobile.MobileLocation;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements AccelerometerListener, MobileLocationChangeListener {
    public static final String TAG = "AccuracyTest";

    public Menu main_menu;



    public Map<Long, Long> FloorHeightMap = new ConcurrentHashMap<>();


    // LocationEngine Implementation

    Logger log = Log4jHelper.getLogger(TAG);

    public boolean hasDatalocationEngine;
    public boolean isFloorSelected = false;

    private static final String AWP_BEACON_MAC_FORMAT = "9000";
    private static final String AWP_FINAL_MAC_FORMAT = "0014EB";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bleScanner;

    private static final long SCAN_PERIOD = 3000;

    public double accelerometerX = 0;
    public double accelerometerY = 0;
    public double accelerometerZ = 0;

    public ConfigurationHandler configurationHandler;
    public MobileApplication mobileApplication;

    Handler mHandler = new Handler();
    List<ScanFilter> scanFilters = new ArrayList<>();
    BleScanCallback bleScanCallback;

    MainActivity mainActivity;

    public FragmentHeader fragmentHeader;
    public FragmentContent fragmentContent;

    public DatabaseManager databaseManager;
    public DatabaseHandler databaseHandler;

    public ProgressDialog progressDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
 //   public LocalCacheManagerLayout cacheManagerLayout;




    //PHUNWARE awarehealth changes
    //OAuth URL and data. In your application, you will most likely get the
    //username and password via a UI screen.
    private String oauthUrl = "https://oauthservice-phun.ahealth.awarepoint.com/oauth/token"; //replace for your target environment
    private String oauthUserName = "phunware"; //replace with your own
    private String oauthPassword = "uUtGY912mN8"; //replace with your own
    private String oauthApiKey = "phunwareapikey"; //replace with your own
    private OAuth2Response oauthResponse = null;
    public ServiceHandler sh;

    public class GetOAuthToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            OAuth2Client oauthClient = new OAuth2Client();
            OAuthParameters params = new OAuthParameters(oauthUrl, oauthApiKey, oauthUserName, oauthPassword);

            try {
                oauthResponse = oauthClient.SubmitRequest(params);

                //Create new service handler for other HTTP calls to the API to use
                //and pass the OAuth token in the constructor so every API call
                //can use it.
                sh = new ServiceHandler(oauthResponse.getAccessToken());
            }
            catch (Exception exc)
            {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
    }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = this;
        new GetOAuthToken().execute();
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressDialog = new ProgressDialog(this);

        databaseManager = new DatabaseManager(this);

        fragmentHeader = (FragmentHeader) getFragmentManager().findFragmentById(R.id.fragment_header);
        fragmentContent = (FragmentContent) getFragmentManager().findFragmentById(R.id.fragment_content);
        fragmentHeader.fragmentMenupw.setFragmentContent(fragmentContent);

        fragmentOnClicksHandler();
        fragmentHeader.loadSites(databaseManager);
        buttonClicksHandler();
        hideKeyboard(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // LocationEngine Implementation
        mobileApplication = new MobileApplication();
        initializeBLEManager();

        //Start Accelerometer Listening
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }

      configurationHandler = new ConfigurationHandler();
        // End LocationEngine Implementation


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        main_menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String title = String.valueOf(item.getTitle());

        if (id == R.id.menu_display_header) {

            if (title.contains("Display")) {
                if (mainActivity.fragmentHeader.mfragmentView.getVisibility() == View.GONE) {
                    mainActivity.fragmentHeader.mfragmentView.setVisibility(View.VISIBLE);
                    item.setTitle("Hide Header");
                }
            } else if (title.contains("Hide")) {
                if (mainActivity.fragmentHeader.mfragmentView.getVisibility() == View.VISIBLE) {
                    mainActivity.fragmentHeader.mfragmentView.setVisibility(View.GONE);
                    item.setTitle("Display Header");
                }
            }

            return true;
        } else if (id == R.id.menu_start_ble_scanning) {

            if (title.contains("Start")) {
                boolean success = scanLeDevice(true);

                if (success)
                    item.setTitle("Stop Ble Scanning");


            } else if (title.contains("Stop")) {
                if (Build.VERSION.SDK_INT < 21) {
                    if (bluetoothAdapter.isDiscovering())
                        bluetoothAdapter.stopLeScan(mLeScanCallback);
                } else {
                    bleScanner.stopScan(bleScanCallback);
                }
                item.setTitle("Start Ble Scanning");
            }

            return true;
        }else if(id == R.id.menu_SwithMap) {
            //LE-103
            if (title.contains("GOOGLE")) {
                if (mainActivity.fragmentContent.googleFragment.mfragmentView.getVisibility() == View.GONE) {
                    mainActivity.fragmentContent.googleFragment.mfragmentView.setVisibility(View.VISIBLE);
                    mainActivity.fragmentContent.fragmentRoomTransition.mfragmentView.setVisibility(View.GONE);
                    item.setTitle("SWITCH TO AWP MAP");
                }
            } else if (title.contains("AWP")) {
                if (mainActivity.fragmentContent.fragmentRoomTransition.mfragmentView.getVisibility() == View.GONE) {
                    mainActivity.fragmentContent.fragmentRoomTransition.mfragmentView.setVisibility(View.VISIBLE);
                    mainActivity.fragmentContent.googleFragment.mfragmentView.setVisibility(View.GONE);
                    item.setTitle(R.string.menu_switchMap);
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createSyncDialogProgress(String message) {
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Running: " + message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();
    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void buttonClicksHandler() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Button btnRequestToken = (Button) findViewById(R.id.btn_sync_config);
                btnRequestToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        try {
              if (sh!=null) {
                                SyncBeaconConfig syncbeacon = new SyncBeaconConfig(mainActivity);
                                syncbeacon.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                Toast.makeText(mainActivity, "WAITING FOR TOKEN ", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "Waiting for the token");

                            }

                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                });


                fragmentHeader.mfragmentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(MainActivity.this);
                    }
                });

            }
        });

    }


    public void fragmentOnClicksHandler() {

        fragmentHeader.mfragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
            }
        });

        fragmentContent.mfragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
            }
        });

        fragmentContent.fragmentRoomTransition.mfragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
            }
        });

        fragmentHeader.fragmentMenupw.mfragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(MainActivity.this);
            }
        });

    }


    //LocationEngine Implementation


    public boolean scanLeDevice(final boolean enable) {

        try {

            if (mobileApplication.coreLocationEngine == null) {
                mobileApplication.createLocationEngine(configurationHandler, this);
                  configurationHandler.handleFullConfiguration();
            }


            if (Build.VERSION.SDK_INT >= 21) {

                bleScanCallback = new BleScanCallback(mainActivity);

                Set<Long> beaconDevices = configurationHandler.getBeaconConfigLongMacs();

                Iterator<Long> beaconsLong = beaconDevices.iterator();
                while (beaconsLong.hasNext()) {
                    long device = beaconsLong.next();

                    String macString = convertMacLongToString(Long.toHexString(device).toUpperCase());

                    if (macString.length() == 17) {
                        scanFilters.add(new ScanFilter.Builder().setDeviceAddress(macString).build());


                    }
                }

            }

            if (enable) {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT < 21) {
                            bluetoothAdapter.stopLeScan(mLeScanCallback);
                            bluetoothAdapter.startLeScan(mLeScanCallback);
                        }
                    }
                }, SCAN_PERIOD);

                if (Build.VERSION.SDK_INT < 21) {
                    bluetoothAdapter.startLeScan(mLeScanCallback);
                } else {

                    bleScanner.startScan(scanFilters, new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build(), bleScanCallback);
                    //bleScanner.startScan(null, new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build(),bleScanCallback );
                }
            } else {
                if (Build.VERSION.SDK_INT < 21) {
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }

        } catch (Exception err) {
            Log.e("scanLeDevice() ", err.getMessage());
            err.printStackTrace();

            return false;
        }

        return true;
    }


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long now = System.nanoTime();
                            if (device != null && rssi != 0) { //0 rssi means no rssi info available
                                Log.i("onLeScan", device.toString() + " : " + String.valueOf(rssi) + " : " + String.valueOf(now));

                                bleBeaconResult(device.getAddress(), now, rssi, scanRecord);
                            }
                        }
                    });
                }
            };


    //   public void bleBeaconResult(String device, final long timestampNanos, final int deviceRssi, byte[] scanRecord) {
    public void bleBeaconResult(String device, final long timestampNanos, final int deviceRssi, byte[] scanBytes) {
        String awpMac = device.toUpperCase().replace(":", "");

        if (awpMac.substring(0, 6).equals(AWP_FINAL_MAC_FORMAT)) {
            Beacon iBeacon = Beacon.parse(scanBytes);
    // throwing away iBeacon only care about AWP beacons so sending only AWP beacons to LE
            if (iBeacon instanceof IBeacon) {
                System.out.println("scan record " + iBeacon.getAddress() + " device id " + device);
            } else {
                byte rssi = (byte) deviceRssi;
                device = device.replace(":", "");

                String macAwp = AWP_BEACON_MAC_FORMAT + device.substring(6, 12);

                Long macLong = Long.parseLong(macAwp, 16);

                long tagPid = 0;

                boolean isInMotion = true;

                mobileApplication.mobileDevicePacketReceiver.publish(tagPid, macLong, rssi, timestampNanos, isInMotion, accelerometerX, accelerometerY, accelerometerZ);
                accelerometerX = 0;
                accelerometerY = 0;
                accelerometerZ = 0;
            }
        }
    }

    public void initializeBLEManager() {

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            bleScanner = bluetoothAdapter.getBluetoothLeScanner();

            if (bleScanner == null) {
                Toast.makeText(this, "Bluetooth Radio is off!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
    }


    protected String convertMacLongToString(String mac) {
        String macFormat = "";

        try {

            if (mac.length() >= 10) {
                if (mac.substring(0, 4).equals(AWP_BEACON_MAC_FORMAT)) {

                    if (mac.substring(0, 2).equals("90")) {
                        mac = "00" + mac;
                    }
                    String macAwp = AWP_FINAL_MAC_FORMAT + mac.substring(6, 12);

                    for (int li = 0; li < macAwp.length(); li = li + 2) {
                        String byteMac = macAwp.substring(li, li + 2);
                        macFormat += byteMac + ":";
                    }

                    return macFormat.substring(0, macFormat.length() - 1);
                }
            }
        } catch (Exception err) {
            Log.e("convertMacLongToString", mac + " Error: " + err.getMessage());
        }
        return "";
    }


    long prevRoomId = 0;
    long prevSubroomId = 0;
    double prevX = 0;
    double prevY = 0;
    int displayCount = 0;
    MobileLocation privLocation = null;
    private Handler uiCallback = new Handler() {
        public void handleMessage(Message msg) {

            try {
                // cannot specify density pixel programmatically like you can in xml. have to calculate manually.
              //  final float scale = getResources().getDisplayMetrics().density;

                if (msg.what == MessageType.CLOSEST_BEACON.getValue()) {
                    Log.d("Closest Beacon", String.valueOf(msg.obj));


                    MobileLocation mobileLocation = (MobileLocation) msg.obj;

                    if ((prevRoomId != mobileLocation.roomId) || (prevSubroomId != mobileLocation.subroomId) || (prevX != mobileLocation.x) || (prevY != mobileLocation.y)) {
                        prevRoomId = mobileLocation.roomId;
                        prevSubroomId = mobileLocation.subroomId;
                        prevX = mobileLocation.x;
                        prevY = mobileLocation.y;


                        double x = mobileLocation.x;
                             double y = mobileLocation.y;
                            //LE-103
                        fragmentContent.googleFragment.updateLatNLon(mobileLocation.latitude, mobileLocation.longitude);


                        if (mainActivity.fragmentContent.fragmentRoomTransition.mfragmentView.getVisibility() == View.VISIBLE) {
                            fragmentContent.fragmentRoomTransition.selectImageMap();
                            fragmentContent.fragmentRoomTransition.drawLocationRoomId(x, y);
                            fragmentContent.fragmentRoomTransition.onTouch(fragmentContent.fragmentRoomTransition.mfragmentView.findViewById(R.id.imgMap), null);
                        }

                        if(mainActivity.fragmentContent.googleFragment.mfragmentView.getVisibility() == View.VISIBLE) {
                            fragmentContent.googleFragment.displayMap();
                        }
                    }
                }

            } catch (Exception err) {
                Toast.makeText(mainActivity, "Error defining Location: " + err.getMessage(), Toast.LENGTH_LONG).show();

                Log.e("handleMessage Error", err.getMessage());
                err.printStackTrace();
            }
        }

    };

    @Override
    public void onShake(float force) {
    }

    @Override
    public void onAccelerationChanged(final float x, final float y, final float z) {

        runOnUiThread(new Runnable() {
            public void run() {

                accelerometerX = x;
                accelerometerY = y;
                accelerometerZ = z;

            }

        });

    }


    @Override
    public void notifyMobileLocationChange(MobileLocation mobileLocation) {

        Log.i("Location Changed", mobileLocation.toString());
        Message message = Message.obtain();
        message.what = MessageType.CLOSEST_BEACON.getValue();
        message.obj = mobileLocation;

        uiCallback.sendMessage(message);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
