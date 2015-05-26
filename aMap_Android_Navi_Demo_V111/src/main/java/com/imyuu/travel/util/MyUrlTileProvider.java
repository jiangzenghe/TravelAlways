package com.imyuu.travel.util;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.amap.api.maps.model.UrlTileProvider;
public class MyUrlTileProvider extends UrlTileProvider{

	 public MyUrlTileProvider(int tileWidth,int tileHeight) {
	        super(tileWidth,tileHeight);
	 }
	
	public static final String URL_TILED_IMAGE ="http://imyuu.com/tile/22/";
      @Override
       public URL getTileUrl(int x, int y, int zoom) {
//                if (zoom < 9 || zoom > 15) {
//                    return null;
//                }
                String url = String.format(URL_TILED_IMAGE + "%d/%d_%d", zoom, x, y);
                Log.d("MyPointTiledLayer",url);
                try {
                    return new URL(url+".png");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("MyPointTiledLayer",e.getLocalizedMessage());
                }
                return null;
        }
     
//        if (tileProvider != null) {
//                tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
//                                .tileProvider(tileProvider)
//                                .diskCacheDir("/storage/amap/cache").diskCacheEnabled(true)
//                                .diskCacheSize(100));
//
//        }


}
