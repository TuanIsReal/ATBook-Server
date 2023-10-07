package com.tuanisreal.utils;

import org.apache.commons.codec.binary.Base64;

public class Util {

    public static String byteToString(byte[] b) {
        int n = b.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append((char) b[i]);
        }
        return builder.toString();
    }

    public static String encodeBase64(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    public static String encodeSafeUrlBase64(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }
}
