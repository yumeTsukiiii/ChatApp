package com.yumetsuki.chatapp.utils;

import android.text.Editable;

import java.util.Arrays;

public class StringUtils {

    public static boolean isNullOrEmpty(Editable text) {
        return text == null || text.toString().isEmpty();
    }

    public static boolean isExistNullOrEmpty(Editable...strings) {
        return Arrays.stream(strings)
                .anyMatch(string -> string == null || string.toString().isEmpty());
    }

}
