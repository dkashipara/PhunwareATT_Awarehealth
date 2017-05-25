package com.awarepoint.androidaccuracytest.iBeaconMask;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jlubawy on 7/13/2016.
 */
public class IBeacon extends Beacon {
    public static final int TYPE = 0x1502;

    protected UUID proximityUuid;
    protected int major;
    protected int minor;
    protected byte measuredPower;

    public IBeacon(String address, int rssi, List<AdvertisementData> advDataList, byte[] advData, long timestamp) {
        super(address, rssi, advDataList, advData, timestamp);

        AdvertisementData mfgData = Beacon.findAdvDataType(advDataList, Beacon.ADV_DATA_TYPE_MANUFACTURER_SPECIFIC_DATA);
        byte[] data = mfgData.getData();

        this.proximityUuid = UUID.fromBytes(Arrays.copyOfRange(mfgData.getData(), 4, 20));
        this.major = ((data[20] & 0xFF) << 8) | (data[21] & 0xFF);
        this.minor = ((data[22] & 0xFF) << 8) | (data[23] & 0xFF);
        this.measuredPower = data[24];
    }

    public IBeacon(String address, byte[] advData, UUID proximityUuid, int major, int minor, byte measuredPower) {
        super(address, advData);
        this.proximityUuid = proximityUuid;
        this.major = major;
        this.minor = minor;
        this.measuredPower = measuredPower;
    }

    public IBeacon(byte[] advData, UUID proximityUuid, int major, int minor, byte measuredPower) {
        super(advData);
        this.proximityUuid = proximityUuid;
        this.major = major;
        this.minor = minor;
        this.measuredPower = measuredPower;
    }

    @Override
    public String toString() {
        return "IBeacon{" +
                "proximityUuid=" + proximityUuid +
                ", major=" + major +
                ", minor=" + minor +
                ", measuredPower=" + measuredPower +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IBeacon that = (IBeacon) o;

        if (major != that.major) return false;
        if (minor != that.minor) return false;
        if (measuredPower != that.measuredPower) return false;
        return proximityUuid != null ? proximityUuid.equals(that.proximityUuid) : that.proximityUuid == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (proximityUuid != null ? proximityUuid.hashCode() : 0);
        result = 31 * result + major;
        result = 31 * result + minor;
        result = 31 * result + (int) measuredPower;
        return result;
    }

    public byte getMeasuredPower() {
        return measuredPower;
    }

    public UUID getProximityUuid() {
        return proximityUuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public static class UUID {
        private final byte[] bytes;

        public static UUID fromBytes(byte[] bytes) {
            return new UUID(bytes);
        }

        public static UUID fromString(String s) {
            s = s.replaceAll("-", "");
            if (s.length() != 32) {
                throw new IllegalArgumentException();
            }
            return fromBytes(Hex.decode(s));
        }

        private UUID(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UUID uuid = (UUID) o;

            return Arrays.equals(bytes, uuid.bytes);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);
        }

        @Override
        public String toString() {
            final String hexStr = Hex.encode(bytes);
            return hexStr.substring(0, 8) + "-"
                    + hexStr.substring(8, 12) + "-"
                    + hexStr.substring(12, 16) + "-"
                    + hexStr.substring(16, 20) + "-"
                    + hexStr.substring(20, hexStr.length());
        }
    }
}
