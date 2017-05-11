package com.mdtermproject.atsea.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsoluteLayout;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.Graphics;

public class MainActivity extends Activity {

    private CanvasView fore;
    private CanvasView back;
    private CanvasView guiLayer;

    private JoystickView joystick;

    private static Context ctx;

    public static String FILE_SAVE = "";

    public static AbsoluteLayout test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            MainActivity.ctx = this;
            FILE_SAVE = getFilesDir().getAbsolutePath();

            back = (CanvasView) findViewById(R.id.background);
            back.setDrawRunnable(Graphics.BACKGROUND_DRAW);

            fore = (CanvasView) findViewById(R.id.foreground);
            fore.setDrawRunnable(Graphics.FOREGROUND_DRAW);

            guiLayer = (CanvasView) findViewById(R.id.guiLayer);
            guiLayer.setDrawRunnable(Graphics.MINIMAP_DRAW);

            test = (AbsoluteLayout) LayoutInflater.from(this).inflate(R.layout.firstgui, null);

            Point windowSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(windowSize);

            Graphics.initialize(getResources(), windowSize.x, windowSize.y);

            joystick = (JoystickView) findViewById(R.id.move);
            joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
                @Override
                public void onMove(int angle, int strength) {
                    Game.getPlayer().setTargetMotion(strength, angle);
                }
            });

            Game.init(guiLayer, fore, back, getResources());

        if (!Game.isStarted()) {
            Game.start();
        }//if

    }

    public static Context getContext() {
        return ctx;
    }
}
