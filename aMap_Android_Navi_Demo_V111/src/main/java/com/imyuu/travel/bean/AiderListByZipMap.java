package com.imyuu.travel.bean;

import java.util.ArrayList;
import java.util.List;

public class AiderListByZipMap {

    private List aiderByZipMaps;

    public AiderListByZipMap() {
    }

    public List getAiderByZipMaps() {
        if (aiderByZipMaps == null) {
            aiderByZipMaps = new ArrayList();
        }
        return aiderByZipMaps;
    }

    public void setAiderByZipMaps(List list) {
        aiderByZipMaps = list;
    }
}
