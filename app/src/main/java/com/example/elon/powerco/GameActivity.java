package com.example.elon.powerco;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GameLoopView gameLoopView = (GameLoopView) findViewById(R.id.GameLoopView);
        gameLoopView.setTextView(textView);
    }
}
