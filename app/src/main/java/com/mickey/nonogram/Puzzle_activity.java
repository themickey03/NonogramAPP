package com.mickey.nonogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Puzzle_activity extends AppCompatActivity {
    GridAdapter adapter;
    int lvl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_layout);

        Intent thisIntent = getIntent();
        int lvl = thisIntent.getIntExtra("lvl", -1);
        this.lvl = lvl;
        TextView tw_lvl_name = findViewById(R.id.lvl_name);
        tw_lvl_name.setText("Уровень " + lvl);
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.activity_grid);
        GridLayoutManager manager = new GridLayoutManager(this, 23){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 11 == 0)
                    return 3;
                else
                    return 2;
            }

        });

        recyclerView.setLayoutManager(manager);
        adapter = new GridAdapter(this, lvl);
        recyclerView.setAdapter(adapter);
    }


}