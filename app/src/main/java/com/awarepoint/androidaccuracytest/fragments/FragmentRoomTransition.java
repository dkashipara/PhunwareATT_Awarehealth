package com.awarepoint.androidaccuracytest.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.awarepoint.androidaccuracytest.Database.DatabaseManager;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.MapTiles;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas.AreaHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas.AreaPointHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.FloorConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps.MapMetadataHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps.MapTilesHandler;
import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ureyes on 4/26/2016.
 */
public class FragmentRoomTransition extends Fragment implements View.OnTouchListener {
    public View mfragmentView;
    AreaHandler areaHandler;

    DatabaseManager databaseManager;

    public Spinner spinnerFloor = null;
    public Spinner spinnerBuilding = null;
    public Spinner spinnerCampus = null;

    MapMetadataHandler mapMetadataHandler;
    FloorConfigHandler fch;
    MapTilesHandler mapTilesHandler;

    AreaPointHandler areaPointHandler;

    ImageView mapImage;

    ZoomControls zoomControl;

    Paint paintCircle;
    Paint paintBorder;
    Canvas canvas;

    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    MainActivity mainActivity;

    double floorWidth;
    double floorHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mfragmentView = inflater.inflate(R.layout.layout_fragment_room_transition, null);

        paintCircle = new Paint();                          //define paint and paint color
        paintCircle.setColor(Color.rgb(255, 204, 51));
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setAntiAlias(true);

        paintBorder = new Paint();                          //define paint and paint color
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setAntiAlias(true);
        mapImage = (ImageView) mfragmentView.findViewById(R.id.imgMap);
        mapImage.setOnTouchListener(this);


        mainActivity = (MainActivity) getActivity();

