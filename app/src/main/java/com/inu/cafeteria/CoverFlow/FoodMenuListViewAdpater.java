package com.inu.cafeteria.CoverFlow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.inu.cafeteria.R;


/**
 * Created by ksj on 2018. 3. 13..
 */

public class FoodMenuListViewAdpater extends BaseAdapter {

    JsonArray jsonArray;
    Context context;

    public FoodMenuListViewAdpater(Context context) {
        this.context = context;
        this.jsonArray = new JsonArray();
    }

    @Override
    public int getCount() {
        return jsonArray.size();
    }

    public void setItems(JsonArray items) {
        jsonArray = items;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return jsonArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_food_menu, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.ifm_text_title) ;
        TextView menuTextView = (TextView) convertView.findViewById(R.id.ifm_text_menu) ;

//        if(position == this.getCount() - 1) {
//            titleTextView.setText("");
//            menuTextView.setText("");
//            return convertView;
//        }

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        JsonObject dividedInfoByTitle = jsonArray.get(position).getAsJsonObject();

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(dividedInfoByTitle.get("TITLE").getAsString());
        menuTextView.setText(dividedInfoByTitle.get("MENU").getAsString());

        return convertView;
    }
}
