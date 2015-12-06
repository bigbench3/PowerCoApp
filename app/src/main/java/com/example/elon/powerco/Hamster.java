package com.example.elon.powerco;

/**
 * Created by bhay on 12/5/2015.
 */
import android.content.Context;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

public class Hamster {
    protected float x, y;
    private int hamsterLevel, wheelLevel;
    private float width, height;
    private Bitmap bitmap;
    private int screenWidth, screenHeight;
    private ArrayList<Integer> hamster;

    private final float SCALE = 0.1f;

    public Hamster(Context context){

        hamster = new ArrayList<>();

//        hamster.add(R.drawable.hamster0);
//        hamster.add(R.drawable.hamster1);
//        hamster.add(R.drawable.hamster2);
//        hamster.add(R.drawable.hamster3);

        bitmap = BitmapFactory.decodeResource(context.getResources(), hamster.get(hamsterLevel));

        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        // figure out the screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // start in top/left corner
        x = screenWidth/2;
        y = screenHeight/2;
    }

    public void doDraw(Canvas canvas) {
        // draw the Hamster
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y- height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }

    public int getHamsterLevel() {
        return hamsterLevel;
    }

    public void setHamsterLevel(int hamsterLevel) {
        this.hamsterLevel = hamsterLevel;
    }

    public int getWheelLevel() {
        return wheelLevel;
    }

    public void setWheelLevel(int wheelLevel) {
        this.wheelLevel = wheelLevel;
    }

}
