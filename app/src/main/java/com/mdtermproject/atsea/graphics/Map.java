package com.mdtermproject.atsea.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.mdtermproject.atsea.utils.NewMatrix;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Map {
    int w;
    int h;

    int[][] islands;
    int count;

    public Map(int w, int h, int numIslands) {
        this.w = w;
        this.h = h;
        islands = new int[numIslands][4];
        count = 0;
    }//Map

    public void addIsland(int x, int y, int w, int h) {
        if (count < islands.length) {
            islands[count][0] = x;
            islands[count][1] = y;
            islands[count][2] = w;
            islands[count++][3] = h;
        } else {
            throw new RuntimeException("Attempted to add too many islands to the map!");
        }//else
    }//addIsland

    public void drawIsland(Canvas c, NewMatrix translation, int idx) {
        float baseX = translation.getX();
        float baseY = translation.getY();

        //NORTH BEACH
        for (int k = islands[idx][0] + 1; k < islands[idx][2] + islands[idx][0] - 1; k++) {
            c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 1), baseX + k * GraphicsBase.TILE_SIZE, baseY + (islands[idx][1]) * GraphicsBase.TILE_SIZE, new Paint());
        }//for

        //SOUTH BEACH
        for (int k = islands[idx][0] + 1; k < islands[idx][2] + islands[idx][0] - 1; k++) {
            c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 33), baseX + k * GraphicsBase.TILE_SIZE, baseY + (islands[idx][1] + islands[idx][3] - 1) * GraphicsBase.TILE_SIZE, new Paint());
        }//for

        //WEST BEACH
        for (int k = islands[idx][1] + 1; k < islands[idx][3] + islands[idx][1] - 1; k++) {
            c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 16), baseX + islands[idx][0] * GraphicsBase.TILE_SIZE, baseY + k * GraphicsBase.TILE_SIZE, new Paint());
        }//for

        //EAST BEACH
        for (int k = islands[idx][1] + 1; k < islands[idx][3] + islands[idx][1] - 1; k++) {
            c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 18), baseX + (islands[idx][0] + islands[idx][2] - 1) * GraphicsBase.TILE_SIZE, baseY + k * GraphicsBase.TILE_SIZE, new Paint());
        }//for

        //INNER
        for (int i = islands[idx][1] + 1; i < islands[idx][3] + islands[idx][1] - 1; i++) {
            for (int k = islands[idx][0] + 1; k < islands[idx][2] + islands[idx][0] - 1; k++) {
                c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 39), baseX + k * GraphicsBase.TILE_SIZE, baseY + i * GraphicsBase.TILE_SIZE, new Paint());
            }//for
        }//for

        //NORTHWEST
        c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 0), baseX + islands[idx][0] * GraphicsBase.TILE_SIZE, baseY + islands[idx][1] * GraphicsBase.TILE_SIZE, new Paint());

        //NORTHEAST
        c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 2), baseX + (islands[idx][0] + islands[idx][2] - 1) * GraphicsBase.TILE_SIZE, baseY + islands[idx][1] * GraphicsBase.TILE_SIZE, new Paint());

        //SOUTHEAST
        c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 34), baseX + (islands[idx][0] + islands[idx][2] - 1) * GraphicsBase.TILE_SIZE, baseY + (islands[idx][1] + islands[idx][3] - 1) * GraphicsBase.TILE_SIZE, new Paint());

        //SOUTHWEST
        c.drawBitmap(GraphicsBase.getImageFromId(GraphicsBase.TILE_START_ID + 32), baseX + islands[idx][0] * GraphicsBase.TILE_SIZE, baseY + (islands[idx][1] + islands[idx][3] - 1) * GraphicsBase.TILE_SIZE, new Paint());

    }//drawIsland

}