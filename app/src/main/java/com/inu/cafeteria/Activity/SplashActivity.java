package com.inu.cafeteria.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.Application.MarketVersionChecker;
import com.inu.cafeteria.EmergencyTemporary.VersionResult;
import com.inu.cafeteria.Model.LoginData;
import com.inu.cafeteria.Model.LoginResult;
import com.inu.cafeteria.Model.MessageResult;
import com.inu.cafeteria.Model.WaitData;
import com.inu.cafeteria.Model.WaitResult;
import com.inu.cafeteria.Network.NetworkService;
import com.inu.cafeteria.R;
import com.inu.cafeteria.Utility.Dialog;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {


    NetworkService networkService;

    // 로그인 여부를 위한 정보
    WaitData waitData;
    LoginData loginData;

    SharedPreferences studentInfo, checkNoReplayNotice;
    SharedPreferences.Editor student, noReplayNoticeDate;
    String token;

    // 공지사항 체크
    private String alreadyConfirmAllNotice;
    private String alreadyConfirmAndroidNotice;
    private String curAllNoticeId;
    private String curAndroidNoticeId;


    // 버전 체크
    String storeVersion;
    String deviceVersion;
    private BackgroundThread mBackgroundThread;
    private DeviceVersionCheckHandler deviceVersionCheckHandler;


    // 다이얼로그
    private Dialog mDialog;

    Intent loginIntent, mainIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initNetwork();
        initView();



        // 버전 체크
        mBackgroundThread.start();

    }


    private void initNetwork() {

        networkService = ApplicationController.getInstance().getNetworkService();

    }


    private void initView() {

        studentInfo = getSharedPreferences("studentInfo", Activity.MODE_PRIVATE);
        checkNoReplayNotice = getSharedPreferences("checkNoReplayNotice", Activity.MODE_PRIVATE);

        student = studentInfo.edit();
        noReplayNoticeDate = checkNoReplayNotice.edit();

        token = studentInfo.getString("inputtoken", null);

        mBackgroundThread = new BackgroundThread();
        deviceVersionCheckHandler = new DeviceVersionCheckHandler(this);


        loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        mainIntent = new Intent(SplashActivity.this, MainActivity.class);
    }


    // 버전 체크
    public class BackgroundThread extends Thread {



        @Override
        public void run() {

//            storeVersion = MarketVersionChecker.getMarketVersionFast(getPackageName());

            Call<VersionResult> versionResultCall = networkService.getVersionREsult();
            versionResultCall.enqueue(new Callback<VersionResult>() {
                @Override
                public void onResponse(Call<VersionResult> call, Response<VersionResult> response) {
                    if(response.isSuccessful()) {

                        storeVersion = response.body().android.latest;

                        // 디바이스 버전 가져옴
                        try {
                            deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }


                        // 핸들러로 메세지 전달
                        deviceVersionCheckHandler.sendMessage(deviceVersionCheckHandler.obtainMessage());
                    } else {
                        mDialog = new Dialog(SplashActivity.this,
                                "오류",
                                "네트워크 연결을 확인해주세요.",
                                errorCloseApp,
                                "확인");
                        mDialog.show();

                    }
                }

                @Override
                public void onFailure(Call<VersionResult> call, Throwable t) {
                    mDialog = new Dialog(SplashActivity.this,
                            "오류",
                            "네트워크 연결을 확인해주세요.",
                            errorCloseApp,
                            "확인");
                    mDialog.show();
                }
            });

            // 패키지 네임 전달



        }
    }


    // 핸들러 객체 만들기
    private static class DeviceVersionCheckHandler extends Handler {

        private final WeakReference<SplashActivity> mainActivityWeakReference;

        public DeviceVersionCheckHandler(SplashActivity splashActivity) {
            mainActivityWeakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {

            SplashActivity activity = mainActivityWeakReference.get();

            if (activity != null) {
                // 핸들 메세지로 결과 값 전달
                activity.handleMessage(msg);
            }
        }
    }


    // 핸들러에서 넘어온 값 체크
    private void handleMessage(Message msg) {

        // 서버 연결이 원활하지 않은 경우
        if(storeVersion == null) {
            mDialog = new Dialog(SplashActivity.this,
                    "오류",
                    "버전 정보를 가져오지 못했습니다.\n데이터 연결을 확인해주세요.",
                    errorCloseApp,
                    "확인");
            mDialog.show();

            return;
        }

        // 업데이트 필요
        if (storeVersion.compareTo(deviceVersion) > 0) {
            Log.d("version", storeVersion.compareTo(deviceVersion) + "");
            mDialog = new Dialog(SplashActivity.this,
                    "업데이트",
                    "수정 사항이 있으니,\n업데이트 해주시기 바랍니다.",
                    updateConfirm,
                    "확인");
            mDialog.show();

            return;
        }

        // 업데이트 불필요
        else {

            // 공지사항
            showNotification();

        }
    }


    // 공지사항
    private void showNotification() {


        Call<MessageResult> call = networkService.getMessageResult();
        call.enqueue(new Callback<MessageResult>() {

            @Override
            public void onResponse(Call<MessageResult> call, final Response<MessageResult> response) {

                alreadyConfirmAllNotice = checkNoReplayNotice.getString("all", "");
                alreadyConfirmAndroidNotice = checkNoReplayNotice.getString("android", "");
                curAllNoticeId = response.body().getAll().getId();
                curAndroidNoticeId = response.body().getAndroid().getId();

                if (response.isSuccessful()) {

                    // 공통 공지사항
                    if (!response.body().getAll().getMessage().equals("") && !alreadyConfirmAllNotice.equals(curAllNoticeId)) {

                        mDialog = new Dialog(SplashActivity.this,
                                response.body().getAll().getTitle(),
                                response.body().getAll().getMessage(),
                                initConfirm,
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        noReplayNoticeDate.putString("all", response.body().getAll().getId());
                                        noReplayNoticeDate.commit();

                                        mDialog.cancel();
                                        checkPush();
                                    }
                                },
                                "확인",
                                "다시 보지않기");
                        mDialog.show();
                    }

                    // 안드로이드 공지사항
                    else if (!response.body().getAndroid().getMessage().equals("") && !alreadyConfirmAndroidNotice.equals(curAndroidNoticeId)) {

                        mDialog = new Dialog(SplashActivity.this,
                                response.body().getAndroid().getTitle(),
                                response.body().getAndroid().getMessage(),
                                initConfirm,
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        noReplayNoticeDate.putString("android", response.body().getAndroid().getId());
                                        noReplayNoticeDate.commit();

                                        mDialog.cancel();
                                        checkPush();
                                    }
                                },
                                "확인",
                                "다시 보지않기");
                        mDialog.show();

                    }

                    // 공지사항이 모두 없는 경우
                    else {
                        checkPush();
                    }
                }
                else {
                    Log.d("debug, ", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {

                Log.d("debug", t.getMessage().toString());

                Toast.makeText(SplashActivity.this, "서버 접속이 끊겼습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void checkPush() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 기다리는 푸시가 있는지 확인
                checkPushExistOrNot();
            }
        }, 500);
    }


    // 기다리는 푸시가 있는지 확인
    private void checkPushExistOrNot() {

        waitData = new WaitData(FirebaseInstanceId.getInstance().getToken());

        Call<WaitResult> waitResultCall = networkService.getWaitResult(waitData);
        waitResultCall.enqueue(new Callback<WaitResult>() {

            @Override
            public void onResponse(Call<WaitResult> call, Response<WaitResult> response) {

                // 기다리는 푸시가 없는 경우
                if (response.body() == null) {

                    // 자동 로그인
                    if (token != null) {
                        autoLogin();
                    }
                    // 수동 로그인
                    else {
                        startActivity(loginIntent);
                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }


                // 기다리는 푸시가 있는 경우
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("code", response.body().getCafecode());
                    bundle.putString("name", ApplicationController.getInstance().getCoverflowTitles()[Integer.parseInt(response.body().getCafecode()) % 7]);

                    // 기다리고 있는 푸시 번호 입력
                    for (int i = 0; i < response.body().getNum().size(); i++) {

                        String numBody = response.body().getNum().get(i);
                        String tmp = "";

                        if (!numBody.toString().equals(null)) {
                            tmp = numBody.toString() + "";
                        }
                        bundle.putString("num" + (i + 1), tmp.equals(null) ? "-1" : tmp);
                    }
                    for (int i = response.body().getNum().size(); i < 3; i++) {

                        bundle.putString("num" + (i + 1), "-1");
                    }

                    mainIntent.putExtras(bundle);
                    startActivity(mainIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<WaitResult> call, Throwable t) {

                Log.d("debug, ", t.getMessage().toString());
                startActivity(loginIntent);
                finish();
            }
        });
    }



    // 자동 로그인 여부 확인
    private void autoLogin() {

        loginData = new LoginData(
                "",
                "",
                true,
                token,
                ""
        );


        Call<LoginResult> loginResultCall = networkService.getLoginResult(loginData);
        loginResultCall.enqueue(new Callback<LoginResult>() {

            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                // 서버에서 로그인 관련 데이터를 지운 경우
                if (response.body() == null) {

                    student.clear();
                    student.commit();

                    startActivity(loginIntent);
                    finish();

                } else {

                    student.putString("inputbarcode", response.body().getBarcode());
                    student.commit();

                    startActivity(mainIntent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {

                Log.d("debug, ", t.getMessage().toString());
                startActivity(loginIntent);
                finish();

            }
        });
    }


    private View.OnClickListener errorCloseApp = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            mDialog.cancel();
            finish();
        }
    };

    private View.OnClickListener updateConfirm = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(Intent.ACTION_VIEW);

            if (ApplicationController.getInstance().getNation().equals("korea")) {
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            }
            else if (ApplicationController.getInstance().getNation().equals("china")) {
//                intent.setData(Uri.parse("http://uicoop.ac.kr/mobile/notice_view.html?num=1279&page=1&seltb="));
                intent.setData(Uri.parse("http://inucafeteriaaws.us.to:3829/INU%20Cafeteria.apk"));
            }

            startActivity(intent);
        }
    };

    private View.OnClickListener initConfirm = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            mDialog.cancel();
            checkPush();
        }
    };

    private View.OnClickListener noReplayNotice = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            noReplayNoticeDate.putString("date", "1");
            noReplayNoticeDate.commit();

            mDialog.cancel();
            checkPush();
        }
    };

}
