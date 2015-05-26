package com.imyuu.travel.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;
import java.io.PrintStream;

public class CustomTileOnlineProvider    implements TileProvider
{

    public static final int BUFFER_SIZE = 16384;
    private static final int DEFAULT_SRC_WIDTH = 1024;
    private static final int NUMS_OF_ORIGNAL = 4;
    private static final int QUALITY = 50;
    private static final int TILE_HEIGHT = 256;
    private static final int TILE_WIDTH = 256;
    private Bitmap bitmap;
    private int blockCoordinateX;
    private int blockCoordinateY;
    private String fileDirectory;
    private float scale;
    private int srcHeight;
    private int srcWidth;
    private String tilePath;

    public CustomTileOnlineProvider(int i, int j, int k, int l, String s)
    {
        srcWidth = i;
        srcHeight = j;
        fileDirectory = s;
        blockCoordinateX = k;
        blockCoordinateY = l;
    }

    private Bitmap cropBitmap(Bitmap bitmap1, int i, int j, int k, int l)
    {
        return Bitmap.createBitmap(bitmap1, i, j, k, l, null, true);
    }

    private String get18FileName(int i, int j)
    {
        return (new StringBuilder(String.valueOf(fileDirectory))).append("src/").append(i).append('_').append(j).append(".png").toString();
    }

    private byte[] getTileBytes(String s)
    {
    	//65536
         Bitmap bitmap1 = BitmapUtils.getAutoSizeBitmap(0x10000, s);
        byte abyte0[] = BitmapUtils.convertBitmapToBytes(bitmap1);
        BitmapUtils.releaseBitmap(bitmap1);
        return abyte0;
    }

    private String getTileFilename(int i, int j, int k)
    {
        return (new StringBuilder(String.valueOf(fileDirectory))).append(k).append('/').append(i).append('_').append(j).append(".png").toString();
    }

    private int fixYCoordinate(int y, int zoom) {
        int size = 1 << zoom; // size = 2^zoom
        return size - 1 - y;
    }
    
