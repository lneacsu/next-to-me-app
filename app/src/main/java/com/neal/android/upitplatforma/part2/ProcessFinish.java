package com.neal.android.upitplatforma.part2;

import com.neal.android.upitplatforma.Place;

import java.util.List;

public interface ProcessFinish {
        void processFinish(List<Place> output);
        void processFinishNextPage(String output);

}
