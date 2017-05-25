package com.awarepoint.androidaccuracytest.iBeaconMask;

/**
 * Created by jlubawy on 11/14/2016.
 */

public class Hex {
    public static String encode(final byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        final int[] ints = toInts(data);
        StringBuilder sb = new StringBuilder();
        for (int i : ints) {
            sb.append(String.format("%02X", i));
        }
        return sb.toString();

    }

    public static byte[] decode(final String s) {
        // Remove spaces first
        final String hexString = s.replaceAll(" ", "");

        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }

        byte[] data = new byte[hexString.length() / 2];

        int i = 0;
        int j = 0;
        while (i < hexString.length()) {
            data[j] = Integer.valueOf(hexString.substring(i, i + 2), 16).byteValue();
            i += 2;
            j += 1;
        }

        return data;
    }

    public static int[] toInts(final byte[] data) {
        int[] ints = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            ints[i] = data[i] & 0xFF;
        }
        return ints;
    }
}
