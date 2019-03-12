package com.hungth.cotoan.utils.common;

import java.util.ArrayList;
import java.util.List;

public class MethodUtils {

    public static <T> List<T> reverse(List<T> list) {
        List reversed = new ArrayList();
        for (int i = list.size() - 1; i >= 0; i--) {
            T t = list.get(i);
            reversed.add(t);
        }
        return reversed;
    }
}
