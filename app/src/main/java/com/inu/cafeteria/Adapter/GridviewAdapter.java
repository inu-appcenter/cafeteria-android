package com.inu.cafeteria.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> food;

    public GridviewAdapter(Context context) {
        mContext = context;
        food = new ArrayList<>();
    }

    @Override
    public int getCount() {
        // 뷰에 나타날 데이터 개수 반환
        return food.size();
    }

    @Override
    public Object getItem(int i) {
        // i번째에 해당하는 데이터 반환
        return food.get(i);
    }

    @Override
    public long getItemId(int i) {
        // i번째에 해당하는 데이터의 아이디를 반환
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv;

        if (view == null) {
            tv = new TextView(mContext);
            tv.setPadding(10, 10, 40, 40);
            tv.setTextSize(25);
            tv.setTextColor(Color.parseColor("#6296ae"));
            tv.setGravity(1);
        } else {
            tv = (TextView) view;
        }
        tv.setText(food.get(i));
        return tv;
    }

    public void add(String number) {
        food.add(number);
        if (food.size() > 12) {
            food.remove(0);
        }
    }
}