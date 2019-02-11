package com.inu.cafeteria.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.Model.ErrorMsgData;
import com.inu.cafeteria.Model.ErrorMsgResult;
import com.inu.cafeteria.Network.NetworkService;
import com.inu.cafeteria.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquireActivity extends AppCompatActivity implements View.OnClickListener{


    NetworkService networkService;


    EditText editErrormsg;
    Button btnSend;


    // 액션바, 드로어
    private android.support.v7.widget.Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    ErrorMsgData errorMsgData;

    SharedPreferences studentInfo;
    String sno, sname;

    CheckBox consentInfoCollection;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);


        initNetwork();
        initView();
        initEvent();
        initActionbar();
        initActionbarMenu();

    }

    private void initNetwork() {

        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void initView() {

        editErrormsg = (EditText) findViewById(R.id.ai_edit_errormsg);
        btnSend = (Button) findViewById(R.id.ai_btn_send);


        studentInfo = getSharedPreferences("studentInfo", Activity.MODE_PRIVATE);
        sno = studentInfo.getString("inputsno", null);
        sname = studentInfo.getString("inputsname", null);

        consentInfoCollection = (CheckBox) findViewById(R.id.ai_checkbox_consent_to_personal_information_collection);

    }

    private void initEvent() {

        btnSend.setOnClickListener(this);
    }

    private void initActionbar() {

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.vt_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    // 툴바 메뉴 이미지 설정 및 활성화
    private void initActionbarMenu(){

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.btn_back);
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    // 툴바에 있는 뒤로가기 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ai_btn_send:

                if (editErrormsg.getText().toString().equals("")) {

                    Toast.makeText(this, "문의내용이 입력 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("Gayoon, ", Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE + " " + Build.VERSION.SDK_INT + Build.DEVICE.toString());
                    Log.d("Gayoon, ", BluetoothAdapter.getDefaultAdapter().getName());


                } else if(!consentInfoCollection.isChecked()) {
                    Toast.makeText(this, "개인정보 수집을 동의해주세요.", Toast.LENGTH_SHORT).show();
                }

                else {

                    // 디바이스 정보 (ex) Samsung Galaxy S7 7.0 (SM-G930S) sdk25
//                    String deviceInfo = Build.BRAND + " " + BluetoothAdapter.getDefaultAdapter().getName() + " " + Build.VERSION.RELEASE + " (" + Build.MODEL + ") sdk" + Build.VERSION.SDK_INT;
                    String deviceInfo = "";
                    // 학번, 메시지, 디바이스 정보, 패키지 네임
//                    errorMsgData = new ErrorMsgData(sno, editErrormsg.getText().toString(), deviceInfo, getPackageName());
                    errorMsgData = new ErrorMsgData(sno, editErrormsg.getText().toString(), deviceInfo, getPackageName());

                    Call<ErrorMsgResult> errorMsgResultCall = networkService.getErrorMsgResult(errorMsgData);
                    errorMsgResultCall.enqueue(new Callback<ErrorMsgResult>() {

                        @Override
                        public void onResponse(Call<ErrorMsgResult> call, Response<ErrorMsgResult> response) {

//                            Log.d("Gayoon, ", Build.BRAND + " " + BluetoothAdapter.getDefaultAdapter().getName() + " " + Build.VERSION.RELEASE + " (" + Build.MODEL + ") sdk" + Build.VERSION.SDK_INT);
                        }

                        @Override
                        public void onFailure(Call<ErrorMsgResult> call, Throwable t) {

                        }

                    });


                    Toast.makeText(this, "문의사항이 접수 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }
}
