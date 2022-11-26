package com.mickey.nonogram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class GridAdapter extends RecyclerView.Adapter {
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;
    private static final int LAYOUT_THREE = 2;


    private final LayoutInflater mInflater;
    private final String[] upperText;
    private final String[] leftText;
    private final int[][] puzzle;
    private final int[][] user_guest = new int[10][10];

    AlertDialog.Builder builder;

    GridAdapter(Context context, int lvl) {
        Resources res = context.getResources();

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

    @Override
    public int getItemCount() {
        //len of up, len of left, len of puzzle, left up empty block
        return leftText.length + upperText.length + 100;
    }


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


    //****************  VIEW HOLDER 1 ******************//

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView tw;

        public ViewHolderOne(View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.text_upper);
        }
    }


    //****************  VIEW HOLDER 2 ******************//

    public class ViewHolderTwo extends RecyclerView.ViewHolder {

        TextView tw;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.left_text);
        }
    }

    //****************  VIEW HOLDER 3 ******************//

    public class ViewHolderThree extends RecyclerView.ViewHolder {
        Button btn;

        public ViewHolderThree(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_rv);
        }
    }

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
            vaultItemHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ColorDrawable buttonColor = (ColorDrawable) view.findViewById(R.id.btn_rv).getBackground();
                    int colorId = buttonColor.getColor();
                    int index_left = (position/11)-1;
                    int index_up = (position%11)-1;
                    if (colorId == Color.BLACK) {
                        view.findViewById(R.id.btn_rv).setBackgroundColor(Color.WHITE);
                        user_guest[index_left][index_up] = 0;
                    }
                    if (colorId == Color.WHITE) {
                        view.findViewById(R.id.btn_rv).setBackgroundColor(Color.BLACK);
                        user_guest[index_left][index_up] = 1;
                    }
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
