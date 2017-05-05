package com.mdtermproject.atsea.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.base.Map;
import com.mdtermproject.atsea.utils.NewMatrix;

import java.util.ArrayList;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Graphics {

    /*
    CONSTANTS
     */
    public final static Paint OCEAN = new Paint();
    public final static Paint TEMP = new Paint();

    private static ArrayList<Bitmap> images;
    public final static int PLAYER_ID = 0;
    public static int TILE_START_ID;

    public final static int TILE_SIZE = 100;

    /*
    DRAW RUNNABLES
     */
    public final static DrawRunnable BACKGROUND_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {
            c.drawRect(0, 0, c.getWidth(), c.getHeight(), OCEAN);

            if (Game.getMap() != null) {
                Game.getMap().drawMap(c, Game.getPlayer().getShipExternal());
            }//if

        }//onDraw
    };

    public final static DrawRunnable FOREGROUND_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {
            drawBitmap(c, PLAYER_ID, Game.getPlayer().getShipInternal());
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
    }//initialize

    public static void loadImages(Resources res) {
        images = new ArrayList<>();

        try {

            images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_5), 88, 151, false));

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


    public static void drawBitmap(Canvas c, int id, NewMatrix transform) {

        if (id < images.size()) {
            float x = transform.getX();
            float y = transform.getY();
            float x2 = transform.getX() + images.get(id).getWidth();
            float y2 = transform.getY() + images.get(id).getHeight();

            if (x < c.getWidth() && y < c.getHeight() && x2 > 0 && y2 > 0) {
                c.drawBitmap(images.get(id), transform, new Paint());
            }//if
        }//if
    }//void


}
