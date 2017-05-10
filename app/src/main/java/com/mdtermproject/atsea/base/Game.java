package com.mdtermproject.atsea.base;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Base64;
import android.util.Log;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.entities.Ship;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.Graphics;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Game {

    private static String TAG = "Game.java";

    private static boolean REFRESH_MINIMAP = false;
    private static boolean REFRESH_FOREGROUND = false;
    private static boolean REFRESH_BACKGROUND = false;

    private static Map map;
    private static Ship player;

    private static CanvasView miniMap;
    private static CanvasView fore;
    private static CanvasView back;

    public static void init(CanvasView m, CanvasView f, CanvasView b, Resources res) {
        miniMap = m;
        fore = f;
        back = b;

        map = parseTMXMap(res, R.xml.bn);

        player = new Ship();
        player.setTranslate(map.getSpawn());

        miniMap.refresh();
    }

    public static Map parseTMXMap(Resources res, int mapId) {
        XmlResourceParser xml = res.getXml(mapId);
        Map result = null;

        PointF spawn = new PointF();
        RectF exit = new RectF();

        int width = 0;
        int height = 0;
        boolean READ_STRING = false;
        float tileRatio = Graphics.TILE_SIZE / 64f;

        try {
            int event = xml.getEventType();
            while (event != XmlResourceParser.END_DOCUMENT) {

                if (event == XmlResourceParser.START_TAG) {
                    if (xml.getName().equals("data")) {
                        READ_STRING = true;
                    } else if (xml.getName().equals("layer")) {
                        width = xml.getAttributeIntValue(null, "width", 0);
                        height = xml.getAttributeIntValue(null, "height", 0);
                    } else if (xml.getName().equals("object")) {
                        Log.i(TAG, "parseTMXMap: ");
                        if (xml.getAttributeValue(null, "type").equals("spawn")) {
                            spawn = new PointF(xml.getAttributeIntValue(null, "x", 0) * tileRatio, xml.getAttributeIntValue(null, "y", 0) * tileRatio);
                        } else if (xml.getAttributeValue(null, "type").equals("exit")) {
                            int x = xml.getAttributeIntValue(null, "x", 0);
                            int y = xml.getAttributeIntValue(null, "y", 0);
                            int eWidth = xml.getAttributeIntValue(null, "width", 0);
                            int eHeight = xml.getAttributeIntValue(null, "height", 0);
                            exit = new RectF(x * tileRatio, y * tileRatio, (x + eWidth) * tileRatio, (y + eHeight) * tileRatio);
                        }//else
                    }//elseif
                } else if (READ_STRING) {
                    byte[] tileset = Base64.decode(xml.getText(), 0);

                    result = new Map(width, height, tileset);
                    READ_STRING = false;
                }//else

                event = xml.next();
            }//while
        } catch (Exception e) {
            e.printStackTrace();
        }//catch

        result.setSpawn(spawn);
        Log.i(TAG, "parseTMXMap: " + exit);
        result.setExit(exit);

        return result;
    }//parseMap

    public static void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long timer = System.currentTimeMillis();
                while (true) {

                    player.update((int) (System.currentTimeMillis() - timer));

                    if(map.isOverExit(player.getCentre())){
                        Log.i("Game", "Over");
                    }
                    timer = System.currentTimeMillis();

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }//catch

                    if (REFRESH_FOREGROUND) {
                        fore.refresh();
                        REFRESH_FOREGROUND = false;
                    }//if

                    if (REFRESH_BACKGROUND) {
                        back.refresh();
                        REFRESH_BACKGROUND = false;
                    }//if

                    if (REFRESH_MINIMAP) {
                        miniMap.refresh();
                        REFRESH_MINIMAP = false;
                    }//if
                }//while

            }
        }).start();
    }

    public static Map getMap() {
        return map;
    }//getMap

    public static Ship getPlayer() {
        return player;
    }

    public static void refreshForeground() {
        REFRESH_FOREGROUND = true;
    }

    public static void refreshBackground() {
        REFRESH_BACKGROUND = true;
    }

    public static void refreshMiniMap() {
        REFRESH_MINIMAP = true;
    }

}
