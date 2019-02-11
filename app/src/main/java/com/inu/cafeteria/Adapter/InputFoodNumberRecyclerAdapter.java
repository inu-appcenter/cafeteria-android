package com.inu.cafeteria.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.inu.cafeteria.R;

public class InputFoodNumberRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_FOOTER = 3;

    private int count;
    Context context;
    String[] loadData;

    private EditText firstInputFoodNumber;
    private int hintSize = 13;
    private int textSize = 24;

    public InputFoodNumberRecyclerAdapter(Context context, int count, String[] loadData) {
        this.context = context;
        this.count = count;
        this.loadData = loadData;
        Log.i("loadData", this.loadData[0] + " " + this.loadData[1] + " " + this.loadData[2]);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recycler_input_food_number_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        }
        else if (holder instanceof FooterViewHolder) {
            // 뷰타입이 푸터 생성하기 버튼인 경우
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        }
        else if (holder instanceof ViewHolder) {
            // 뷰타입이 아이템인 경우
            ViewHolder itemHolder = (ViewHolder) holder;
            if(position == 0){
                firstInputFoodNumber = itemHolder.editFoodNumber;
                firstInputFoodNumber.setHint("주문번호를 입력해주세요");
                firstInputFoodNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, hintSize);
                // for differentiate text size and hint size
                firstInputFoodNumber.addTextChangedListener(textWatcherInput);
            }

            ((ViewHolder) holder).editFoodNumber.setText(loadData[position]);
            ((ViewHolder) holder).editFoodNumber.requestFocus();
        }
    }



    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == count) { return  TYPE_FOOTER; }
        else if (position == 0) { return TYPE_HEADER; }
        return  TYPE_ITEM;

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        EditText editFoodNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            editFoodNumber = (EditText) itemView.findViewById(R.id.vrifni_edit_input_food_number);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addList() {
        count++;
    }

    TextWatcher textWatcherInput = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() != 0) {
                // set the text size
                firstInputFoodNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            } else {
                // set the hint size
                firstInputFoodNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, hintSize);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}