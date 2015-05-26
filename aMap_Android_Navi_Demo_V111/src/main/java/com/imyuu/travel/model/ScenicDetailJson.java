package com.imyuu.travel.model;

import java.util.List;

public class ScenicDetailJson extends ScenicAreaJson {
	@Override
	public String toString() {
		return super.toString()+"ScenicDetailJson [mapSize=" + mapSize
				+ ", recommendScenicList=" + recommendScenicList
				+ ", souvenirList=" + souvenirList + ", gameList=" + gameList
				+ "]";
	}

	private String mapSize;
	private List<Recommend> recommendScenicList;
	private List<Recommend> souvenirList; // ---����Ʒ�б�
	private List<Recommend> gameList;

	public String getMapSize() {
		return mapSize;
	}

	public void setMapSize(String mapSize) {
		this.mapSize = mapSize;
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