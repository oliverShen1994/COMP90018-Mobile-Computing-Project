package com.android.group_12.crushy.Utils;

// Java program to check if an email address
// is valid using Regex.

import java.util.regex.Pattern;

/**
 * Acknowledgement: https://www.geeksforgeeks.org/check-email-address-valid-not-java/
 */
public class EmailValidationUtil {
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}



