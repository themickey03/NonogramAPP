package com.mickey.nonogram;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClickBtn_lvl1 (View v) {
        Intent intent_lvl1 = new Intent(this, Puzzle_activity.class);
        intent_lvl1.putExtra("lvl",1);
        startActivity(intent_lvl1);
    }

    public void onClickBtn_lvl2 (View v) {
        Intent intent_lvl2 = new Intent(this, Puzzle_activity.class);
        intent_lvl2.putExtra("lvl",2);
        startActivity(intent_lvl2);
    }

    public void onClickBtn_lvl3 (View v) {
        Intent intent_lvl3 = new Intent(this, Puzzle_activity.class);
        intent_lvl3.putExtra("lvl",3);
        startActivity(intent_lvl3);
    }

    public void onClickBtn_lvl4 (View v) {
        Intent intent_lvl4 = new Intent(this, Puzzle_activity.class);
        intent_lvl4.putExtra("lvl",4);
        startActivity(intent_lvl4);
    }
}