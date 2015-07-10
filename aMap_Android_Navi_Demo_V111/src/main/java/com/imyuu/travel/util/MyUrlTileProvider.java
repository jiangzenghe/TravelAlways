package com.imyuu.travel.util;

import android.util.Log;

import com.amap.api.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class MyUrlTileProvider extends UrlTileProvider {

    private String scenicId;

    public MyUrlTileProvider(int tileWidth, int tileHeight) {
        super(tileWidth, tileHeight);
    }

    public MyUrlTileProvider(int tileWidth, int tileHeight, String scenicId) {
        super(tileWidth, tileHeight);
        this.scenicId = scenicId;
    }

    @Override
    public URL getTileUrl(int x, int y, int zoom) {

        //String URL_TILED_IMAGE ="http://imyuu.com/tile/"+scenicId+"/";
        String URL_TILED_IMAGE = Config.getMapImageURL(scenicId);
        String url = String.format(URL_TILED_IMAGE, zoom, x, y);
        Log.d("MyPointTiledLayer", url);
        try {
            return new URL(url + ".png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("MyPointTiledLayer", e.getLocalizedMessage());
        }
        return null;
    }

}
