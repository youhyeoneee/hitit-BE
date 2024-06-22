package com.pda.utils.api_utils;

public class CustomNumberUtils {
    public static long roundToNearestHundred(long value) {
        return Math.round(value / 100) * 100;
    }
}
