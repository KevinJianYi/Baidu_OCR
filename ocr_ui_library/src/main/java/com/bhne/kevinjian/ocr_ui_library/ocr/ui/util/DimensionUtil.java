/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.bhne.kevinjian.ocr_ui_library.ocr.ui.util;

import android.content.res.Resources;

public class DimensionUtil {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
