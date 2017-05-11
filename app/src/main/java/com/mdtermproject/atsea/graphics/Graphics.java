package com.mdtermproject.atsea.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;

import com.mdtermproject.atsea.base.Game;
import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.base.MainActivity;
import com.mdtermproject.atsea.utils.TransformationMatrix;

import java.util.ArrayList;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Graphics {

    /*
    CONSTANTS
     */
    public final static Paint OCEAN = new Paint();
    public final static Paint SMALL_MAP_BACKGROUND = new Paint();
    public final static Paint SMALL_MAP_FOREGROUND = new Paint();
    public final static Paint SMALL_MAP_PLAYER = new Paint();

    private static ArrayList<Bitmap> images;
    public final static int PLAYER_ID = 0;
    public static int TILE_START_ID;

    public final static int TILE_SIZE = 100;

    public final static int SMALL_MAP_SIZE = 200;

    public static TransformationMatrix DRAW_PLAYER;

    /*
    DRAW RUNNABLES
     */
    public final static DrawRunnable BACKGROUND_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {
            c.drawRect(0, 0, c.getWidth(), c.getHeight(), OCEAN);

            TransformationMatrix translation = Game.getPlayer().getTranslate().clone();
            translation.translate(-Graphics.DRAW_PLAYER.getX(), -Graphics.DRAW_PLAYER.getY());

            if (Game.getMap() != null) {
                Game.getMap().drawMap(c, translation);
            }//if

        }//onDraw
    };

    public final static DrawRunnable FOREGROUND_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {

            TransformationMatrix rotation = Game.getPlayer().getRotate().clone();

            TransformationMatrix translation = Game.getPlayer().getTranslate().clone();
            translation.translate(-Graphics.DRAW_PLAYER.getX(), -Graphics.DRAW_PLAYER.getY());

            if (translation.getX() < 0) {
                rotation.postTranslate(DRAW_PLAYER.getX() + translation.getX(), 0);
            } else {
                rotation.postTranslate(DRAW_PLAYER.getX(), 0);
            }//else

            if (translation.getY() < 0) {
                rotation.postTranslate(0, DRAW_PLAYER.getY() + translation.getY());
            } else {
                rotation.postTranslate(0, DRAW_PLAYER.getY());
            }//else

            drawBitmap(c, PLAYER_ID, rotation);
        }
    };



    public final static DrawRunnable MINIMAP_DRAW = new DrawRunnable() {
        @Override
        public void onDraw(Canvas c) {

            if (Game.getGuiRefreshState() == -2) {
                Game.getMap().drawMiniMap(c, Game.getPlayer().getTranslate());
            } else if (Game.getGuiRefreshState() != -1) {
                drawLayout(c, MainActivity.getContext(), Game.getGuiRefreshState());
            }//else
        }
    };

    /*
    INITIALIZATION METHODS
     */

    public static void initialize(Resources res, int canvasWidth, int canvasHeight) {
        OCEAN.setColor(Color.rgb(52, 154, 217));
        OCEAN.setStyle(Paint.Style.FILL);

        SMALL_MAP_BACKGROUND.setColor(Color.argb(127, 255, 255, 255));
        SMALL_MAP_BACKGROUND.setStyle(Paint.Style.FILL);

        SMALL_MAP_FOREGROUND.setColor(Color.argb(127, 0, 0, 0));
        SMALL_MAP_FOREGROUND.setStyle(Paint.Style.FILL);

        SMALL_MAP_PLAYER.setColor(Color.argb(127, 0, 0, 255));
        SMALL_MAP_PLAYER.setStyle(Paint.Style.FILL);

        DRAW_PLAYER = new TransformationMatrix();
        DRAW_PLAYER.setDimensions(151, 88);
        DRAW_PLAYER.translate(canvasWidth / 2 - DRAW_PLAYER.getW() / 2, canvasHeight / 2 - DRAW_PLAYER.getH() / 2);

        loadImages(res);
    }//initialize

    public static void loadImages(Resources res) {
        images = new ArrayList<>();

        try {

            images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.ship_5), (int) DRAW_PLAYER.getW(), (int) DRAW_PLAYER.getH(), false));

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


    public static void drawBitmap(Canvas c, int id, TransformationMatrix transform) {

        if (id < images.size()) {
            float x = transform.getX();
            float y = transform.getY();
            float x2 = transform.getX() + images.get(id).getWidth();
            float y2 = transform.getY() + images.get(id).getHeight();

            if (x < c.getWidth() && y < c.getHeight() && x2 > 0 && y2 > 0) {
                c.drawBitmap(images.get(id), transform, new Paint());
            }//if
        }//if
    }//drawBitmap

    public static void drawLayout(Canvas c, Context ctx, int layoutId) {
        AbsoluteLayout gui = (AbsoluteLayout) LayoutInflater.from(ctx).inflate(layoutId, null);
        gui.measure(c.getWidth(), c.getHeight());
        gui.layout(0, 0, c.getWidth(), c.getHeight());

        gui.draw(c);
    }//drawLayout


}
