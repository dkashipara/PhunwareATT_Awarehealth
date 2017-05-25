package com.awarepoint.androidaccuracytest.iBeaconMask;

import java.util.List;

/**
 * Created by jlubawy on 7/7/2016.
 */
public class AwpEnvTagSampleBeacon extends Beacon {
    public final static int TYPE = 0x03;

    public static final int SAMPLE_TYPE_NONE = 0x00;
    public static final int SAMPLE_TYPE_TEMPERATURE_ABSOLUTE = 0x01;
    public static final int SAMPLE_TYPE_HUMIDITY = 0x02;
    public static final int SAMPLE_TYPE_TEMPERATURE_OFFSET = 0x03;
    public static final int SAMPLE_TYPE_UNKNOWN = 0xFF;

    private byte measuredPower;
    private int sampleType;
    private long sample;
    private int sampleInterval;

    public AwpEnvTagSampleBeacon(String address, int rssi, List<AdvertisementData> advDataList, byte[] advData, long timestamp) {
        super(address, rssi, advDataList, advData, timestamp);

        final AdvertisementData mfgData = Beacon.findAdvDataType(advDataList, Beacon.ADV_DATA_TYPE_MANUFACTURER_SPECIFIC_DATA);
        final int[] mfgDataInts = Hex.toInts(mfgData.getData());

        this.measuredPower = (byte) mfgDataInts[4];
        this.sampleType = mfgDataInts[5];
        this.sample = (mfgDataInts[6] << 24) | (mfgDataInts[7] << 16) | (mfgDataInts[8] << 8) | mfgDataInts[9];
        this.sampleInterval = (mfgDataInts[10] << 8) | mfgDataInts[11];
    }

    public AwpEnvTagSampleBeacon(byte[] advData, byte measuredPower, int sampleType, long sample, int sampleInterval) {
        super(advData);
        this.measuredPower = measuredPower;
        this.sampleType = sampleType;
        this.sample = sample;
        this.sampleInterval = sampleInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AwpEnvTagSampleBeacon that = (AwpEnvTagSampleBeacon) o;

        if (measuredPower != that.measuredPower) return false;
        if (sampleType != that.sampleType) return false;
        if (sample != that.sample) return false;
        return sampleInterval == that.sampleInterval;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) measuredPower;
        result = 31 * result + sampleType;
        result = 31 * result + (int) (sample ^ (sample >>> 32));
        result = 31 * result + sampleInterval;
        return result;
    }

    @Override
    public String toString() {
        return "AwpEnvTagSampleBeacon{" +
                "measuredPower=" + measuredPower +
                ", sampleType=" + sampleType +
                ", sample=" + sample +
                ", sampleInterval=" + sampleInterval +
                '}';
    }

    public byte getMeasuredPower() {
        return measuredPower;
    }

    public int getSampleType() {
        return sampleType;
    }

    public long getSample() {
        return sample;
    }

    public int getSampleInterval() {
        return sampleInterval;
    }
}
