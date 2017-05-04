package com.mdtermproject.atsea.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.mdtermproject.atsea.R;
import com.mdtermproject.atsea.graphics.CanvasView;
import com.mdtermproject.atsea.graphics.GraphicsBase;

public class MainActivity extends Activity {

    private CanvasView fore;
    private CanvasView back;

    private JoystickView joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GraphicsBase.initialize(getResources());

        fore = (CanvasView) findViewById(R.id.foreground);
        fore.setDrawRunnable(GraphicsBase.FOREGROUND_DRAW);

        back = (CanvasView) findViewById(R.id.background);
        back.setDrawRunnable(GraphicsBase.BACKGROUND_DRAW);

        joystick = (JoystickView) findViewById(R.id.move);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                GameLoop.player.attemptSetMotion(strength, angle);
            }
        });

        GameLoop.init(fore, back);
        GameLoop.loop();
    }
}
