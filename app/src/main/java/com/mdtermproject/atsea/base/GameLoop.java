package com.mdtermproject.atsea.base;

import com.mdtermproject.atsea.entities.Ship;
import com.mdtermproject.atsea.graphics.CanvasView;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class GameLoop {

    public static Ship player = new Ship();

    private static CanvasView fore;
    private static CanvasView back;

    public static void init(CanvasView f, CanvasView b) {
        fore = f;
        back = b;
    }

    public static void loop() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                long timer = System.currentTimeMillis();
                while (true) {

                    player.update((int) (System.currentTimeMillis() - timer));
                    timer = System.currentTimeMillis();

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }//catch

                    fore.postInvalidate();
                    back.postInvalidate();
                }//while

            }
        }).start();
    }

}
