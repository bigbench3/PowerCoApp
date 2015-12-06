package com.example.elon.powerco;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by bhay on 12/5/2015.
 */
public class Resource {
    private String type;
    private int level;
    private float width, height;
    private Bitmap bitmap;
    private int screenWidth, screenHeight;
    private final float SCALE = 0.1f;
    private ArrayList<Integer> water, wind, solar, coal;

    public Resource(Context context){

        water = new ArrayList<>();
        wind = new ArrayList<>();
        solar = new ArrayList<>();
        coal = new ArrayList<>();

        water.add(R.drawable.water0);
        water.add(R.drawable.water1);
        water.add(R.drawable.water2);
        water.add(R.drawable.water3);

        wind.add(R.drawable.wind0);
        wind.add(R.drawable.wind1);
        wind.add(R.drawable.wind2);
        wind.add(R.drawable.wind3);

        solar.add(R.drawable.solar0);
        solar.add(R.drawable.solar1);
        solar.add(R.drawable.solar2);
        solar.add(R.drawable.solar3);

        coal.add(R.drawable.coal0);
        coal.add(R.drawable.coal1);
        coal.add(R.drawable.coal2);
        coal.add(R.drawable.coal3);


        String resourceType = getType();
        int resourceLevel = getLevel();

        if(resourceType.equals("water")){
            bitmap = BitmapFactory.decodeResource(context.getResources(), water.get(resourceLevel));
        } else if(resourceType.equals("wind")){
            bitmap = BitmapFactory.decodeResource(context.getResources(), wind.get(resourceLevel));
        } else if(resourceType.equals("solar")){
            bitmap = BitmapFactory.decodeResource(context.getResources(), solar.get(resourceLevel));
        } else{
            bitmap = BitmapFactory.decodeResource(context.getResources(), coal.get(resourceLevel));
        }


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
        x = screenWidth/4;
        y = screenHeight/4;
    }

    public void doDraw(Canvas canvas) {
        // draw the bird
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y- height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
