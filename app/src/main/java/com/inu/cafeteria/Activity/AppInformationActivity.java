package com.inu.cafeteria.Activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inu.cafeteria.R;

public class AppInformationActivity extends AppCompatActivity implements View.OnClickListener {


//    Button btnBack;
    TextView textVersion;
    String versionName;

    PackageInfo pi;

    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_infomation);


        initView();
        initEvent();
        initActionbar();

        // 버전 가져오기
        setVersion();
    }


    private void initView() {

        // 뷰 초기화
//        btnBack = (Button) findViewById(R.id.aai_btn_back);
        textVersion = (TextView) findViewById(R.id.aai_text_version);

    }

    private void initEvent() {

//        btnBack.setOnClickListener(this);
    }

    private void setVersion() {

        try {

            pi = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
            Log.d("PackageManager", "NameNotFoundException e Errer");
        }

        versionName = pi.versionName;
        textVersion.setText(versionName);

    }

    private void initActionbar() {

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.vt_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

//            case R.id.aai_btn_back:
//                finish();
//                break;

            default:
                break;
        }

    }
}
