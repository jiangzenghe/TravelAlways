package com.imyuu.travel.util;

import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomTileProvider
    implements TileProvider
{

    public static final int BUFFER_SIZE = 16384;
    private static final int TILE_HEIGHT = 256;
    private static final int TILE_WIDTH = 256;
    private String tilePath;

    public CustomTileProvider(String s)
    {
        tilePath = s;
    }

    private String getTileFilename(int i, int j, int k)
    {
        return (new StringBuilder(String.valueOf(tilePath))).append(k).append('/').append(i).append('_').append(j).append(".png").toString();
    }

    private byte[] readTileImage(int x, int y, int zoom) {
        InputStream in = null;
        ByteArrayOutputStream buffer = null;
 
        File f = new File(getTileFilename(x, y, zoom));
        if(f.exists()){
            try {
                buffer = new ByteArrayOutputStream();
                in = new FileInputStream(f);
                
                int nRead;
                byte[] data = new byte[BUFFER_SIZE];
    
                while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
 
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            } finally {
                if (in != null) try { in.close(); } catch (Exception ignored) {}
                if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
            }
        }else{
            return null;
        }
    }

    public final Tile getTile(int x, int y, int zoom)
    {
        byte abyte0[] = readTileImage(x, y, zoom);
        if (abyte0 == null)
        {
            return null;
        } 
        else
        {
            return new Tile(256, 256, abyte0);
        }
    }

    public int getTileHeight()
    {
        return 256;
    }

    public int getTileWidth()
    {
        return 256;
    }
}
