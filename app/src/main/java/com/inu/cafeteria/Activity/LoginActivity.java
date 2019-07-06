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

/**
 * 로그인을 처리하는 액티비티
 * 처음이면 안내화면을 띄워줌.
 *
 * 루틴 실행 순서:
 * (onCreate)
 * 1. 필드 초기화
 * 2. 리스너 설정
 * 3. 애니메이션 설정
 * 4. 앱 실행이 처음이면 안내 레이아웃 표시
 *
 * (onResume)
 * 5. 애니메이션 시작
 *
 * (사용자가 버튼 클릭)
 * 6. 버튼 종류에 따라 처리
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * retrofit 객체. ApplicationController에서 가져올 예정.
     */
    NetworkService networkService;

    /**
     * 서버에 보낼 데이터를 담을 객체
     */
    LoginData loginData;

    /**
     * 자동로그인 정보 쓸 shared preference
     */
    SharedPreferences studentInfo, checkFirst;
    SharedPreferences.Editor student, isFirstEditor;
    String isAppFirst;

    /**
     * 회원 정보 담을 UI 객체들
     */
    private Button btnLogin;
    private EditText ed_id;
    private EditText ed_pw;
    private CheckBox cb_login;
    private TextView btnLoginNoMember;

    /**
     * 기타 UI 객체들
     */
    FrameLayout imageview; // 이름은 imageview인데 사실은 FrameLayout이다.....
    AnimationDrawable animationDrawable;

    /**
     * 사용할 레이아웃들
     */
    private RelativeLayout relativeLayoutStart;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    /**
     * 사용할 애니메이션들
     */
    Animation animationFadeOut;
    Animation animationFadeIn;

    /**
     * 뒤로가기 버튼 동작을 처리할 녀석. Handler 이름을 달고 있지만 Handler와는 관련 없음.
     */
    private BackPressCloseHandler backPressCloseHandler;

    /**
     * 메인 액티비티를 띄울 인텐트
     */
    Intent mainIntent;

    /**
     * 액티비티 띄울 떄에 쓸 번들
     */
    Bundle bundle;


    /**
     * 진입점
     * @param savedInstanceState 안써요
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // networkService 객체 대입
        initNetwork();

        // 객체 초기화
        initView();

        // 클릭 리스너 설정
        initEvent();

        // 배경 애니메이션 설정
        animationView();

        // 앱 실행이 처음이면 안내를 띄움
        checkIsAppFirstRun();
    }

    /**
     * ApplicationController에서 networkService 가져오기
     */
    private void initNetwork() {
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    /**
     * UI와 기타 객체들 초기화
     */
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

    /**
     * 클릭 리스너 설정
     */
    private void initEvent() {
        btnLogin.setOnClickListener(this);
        btnLoginNoMember.setOnClickListener(this);

        relativeLayoutStart.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }

    /**
     * 애니메이션 설정
     */
    private void animationView() {

        // 이미지뷰의 배경이 null이 아니며 AnimationDrawable 타입일 때에만 가져옴
        animationDrawable = (AnimationDrawable)
                        (imageview.getBackground() != null && imageview.getBackground() instanceof AnimationDrawable
                                ? imageview.getBackground()
                                : null);

        if (animationDrawable != null) {
            /*
             * 잘 가져왔으면 애니메이션 설정해줌.
             * 이 drawable은 @drawable/al_animation.xml에서 온 것임.
             */

            //animationDrawable.setEnterFadeDuration(10);
            animationDrawable.setExitFadeDuration(2000);
            animationDrawable.setAlpha(240);
        }
    }

    /**
     * 앱 실행이 처음인 경우에는 안내를 띄워줍니다.
     */
    private void checkIsAppFirstRun() {

        // shared preference로부터 isFirst에 대해 가져옵니다.
        isAppFirst = checkFirst.getString("isFirst", null);

        if (isAppFirst != null) {
            /*
             * 처음이 아니며 싶으면 안내는 생략해줍니다.
             */
            relativeLayout.setVisibility(View.GONE); /* 스플래시 레이아웃 */
            linearLayout.setVisibility(View.VISIBLE); /* 로그인 레이아웃 */
        }
        else {
            /*
             * 처음이면 그렇다고 써줍니다.
             */
            SharedPreferences.Editor isFirstEditor = checkFirst.edit();
            isFirstEditor.putString("isFirst", "yes");
            isFirstEditor.commit(); /* 백그라운드에서 쓰는 apply()가 더 좋다고 doc이 그러네요 */
        }
    }

    /**
     * 이 액티비티에 들어있는 버튼이 눌렸을 때에 실행될 녀석.
     * @param view 어떤 녀석이 눌렸는가
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            /*
             * 로그인 버튼이 눌린 경우:
             * 학번과 비밀번호 string을 가져와서 로그인 정보를 생성한 뒤에
             * 서버르 던져서 결과를 받음.
             * 로그인 성공하면 서버가 준 토큰과 바코드를 잘 저장함.
             */
            case R.id.al_btn_login: {

                // 입력된 데이터를 기반으로 loginData 생성
                loginData = new LoginData(
                        ed_id.getText().toString(),
                        ed_pw.getText().toString(),
                        cb_login.isChecked(),
                        "",
                        "android"
                );

                Call<LoginResult> loginResultCall = networkService.getLoginResult(loginData);
                loginResultCall.enqueue(new Callback<LoginResult>() {

                    /**
                     * 서버와 통신 성공!
                     *
                     * @param call
                     * @param response
                     */
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.isSuccessful()) {
                            /*
                             * 로그인 성공인 경우
                             */

                            // 자동로그인이 체크 된 경우에는 반환된 토큰 저장.
                            if (cb_login.isChecked()) {
                                student.putString("inputtoken", response.body().getToken().toString());
                            }

                            // 받아온 바코드도 저장.
                            student.putString("inputsno", ed_id.getText().toString());
                            student.putString("inputbarcode", response.body().getBarcode().toString());
                            student.commit();

                            // 메인 액티비티로 넘어감.
                            startActivity(mainIntent);
                            finish();

                        } else {
                            /*
                             * 로그인 실패인 경우!
                             */

                            Toast.makeText(LoginActivity.this, "로그인 정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("debug, ", response.errorBody().toString());
                        }
                    }

                    /**
                     * 실패하면 뭐... 그럴 수 있지..
                     *
                     * @param call
                     * @param t
                     */
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "인터넷 환경을 설정해주세요.", Toast.LENGTH_SHORT).show();
                        Log.d("debug, ", t.getMessage().toString());
                    }
                });

                break;
            }

            /*
             * 비회원 로그인 벝튼이 눌린 경우:
             * 그냥 진행한다.
             */
            case R.id.al_text_nonmember: {

                // 학생 정보가 없으니 지워버린다!
                student.clear();
                student.commit();

                startActivity(mainIntent);
                finish();

                break;
            }

            /*
             * 안내 화면에서 빠져나가고 싶은 의지가 표명되었을 때:
             * 안내 화면 페이드 아웃, 로그인 화면 페이드 인.
             */
            case R.id.al_layout_start: {

                // 주의: relativeLayout은 안내 화면이다.
                // 안내 화면 없애고,
                relativeLayout.setAnimation(animationFadeOut);
                relativeLayout.setVisibility(View.GONE);

                // 주의: linearLayout은 로그인 화면이다.
                // 로그인 화면 표시하기.
                linearLayout.setAnimation(animationFadeIn);
                linearLayout.setVisibility(View.VISIBLE);

                break;
            }

            /*
             * 그냥 배경이 눌린 경우에는 키보드 치우기
             */
            case R.id.al_layout_login: {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
            }

            default:
                break;
        }
    }

    /**
     * 뒤로가기가 눌렸을때, backPressCloseHandler에게 맡겨버림.
     */
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressedActivity();
    }

    /**
     * 애니메이션 시작해줌.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    /**
     * 애니메이션 멈춰줌.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }
}