    private Tile getTileFormFile(int x, int y, int zoom)
    {   
    	y = fixYCoordinate(y, zoom);
    	
    	
    	System.out.println((new StringBuilder("x=")).append(x).append(",y=").append(y).append(",zoom=").append(zoom).toString());
        double d = Math.pow(2D, 18 - zoom);
       
        String s = getTileFilename(x, y, zoom);
        if (FileUtils.isExist(s)) {
	         System.out.println("file exist");
	         Tile tile = new Tile(256, 256, getTileBytes(s));
	         return tile;
        }
        
        
         int level = (int)(256D * d);
         int i1 = (int)(d * (double)x);
         int j1 = (int)(d * (double)y);
        
         int x_scope = srcWidth / 256;
         int y_scope = srcHeight / 256;
         if (i1 >= blockCoordinateX && i1 <= x_scope + blockCoordinateX &&
         		j1 >= blockCoordinateY && j1 <= y_scope + blockCoordinateY) 
         	{
   
         System.out.println((new StringBuilder("x18=")).append(i1).append(",y18=").append(j1).toString());
          int i2 = blockCoordinateX + 4 * ((i1 - blockCoordinateX) / 4);
         int j2 = blockCoordinateY + 4 * ((j1 - blockCoordinateY) / 4);
         System.out.println((new StringBuilder("x18Start=")).append(i2).append(",y18Start=").append(j2).append(",itemWidth=").append(level).toString());
        
        int  k2 = (int)((1024D / d) * (1024D / d));
         System.out.println((new StringBuilder("sampleSize=")).append(d).append(",maxPixels=").append(k2).append(",filePath=").append(s).toString());
       
         float f = 256 * (i1 - blockCoordinateX);
         float f1 = 256 * (j1 - blockCoordinateY);
         float f2 = 256 * (i1 - blockCoordinateX);
         float f3 = 256 * (j1 - blockCoordinateY);
         
         float f4 = f2 + (float)level;
         float f5 = f3 + (float)level;
         float f6 = f2 + (float)level;
        System.out.println((new StringBuilder("x18Pixels=")).append(f).append(",y18Pixels=").append(f1).toString());

        float f7 = f % 1024F;
        float f8 = f1 % 1024F;
       
         	} 
         
         return null;
    	/*
        if (f7 + (float)level <= 1024F)
        {
        	
        
        
        int l2 = 1;
          goto _L7
_L17:
        int i3;
        System.out.println((new StringBuilder("column=")).append(l2).append(",row=").append(i3).toString());
        Bitmap bitmap1;
        int j3;
        bitmap1 = null;
        j3 = 0;
        Bitmap bitmap2;
        Canvas canvas;
        bitmap2 = Bitmap.createBitmap(256, 256, android.graphics.Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap2);
        int k3;
        int l3;
        k3 = 0;
        l3 = 0;
_L31:
        if (l3 < l2) goto _L9; else goto _L8
_L8:
        canvas.save(31);
        canvas.restore();
        if (!BitmapUtils.saveBitmap2file(bitmap2, getTileFilename(i, j, zoom), 100)) goto _L11; else goto _L10
_L10:
        byte abyte0[] = BitmapUtils.convertBitmapToBytes(bitmap2);
        System.out.println((new StringBuilder("res=")).append(bitmap2).toString());
        BitmapUtils.releaseBitmap(bitmap2);
        tile = new Tile(256, 256, abyte0);
          goto _L12
        Exception exception;
        exception;
        throw exception;
//_L6:
         	}
        if (((float)l - (1024F - f7)) % 1024F != 0.0F) goto _L14; else goto _L13
_L13:
        int j8 = (int)((float)level - (1024F - f7));
        l2 = 1 + j8 / 1024;
          goto _L7
_L14:
        if (((float)l - (1024F - f7)) / 1024F >= 0.0F) goto _L16; else goto _L15
_L15:
        l2 = 2;
          goto _L7
_L16:
        l2 = 2 + (int)((float)level - (1024F - f7)) / 1024;
          goto _L7
_L28:
        if (((float)level - (1024F - f8)) % 1024F != 0.0F)
        {
            break MISSING_BLOCK_LABEL_802;
        }
        i3 = 1 + (int)((float)level - (1024F - f8)) / 1024;
          goto _L17
        if (((float)level - (1024F - f8)) / 1024F >= 0.0F)
        {
            break MISSING_BLOCK_LABEL_825;
        }
        i3 = 2;
          goto _L17
        i3 = 2 + (int)((float)level - (1024F - f8)) / 1024;
          goto _L17
_L30:
        int j4;
        int k4;
        int l4;
        k4 = i2 + 4 * (l3 % l2);
        l4 = j2 + 4 * (j4 % i3);
        float f9;
        float f10;
        f9 = 256 * (k4 - blockCoordinateX);
        f10 = 256 * (l4 - blockCoordinateY);
        System.out.println((new StringBuilder("tempX18Pixels=")).append(f9).append(",tempY18Pixels=").append(f10).toString());
        if (l3 != 0 || j4 != 0) goto _L19; else goto _L18
_L18:
        int k5;
        int i6;
        int j6;
        System.out.println("����");
        break MISSING_BLOCK_LABEL_949;
_L24:
        int l5;
        k6 = (int)((double)i6 / d);
        l6 = (int)((double)j6 / d);
        j3 = (int)((double)k5 / d);
        i7 = (int)((double)l5 / d);
        System.out.println((new StringBuilder("tempX18=")).append(k4).append(",tempY18=").append(l4).append(",tempX=").append(k6).append(",tempY=").append(l6).append(",tempWidth=").append(j3).append(",tempHeight=").append(i7).toString());
        s1 = get18FileName(k4, l4);
        System.out.println((new StringBuilder("file18=")).append(s1).toString());
        if (!FileUtils.isExist(s1)) goto _L21; else goto _L20
_L20:
        System.out.println("��ԭͼ");
        bitmap1 = BitmapUtils.getAutoSizeBitmap(k2, s1);
_L26:
        if (bitmap1 == null) goto _L23; else goto _L22
_L22:
        if (!bitmap1.isRecycled())
        {
            Bitmap bitmap4 = cropBitmap(bitmap1, k6, l6, j3, i7);
            System.out.println((new StringBuilder("chip.width=")).append(bitmap4.getWidth()).append(",chip.height=").append(bitmap4.getHeight()).toString());
            canvas.drawBitmap(bitmap4, k3, i4, null);
            System.out.println((new StringBuilder("chipOffsetX=")).append(k3).append(",chipOffsetY=").append(i4).toString());
            BitmapUtils.releaseBitmap(bitmap4);
            BitmapUtils.releaseBitmap(bitmap1);
        }
          goto _L23
_L19:
        if (j4 != 0 || l3 <= 0)
        {
            break MISSING_BLOCK_LABEL_1340;
        }
        int i8 = l2 - 1;
        if (l3 != i8)
        {
            break MISSING_BLOCK_LABEL_1340;
        }
        System.out.println("����");
        break MISSING_BLOCK_LABEL_1311;
        if (l3 != 0 || j4 <= 0)
        {
            break MISSING_BLOCK_LABEL_1397;
        }
        int l7 = i3 - 1;
        if (j4 != l7)
        {
            break MISSING_BLOCK_LABEL_1397;
        }
        System.out.println("����");
        break MISSING_BLOCK_LABEL_1372;
        if (l3 <= 0 || j4 <= 0)
        {
            break MISSING_BLOCK_LABEL_1463;
        }
        int j7 = i3 - 1;
        if (j4 != j7)
        {
            break MISSING_BLOCK_LABEL_1463;
        }
        int k7 = l2 - 1;
        if (l3 != k7)
        {
            break MISSING_BLOCK_LABEL_1463;
        }
        System.out.println("����");
        Bitmap bitmap3;
        if ((int)(f6 - f9) % 1024 == 0)
        {
            k5 = 1024;
        } else
        {
            k5 = (int)(f6 - f9) % 1024;
        }
        break MISSING_BLOCK_LABEL_1854;
        System.out.println("�м�");
        if (l3 == 0)
        {
            i6 = (int)(f2 - f9);
            k5 = 1024 - i6;
            l5 = 1024;
            j6 = 0;
        } else
        {
            int i5 = l2 - 1;
            if (l3 == i5)
            {
                if ((int)(f4 - f9) % 1024 == 0)
                {
                    k5 = 1024;
                } else
                {
                    k5 = (int)(f4 - f9) % 1024;
                }
                l5 = 1024;
                i6 = 0;
                j6 = 0;
            } else
            if (j4 == 0)
            {
                if ((int)(f3 - f10) % 1024 == 0)
                {
                    j6 = 1024;
                } else
                {
                    j6 = (int)(f3 - f10) % 1024;
                }
                k5 = 1024;
                l5 = 1024 - j6;
                i6 = 0;
            } else
            {
                int j5 = i3 - 1;
                if (j4 == j5)
                {
                    k5 = 1024;
                    if ((int)(f5 - f10) % 1024 == 0)
                    {
                        l5 = 1024;
                    } else
                    {
                        l5 = (int)(f5 - f10) % 1024;
                    }
                    i6 = 0;
                    j6 = 0;
                } 
                else
                {
                    k5 = 1024;
                    l5 = 1024;
                    i6 = 0;
                    j6 = 0;
                }
            }
        }
          goto _L24
_L21:
        if (k4 < blockCoordinateX || k4 > k1 + blockCoordinateX || l4 < blockCoordinateY || l4 > l1 + blockCoordinateY) goto _L26; else goto _L25
_L25:
        System.out.println((new StringBuilder("本地没有原图，构造url下载原图 tempX18=")).append(k4).append(",tempY18=").append(l4).append(",zoom=").append(18).toString());
        bitmap3 = BitmapUtils.getAutoSizeBitmap(0x100000, (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/pingyao.png").toString());
        if (!BitmapUtils.saveBitmap2file(bitmap3, s1, 100))
        {
            break MISSING_BLOCK_LABEL_1662;
        }
label0:
        {
            if (18 > zoom)
            {
                break label0;
            }
            bitmap1 = bitmap3;
        }
          goto _L26
        BitmapUtils.releaseBitmap(bitmap3);
        bitmap1 = BitmapUtils.getAutoSizeBitmap(k2, s1);
          goto _L26
        System.err.println("bitmap save failed");
          goto _L26
_L11:
        System.err.println("bitmap save failed");
        BitmapUtils.releaseBitmap(bitmap2);
        tile = null;
          goto _L12
_L2:
        tile = null;
          goto _L12
_L7:
        if (f8 + (float)l > 1024F) goto _L28; else goto _L27
_L27:
        i3 = 1;
          goto _L17
_L9:
        i4 = 0;
        j4 = 0;
_L32:
        if (j4 < i3) goto _L30; else goto _L29
_L29:
        k3 += j3;
        l3++;
          goto _L31
        i6 = (int)(f2 - f9);
        j6 = (int)(f3 - f10);
        int i4;
        int k6;
        int l6;
        int i7;
        String s1;
        if (l2 > 1)
        {
            k5 = 1024 - i6;
        } else
        {
            k5 = l;
        }
        if (i3 > 1)
        {
            l5 = 1024 - j6;
        } else
        {
            l5 = l;
        }
          goto _L24
_L23:
        i4 += i7;
        j4++;
          goto _L32
        j6 = (int)(f3 - f10);
        if ((int)(f4 - f9) % 1024 == 0)
        {
            k5 = 1024;
        } else
        {
            k5 = (int)(f4 - f9) % 1024;
        }
        if (i3 > 1)
        {
            l5 = 1024 - j6;
            i6 = 0;
        } else
        {
            l5 = l;
            i6 = 0;
        }
          goto _L24
        i6 = (int)(f2 - f9);
        if (l2 > 1)
        {
            k5 = 1024 - i6;
        } else
        {
            k5 = l;
        }
        l5 = (int)(f5 - f10);
        j6 = 0;
          goto _L24
        if ((int)(f5 - f10) % 1024 == 0)
        {
            l5 = 1024;
        } else
        {
            l5 = (int)(f5 - f10) % 1024;
        }
        i6 = 0;
        j6 = 0;
          goto _L24
          */
         	
    }

    public final Tile getTile(int i, int j, int k)
    {
        return getTileFormFile(i, j, k);
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