        return mfragmentView;
    }


    public void loadCampus(DatabaseManager dbManager) {
        databaseManager = dbManager;

        mapMetadataHandler = new MapMetadataHandler(databaseManager);
        fch= new FloorConfigHandler(databaseManager);
        mapTilesHandler = new MapTilesHandler(databaseManager);

        areaHandler = new AreaHandler(databaseManager);
        areaPointHandler = new AreaPointHandler(databaseManager);

        List<String> campusList = areaHandler.checkCampusForSite();

        campusList.add(0, "Select Campus..");
        loadDataInSpinner(R.id.spinner_region_campus, campusList);

        onClickSpinners();
    }


    public void onClickSpinners() {

        spinnerCampus = (Spinner) mfragmentView.findViewById(R.id.spinner_region_campus);
        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //clean previous data for Building, Floor
                loadDataInSpinner(R.id.spinner_region_building, null);
                loadDataInSpinner(R.id.spinner_region_floor, null);

                if (position > 0) {

                    TextView optionSelected = (TextView) spinnerCampus.getSelectedView();
                    String CampusSelected = optionSelected.getText().toString();
                    List<String> buildingList = areaHandler.checkBuildingsForCampus(CampusSelected);

                    buildingList.add(0, "Select Building..");

                    loadDataInSpinner(R.id.spinner_region_building, buildingList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBuilding = (Spinner) mfragmentView.findViewById(R.id.spinner_region_building);
        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    TextView optionSelected = (TextView) spinnerBuilding.getSelectedView();

                    if (optionSelected != null) {
                        String BuildingSelected = optionSelected.getText().toString();
                        List<String> floorList = areaHandler.checkFloorsForBuilding(BuildingSelected);

                        floorList.add(0, "Select Floor..");
                        loadDataInSpinner(R.id.spinner_region_floor, floorList);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFloor = (Spinner) mfragmentView.findViewById(R.id.spinner_region_floor);
        spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (position > 0) {
                        mainActivity.isFloorSelected = true;

                        displayMap();
                    }
                } catch (Exception err) {
                    Log.e(mainActivity.TAG, err.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void loadDataInSpinner(int spinnerLayout, List<String> dataList) {

        if (dataList == null) {
            dataList = new ArrayList<String>();
        }
        Spinner spinner = (Spinner) mfragmentView.findViewById(spinnerLayout);
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.layout_spinners, dataList));

    }

    public void selectImageMap() {

        try {
            TextView campusSelected = (TextView) spinnerCampus.getSelectedView();
            TextView buildingSelected = (TextView) spinnerBuilding.getSelectedView();
            TextView floorSelected = (TextView) spinnerFloor.getSelectedView();

            if (floorSelected != null) {
                String campusName = campusSelected.getText().toString();
                String buildingName = buildingSelected.getText().toString();
                String floorName = floorSelected.getText().toString();

                Area areaSelected = areaHandler.checkRecordFloorByName(campusName, buildingName, floorName);

                if (areaSelected != null) {
                 int nativeZoom = 2;

                    TextView txtZoomLevel = (TextView) mfragmentView.findViewById(R.id.txt_zoomLevel);
                    txtZoomLevel.setText(String.valueOf(nativeZoom));

                    List<MapTiles> mapTilesForAreaId = mapTilesHandler.checkMapTilesForAreaId(areaSelected.getAreaId());
//hard coded for phunware
                    Bitmap bitmapFullMap = Bitmap.createBitmap(3677,2718, Bitmap.Config.ARGB_8888);
                //    Bitmap bitmapFullMap = Bitmap.createBitmap(1839,1359, Bitmap.Config.ARGB_8888);
                   canvas = new Canvas(bitmapFullMap);
                    Bitmap bitmap;

                    if (mapTilesForAreaId.size() > 0) {

                        for (int li = 0; li < mapTilesForAreaId.size(); li++) {
                            MapTiles mapTiles = mapTilesForAreaId.get(li);

                            byte[] mapBytes = mapTiles.getGraphic();
                            bitmap = BitmapFactory.decodeByteArray(mapBytes, 0, mapBytes.length);

                            canvas.drawBitmap(bitmap, mapTiles.getX(), mapTiles.getY(), null);
                            //canvas.drawBitmap(bitmap, mapTiles.getX() * 256, mapTiles.getY() * 256, null);
                        }
                    }
                  //      Resources res = mainActivity.getResources();

                  //  mapImage.setImageDrawable(drawable);
                       mapImage.setImageBitmap(bitmapFullMap);
                        mapImage.setVisibility(View.VISIBLE);

                        ((MainActivity) getActivity()).hideKeyboard(getActivity());



                } else {
                    Toast.makeText(getActivity(), "Area Selected not Loaded", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception err) {
            Toast.makeText(getActivity(), "Error loading MapTiles: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(mainActivity.TAG, err.getMessage());
            err.printStackTrace();
        }
    }

    public void drawLocationRoomId(double x, double y) {

        try {

            mapImage.setAdjustViewBounds(true);

            //  int xx = canvas.getWidth();
            int yy = canvas.getHeight();

            canvas.drawCircle((float) x, (float) (yy - y), 70, paintCircle);
            canvas.drawCircle((float) x, (float) (yy - y), 70, paintBorder);

            mapImage.invalidate();

        } catch (Exception err) {
            Log.e(mainActivity.TAG, err.getMessage());
            err.printStackTrace();
            ;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        if (event != null) {

            dumpEvent(event);
            // Handle touch events here...

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:   // first finger down only
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;

                case MotionEvent.ACTION_UP: // first finger lifted

                case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                    mode = NONE;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                    oldDist = spacing(event);
                    Log.d(mainActivity.TAG, "oldDist=" + oldDist);
                    if (oldDist > 5f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                    } else if (mode == ZOOM) {
                        // pinch zooming
                        float newDist = spacing(event);
                        if (newDist > 5f) {
                            matrix.set(savedMatrix);
                            scale = newDist / oldDist; // setting the scaling of the
                            // matrix...if scale > 1 means
                            // zoom in...if scale < 1 means
                            // zoom out
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }
        }
        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }


    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Show an event in the LogCat view, for debugging
     */
    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
    }


    public void displayMap() {
        boolean hasBeacons = false;
        boolean hasRegions = false;
        boolean hasRooms = false;
        boolean hasFloor = false;
        boolean hasAlgorithmConfigs = false;


        try {

            Set<com.awarepoint.locationengine.configuration.domain.device.BeaconConfig> beaconConfigLE = mainActivity.configurationHandler.getBeaconConfigDTOs();
            if (beaconConfigLE.size() > 0)
                hasBeacons = true;

            Set<com.awarepoint.locationengine.configuration.domain.topography.RegionConfig> regionConfigLE = mainActivity.configurationHandler.getRegionConfigDTOs();
            if (regionConfigLE.size() > 0)
                hasRegions = true;


            Set<com.awarepoint.locationengine.configuration.domain.topography.RoomConfig> roomConfigLE = mainActivity.configurationHandler.getRoomConfigDTOs();
            if (roomConfigLE.size() > 0)
                hasRooms = true;

            Set<com.awarepoint.locationengine.configuration.domain.algorithm.AlgorithmConfig> algorithmConfigLE = mainActivity.configurationHandler.getAlgorithmConfigDTOs();
            if (algorithmConfigLE.size() > 0)
                hasAlgorithmConfigs = true;


            Set<com.awarepoint.locationengine.configuration.domain.topography.FloorConfig> floorConfigLE = mainActivity.configurationHandler.getFloorConfigDTOs();
            if (floorConfigLE.size() > 0)
                hasFloor = true;


            if (hasBeacons && hasRegions && hasFloor && hasRooms && hasAlgorithmConfigs) {
                mainActivity.hasDatalocationEngine = true;
                //   mainActivity.configurationHandler.handleFullConfiguration();
                boolean success = mainActivity.scanLeDevice(true);

                if (success) {
                    MenuItem mainItem = mainActivity.main_menu.findItem(R.id.menu_start_ble_scanning);
                    mainItem.setTitle("Stop Ble Scanning");
                }


                mainActivity.fragmentContent.fragmentRoomTransition.selectImageMap();

            } else {
                Log.i("accuracy test tool ", "loadLocationEngineConfiguration: is not complete ");

                String message = "";
                if (!hasBeacons)
                    message = " - Beacons List in LE is Empty";

                if (!hasRegions)
                    message += " - Regions List in LE is Empty";

                if (!hasFloor)
                    message += " - Floor List in LE is Empty";

                if (!hasRooms)
                    message += " - Rooms List in LE is Empty";

                Toast.makeText(mainActivity, "Data is not complete, Send Input to Location Engine (button SEND LE INPUT), message:" + message, Toast.LENGTH_LONG).show();
            }

        } catch (Exception err) {
            Toast.makeText(getActivity(), "Error displayMap: " + err.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}
