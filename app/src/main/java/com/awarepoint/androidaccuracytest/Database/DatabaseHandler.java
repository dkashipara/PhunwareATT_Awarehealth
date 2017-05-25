package com.awarepoint.androidaccuracytest.Database;

import com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas.AreaHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Areas.AreaPointHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.AlgorithmConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.BeaconConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.FloorConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.FloorConfigVerticesHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.RoomConfigHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.LocationEngine.RoomConfigNeighborsHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps.MapHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps.MapMetadataHandler;
import com.awarepoint.androidaccuracytest.Database.TablesHandler.Maps.MapTilesHandler;

/**
 * Created by ureyes on 3/29/2016.
 */
public class DatabaseHandler {

    public MapHandler mapHandler;
    public MapMetadataHandler mapMetadataHandler;
    public MapTilesHandler mapTilesHandler;

    public AreaHandler areaHandler;
    public AreaPointHandler areaPointHandler;

    public BeaconConfigHandler beaconConfigHandler;
    //  public RegionConfigHandler regionConfigHandler;
    //  public RegionConfigVerticesHandler regionConfigVerticesHandler;
    public RoomConfigHandler roomConfigHandler;
    public RoomConfigNeighborsHandler roomConfigNeighborsHandler;

    public FloorConfigHandler floorConfigHandler;
    public FloorConfigVerticesHandler floorConfigVerticesHandler;
    public AlgorithmConfigHandler algoConfHandler;


    public DatabaseHandler(DatabaseManager databaseManager) {
        mapHandler = new MapHandler(databaseManager);
        mapMetadataHandler = new MapMetadataHandler(databaseManager);
        mapTilesHandler = new MapTilesHandler(databaseManager);

        areaHandler = new AreaHandler(databaseManager);
        areaPointHandler = new AreaPointHandler(databaseManager);

        beaconConfigHandler = new BeaconConfigHandler(databaseManager);
        //    regionConfigHandler = new RegionConfigHandler(databaseManager);
        //    regionConfigVerticesHandler = new RegionConfigVerticesHandler(databaseManager);
        roomConfigHandler = new RoomConfigHandler(databaseManager);
        roomConfigNeighborsHandler = new RoomConfigNeighborsHandler(databaseManager);

        floorConfigHandler = new FloorConfigHandler(databaseManager);
        floorConfigVerticesHandler = new FloorConfigVerticesHandler(databaseManager);
        algoConfHandler = new AlgorithmConfigHandler(databaseManager);

    }


    public void cleanDatabaseMaps() {
        mapHandler.clearTableMap();
        mapMetadataHandler.clearTableMapMetadata();
        mapTilesHandler.clearTableMapTiles();
    }

    public void cleanDatabaseBeacons() {
        beaconConfigHandler.clearTableBeacons();
    }


    public void cleanDatabaseData() {
        areaHandler.clearTableArea();
        areaPointHandler.clearTableAreaPoint();

        //  regionConfigHandler.clearTableRegions();
        //  regionConfigVerticesHandler.clearTableRegionsVertices();
        roomConfigHandler.clearTableRooms();
        roomConfigNeighborsHandler.clearTableRoomNeighbors();
        floorConfigHandler.clearTableFloor();
        floorConfigVerticesHandler.clearTableFloorsVertices();
        algoConfHandler.clearTableAlgoConf();
    }

}
