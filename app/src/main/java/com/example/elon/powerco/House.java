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
public class House {

    protected float x, y;
    private int houseLevel;
    private float width, height;
    private Bitmap bitmap;
    private int screenWidth, screenHeight;
    private ArrayList<Integer> house;
    private final float SCALE = 0.1f;

    public House(Context context){

        house = new ArrayList<>();


//    house.add(R.drawable.house0);
//    house.add(R.drawable.house1);
//    house.add(R.drawable.house2);
//    house.add(R.drawable.house3);

//    bitmap = BitmapFactory.decodeResource(context.getResources(), house.get(houseLevel));
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.pb_sq_40690_lg);


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
        // draw the house
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x + width/2), (int) (y- height/2),
                        (int) (x + x), (int) (y + height/2)),
                null);
    }

    public int getHouseLevel() {
        return houseLevel;
    }

    public void setHouseLevel(int houseLevel) {
        this.houseLevel = houseLevel;
    }
}
