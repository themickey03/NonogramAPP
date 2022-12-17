package com.marowee.nonogram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class GridAdapter extends RecyclerView.Adapter {
    /*
     * Данный адаптер модифицирован, относительно базовой его реализации.
     * В концепции, используемой для реализации программы необходимо было разделить 3 типа данных:
     *      1. Верхний ряд цифр -> необходимые условия для игры, пишутся в столбец
     *      2. Левый ряд цифр -> необходимые условия для игры, пишутся в строку
     *      3. Основное поле для игры -> кнопки
     *
     * Каждый из параметров объявляется ниже
     */
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;
    private static final int LAYOUT_THREE = 2;

    /*
     * Объявление переменных:
     *      1. mInflater -> переводит информацию из XML в View, необходимость для Adapter
     *      2. upperText -> Array, массив с данными для верхнего ряда цифр
     *      3. leftText -> Array, массив с данными для левого ряда цифр
     *      4. puzzle -> TwoDimension Array -> двумерный массив с правильным решением пазла
     *      5. user_guest -> TwoDimension Array -> двумерный массив с предположением пользователя
     */
    private final LayoutInflater mInflater;
    private final String[] upperText;
    private final String[] leftText;
    private final int[][] puzzle;
    private final int[][] user_guest = new int[10][10];

    //объявление сборщика для Alert, необходимо для уведомления пользователя об успешном решении
    AlertDialog.Builder builder;

    /*
     * GridAdapter -> Конструктор класса, в нем проиходит обращение к хранилищам данных, для
     *                "импорта" необходимых ресурсов.
     *
     * @param context -> Контекст адаптера, содержит в себе все переменные и данные в реальном времени
     * @param lvl -> номер уровня по порядку, передается из MainActivity
     */
    GridAdapter(Context context, int lvl) {
        Resources res = context.getResources();

        /*
         * параметры line_id_lvl, upper_text_id, left_text_id -> строковые параметры, которые
         * формируют в себе ID ресурса, чтобы потом к нему обратится.
         *
         * параметры upper_text, left_text, puzzle -> формируют в себе данные из ресурсов, для работы
         * кода.
         * puzzle -> двумерный массив. содержит в себе правильное решеине пазла.
         */
        String line_id_lvl = "line_" + lvl + "_";
        String upper_text_id = "upper_text_puzzle_" + lvl;
        int upper_text_resID = res.getIdentifier(upper_text_id, "array", context.getPackageName());
        String[] upper_text = res.getStringArray(upper_text_resID);

        String left_text_id = "left_text_puzzle_" + lvl;
        int left_text_resID = res.getIdentifier(left_text_id, "array", context.getPackageName());
        String[] left_text = res.getStringArray(left_text_resID);

        int[][] puzzle = new int [10][10];
        for (int i = 0; i < puzzle.length; i++){
            String lineID = line_id_lvl + (i+1);
            int resID = res.getIdentifier(lineID, "array", context.getPackageName());
            puzzle[i] = res.getIntArray(resID);
        }
        this.puzzle = puzzle;
        this.upperText = upper_text;
        this.leftText = left_text;

        this.mInflater = LayoutInflater.from(context);
    }

    /*
     * В методе getItemViewType формируется представление, о том, какой вид представления
     * использовать для определенного элемента по его позиции. Если он меньше 11 -> то первый, так
     * как позиция элемента это его номер по порядку, а таблица 11 на 11. Если элемент делится на 11
     * без остатка - то это левая колонка, а все остальное - пазл.
     */
    @Override
    public int getItemViewType(int position)
    {
        if (position < 11)
        {
            return LAYOUT_ONE;
        }
        else if (position % 11 == 0) {
            return LAYOUT_TWO;
        }
        else {
            return LAYOUT_THREE;
        }
    }

    /*
     * метод, позволяющий коду узнать об общем кол-ве элементов. Необходим для правильного
     * формирования таблицы. Формирование таблицы идет от 0 до ItemCount,поэтому возвращать этот
     * метод должен правильное колличество. В данном случае возвращает сумму верхнего ряда и левого
     * плюс 100, так как игровое поле 10 на 10.
     */
    @Override
    public int getItemCount() {
        return leftText.length + upperText.length + 100;
    }

    /*
     * Основной метод в Adapter, формирует View тип для элементов. Исходя из номера элемента, и
     * его пренадлежности к нужной развертке - возвращает нужный View для адаптера.
     */
    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==LAYOUT_ONE)
        {
            View view = mInflater.inflate(R.layout.upper_text,parent,false);
            return new ViewHolderOne(view);
        }
        if(viewType==LAYOUT_TWO)
        {
            View view = mInflater.inflate(R.layout.left_text,parent,false);
            return new ViewHolderTwo(view);
        }
        else
        {
            View view = mInflater.inflate(R.layout.buttons,parent,false);
            return new ViewHolderThree(view);
        }
    }


    //****************  НАЧАЛО VIEW HOLDER НОМЕР 1 ******************//

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView tw;
        public ViewHolderOne(View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.text_upper);
        }
    }

    /*
     * Следующий блок кода определяет переменные для каждого конкретного View
     * Переменные и данные каждого View зависят от необходимых аргументов.
     */

    //****************  КОНЕЦ VIEW HOLDER НОМЕР 1 ******************//


    //****************  НАЧАЛО VIEW HOLDER НОМЕР 2 ******************//

    public class ViewHolderTwo extends RecyclerView.ViewHolder {

        TextView tw;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.left_text);
        }
    }

    //****************  КОНЕЦ VIEW HOLDER НОМЕР 2 ******************//

    //****************  НАЧАЛО VIEW HOLDER НОМЕР 3 ******************//

    public class ViewHolderThree extends RecyclerView.ViewHolder {
        Button btn;

        public ViewHolderThree(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_rv);
        }
    }

    //****************  КОНЕЦ VIEW HOLDER НОМЕР 3 ******************//

    /*
     * Метод onBindViewHolder производит "привязку" объявленных переменных, и данных из ресурсов
     * Привязка и функции каждого элемента зависят от нужного типа разветки. Для развертки,
     * отвечающей за игровое поле есть функции. для остальных -> только привязка текста к TextView
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if(holder.getItemViewType() == LAYOUT_ONE)
        {
            ViewHolderOne vaultItemHolder = (ViewHolderOne) holder;
            vaultItemHolder.tw.setText(upperText[position]);
            vaultItemHolder.tw.setTextColor(Color.WHITE);
        }
        if(holder.getItemViewType() == LAYOUT_TWO)
        {
            ViewHolderTwo vaultItemHolder = (ViewHolderTwo) holder;
            vaultItemHolder.tw.setText(leftText[(position / 11) - 1]);
            vaultItemHolder.tw.setMaxLines(1);
            vaultItemHolder.tw.setTextColor(Color.WHITE);
        }
        if(holder.getItemViewType() == LAYOUT_THREE)
        {
            ViewHolderThree vaultItemHolder = (ViewHolderThree) holder;
            vaultItemHolder.btn.setBackgroundColor(Color.WHITE);
            /*
             * Важно подметить, что каждый элемент в игровом поле - кнопка.
             * Соответственно, для каждой кнопки привязывается общая функция.
             * Суть функции такова:
             *      Каждый раз когда нажимается кнопка нужно сравнить решение, и состояние
             *      игрового поля игрока, чтобы удостовериться в решении пазла.
             *      Если решение игрока и истинный вид пазла совпадает -> игра завершается
             *      и вызывается Alert. Если не совпадает -> нужно изменить двумерный массив
             *      user_guest, и дать пользователю играть дальше.
             */
            vaultItemHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                     * buttonColor -> обращение к классу ColorDrawable, чтобы была возможность
                     * определить текущий цвет кнопки. Без этой хитрости - невозможно получить
                     * цвет кнопки, а значит невозможно и реализовать правильно изменение цвета на
                     * противоположный
                     */
                    ColorDrawable buttonColor = (ColorDrawable) view.findViewById(R.id.btn_rv).getBackground();
                    //colorId -> текущий цвет конкретной кнопки
                    int colorId = buttonColor.getColor();
                    //index_left -> получение позиции кнопки(они считаются от 0 до 121) по горизонтали
                    int index_left = (position/11)-1;
                    //index_up -> получение позиции кнопки(они считаются от 0 до 121) по вертикали
                    int index_up = (position%11)-1;
                    /*
                     * 0 -> эквивалент белого цвета в массиве user_guest
                     * 1 -> эквивалент черного цвета в массиве user_gues
                     * если цвет кнопки - черный -> изменить на белый, записать в user_guest 0
                     * если цвет кнопки - белый -> изменить на черный, записать в user_guest 1
                     */
                    if (colorId == Color.BLACK) {
                        view.findViewById(R.id.btn_rv).setBackgroundColor(Color.WHITE);
                        user_guest[index_left][index_up] = 0;
                    }
                    if (colorId == Color.WHITE) {
                        view.findViewById(R.id.btn_rv).setBackgroundColor(Color.BLACK);
                        user_guest[index_left][index_up] = 1;
                    }

                    /*
                     * В условии производится сравнение массивов puzzle(правильного решения)
                     * и user_guest -> предположения пользователя о правильном решении.
                     *
                     * Если массивы равны -> вызывается Builder для AlertDialog.
                     * Builder необходимым для установки параметров заголовка, основного текста
                     * кнопок и их действий у Alert(уведомления на весь экран).
                     *
                     * Когда пользователь решает правильно пазл - его уведомляют о правильном
                     * решении, и дают две опции -> перейти в главное меню, или остаться на уровне
                     */
                    if (Arrays.deepEquals(puzzle, user_guest)) {
                        builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Поздравляем, вы решили уровень!" ).setTitle("Поздравляем!");

                        builder.setMessage("Вы прошли уровень!\nВыйти в меню?")
                                .setCancelable(false)
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Activity activity = (Activity)view.getContext();
                                        activity.finish();
                                    }
                                })
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.setTitle("Поздравляем!");
                        alert.show();
                    }
                }
            });
        }
    }

}
