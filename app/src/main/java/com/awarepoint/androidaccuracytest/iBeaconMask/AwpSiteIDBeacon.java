package com.awarepoint.androidaccuracytest.iBeaconMask;

import java.util.List;

/**
 * Created by jlubawy on 7/7/2016.
 */
public class AwpSiteIDBeacon extends Beacon {
    public final static int TYPE = 0x01;

    private byte measuredPower;
    private int siteId;
    private String ipAddress;

    public AwpSiteIDBeacon(String address, int rssi, List<AdvertisementData> advDataList, byte[] advData, long timestamp) {
        super(address, rssi, advDataList, advData, timestamp);

        final AdvertisementData mfgData = Beacon.findAdvDataType(advDataList, Beacon.ADV_DATA_TYPE_MANUFACTURER_SPECIFIC_DATA);
        final int[] mfgDataInts = Hex.toInts(mfgData.getData());

        this.measuredPower = (byte) mfgDataInts[4];
        this.siteId = (mfgDataInts[5] << 24) | (mfgDataInts[6] << 16) | (mfgDataInts[7] << 8) | mfgDataInts[8];
        this.ipAddress = String.format("%d.%d.%d.%d", mfgDataInts[9], mfgDataInts[10], mfgDataInts[11], mfgDataInts[12]);
    }

    public AwpSiteIDBeacon(byte[] advData, byte measuredPower, int siteId, String ipAddress) {
        super(advData);
        this.measuredPower = measuredPower;
        this.siteId = siteId;
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "AwpSiteIDBeacon{" +
                "measuredPower=" + measuredPower +
                ", siteId=" + siteId +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwpSiteIDBeacon that = (AwpSiteIDBeacon) o;

        if (measuredPower != that.measuredPower) return false;
        if (siteId != that.siteId) return false;
        return ipAddress != null ? ipAddress.equals(that.ipAddress) : that.ipAddress == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) measuredPower;
        result = 31 * result + siteId;
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        return result;
    }

    public byte getMeasuredPower() {
        return measuredPower;
    }

    public int getSiteId() {
        return siteId;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
