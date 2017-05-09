package com.mdtermproject.atsea.base;

import android.graphics.Canvas;

import com.mdtermproject.atsea.graphics.Graphics;
import com.mdtermproject.atsea.utils.NewMatrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Map {

    private int w;
    private int h;
    private byte[] mapArray;

    public Map(int w, int h, byte[] raw) {
        this.w = w;
        this.h = h;

        this.mapArray = new byte[raw.length / 4];
        for (int i = 0; i < raw.length; i += 4) {
            mapArray[i / 4] = raw[i];
        }//for
    }//Map

    public void drawMap(Canvas c, NewMatrix translation) {
        //coordinate of top-leftmost tile to draw
        int xIdx = Math.max(0, (int) translation.getX() / Graphics.TILE_SIZE);
        int yIdx = Math.max(0, (int) translation.getY() / Graphics.TILE_SIZE);

        int screenWidthInTiles = (c.getWidth() / Graphics.TILE_SIZE) + 2;
        int screenHeightInTiles = (c.getHeight() / Graphics.TILE_SIZE) + 2;

        int xTileOffset = (int) (-translation.getX() % Graphics.TILE_SIZE);
        int yTileOffset = (int) (-translation.getY() % Graphics.TILE_SIZE);

        if(xIdx == 0) {
            xTileOffset = Math.min(0, xTileOffset);
        }//if

        if(yIdx == 0) {
            yTileOffset = Math.min(0, yTileOffset);
        }//if

        for (int x = xIdx; x < xIdx + screenWidthInTiles; x++) {
            for (int y = yIdx; y < yIdx + screenHeightInTiles; y++) {
                int imgId = mapArray[x + y * w] - 1;

                if (imgId != -1) {
                    Graphics.drawBitmap(c, imgId + Graphics.TILE_START_ID, xTileOffset + (x - xIdx) * Graphics.TILE_SIZE, yTileOffset + (y - yIdx) * Graphics.TILE_SIZE);
                }//if
            }//for
        }//for
    }//void

    public int getTileType(int x, int y) {
        return mapArray[x / Graphics.TILE_SIZE + y * w / Graphics.TILE_SIZE];
    }
}