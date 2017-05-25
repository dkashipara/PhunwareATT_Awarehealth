package com.awarepoint.androidaccuracytest.iBeaconMask;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jlubawy on 7/5/2016.
 */
public class Beacon {
    public static final String TAG = "Beacon";

    public static final int MFG_ID_APPLE = 0x004C;
    public static final int MFG_ID_AWAREPOINT = 0x023C;

    public static final String OUI_AWAREPOINT = "00:14:EB";

    public static final String TEST_ADDRESS = OUI_AWAREPOINT + ":FF:FF:FF";
    public static final int TEST_RSSI = 0;
    public static final long TEST_TIMESTAMP_NANOS = 0;

    /* Flags */
    public static final int ADV_DATA_TYPE_FLAGS = 0x01;
    /* Service */
    public static final int ADV_DATA_TYPE_MORE_16BIT_SERVICE_UUIDS_AVAILABLE = 0x02;
    public static final int ADV_DATA_TYPE_COMPLETE_LIST_OF_16BIT_SERVICE_UUIDS = 0x03;
    public static final int ADV_DATA_TYPE_MORE_32BIT_SERVICE_UUIDS_AVAILABLE = 0x04;
    public static final int ADV_DATA_TYPE_COMPLETE_LIST_OF_32BIT_SERVICE_UUIDS = 0x05;
    public static final int ADV_DATA_TYPE_MORE_128BIT_SERVICE_UUIDS_AVAILABLE = 0x06;
    public static final int ADV_DATA_TYPE_COMPLETE_LIST_OF_128BIT_SERVICE_UUIDS = 0x07;
    /* Local Name */
    public static final int ADV_DATA_TYPE_SHORTENED_LOCAL_NAME = 0x08;
    public static final int ADV_DATA_TYPE_COMPLETE_LOCAL_NAME = 0x09;
    /* TX Power Level */
    public static final int ADV_DATA_TYPE_TX_POWER_LEVEL = 0x0A;
    /* Simple Pairing Optional OOB Tags */
    public static final int ADV_DATA_TYPE_CLASS_OF_DEVICE = 0x0D;
    public static final int ADV_DATA_TYPE_SIMPLE_PAIRING_HASH_C = 0x0E;
    public static final int ADV_DATA_TYPE_SIMPLE_PAIRING_RANDOMIZER_R = 0x0F;
    /* Security Manager TK Value */
    public static final int ADV_DATA_TYPE_SECURITY_MANAGER_TX_VALUE = 0x10;
    /* Security Manager OOB Flags */
    public static final int ADV_DATA_TYPE_SECURITY_MANAGER_OOB_FLAGS = 0x11;
    /* Slave Connection Interval Range */
    public static final int ADV_DATA_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE = 0x12;
    /* Service Solicitation */
    public static final int ADV_DATA_TYPE_LIST_OF_16BIT_SERVICE_UUIDS = 0x14;
    public static final int ADV_DATA_TYPE_LIST_OF_128BIT_SERVICE_UUIDS = 0x15;
    /* Service Data */
    public static final int ADV_DATA_TYPE_SERVICE_DATA = 0x16;
    /* Manufacturer Specific Data */
    public static final int ADV_DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xFF;

    protected String address;
    protected int rssi;
    protected List<AdvertisementData> advDataList;
    protected byte[] advData;
    protected long timestampNanos;

    private boolean isExpired = false;


    public static Beacon parse(String address, byte[] advData) {
        return parse(address, TEST_RSSI, advData, TEST_TIMESTAMP_NANOS);
    }

    public static Beacon parse(byte[] advData) {
        return parse(TEST_ADDRESS, advData);
    }

