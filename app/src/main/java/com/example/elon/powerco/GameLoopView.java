package com.example.elon.powerco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Created by bhay on 11/30/2015.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback {

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private final String filename = "myoutput.txt";
    private TextView wattView, moneyView;
    private boolean reset;
    private int watts;
    private float money;
    private Hamster hamster;
    private Resource water, coal, wind, solar;
    private Salesman salesman;
    private House house;
    private ArrayList<Integer> data = new ArrayList<Integer>();


    public GameLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // remember the context for finding resources
        this.context = context;

        // want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        watts = 0;
        money = 0;
        hamster = new Hamster(context);
        water = new Resource(context, "water");
        solar = new Resource(context, "solar");
        wind = new Resource(context, "wind");
        coal = new Resource(context, "coal");
        salesman = new Salesman(context);
        house = new House(context);

        try {
            getPersistentData(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // game loop thread -- add a handler to update the TextView
        thread = new GameLoopThread(msgHandler);
    }

    Handler msgHandler = new Handler() {
            public void handleMessage(Message m){
                wattView.setText(m.getData().getString("watts"));
                moneyView.setText((m.getData().getString("money")));
            }
    };


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // thread exists, but is in terminated state
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameLoopThread(msgHandler);
        }

        // start the game loop
        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Game Loop Thread
    private class GameLoopThread extends Thread {


        private boolean isRunning = false;
        private long lastTimeMoney, lastTimeResources;

        // frames per second calculation
        private int frames;
        private long nextUpdate;



        // the handler for updates to the TextView
        private Handler handler;



        public GameLoopThread(Handler handler) {

            this.handler = handler;

            int watts = data.get(0);
            int money = data.get(1);
            setWatts(watts);
            setMoney(money);

            int hamsterLevel = data.get(2);
            int wheelLevel= data.get(3);
            hamster.setHamsterLevel(hamsterLevel);
            hamster.setWheelLevel(wheelLevel);
            setHamster(hamster);

            int waterLevel = data.get(4);
            water.setType("water");
            water.setLevel(waterLevel);
            setWater(water);

            int solarLevel = data.get(5);
            solar.setType("solar");
            solar.setLevel(solarLevel);
            setSolar(solar);

            int windLevel = data.get(6);
            wind.setType("wind");
            wind.setLevel(windLevel);
            setWind(wind);

            int coalLevel = data.get(7);
            coal.setType("coal");
            coal.setLevel(coalLevel);
            setCoal(coal);

            int saleSpeed = data.get(8);
            int salePrice = data.get(9);
            salesman.setSpeed(saleSpeed);
            salesman.setPrice(salePrice);
            setSalesman(salesman);

            int houseLevel = data.get(10);
            house.setHouseLevel(houseLevel);
            setHouse(house);
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        // the main loop
        @Override
        public void run() {


            lastTimeMoney = System.currentTimeMillis();
            lastTimeResources = System.currentTimeMillis();

            while (isRunning) {

//                TextView wattView1 = getWattView();
//                wattView1.setText("Watts: " + getWatts());
//                setWattView(wattView1);
//
//                TextView moneyView1 = getMoneyView();
//                wattView1.setText("Money: $" + getMoney());
//                setMoneyView(moneyView1);

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();



                    if(now >= lastTimeMoney +5000){
                        lastTimeMoney = now;
                        doUpdateMoney(salesman);
                    }

                    if(now >= lastTimeResources + 1000){
                        lastTimeResources = now;
                        doUpdateResource(water);
                        doUpdateResource(solar);
                        doUpdateResource(wind);
                        doUpdateResource(coal);
                    }

                    doDraw(canvas);

                    doUpdate();

                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }

        private void sendMessage(String wattMessage, String moneyMessage) {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            b.putString("watts", wattMessage);
            b.putString("money", moneyMessage);
            msg.setData(b);
            handler.sendMessage(msg);
        }

        private void doUpdate(){
            String wattsString = "Watts: " + getWatts();
            String moneyString = "Money: $" + getMoney();
            sendMessage(wattsString, moneyString);
        }


        // draw all objects in the game
        private void doDraw(Canvas canvas) {
            getHamster().doDraw(canvas);
            getWater().doDraw(canvas);
            getSolar().doDraw(canvas);
            getWind().doDraw(canvas);
            getCoal().doDraw(canvas);
            getHouse().doDraw(canvas);
        }

        // move all objects in the game
        private void doUpdateResource(Resource resource) {

            if(resource.getType().equals("water")){
                watts = watts + (resource.getLevel()*10);
            }

            if(resource.getType().equals("solar")){
                watts = watts + (resource.getLevel()*7);
            }

            if(resource.getType().equals("wind")){
                watts = watts + (resource.getLevel()*4);
            }

            if(resource.getType().equals("coal")){
                watts = watts + (resource.getLevel()*200);
                if(resource.getLevel() > 0) {
                    resource.setLevel(resource.getLevel() - 1);
                }
            }
        }

        private void doUpdateMoney(Salesman salesman) {

            if(watts >= getSalesman().getSpeed()){
                watts = watts - (int)getSalesman().getSpeed();
                money = money + (getSalesman().getSpeed() * getSalesman().getPrice());
            }
        }


    }

    /* THE GAME */


    // touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setWatts(getWatts()+ ((getHamster().getHamsterLevel() + 1) *
                (getHamster().getWheelLevel() + 1)));

        System.out.println("Hamster Level is " + hamster.getHamsterLevel());
        System.out.println("Wheel Level is " + hamster.getWheelLevel());
        System.out.println("Water Level is " + water.getLevel());
        System.out.println("Wind Level is " + wind.getLevel());

        return true;
    }

    public void getPersistentData(Context context) throws IOException {
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(filename);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            String[] array = line.split(",");

            for(int i = 0; i < array.length; i++){
//                System.out.println(array[i]);
                int level = (int) Double.parseDouble(array[i]);
                System.out.println(level);
                data.add(level);
            }

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void putPersistentData(Context context) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(watts + ", " + money + ", " + hamster.getHamsterLevel() + ", " +
                    hamster.getWheelLevel() + ", " + water.getLevel() + ", " + solar.getLevel() +
                    ", " + wind.getLevel() + ", " + coal.getLevel() + ", " + salesman.getSpeed() +
                    ", " + salesman.getPrice() + ", " + house.getHouseLevel());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }



    public void setWattView(TextView textView){this.wattView = textView;}

    public void setMoneyView(TextView textView){this.moneyView = textView;}

    public boolean isReset() {return reset;}

    public void setReset(boolean reset) {this.reset = reset;}

    public GameLoopThread getThread() {
        return thread;
    }

    public void setThread(GameLoopThread thread) {
        this.thread = thread;
    }

    public Hamster getHamster() {
        return hamster;
    }

    public void setHamster(Hamster hamster) {
        this.hamster = hamster;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Resource getWater() {
        return water;
    }

    public void setWater(Resource water) {
        this.water = water;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Resource getCoal() {
        return coal;
    }

    public void setCoal(Resource coal) {
        this.coal = coal;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Resource getWind() {
        return wind;
    }

    public void setWind(Resource wind) {
        this.wind = wind;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Resource getSolar() {
        return solar;
    }

    public void setSolar(Resource solar) {
        this.solar = solar;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Salesman getSalesman() {
        return salesman;
    }

    public void setSalesman(Salesman salesman) {
        this.salesman = salesman;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getWatts() { return watts; }

    public void setWatts(int watts) {
        this.watts = watts;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public float getMoney() { return money; }

    public void setMoney(float money) {
        this.money = money;
        try {
            putPersistentData(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public TextView getWattView() {
        return wattView;
    }

    public TextView getMoneyView() {
        return moneyView;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;

        int watts = data.get(0);
        int money = data.get(1);
        setWatts(watts);
        setMoney(money);

        int hamsterLevel = data.get(2);
        int wheelLevel= data.get(3);
        hamster.setHamsterLevel(hamsterLevel);
        hamster.setWheelLevel(wheelLevel);
        setHamster(hamster);

        int waterLevel = data.get(4);
        water.setType("water");
        water.setLevel(waterLevel);
        setWater(water);

        int solarLevel = data.get(5);
        solar.setType("solar");
        solar.setLevel(solarLevel);
        setSolar(solar);

        int windLevel = data.get(6);
        wind.setType("wind");
        wind.setLevel(windLevel);
        setWind(wind);

        int coalLevel = data.get(7);
        coal.setType("coal");
        coal.setLevel(coalLevel);
        setCoal(coal);

        int saleSpeed = data.get(8);
        int salePrice = data.get(9);
        salesman.setSpeed(saleSpeed);
        salesman.setPrice(salePrice);
        setSalesman(salesman);

        int houseLevel = data.get(10);
        house.setHouseLevel(houseLevel);
        setHouse(house);
    }

    public void resetGame(){
        setWatts(0);
        setMoney(5000);

        hamster.setHamsterLevel(0);
        hamster.setWheelLevel(0);
        setHamster(hamster);

        water.setType("water");
        water.setLevel(0);
        setWater(water);

        solar.setType("solar");
        solar.setLevel(0);
        setSolar(solar);

        wind.setType("wind");
        wind.setLevel(0);
        setWind(wind);

        coal.setType("coal");
        coal.setLevel(0);
        setCoal(coal);

        salesman.setSpeed(10);
        salesman.setPrice(1);
        setSalesman(salesman);

        house.setHouseLevel(0);
        setHouse(house);

        try {
            putPersistentData(context);
            System.out.println("IT RESETS");
        } catch (Exception e){

        }
    }
}
