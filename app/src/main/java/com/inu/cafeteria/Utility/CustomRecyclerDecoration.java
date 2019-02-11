package com.inu.cafeteria.Utility;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomRecyclerDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public CustomRecyclerDecoration(int divHeight) {
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = divHeight;
    }
}