    public static Beacon parse(String address, int rssi, byte[] scanRecord, long timestampNanos) {
        List<AdvertisementData> advDataList = new ArrayList<>();
        int currPos = 0;

        try {
            while (currPos < scanRecord.length) {
                final int length = scanRecord[currPos++] & 0xFF;
                if (length == 0) {
                    break;
                }

                final int dataLength = length - 1;
                final int type = scanRecord[currPos++] & 0xFF;

                final byte[] data = Arrays.copyOfRange(scanRecord, currPos, currPos + dataLength);
                advDataList.add(new AdvertisementData(type, length, data));

                currPos += dataLength;
            }

            if (advDataList.isEmpty()) {
                advDataList = null;
            }
        } catch (Exception e) {
            Log.e(TAG, String.format("error parsing advertisement data: address=%s, currPos=%d", address, currPos));
            advDataList = null;
        }

        byte[] advData;

        try {
            if (advDataList != null) {
                // If the advertisement data was successfully parsed then concatenate it (strips zeros from the end)

                advData = AdvertisementData.getBytes(advDataList);

                final AdvertisementData mfgData = findAdvDataType(advDataList, ADV_DATA_TYPE_MANUFACTURER_SPECIFIC_DATA);

                if (mfgData != null && mfgData.length >= 4) {
                    // If there is manufacturer specific data it is either an Awarepoint beacon, iBeacon,
                    // or an AltBeacon.

                    final int[] mfgDataInts = Hex.toInts(mfgData.getData());
                    final int companyId = (mfgDataInts[1] << 8) | mfgDataInts[0];

                    switch (companyId) {
                        case MFG_ID_AWAREPOINT: {
                            if (mfgDataInts[3] == AwpLocationBeacon.TYPE) {
                                return new AwpLocationBeacon(address, rssi, advDataList, advData, timestampNanos);
                            } else if (mfgDataInts[3] == AwpSiteIDBeacon.TYPE) {
                                return new AwpSiteIDBeacon(address, rssi, advDataList, advData, timestampNanos);
                            } else if (mfgDataInts[3] == AwpEnvTagSampleBeacon.TYPE) {
                                return new AwpEnvTagSampleBeacon(address, rssi, advDataList, advData, timestampNanos);
                            } else {
                                Log.d(TAG, "Found unsupported AWP Beacon: " + mfgData.toString());
                            }
                        }
                        break;

                        case MFG_ID_APPLE: {
                            final int appleBeaconType = (mfgDataInts[3] << 8) | mfgDataInts[2];
                            if (appleBeaconType == IBeacon.TYPE) {
                                final String oui = address.substring(0, 8);
                                if (oui.equals(OUI_AWAREPOINT)) {
                                    // If an Awarepoint OUI then this is an Awarepoint iBeacon
                                    return new AwpIBeacon(address, rssi, advDataList, advData, timestampNanos);
                                } else {
                                    // Else non-Awarepoint iBeacon
                                    return new IBeacon(address, rssi, advDataList, advData, timestampNanos);
                                }
                            }
                        }
                        break;

                        default: {
                            // unsupported company ID, fallthrough
                        }
                        break;
                    }
                } else {
                    // Else this could possibly be an Eddystone beacon, need to check service UUIDs
                }
            } else {
                // Else we couldn't parse the advertisement data then return the raw data
                advData = scanRecord;
            }
        } catch (Exception e) {
            advData = scanRecord;
            Log.e(TAG, String.format("error creating beacon: address=%s, advData=%s", address, Hex.encode(advData)));
            // TODO: Create an "ExceptionBeacon"
        }

        // Unknown beacon type, return vanilla beacon
        return new Beacon(address, rssi, advDataList, advData, timestampNanos);
    }

    public Beacon(String address, byte[] advData) {
        this(address, TEST_RSSI, null, advData, TEST_TIMESTAMP_NANOS);
    }

    public Beacon(byte[] advData) {
        this(TEST_ADDRESS, advData);
    }

    public Beacon(String address, int rssi, List<AdvertisementData> advDataList, byte[] advData, long timestampNanos) {
        this.address = address;
        this.rssi = rssi;
        this.advDataList = advDataList;
        this.advData = advData;
        this.timestampNanos = timestampNanos;
    }

    public String getAddress() {
        return address;
    }

    public int getRssi() {
        return rssi;
    }

    public List<AdvertisementData> getAdvDataList() {
        return advDataList;
    }

    public String getAdvDataHexString() {
        return Hex.encode(advData);
    }

    public long getTimestampNanos() {
        return timestampNanos;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    @Override
    public String toString() {
        return "Beacon{" +
                "address='" + address + '\'' +
                ", rssi=" + rssi +
                ", advData=" + getAdvDataHexString() +
                ", timestampNanos=" + timestampNanos +
                ", isExpired=" + isExpired +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beacon beacon = (Beacon) o;

        if (rssi != beacon.rssi) return false;
        if (timestampNanos != beacon.timestampNanos) return false;
        if (isExpired != beacon.isExpired) return false;
        if (address != null ? !address.equals(beacon.address) : beacon.address != null)
            return false;
        if (advDataList != null ? !advDataList.equals(beacon.advDataList) : beacon.advDataList != null)
            return false;
        return Arrays.equals(advData, beacon.advData);

    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + rssi;
        result = 31 * result + (advDataList != null ? advDataList.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(advData);
        result = 31 * result + (int) (timestampNanos ^ (timestampNanos >>> 32));
        result = 31 * result + (isExpired ? 1 : 0);
        return result;
    }

    public static AdvertisementData findAdvDataType(List<AdvertisementData> advDataList, int type) {
        for (AdvertisementData advData : advDataList) {
            if (advData.getType() == type) {
                return advData;
            }
        }
        return null;
    }

    public static class AdvertisementData {
        private final int type;
        private final int length;
        private final byte[] data;

        public AdvertisementData(int type, int length, byte[] data) {
            this.type = type;
            this.length = length;
            this.data = data;
        }

        public int getType() {
            return type;
        }

        public int getLength() {
            return length;
        }

        public byte[] getData() {
            return data;
        }

        public static byte[] getBytes(List<AdvertisementData> advDataList) {
            int advDataLength = 0;
            for (AdvertisementData ad : advDataList) {
                advDataLength += 1 + ad.getLength();
            }
            byte[] advData = new byte[advDataLength];

            int i = 0;
            for (AdvertisementData ad : advDataList) {
                final byte[] data = ad.getData();
                final int dataLength = data.length;

                advData[i++] = (byte) (dataLength + 1);
                advData[i++] = (byte) ad.getType();

                for (int j = 0; j < dataLength; j++) {
                    advData[i++] = data[j];
                }
            }

            return advData;
        }

        @Override
        public String toString() {
            return String.format("AdvertisementData{ type=%02X, length=%d, data=%s }", type, length, Hex.encode(data));
        }
    }
}
