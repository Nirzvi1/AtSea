package com.mdtermproject.atsea.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mdtermproject.atsea.R;

/**
 * Created by Alec Krawciw on 2017-05-18.
 */

public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        AssetManager am = context.getApplicationContext().getAssets();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        Typeface typeface = Typeface.createFromAsset(am, "fonts/" + a.getString(R.styleable.CustomTextView_fontName));

        //It is important to destroy the array
        a.recycle();
        setTypeface(typeface);
    }
}
