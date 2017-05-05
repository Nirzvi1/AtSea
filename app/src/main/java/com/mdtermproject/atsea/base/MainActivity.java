package com.mdtermproject.atsea.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.Graphics;

public class MainActivity extends Activity {

    private CanvasView fore;
    private CanvasView back;

    private JoystickView joystick;

    public static String FILE_SAVE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Graphics.initialize(getResources());

        FILE_SAVE = getFilesDir().getAbsolutePath();

        fore = (CanvasView) findViewById(R.id.foreground);
        fore.setDrawRunnable(Graphics.FOREGROUND_DRAW);

        back = (CanvasView) findViewById(R.id.background);
        back.setDrawRunnable(Graphics.BACKGROUND_DRAW);

        joystick = (JoystickView) findViewById(R.id.move);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Game.getPlayer().slowlySetMotion(strength, angle);
            }
        });

        Game.init(fore, back, getResources());
        Game.start();
    }
}
