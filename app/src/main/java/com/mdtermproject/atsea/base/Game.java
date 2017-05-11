package com.mdtermproject.atsea.base;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PointF;
import android.util.Base64;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.entities.Ship;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.Graphics;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Game {

    private static boolean initialized = false;
    private static boolean started = false;

    private static boolean REFRESH_GUI_LAYER = false;
    private static int REFRESH_GUI_STATE = -2;

    private static boolean REFRESH_FOREGROUND = false;
    private static boolean REFRESH_BACKGROUND = false;

    private static Map map;
    private static Ship player;

    private static CanvasView guiLayer;
    private static CanvasView fore;
    private static CanvasView back;

    public static void init(CanvasView g, CanvasView f, CanvasView b, Resources res) {

        guiLayer = g;
        fore = f;
        back = b;

        if (!initialized) {
            map = parseTMXMap(res, R.xml.southmount_isles);

            player = new Ship();
            player.setTranslate(Graphics.DRAW_PLAYER.getX(), Graphics.DRAW_PLAYER.getY());
        }//if

        guiLayer.refresh();

        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static boolean isStarted() {
        return started;
    }

    public static Map parseTMXMap(Resources res, int mapId) {
        XmlResourceParser xml = res.getXml(mapId);
        Map result = null;

        PointF spawn = new PointF();
        PointF exit = new PointF();

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
                    } else if (xml.getName().equals("object")) {
                        if (xml.getAttributeValue(null, "type").equals("spawn")) {
                            spawn = new PointF(xml.getAttributeFloatValue(null, "x", 0), xml.getAttributeFloatValue(null, "y", 0));
                        } else if (xml.getAttributeValue(null, "type").equals("exit")) {
                            exit = new PointF(xml.getAttributeFloatValue(null, "x", 0), xml.getAttributeFloatValue(null, "y", 0));
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
        result.setExit(exit);

        return result;
    }//parseMap

    public static void start() {
        started = true;

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

                    if (REFRESH_GUI_LAYER) {
                        guiLayer.refresh();
                        REFRESH_GUI_LAYER = false;
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

    public static void refreshGUI() {
        REFRESH_GUI_LAYER = true;
        REFRESH_GUI_STATE = -2;
    }

    public static void refreshGUI(int layoutId) {
        REFRESH_GUI_LAYER = true;
        REFRESH_GUI_STATE = layoutId;
    }

    public static int getGuiRefreshState() {
        return REFRESH_GUI_STATE;
    }

}
