package com.hungth.cotoan.data.model;

import android.support.annotation.IntDef;

import static com.hungth.cotoan.data.model.CalculatorType.INVISIBLE;
import static com.hungth.cotoan.data.model.CalculatorType.VISIBLE;

@IntDef({VISIBLE, INVISIBLE})
public @interface CalculatorType {
    int VISIBLE = 0;
    int INVISIBLE = 1;
}
