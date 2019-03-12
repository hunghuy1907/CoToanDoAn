package com.hungth.cotoan.utils.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CuD HniM on 18/10/05.
 */
public class DisplayUtils {

    public static void makeToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
