package com.mdtermproject.atsea.base;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Base64;
import android.util.Log;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.entities.Ship;
import com.mdtermproject.atsea.graphics.CanvasView;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Game {

    private static boolean REFRESH_FOREGROUND = false;
    private static boolean REFRESH_BACKGROUND = false;

    private static Map map;
    private static Ship player = new Ship();

    private static CanvasView fore;
    private static CanvasView back;

    public static void init(CanvasView f, CanvasView b, Resources res) {
        fore = f;
        back = b;

        map = parseTMXMap(res, R.xml.map3);
    }

    public static Map parseTMXMap(Resources res, int mapId) {
        XmlResourceParser xml = res.getXml(mapId);
        Map result = null;

        int width = 0;
        int height = 0;
        boolean READ_STRING = false;

        try {
            int event = xml.getEventType();
            while (event != XmlResourceParser.END_DOCUMENT) {

                if (event == XmlResourceParser.START_TAG) {
                    if (xml.getName().equals("data")) {
                        READ_STRING = true;
                    } else if (xml.getName().equals("layer")) {
                        width = xml.getAttributeIntValue(null, "width", 0);
                        height = xml.getAttributeIntValue(null, "height", 0);
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

        return result;
    }//parseMap

    public static void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long timer = System.currentTimeMillis();
                while (true) {

                    player.update((int) (System.currentTimeMillis() - timer));
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

}
