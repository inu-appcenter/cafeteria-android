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

/**
 * 애플리케이션 시작점.
 * 이미 등록된 대기번호와 로그인 상태를 처리함.
 *
 * 루틴 실행 순서는 다음과 같다:
 * 1. 필드 초기화
 * 2. 버전 확인
 * 3. 공지사항 확인
 * 4. 등록되어있는 대기번호 확인
 * 5. 로그인 처리 -> 로그인화면으로 안내 또는 메인액티비티 실행
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * 레트로핏 인터페이스 정의.
     * 로그인, 에러, 음식 메뉴, 광고 등을 가져오는 메소드 포함.
     */
    NetworkService networkService;

    /**
     * retrofit 수행 결과가 담길 곳.
     */
    WaitData waitData;
    LoginData loginData;

    /**
     * 로그인 정보와 설명 다시 안보기 체크 여부를 저장할 shared preference.
     */
    SharedPreferences studentInfo, checkNoReplayNotice;
    SharedPreferences.Editor student, noReplayNoticeDate;
    String token;

    /**
     * 공지사항 가져와서 담을 문자열.
     */
    private String alreadyConfirmAllNotice;
    private String alreadyConfirmAndroidNotice;
    private String curAllNoticeId;
    private String curAndroidNoticeId;

    /**
     * 버전 확인할 때에 쓸 문자열.
     */
    String storeVersion;
    String deviceVersion;

    /**
     * 백그라운드 스레드. SplashActivity 안에 정의됨.
     */
    private BackgroundThread mBackgroundThread;

    /**
     * 버전 확인할 때에 쓸 handler.
     */
    private DeviceVersionCheckHandler deviceVersionCheckHandler;

    /**
     * 그냥 다이얼로그.
     */
    private Dialog mDialog;

    /**
     * LoginActivity 띄을 intent.
     * initView()에서 초기화됨.
     */
    Intent loginIntent;

    /**
     * MainActivity 띄울 intent.
     * initView()에서 초기화됨.
     */
    Intent mainIntent;


    /**
     * 시작점
     * @param savedInstanceState 안씀. 의미없음.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 네트워크 설정
        initNetwork();

        // 필드 초기화
        initView();

        // 버전 확인
        mBackgroundThread.start();
    }

    /**
     * networkService 객체를 채워넣는다.
     */
    private void initNetwork() {
        /*
         * ApplicationController의 인스턴스로부터 networkService 객체를 받아온다.
         * ApplicationController는...의존성 주입과 비슷한 개념으로 활용한 것 같다..신박하다..
         */
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    /**
     * 위에 선언된 필드에 대한 초기화를 여기에서 한다.
     */
    private void initView() {

        /*
         * shared preference를 여기에서 가져온다. 조금 느릴 수도 있다.
         */
        studentInfo = getSharedPreferences("studentInfo", Activity.MODE_PRIVATE);
        checkNoReplayNotice = getSharedPreferences("checkNoReplayNotice", Activity.MODE_PRIVATE);

        /*
         * editor 객체도 가져온다.
         * 그런데 document는 이렇게 쓰지 말라고 한다.
         * edit()을 했으면 apply()나 commit()을 하라고 한다..
         * editor 객체를 들고다니는게 싫은가보다...
         */
        student = studentInfo.edit();
        noReplayNoticeDate = checkNoReplayNotice.edit();

        /*
         * 토큰이 이거였구나
         */
        token = studentInfo.getString("inputtoken", null);

        /*
         * 버전 확인하는 백그라운드 스레드 객체를 여기에서 만든다.
         */
        mBackgroundThread = new BackgroundThread();

        /*
         * 이 핸들러가 생성되는 스레드가 중요하다.
         * 핸들러는 생성자로 스레드의 looper를 받는데, 이를 인자로 넘겨주지 않는 기본 생성자를 사용할 경우,
         * 생성이 일어난 해당 스레드의 looper가 자동으로 사용된다.
         * 이 initView 메소드는 메인 스레드에서 실행되는 것이 보장되므로, 이 핸들러 또한 UI 스레드의 looper를 가지게 된다.
         * 따라서 어떤 스레드에서든 이 핸들러를 통해 메인 스레드에서 루틴을 실행할 수 있다.
         */
        deviceVersionCheckHandler = new DeviceVersionCheckHandler(this);

        /*
         * 인텐트 초기화가 여기서 일어난다.
         */
        loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        mainIntent = new Intent(SplashActivity.this, MainActivity.class);
    }

    /**
     * 백그라운드에서 실행되며, 버전을 서버로부터 가져온 뒤 이를 확인하는 루틴을 메인 스레드에서 실행시킬 thread.
     */
    public class BackgroundThread extends Thread {

        /**
         * 스레드가 시작될 때에 할 짓.
         */
        @Override
        public void run() {
            // 버전에 관해 서버에 물어보고 결과를 가져옴.
            Call<VersionResult> versionResultCall = networkService.getVersionResult();

            // 그 결과를 가지고 다음과 같은 일을 하는데...
            versionResultCall.enqueue(new Callback<VersionResult>() {

                /**
                 * 서버로부터 응답을 수신했을 때에 실행됨.
                 * 서버측에서 처리를 성공했을 수도, 실패했을 수도 있음.
                 * @param call 다루고자 하는 call 객체
                 * @param response 응답 결과
                 */
                @Override
                public void onResponse(Call<VersionResult> call, Response<VersionResult> response) {
                    if(response.isSuccessful()) {
                        // 만약 대답이 잘 돌아왔으면 멤버변수인 storeVersion에 대입. (플레이스토어 버전)
                        storeVersion = response.body().android.latest;

                        // 그리고 현재 기기에 설치된 이 앱의 버전을 가져올 것인데,
                        try {
                            // 패키지 정보 중에 버전을 가져온다.
                            deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            // 망하면 토하지만 뻗지는 않는다
                            e.printStackTrace();
                        }

                        /*
                         * 이 작업이 백그라운드 스레드에서 돌아가기 때문에 이후 루틴을 직접 호출하지는 못하고,
                         * 핸들러를 통해 메인 스레드로 넘겨버린다.
                         */
                        deviceVersionCheckHandler.sendMessage(deviceVersionCheckHandler.obtainMessage());

                        /*
                         * 사실....
                         * 메인 스레드의 루틴 호출이 목적이라면 방법이 두 개 더 있다.
                         * 첫번째: Activity.runOnUiThread(Runnable) 사용하기.
                         * 두번째: Handler.post(Runnable) 사용하기.
                         */
                    }
                    else {
                        // 대답이 돌아왔으나 그 대답이 '실패'일 때.
                        mDialog = new Dialog(SplashActivity.this,
                                "오류",
                                "네트워크 연결을 확인해주세요.",
                                errorCloseApp,
                                "확인");
                        mDialog.show();

                    }
                }

                /**
                 * 서버로부터 응답을 수신하지 못했을 때에 실행됨.
                 * @param call 다루고자 하는 call 객체
                 * @param t 응답 결과
                 */
                @Override
                public void onFailure(Call<VersionResult> call, Throwable t) {
                    // 다이얼로그를 띄워 사용자에게 알려준다.
                    // 유일한 선택지는 액티비티 종료. (앱 종료)
                    mDialog = new Dialog(SplashActivity.this,
                            "오류",
                            "네트워크 연결을 확인해주세요.",
                            errorCloseApp,
                            "확인");
                    mDialog.show();
                }
            });
        }
    }

    /**
     * 메시지를 받으면 이를 액티비티의 handleMessage로 넘겨주는 핸들러.
     */
    private static class DeviceVersionCheckHandler extends Handler {

        /**
         * 핸들러는 액티비티의 메소드에 접근하기 위해 액티비티의 참조를 가지고 있어야 한다.
         * 하지만 강한 참조를 핸들러가 가지고 있으면 액티비티는 Garbage Collection의 대상이 될 수 없다.
         * 따라서 약한 참조로만 가지고 있어야 한다.
         */
        private final WeakReference<SplashActivity> splashActivityWeakReference;

        public DeviceVersionCheckHandler(SplashActivity splashActivity) {
            splashActivityWeakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {

            SplashActivity activity = splashActivityWeakReference.get();

            if (activity != null) {
                // 핸들 메세지로 결과 값 전달
                activity.handleMessage(msg);
            }
        }
    }

    /**
     * 핸들러가 메시지를 받으면 메인 스레드에서 호출하는 메소드.
     * @param msg 의미없음
     */
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

    /**
     * 공지사항을 가져와서 띄워준다.
     */
    private void showNotification() {

        // getMessageResult는 공지사항을 가져오는 메소드이다.
        Call<MessageResult> call = networkService.getMessageResult();

        // 얘도 가져온 다음에 결과에 따라서 다음 일을 해주는데..
        call.enqueue(new Callback<MessageResult>() {

            /**
             * 결과가 돌아오면 수행할 일들
             * @param call 요청 객체
             * @param response 결과 객체
             */
            @Override
            public void onResponse(Call<MessageResult> call, final Response<MessageResult> response) {

                alreadyConfirmAllNotice = checkNoReplayNotice.getString("all", "");
                alreadyConfirmAndroidNotice = checkNoReplayNotice.getString("android", "");
                curAllNoticeId = response.body().getAll().getId();
                curAndroidNoticeId = response.body().getAndroid().getId();


                if (response.isSuccessful()) {
                    /*
                     * 응답이 성공적일 때.
                     */

                    if (!response.body().getAll().getMessage().isEmpty() &&
                            !alreadyConfirmAllNotice.equals(curAllNoticeId)) {
                        /*
                         * 공통 공지사항 안읽은게 있을 때.
                         */

                        mDialog = new Dialog(SplashActivity.this,
                                response.body().getAll().getTitle(),
                                response.body().getAll().getMessage(),
                                initConfirm, /* 확인하면 다음 푸시를 가져오는 checkPush 호출됨. */
                                new View.OnClickListener() {

                                    /**
                                     * 다시 안보기를 선택하면 이를 shared preference에 기록해둠.
                                     * @param view 안씀.
                                     */
                                    @Override
                                    public void onClick(View view) {

                                        /*
                                         * 주의할 것.
                                         * noReplayNoticeDate는 checkNoReplayNotice.edit()이다.
                                         */
                                        noReplayNoticeDate.putString("all", response.body().getAll().getId());
                                        noReplayNoticeDate.commit();

                                        mDialog.cancel();
                                        checkPush();
                                    }
                                }, /* 다시 보지않기 */
                                "확인",
                                "다시 보지않기");
                        mDialog.show();
                    }

                    else if (!response.body().getAndroid().getMessage().isEmpty() &&
                            !alreadyConfirmAndroidNotice.equals(curAndroidNoticeId)) {
                        /*
                         * 안드로이드 공지사항 안읽은게 있을 때.
                         */

                        mDialog = new Dialog(SplashActivity.this,
                                response.body().getAndroid().getTitle(),
                                response.body().getAndroid().getMessage(),
                                initConfirm, /* 확인 */
                                new View.OnClickListener() {

                                    /**
                                     * 다시 안보기를 선택하면 이를 shared preference에 기록해둠.
                                     * @param view 안씀.
                                     */
                                    @Override
                                    public void onClick(View view) {

                                        /*
                                         * 여기도 주의할 것.
                                         * noReplayNoticeDate는 checkNoReplayNotice.edit()이다!!!
                                         */
                                        noReplayNoticeDate.putString("android", response.body().getAndroid().getId());
                                        noReplayNoticeDate.commit();

                                        mDialog.cancel();

                                        checkPush();
                                    }
                                }, /* 다시 보지않기 */
                                "확인",
                                "다시 보지않기");
                        mDialog.show();

                    }

                    else {
                        /*
                         * 공지사항이 없을 때.
                         */
                        checkPush();
                    }
                }
                else {
                    /*
                     * 응답이 왔으나 실패일 떄.
                     */

                    Log.d("debug, ", response.errorBody().toString());
                }
            }

            /**
             * 결과는 커녕 서버에 닿지도 못했을때(?)
             * @param call 요청 객체
             * @param t 예외 객체
             */
            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {

                // 공지사항을 못 받아왔다고 액티비티를 끄지는 않습니다.
                Log.d("debug", t.getMessage().toString());

                Toast.makeText(SplashActivity.this, "서버 접속이 끊겼습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 잠깐의 딜레이를 두고 checkPushExistOrNot를 호출.
     */
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

    /**
     * 기다리고 있는 음식 번호가 존재하는지 확인 후 MainActivity를 띄우거나 로그인 화면으로 안내함.
     */
    private void checkPushExistOrNot() {

        // 고유의 fcm 토큰 데이터
        waitData = new WaitData(FirebaseInstanceId.getInstance().getToken());

        // 를 이용해서 해당 fcm 토큰에 딸려있는 대기 번호가 있는지 서버에게 물어본다.
        Call<WaitResult> waitResultCall = networkService.getWaitResult(waitData);
        waitResultCall.enqueue(new Callback<WaitResult>() {

            /**
             * 안망하면 처리하고
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<WaitResult> call, Response<WaitResult> response) {

                if (response.body() == null) {
                    /*
                     * 기다리는 음식 번호가 없는 경우.
                     */

                    //로그인을 하는데,
                    if (token != null) {
                        // 로그인 토큰이 있으면 자동 로그인
                        autoLogin();
                    }
                    else {
                        // 없으면 수동 로그인
                        startActivity(loginIntent);
                        finish();

                        // 예쁜 애니메이션까지
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }
                else {
                    /*
                     * 기다리는 음식 번호가 있는 경우.
                     *
                     * 마치 번호를 직접 등록한 직후와 같은 상태를 만들어주어야 한다.
                     * bundel을 잘 채워서 MainActivity를 실행해준다.
                     */

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

            /**
             * 망하면 뭐 로그인 시켜야지..
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<WaitResult> call, Throwable t) {
                Log.d("debug, ", t.getMessage().toString());
                startActivity(loginIntent);
                finish();
            }
        });
    }

    /**
     * 자동로그인을 시도하여 성공하면 MainActivity로 안내하고,
     * 실패하면 로그인 화면으로 안내한다.
     */
    private void autoLogin() {

        // 로그인을 하는데 토큰이 있으니 학번이고 비번이고 다필요업다
        loginData = new LoginData(
                "",
                "",
                true,
                token,
                ""
        );


        Call<LoginResult> loginResultCall = networkService.getLoginResult(loginData);
        loginResultCall.enqueue(new Callback<LoginResult>() {

            /**
             * 서버가 대답을 해주면 처리하고
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                if (response.body() == null) {
                    /*
                     * 토큰이 share preference에는 있지만 서버에는 없는 황당한 경우
                     */

                    student.clear();
                    student.commit();

                    startActivity(loginIntent);
                    finish();

                }
                else {
                    /*
                     * 토큰이 share preference에는 있고 서버에서도 ㅇㅈ하는 부분
                     */

                    student.putString("inputbarcode", response.body().getBarcode());
                    student.commit();

                    startActivity(mainIntent);
                    finish();

                }
            }

            /**
             * 답이 없으면 다시 로그인 시켜야지 뭐..
             * @param call
             * @param t
             */
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
                // 한국이면 스토어로 직접 연결
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            }
            else if (ApplicationController.getInstance().getNation().equals("china")) {
                // 한국이 아니면 apk 다운로드 링크로 연결

                // intent.setData(Uri.parse("http://uicoop.ac.kr/mobile/notice_view.html?num=1279&page=1&seltb="));
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
