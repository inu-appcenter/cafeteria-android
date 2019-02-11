package com.inu.cafeteria.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.Application.BackPressCloseHandler;
import com.inu.cafeteria.Model.LoginData;
import com.inu.cafeteria.Model.LoginResult;
import com.inu.cafeteria.Network.NetworkService;
import com.inu.cafeteria.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inu.cafeteria.R.id.al_view;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    NetworkService networkService;


    // 로그인 여부를 위한 정보
    LoginData loginData;

    SharedPreferences studentInfo, checkFirst;
    SharedPreferences.Editor student, isFirstEditor;
    String isAppFirst;


    // 회원 정보
    private Button btnLogin;
    private EditText ed_id;
    private EditText ed_pw;
    private CheckBox cb_login;
    private TextView btnLoginNoMember;

    FrameLayout imageview;
    AnimationDrawable animationDrawable;

    private RelativeLayout relativeLayoutStart;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;


    Animation animationFadeOut;
    Animation animationFadeIn;

    // 뒤로가기 버튼
    private BackPressCloseHandler backPressCloseHandler;


    Intent mainIntent;
    Bundle bundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initNetwork();
        initView();
        initEvent();
        animationView();
        checkIsAppFirstRun();

    }


    private void initNetwork() {

        networkService = ApplicationController.getInstance().getNetworkService();

    }


    private void initView() {

        btnLogin = (Button) findViewById(R.id.al_btn_login);
        ed_id = (EditText) findViewById(R.id.al_edit_id);
        ed_pw = (EditText) findViewById(R.id.al_edit_pw);
        cb_login = (CheckBox) findViewById(R.id.al_chk_autoLogin);
        btnLoginNoMember = (TextView) findViewById(R.id.al_text_nonmember);

        imageview = (FrameLayout) findViewById(al_view);

        relativeLayoutStart = (RelativeLayout) findViewById(R.id.al_layout_start);
        relativeLayout = (RelativeLayout) findViewById(R.id.al_layout_first_splash);
        linearLayout = (LinearLayout) findViewById(R.id.al_layout_login);


        studentInfo = getSharedPreferences("studentInfo", Activity.MODE_PRIVATE);
        checkFirst = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);

        isAppFirst = checkFirst.getString("isFirst", null);

        student = studentInfo.edit();
        isFirstEditor = checkFirst.edit();

        animationFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

        backPressCloseHandler = new BackPressCloseHandler(this);

        mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        bundle = new Bundle();

    }


    private void initEvent() {
        btnLogin.setOnClickListener(this);
        btnLoginNoMember.setOnClickListener(this);

        relativeLayoutStart.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }


    private void animationView() {

        animationDrawable = (AnimationDrawable) (imageview.getBackground() != null && imageview.getBackground() instanceof AnimationDrawable ?
                imageview.getBackground() : null);

        if (animationDrawable != null) {
            //animationDrawable.setEnterFadeDuration(10);
            animationDrawable.setExitFadeDuration(2000);
            animationDrawable.setAlpha(240);
        }
    }


    private void checkIsAppFirstRun() {

        isAppFirst = checkFirst.getString("isFirst", null);

        if (isAppFirst != null) {
            relativeLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        else {
            SharedPreferences.Editor isFirstEditor = checkFirst.edit();
            isFirstEditor.putString("isFirst", "yes");
            isFirstEditor.commit();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            // 로그인 버튼 누르는 경우
            case R.id.al_btn_login:

                loginData = new LoginData(
                        ed_id.getText().toString(),
                        ed_pw.getText().toString(),
                        cb_login.isChecked(),
                        "",
                        "android"
                );


                Call<LoginResult> loginResultCall = networkService.getLoginResult(loginData);
                loginResultCall.enqueue(new Callback<LoginResult>() {

                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.isSuccessful()) {

                            // 자동로그인이 체크 된 경우
                            if(cb_login.isChecked()) {
                                student.putString("inputtoken", response.body().getToken().toString());
                            }

                            student.putString("inputsno", ed_id.getText().toString());
                            student.putString("inputbarcode", response.body().getBarcode().toString());
                            student.commit();

                            startActivity(mainIntent);
                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this, "로그인 정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("debug, ", response.errorBody().toString());
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {

                        Toast.makeText(LoginActivity.this, "인터넷 환경을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        Log.d("debug, ", t.getMessage().toString());

                    }

                });

                break;


            // 비회원 로그인 버튼 누르는 경우
            case R.id.al_text_nonmember:

                student.clear();
                student.commit();

                startActivity(mainIntent);
                finish();

                break;


            case R.id.al_layout_start:

                // fadeOutFirstSplash
                relativeLayout.setAnimation(animationFadeOut);
                relativeLayout.setVisibility(View.GONE);

                // fadeInLogin
                linearLayout.setAnimation(animationFadeIn);
                linearLayout.setVisibility(View.VISIBLE);

                break;


            // 배경화면 터치 할 경우 키보드 내림
            case R.id.al_layout_login:

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;


            default:

                break;
        }

    }


    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressedActivity();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }


}
