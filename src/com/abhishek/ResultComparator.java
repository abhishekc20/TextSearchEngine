package com.abhishek;

import java.util.Comparator;

public class ResultComparator implements Comparator<Result> {

    @Override
    public int compare(Result a, Result b) {
        double comp = b.getAccuracy() - a.getAccuracy();

        if (comp < 0) {
            return -1;
        } else if (comp > 0) {
            return 1;
        } else return 0;
    }
}
