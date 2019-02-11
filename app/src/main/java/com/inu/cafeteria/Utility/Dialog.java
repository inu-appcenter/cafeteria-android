package com.inu.cafeteria.Utility;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inu.cafeteria.R;


public class Dialog extends android.app.Dialog {


    private TextView mTitleView;
    private TextView mContentView;
    private String mTitle;
    private String mContent;

    private Button mLeftButton;
    private Button mRightButton;
    private String mLeftButtonText;
    private String mRightButtonText;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.view_dialog);


        setLayout();
        setTitle(mTitle);
        setContent(mContent);
        setClickListener(mLeftClickListener, mRightClickListener, mLeftButtonText, mRightButtonText);

    }


    private void setLayout() {

        mTitleView = (TextView) findViewById(R.id.vd_tv_title);
        mContentView = (TextView) findViewById(R.id.vd_tv_content);
        mLeftButton = (Button) findViewById(R.id.vd_btn_left);
        mRightButton = (Button) findViewById(R.id.vd_btn_right);

    }

    private void setTitle(String title) {

        if (title == null) {
            mTitleView.setVisibility(View.GONE);
        }
        else {
            mTitleView.setText(title);
        }

    }

    private void setContent(String content) {
        mContentView.setText(content);
    }

    private void setClickListener(View.OnClickListener left, View.OnClickListener right,
                                  String mLeftButtonText, String mRightButtonText) {


        if (left != null && right != null) {

            mLeftButton.setOnClickListener(left);
            mRightButton.setOnClickListener(right);

            mLeftButton.setText(mLeftButtonText);
            mRightButton.setText(mRightButtonText);
        }

        else if (left != null && right == null) {

            mLeftButton.setOnClickListener(left);
            mLeftButton.setText(mLeftButtonText);

            mLeftButton.setGravity(Gravity.CENTER);
            mRightButton.setVisibility(View.GONE);
        }

    }



    // 버튼이 한 개인 다이얼로그
    public Dialog(Context context, String title , String content,
                  View.OnClickListener singleListener, String singleTitleText) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = singleListener;
        this.mLeftButtonText = singleTitleText;

    }

    // 버튼이 두 개인 다이얼로그
    public Dialog(Context context, String title, String content,
                  View.OnClickListener leftListener, View.OnClickListener rightListener,
                  String mLeftButtonText, String mRightButtonText) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.mLeftButtonText = mLeftButtonText;
        this.mRightButtonText = mRightButtonText;
    }



}
