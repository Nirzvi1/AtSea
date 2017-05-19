package com.mdtermproject.atsea.base;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Base64;
import android.util.Log;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.entities.EnemyShip;
import com.mdtermproject.atsea.entities.PlayerShip;
import com.mdtermproject.atsea.entities.Ship;
import com.mdtermproject.atsea.entities.ShipBuild;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class Game {

    private static String TAG = "Game.java";

    private static boolean initialized = false;
    private static boolean started = false;

    private static boolean REFRESH_GUI_LAYER = false;
    private static int REFRESH_GUI_STATE = -2;
    private static boolean REFRESH_FOREGROUND = false;
    private static boolean REFRESH_BACKGROUND = false;

    private static Map map;
    private static Ship player;
    private static ArrayList<Ship> enemies;

    private static CanvasView guiLayer;
    private static CanvasView fore;
    private static CanvasView back;

    private static Thread game;

    public static void init(CanvasView g, CanvasView f, CanvasView b, Resources res) {

        guiLayer = g;
        fore = f;
        back = b;
        started = false;

        if (!initialized) {
            map = parseTMXMap(res, R.xml.silverspell_lake);

            player = new Ship();
            player.setTranslate(map.getSpawn().x, map.getSpawn().y);
            player.rotate(90);

            Log.i("Spawn", map.getSpawn().toString());

            enemies = new ArrayList<>();
            enemies.add(new EnemyShip());
            enemies.get(0).setShipBuild(new ShipBuild(0.1f, 0.5f, 0.5f, 0, 0, "sfd"));
            enemies.get(0).rotate(90);


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
        RectF exit = new RectF();
        HashMap<String, RectF> regions = new HashMap<>();


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
                        Log.i(TAG, "parseTMXMap: " + xml.getAttributeValue(null, "name"));

                        if (xml.getAttributeValue(null, "name").equals("spawn")) {
                            spawn = new PointF(xml.getAttributeIntValue(null, "x", 0) * tileRatio, xml.getAttributeIntValue(null, "y", 0) * tileRatio);
                        } else {
                            String name = xml.getAttributeValue(null, "name");

                            int x = xml.getAttributeIntValue(null, "x", 0);
                            int y = xml.getAttributeIntValue(null, "y", 0);

                            int eWidth = xml.getAttributeIntValue(null, "width", 0);
                            int eHeight = xml.getAttributeIntValue(null, "height", 0);

                            RectF r = new RectF(x * tileRatio, y * tileRatio, (x + eWidth) * tileRatio, (y + eHeight) * tileRatio);

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

        for (java.util.Map.Entry<String, RectF> entry : regions.entrySet()) {

            result.addRegion(entry.getKey(), entry.getValue());

        }//for

        return result;
    }//parseMap

    public static void start() {
        started = true;

        game = new Thread(new Runnable() {
            @Override
            public void run() {

                long timer = System.currentTimeMillis();
                while (started) {

                    player.update((int) (System.currentTimeMillis() - timer));

                    for (Ship enemy : enemies) {
                        float deltaY = enemy.getCentre().y - player.getCentre().y;
                        float deltaX = enemy.getCentre().x - player.getCentre().x;
                        float angle = (float) -Math.toDegrees(Math.atan2(-deltaY, -deltaX));

                        enemy.setTargetMotion(50, angle);

                        enemy.update((int) (System.currentTimeMillis() - timer));
                    }//for

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

                    if (REFRESH_GUI_LAYER) {
                        guiLayer.refresh();
                        REFRESH_GUI_LAYER = false;
                    }//if
                }//while

            }
        });
        game.start();
    }

    public static void stop(){
        started = false;
        game.interrupt();
    }

    public static Map getMap() {
        return map;
    }//getMap

    public static Ship getPlayer() {
        return player;
    }

    public static ArrayList<Ship> getEnemies() {
        return enemies;
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

    public static List<Ship> getEnemyShips(){
        return enemies;
    }


}
