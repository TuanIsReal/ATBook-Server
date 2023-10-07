package com.tuanisreal.utils;

import java.util.Objects;

public class StringUtil {

    public static boolean validateString(String arg) {
        return Objects.nonNull(arg) && !arg.trim().isEmpty();
    }

    public static boolean validateStrings(String... args) {
        for (String arg : args) {
            if (!validateString(arg)) {
                return false;
            }
        }
        return true;
    }
}
