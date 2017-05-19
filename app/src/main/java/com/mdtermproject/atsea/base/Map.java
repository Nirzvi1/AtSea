package com.mdtermproject.atsea.base;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.mdtermproject.atsea.entities.Ship;
import com.mdtermproject.atsea.graphics.Graphics;
import com.mdtermproject.atsea.utils.TransformationMatrix;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mdtermproject.atsea.graphics.Graphics.TILE_SIZE;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Map {

    private int w;
    private int h;
    private byte[] mapArray;
    private Bitmap miniMapBitmap;

    private PointF spawn;
    private RectF exit;

    private HashMap<String, RectF> regions;

    public Map(int w, int h, byte[] raw) {
        this.w = w;
        this.h = h;

        this.mapArray = new byte[raw.length / 4];
        for (int i = 0; i < raw.length; i += 4) {
            mapArray[i / 4] = raw[i];
        }//for

        miniMapBitmap = generateMiniMapBitmap();

        regions = new HashMap<>();
    }//Map

    public void setSpawn(PointF spawn) {
        this.spawn = spawn;
    }//setSpawn

    public void setExit(RectF exit) {
        this.exit = exit;
    }//setSpawn

    public PointF getSpawn() {
        return spawn;
    }

    public RectF getExit() {
        return exit;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public void drawMap(Canvas c, TransformationMatrix translation) {

        //coordinate of top-leftmost tile to draw
        int xIdx = Math.max(0, (int) translation.getX() / TILE_SIZE);
        int yIdx = Math.max(0, (int) translation.getY() / TILE_SIZE);

        int screenWidthInTiles = (c.getWidth() / TILE_SIZE) + 2;
        int screenHeightInTiles = (c.getHeight() / TILE_SIZE) + 2;

        int xTileOffset = (int) (-translation.getX() % TILE_SIZE);
        int yTileOffset = (int) (-translation.getY() % TILE_SIZE);

        if(xIdx == 0) {
            xTileOffset = Math.min(0, xTileOffset);
        }//if

        if(yIdx == 0) {
            yTileOffset = Math.min(0, yTileOffset);
        }//if

        for (int x = xIdx; x < xIdx + screenWidthInTiles; x++) {
            for (int y = yIdx; y < yIdx + screenHeightInTiles; y++) {
                int imgId = -1;

                if(x + y * w < mapArray.length) {
                    imgId = mapArray[x + y * w] - 1;
                }

                if (imgId != -1) {
                    Graphics.drawBitmap(c, imgId + Graphics.TILE_START_ID, xTileOffset + (x - xIdx) * TILE_SIZE, yTileOffset + (y - yIdx) * TILE_SIZE);
                }//if
            }//for
        }//for
    }//drawMap

    public Bitmap generateMiniMapBitmap() {

        float w = Graphics.SMALL_MAP_SIZE;
        float h = Graphics.SMALL_MAP_SIZE * (getHeight() / getWidth());

        Bitmap result = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(result);

        c.drawRect(c.getWidth() - w, c.getHeight() - h, c.getWidth(), c.getHeight(), Graphics.SMALL_MAP_BACKGROUND);

        float tileDimension = Graphics.SMALL_MAP_SIZE / (float) getWidth();

        for (int i = 0; i < mapArray.length; i++) {
            if (mapArray[i] != 0) {
                float x = tileDimension * (i % getWidth()) + (c.getWidth() - w);
                float y = tileDimension * (i / getWidth()) + (c.getHeight() - h);

                c.drawRect(x, y, x + tileDimension, y + tileDimension, Graphics.SMALL_MAP_FOREGROUND);
            }//if
        }//for

        return result;
    }

    public void drawMiniMap(Canvas c, TransformationMatrix translate) {

        c.drawBitmap(miniMapBitmap, c.getWidth() - miniMapBitmap.getWidth(), c.getHeight() - miniMapBitmap.getHeight(), new Paint());

        float x = translate.getX() * (miniMapBitmap.getWidth() / (float) (w * Graphics.TILE_SIZE)) + (c.getWidth() - miniMapBitmap.getWidth());
        float y = translate.getY() * (miniMapBitmap.getHeight() / (float) (h * Graphics.TILE_SIZE)) + (c.getHeight() - miniMapBitmap.getHeight());

        c.drawRect(x, y, x + 5, y + 5, Graphics.SMALL_MAP_PLAYER);

        for (Ship s : Game.getEnemyShips()) {
            float xE = s.getTranslate().getX() * (miniMapBitmap.getWidth() / (float) (w * Graphics.TILE_SIZE)) + (c.getWidth() - miniMapBitmap.getWidth());
            float yE = s.getTranslate().getY() * (miniMapBitmap.getHeight() / (float) (h * Graphics.TILE_SIZE)) + (c.getHeight() - miniMapBitmap.getHeight());

            c.drawRect(xE, yE, xE + 5, yE + 5, Graphics.SMALL_MAP_PLAYER);
        }

    }

    public boolean collide(Ship entity) {

        double[][] corners = entity.getCorners();
        PointF centre = entity.getCentre();

        for(int i = 0; i < corners.length; i++) {

            if ((corners[i][0] < 0 || corners[i][1] < 0
                    || corners[i][0] > w * TILE_SIZE || corners[i][1] > h * TILE_SIZE)
                    || mapArray[(int) (corners[i][0] / TILE_SIZE) + w * (int) (corners[i][1] / TILE_SIZE)] != 0) {

                double relativeX = corners[i][0] - centre.x;
                double relativeY = corners[i][1] - centre.y;

                double degrees = Math.toDegrees(Math.atan2(-relativeY, relativeX));

                Log.i("Degrees1", degrees + "");

                if (degrees < 0) {
                    degrees += 360;
                }//if

                if (degrees < 180) {
                    degrees = 180 - degrees;
                } else {
                    degrees = 540 - degrees;
                }//elseif

//                degrees = ((degrees % 360) + 360) % 360;

                entity.setTargetMotion(5, (float) degrees);

                Log.i("Degrees", degrees + "");

                return true;
            }//if
        }//for

        return false;
    }

    public int doesCollide(double[][] corners) {

        for(int i = 0; i < corners.length; i++) {

            if (corners[i][0] < 0 || corners[i][1] < 0
                    || corners[i][0] > w * TILE_SIZE || corners[i][1] > h * TILE_SIZE) {
                return i;
            }//if

            if (mapArray[(int) (corners[i][0] / TILE_SIZE) + w * (int) (corners[i][1] / TILE_SIZE)] != 0) {
                return i;
            }//if
        }//for

        return -1;
    }

    public void addRegion(String name, RectF r) {
        if (!regions.containsKey(name)) {
            regions.put(name, r);

            Log.i("Region Added", (name));
        } else {
            int count = 0;

            for (String key : regions.keySet()) {
                if (key.startsWith(name)) {
                    count++;
                }//if
            }//for

            regions.put(name + count, r);

            Log.i("Region Added", (name + count));
        }//else
    }//addRegion

    public String inWhatRegion(PointF p) {

        for (String key : regions.keySet()) {
            if (inRegion(key, p)) {
                return key;
            }//if
        }//for

        return null;
    }

    public boolean inRegion(String regionName, PointF p) {
        return regions.get(regionName).contains(p.x, p.y);
    }//inRegion

    public boolean isOverExit(PointF p){
        return exit.contains(p.x, p.y);
    }
}