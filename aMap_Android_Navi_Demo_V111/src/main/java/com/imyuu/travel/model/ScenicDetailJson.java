package com.imyuu.travel.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ScenicDetailJson extends ScenicAreaJson {

    @Expose
    private List<Recommend> recommendScenicList;
    @Expose
    private List<Recommend> souvenirList; // ---����Ʒ�б�
    private List<Recommend> gameList;

    @Override
    public String toString() {
        return super.toString() + "ScenicDetailJson [mapSize="
                + ", recommendScenicList=" + recommendScenicList
                + ", souvenirList=" + souvenirList + ", gameList=" + gameList
                + "]";
    }


    public List<Recommend> getRecommendScenicList() {
        return recommendScenicList;
    }

    public void setRecommendScenicList(List<Recommend> recommendScenicList) {
        this.recommendScenicList = recommendScenicList;
    }

    public List<Recommend> getSouvenirList() {
        return souvenirList;
    }

    public void setSouvenirList(List<Recommend> souvenirList) {
        this.souvenirList = souvenirList;
    }

    public List<Recommend> getGameList() {
        return gameList;
    }

    public void setGameList(List<Recommend> gameList) {
        this.gameList = gameList;
    }
}