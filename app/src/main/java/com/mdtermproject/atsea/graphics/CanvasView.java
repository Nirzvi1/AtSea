package com.mdtermproject.atsea.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by FIXIT on 2017-05-04.
 */

public class CanvasView extends View {

    private DrawRunnable d;

    public CanvasView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void setDrawRunnable(DrawRunnable draw) {
        this.d = draw;
    }

    public void onDraw(Canvas c) {
        if (d != null) {
            d.onDraw(c);
        }//if
    }

}
