package com.hungth.cotoan.utils.common;

import android.os.Environment;

import static com.example.ducvu212.demomvvm.data.source.remote.Download.IMAGE_DIRECTORY;
import static com.example.ducvu212.demomvvm.data.source.remote.Download.IMAGE_FILE_EXTENSION;

/**
 * Created by framgia on 27/04/2017.
 */

public final class StringUtils {

    private StringUtils() {
        // No-op
    }

    public static boolean isBlank(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isNotBlank(String input) {
        return !isBlank(input);
    }

    public static int convertStringToNumber(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    public static String buildPath(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .append(IMAGE_DIRECTORY)
                .append(name)
                .append(IMAGE_FILE_EXTENSION);
        return builder.toString();
    }
    public static String buildPathCropped(String name) {
        StringBuilder builder = new StringBuilder();
        name = name.concat("_crop");
        builder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .append(IMAGE_DIRECTORY)
                .append(name)
                .append(IMAGE_FILE_EXTENSION);
        return builder.toString();
    }
}
