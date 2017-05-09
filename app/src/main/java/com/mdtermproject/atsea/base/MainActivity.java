package com.mdtermproject.atsea.base;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.Graphics;

public class MainActivity extends Activity {

    private CanvasView fore;
    private CanvasView back;
    private CanvasView miniMap;

    private JoystickView joystick;

    public static String FILE_SAVE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FILE_SAVE = getFilesDir().getAbsolutePath();

        back = (CanvasView) findViewById(R.id.background);
        back.setDrawRunnable(Graphics.BACKGROUND_DRAW);

        fore = (CanvasView) findViewById(R.id.foreground);
        fore.setDrawRunnable(Graphics.FOREGROUND_DRAW);

        miniMap = (CanvasView) findViewById(R.id.minimap);
        miniMap.setDrawRunnable(Graphics.MINIMAP_DRAW);

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

        Game.init(miniMap, fore, back, getResources());
        Game.start();
    }
}
