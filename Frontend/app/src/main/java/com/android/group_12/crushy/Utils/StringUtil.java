package com.android.group_12.crushy.Utils;

import android.text.TextUtils;

public class StringUtil {

    public static String replaceNullOrEmptyString(String stringToCheck) {
        if (stringToCheck == null || TextUtils.isEmpty(stringToCheck)) {
            return "N/A";
        }
        return stringToCheck;
    }
}
