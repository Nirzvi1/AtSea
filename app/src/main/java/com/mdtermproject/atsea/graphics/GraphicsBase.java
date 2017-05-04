package com.mdtermproject.atsea.graphics;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.mdtermproject.atsea.base.GameLoop;
import com.mdtermproject.atsea.R;

import java.util.ArrayList;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class GraphicsBase {

    /*
    CONSTANTS
     */
    public final static Paint OCEAN = new Paint();
    public final static Paint TEMP = new Paint();

    private static ArrayList<Bitmap> images;
    public final static int PLAYER_ID = 0;
    public static int TILE_START_ID;

    private final static Matrix DRAW_PLAYER = new Matrix();

    private static Map map;
    public final static int TILE_SIZE = 100;

    /*
    DRAW RUNNABLES
     */
    public final static DrawRunnable BACKGROUND_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {
            c.drawRect(0, 0, c.getWidth(), c.getHeight(), OCEAN);

            if (map != null) {
                map.drawIsland(c, GameLoop.player.getTranslation(), 0);
            }//if
        }//onDraw
    };

    public final static DrawRunnable FOREGROUND_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {
            drawBitmap(c, PLAYER_ID, GameLoop.player.getRotation());
            Log.i("!!!", GameLoop.player.getRotation().toString());
        }
    };

    /*
    INITIALIZATION METHODS
     */

    public static void initialize(Resources res) {
        OCEAN.setColor(Color.rgb(52, 154, 217));
        OCEAN.setStyle(Paint.Style.FILL);

        TEMP.setColor(Color.rgb(127, 127, 127));
        TEMP.setStyle(Paint.Style.FILL);

        loadImages(res);
        parseMap(res, R.xml.map1);

    }//initialize

    public static void loadImages(Resources res) {
        images = new ArrayList<>();

        try {

            images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_5), 66, 113, false));

            TILE_START_ID = images.size();

            for (int i = 1; i <= 96; i++) {
                int drawableId;

                if (i < 10) {
                    drawableId = R.drawable.class.getField("tile_0" + i).getInt(null);
                } else {
                    drawableId = R.drawable.class.getField("tile_" + i).getInt(null);
                }//else

                images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, drawableId), TILE_SIZE, TILE_SIZE, false));
            }//for
        } catch (Exception e) {
            e.printStackTrace();
        }//catch;
    }//loadImages

    public static void parseMap(Resources res, int mapId) {
        XmlResourceParser xml = res.getXml(mapId);

        try {
            int event = xml.getEventType();
            while (event != XmlResourceParser.END_DOCUMENT) {

                if (event == XmlResourceParser.START_TAG) {
                    if (xml.getName().equals("Island")) {
                        int x = xml.getAttributeIntValue(null, "x", 0);
                        int y = xml.getAttributeIntValue(null, "y", 0);
                        int w = xml.getAttributeIntValue(null, "w", 0);
                        int h = xml.getAttributeIntValue(null, "h", 0);

                        map.addIsland(x, y, w, h);
                    } else if (xml.getName().equals("Map")) {
                        int w = Integer.parseInt(xml.getAttributeValue(null, "w"));
                        int h = Integer.parseInt(xml.getAttributeValue(null, "h"));
                        int numIslands = Integer.parseInt(xml.getAttributeValue(null, "num_islands"));

                        map = new Map(w, h, numIslands);
                    }//else
                }//else

                event = xml.next();
            }//while
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//parseMap

    /*
    IMAGE METHODS
     */
    public static Bitmap getImageFromId(int id) {
        return images.get(id);
    }//getImageFromId

    public static void drawBitmap(Canvas c, int id, float x, float y) {
        if (id < images.size()) {
            c.drawBitmap(images.get(id), x, y, new Paint());
        }//if
    }//drawBitmap


    public static void drawBitmap(Canvas c, int id, Matrix transform) {
        if (id < images.size()) {
            c.drawBitmap(images.get(id), transform, new Paint());
        }//if
    }//void


}
