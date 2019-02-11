package com.inu.cafeteria.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.CoverFlow.ICoverFlowAdapter;
import com.inu.cafeteria.Model.CoverflowModel;
import com.inu.cafeteria.R;
import com.inu.cafeteria.Utility.MyBitmapImageViewTarget;

import java.util.List;

public class CoverFlowAdapter implements ICoverFlowAdapter {

    private List<CoverflowModel> mArray;
    private Context context;
    private int[] coverflowIsMenu;
    private boolean[] coverflowIsAlarm;

    public CoverFlowAdapter(Context context, List<CoverflowModel> mArray) {
        this.context = context;
        this.mArray = mArray;
        coverflowIsMenu = ApplicationController.getInstance().getCoverflowIsMenu();
        coverflowIsAlarm = ApplicationController.getInstance().getCoverflowIsAlarm();

    }

    @Override
    public int getCount() {
        return mArray == null ? 0 : mArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;

        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.view_coverflow_item, null);
            holder.iv_code = (ImageView) convertView.findViewById(R.id.vci_image_code);
            holder.isMenu = (ImageView) convertView.findViewById(R.id.vci_image_is_menu);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public void getData(View view, int position) {

        Holder holder = (Holder) view.getTag();

        CoverflowModel coverflowmodel = mArray.get(position);

        // Glide : 이미지 로딩 라이브러리
        // app gradle에 다음 소스 추가함
        // compile 'com.github.bumptech.glide:glide:3.7.0'
        Glide.with(context)
                .load(coverflowmodel.getImg())
                .asBitmap()
                .centerCrop()
                .fitCenter()
                .into(new MyBitmapImageViewTarget(holder.iv_code));

        // no menu, hide the menu icon
        if(coverflowIsMenu[position] == -1) {
            holder.isMenu.setVisibility(View.INVISIBLE);
        } else {
            holder.isMenu.setVisibility(View.VISIBLE);
        }
    }


    public static class Holder {
        ImageView iv_code;
        ImageView isMenu;
        RelativeLayout inputLock;
    }

}
