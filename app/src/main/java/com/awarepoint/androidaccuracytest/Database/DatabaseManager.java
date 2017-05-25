package com.awarepoint.androidaccuracytest.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.AlgorithmConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.BeaconConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.FloorConfigVertices;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfig;
import com.awarepoint.androidaccuracytest.Database.Tables.LocationEngine.RoomConfigNeighbors;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.Map;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.MapMetadata;
import com.awarepoint.androidaccuracytest.Database.Tables.Maps.MapTiles;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.Area;
import com.awarepoint.androidaccuracytest.Database.Tables.Regions.AreaPoint;
import com.awarepoint.androidaccuracytest.Database.Tables.Sites;


/**
 * Created by ureyes on 11/4/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 13;
    static final String databaseName = "DBAccuracyTest";

    private int siteId = 1004;
    private String ApplianceIP = "https://aps.ahealth.awarepoint.com/";

    //Tables Maps
    private Map map;
    private MapMetadata mapMetadata;
    private MapTiles mapTiles;


    //Tables RegionConfig
    private Area area;
    private AreaPoint areaPoint;

    //Table Sites
    private Sites sites;

    //Tables Location Engine
    private BeaconConfig beaconConfig;
    //private RegionConfig regionConfig;
    //private RegionConfigVertices regionConfigVertices;
    private RoomConfigNeighbors roomConfigNeighbors;
    private RoomConfig roomConfig;
    private FloorConfig floorConfig;
    private FloorConfigVertices floorConfigVertices;
    private AlgorithmConfig algorithmConfigs;
    private Context databaseContext;


    public DatabaseManager(Context context) {
        super(context, databaseName, null, DATABASE_VERSION);

        this.databaseContext = context;

        this.map = new Map();
        this.mapMetadata = new MapMetadata();
        this.mapTiles = new MapTiles();

        this.area = new Area();
        this.areaPoint = new AreaPoint();

        this.beaconConfig = new BeaconConfig();
        // this.regionConfig = new RegionConfig();
        // this.regionConfigVertices = new RegionConfigVertices();
        this.roomConfigNeighbors = new RoomConfigNeighbors();
        this.roomConfig = new RoomConfig();
        this.floorConfig = new FloorConfig();
        this.floorConfigVertices = new FloorConfigVertices();
        this.algorithmConfigs = new AlgorithmConfig();

        this.sites = new Sites();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        map.createTable(db);
        mapTiles.createTable(db);
        mapMetadata.createTable(db);

        area.createTable(db);
        areaPoint.createTable(db);

        beaconConfig.createTable(db);
        //regionConfig.createTable(db);
        //regionConfigVertices.createTable(db);
        roomConfigNeighbors.createTable(db);
        roomConfig.createTable(db);
        floorConfig.createTable(db);
        floorConfigVertices.createTable(db);
        algorithmConfigs.createTable(db);
        sites.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        map.dropTable(db);
        map.dropTable(db);
        mapTiles.dropTable(db);
        mapMetadata.dropTable(db);

        area.dropTable(db);
        areaPoint.dropTable(db);

        beaconConfig.dropTable(db);
        //regionConfig.dropTable(db);
        //regionConfigVertices.dropTable(db);
        roomConfigNeighbors.dropTable(db);
        roomConfig.dropTable(db);
        floorConfig.dropTable(db);
        floorConfigVertices.dropTable(db);
        algorithmConfigs.dropTable(db);
        sites.dropTable(db);

        onCreate(db);
    }


    // Getting Count
    public int getRecordsCount(DatabaseManager databaseManager, String TABLE_NAME) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int total = cursor.getCount();
        cursor.close();

        return total;
    }

    public int getSiteId() {
        return siteId;
    }


    public String getApplianceIP() {
        return ApplianceIP;
    }
}