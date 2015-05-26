package com.imyuu.travel.util;

import com.imyuu.travel.model.ScenicPointJson;

import java.util.Comparator;

public class SortDataByDistance
    implements Comparator
{

    public SortDataByDistance()
    {
    }

    public int compare(Object obj, Object obj1)
    {
        ScenicPointJson spot = (ScenicPointJson)obj;
        ScenicPointJson spot1 = (ScenicPointJson)obj1;
        if (spot.getDistance() >= spot1.getDistance() )
        {
            return 1;
        }
        return -1;
    }
}