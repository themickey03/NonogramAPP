package com.mickey.nonogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Puzzle_activity extends AppCompatActivity {
    /**
     * Объявляются переменные, необходимые для кода.
     *
     * adapter -> GridAdapter, адаптер, программно управляющий расстановкой элементов
     * из массива данных на layout
     *
     * lvl -> Int, номер уровня, получаемый из кнопки, которую нажал пользователь
     */
    GridAdapter adapter;
    int lvl;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_layout);

        /*
         * Intent в данном случае используется для передачи данных в другой Activity.
         * Необходимо передать инфорамцию о выбранном уровне между Activity, и через обращение
         * к Intent данные передаются.
         * В коде Puzzle_activity.java данный параметр будет использован.
         */
        Intent thisIntent = getIntent();
        //Добавление lvl в данные Intent, для использования их
        int lvl = thisIntent.getIntExtra("lvl", -1);
        this.lvl = lvl;

        TextView tw_lvl_name = findViewById(R.id.lvl_name);
        tw_lvl_name.setText("Уровень " + lvl);

        /*
         * RecyclerView -> вид layout, используемый в данном случае для формирования
         * сетки из элементов программно, на этапе обработки кода. Layer в формате xml содержит лишь
         * ссылку на такой элемент, а заполнение его контентом происходит на этапе выполнения
         * кода.
         */
        RecyclerView recyclerView = findViewById(R.id.activity_grid);
        /*
         * GridLayoutManager -> Manager для RecyclerView, используется для определения таблицы
         * нужного размера. Также с его помощью производится програмнное отключение возможности
         * пролистывания таблицы по горизонтали и вертикали. Также через метод .setSpanSizeLookup
         * производится определения колличества столбцов в таблице.
         */
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
        /*
         * В нижеизложенном методе кол-во столбцов и их размер определяется на основе элемента по
         * порядку. Если номер элемента кратен 11 - то длина столбца должна быть выше, т.к.
         * это первый столбец, где информация для игры располагается в строку. Остальные столбцы
         * имеют равный размер.
         */
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 11 == 0)
                    return 3;
                else
                    return 2;
            }

        });

        /*
         * Установка GridLayoutManager как менеджера для View, а адаптер для заполнения данных
         * устанавливается как основной.
         */
        recyclerView.setLayoutManager(manager);
        adapter = new GridAdapter(this, lvl);
        recyclerView.setAdapter(adapter);
    }


}