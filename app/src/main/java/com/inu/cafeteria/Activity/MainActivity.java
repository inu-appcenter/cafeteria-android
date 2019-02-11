package com.inu.cafeteria.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.inu.cafeteria.Adapter.AdvertisementViewPagerAdapter;
import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.Application.BackPressCloseHandler;
import com.inu.cafeteria.Model.ActiveBarcodeData;
import com.inu.cafeteria.Model.ActiveBarcodeResult;
import com.inu.cafeteria.Model.AdvertisementResult;
import com.inu.cafeteria.Model.AdvertisementResultList;
import com.inu.cafeteria.Model.LogoutData;
import com.inu.cafeteria.Model.LogoutResult;
import com.inu.cafeteria.Model.RegisterData;
import com.inu.cafeteria.Model.RegisterResult;
import com.inu.cafeteria.Model.ResetNumModel;
import com.inu.cafeteria.Network.NetworkService;
import com.inu.cafeteria.R;
import com.inu.cafeteria.Utility.CircleIndicator;
import com.inu.cafeteria.Utility.CustomViewPager;
import com.inu.cafeteria.Utility.Dialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    NetworkService networkService;

    // 다이얼로그
    private Dialog mDialog;

    // 드로어
    private android.support.v7.widget.Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView textTitle;
    private FrameLayout frameLayout;
    private ImageView imgBarcode;
    private TextView textGuide;
    private TextView textDot;
    private TextView textSno;

    final static private String DRAWER_OPENED = "1";
    final static private String DRAWER_CLOSED = "0";

    // 핸드폰 기존 밝기 값 저장
    private float originBright;

    // 광고 뷰페이저
    private ArrayList<AdvertisementResultList> advertisementResultList;
    private CustomViewPager advertisementViewPager;
    private CircleIndicator circleIndicator;
    private Timer swipeTimer;
    private TimerTask swipeTimerTask;
    private int currentPage;


    // 뒤로가기 버튼
    private BackPressCloseHandler backPressCloseHandler;

    // 로그아웃
    LogoutData logoutData;

    // 주문번호 초기화
    ResetNumModel resetNumModel;

    // 번호 등록 데이터
    RegisterData registerData;


    // 회원 정보
    SharedPreferences studentInfo, checkNoReplayNoticeDate;
    SharedPreferences.Editor student, noReplayNoticeDate;
    String sno, token, barcode;

    Intent intent, loginIntent, appInfoIntent, inquireIntent;


    // 프래그먼트 관련 정보
    android.support.v4.app.FragmentManager myFragmentManager;
    InputFoodNumberFragment inputFoodNumberFragment;
    WaitingFoodNumberFragment waitingFoodNumberFragment;
    final static String TAG_FRAGMENT = "FRAGMENT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initNetwork();
        initView();
        initEvent();
        initActionbar();
        initDrawer();
        checkMember();
        initAdvertisementItem();
        initFragment();
        // for transparent status bar in kitkat
        initStatusBar();

    }

    private void initNetwork() {

        networkService = ApplicationController.getInstance().getNetworkService();

    }


    private void initView() {

        inputFoodNumberFragment = new InputFoodNumberFragment();
        waitingFoodNumberFragment = new WaitingFoodNumberFragment();

        intent = getIntent();


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.vt_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.am_layout_drawer);
        textTitle = (TextView) findViewById(R.id.drawer_text_title);
        frameLayout = (FrameLayout) findViewById(R.id.drawer_framelayout);
        imgBarcode = (ImageView) findViewById(R.id.drawer_img_barcode);
        textGuide = (TextView) findViewById(R.id.drawer_text_guide);
        textDot = (TextView) findViewById(R.id.drawer_text_dot);
        textSno = (TextView) findViewById(R.id.drawer_text_sno);

        advertisementResultList = new ArrayList<>();
        advertisementViewPager = (CustomViewPager) findViewById(R.id.vd_viewpager_advertisement);
        circleIndicator = (CircleIndicator) findViewById(R.id.vd_indicator_advertisement);
//        layoutForAdvertisementIndicator = (LinearLayout) findViewById(R.id.vd_container_indicator);


        studentInfo = getSharedPreferences("studentInfo", Activity.MODE_PRIVATE);
        checkNoReplayNoticeDate = getSharedPreferences("checkNoReplayNoticeDate", Activity.MODE_PRIVATE);
        sno = studentInfo.getString("inputsno", null);
        token = studentInfo.getString("inputtoken", null);
        barcode = studentInfo.getString("inputbarcode", null);
        student = studentInfo.edit();
        noReplayNoticeDate = checkNoReplayNoticeDate.edit();

        loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        appInfoIntent = new Intent(MainActivity.this, AppInformationActivity.class);
        inquireIntent = new Intent(MainActivity.this, InquireActivity.class);

        backPressCloseHandler = new BackPressCloseHandler(this);

    }


    private void initEvent() {

        imgBarcode.setOnClickListener(this);

    }


    private void initActionbar() {

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }


    private void initDrawer() {


        // 뷰 데이터 설정
        textSno.setText(sno);

        // 드로어 토글 설정
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);


                // turn the brightness back
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.screenBrightness = originBright;
                getWindow().setAttributes(params);


                // 바코드 활성화 (서버 통신)
                Call<ActiveBarcodeResult> activeBarcodeResultCall = networkService.getActiveBarcodeResult(new ActiveBarcodeData(DRAWER_CLOSED, barcode));
                activeBarcodeResultCall.enqueue(new Callback<ActiveBarcodeResult>() {
                    @Override
                    public void onResponse(Call<ActiveBarcodeResult> call, Response<ActiveBarcodeResult> response) {
                        // 서버 통신이 정상적으로 이루어진 경우
                        if (response.isSuccessful()) {
                            imgBarcode.setVisibility(View.INVISIBLE);
                        } else {
                            Log.e("Gayoon", new Gson().toJson(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ActiveBarcodeResult> call, Throwable t) {
                        Log.d("debug, ", t.getMessage());
                    }

                });

                invalidateOptionsMenu();

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                // 가상키보드 내리기
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // 화면 밝기 조절 (밝게)
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                originBright = layoutParams.screenBrightness;
                layoutParams.screenBrightness = 1.0f;
                getWindow().setAttributes(layoutParams);


                // 바코드 활성화 (서버 통신)
                Call<ActiveBarcodeResult> activeBarcodeResultCall = networkService.getActiveBarcodeResult(new ActiveBarcodeData(DRAWER_OPENED, barcode));
                activeBarcodeResultCall.enqueue(new Callback<ActiveBarcodeResult>() {


                    @Override
                    public void onResponse(Call<ActiveBarcodeResult> call, Response<ActiveBarcodeResult> response) {


                        // 서버 통신이 정상적으로 이루어진 경우
                        if (response.isSuccessful()) {

                            if (response.body().getActive().equals("1")) {

                                // 바코드 활성화
                                imgBarcode.setVisibility(View.VISIBLE);
                            } else {

                                // 바코드 비활성화

                                imgBarcode.setVisibility(View.INVISIBLE);
                            }

                        } else {

                            imgBarcode.setVisibility(View.INVISIBLE);

                        }

                    }

                    @Override
                    public void onFailure(Call<ActiveBarcodeResult> call, Throwable t) {

                        imgBarcode.setVisibility(View.INVISIBLE);

                    }

                });
                invalidateOptionsMenu();
            }
        };


        drawerLayout.setDrawerListener(drawerToggle);

        // Create barcode
        if (studentInfo != null) {

            CreateBarcode();
        }
    }


    private void CreateBarcode() {

        MultiFormatWriter gen = new MultiFormatWriter();
        String dataBarcode = barcode;

        try {

            final int WIDTH = 600;
            final int HEIGHT = 360;

            BitMatrix bytemap = gen.encode(dataBarcode, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < WIDTH; ++i)

                for (int j = 0; j < HEIGHT; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i, j) ? Color.BLACK : Color.WHITE);
                }

            imgBarcode.setImageBitmap(bitmap);
            imgBarcode.invalidate();
            Log.i("debug", "barcode create success");

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    // 회원 로그인인지 비회원 로그인인지 체크
    private void checkMember() {

        initActionbarMenu();

        if (barcode != null) {
            // 회원 로그인인 경우
        } else {
            // 비회원 로그인인 경우
            //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            // 바코드 관련한 Text, Layout 재설정
            textGuide.setText("로그인 후 조식할인을\n이용하실 수 있습니다.");
            textGuide.setTextSize(12);
            textGuide.setGravity(Gravity.CENTER);
            textGuide.setAlpha(0.3f);

            textTitle.setAlpha(0.3f);
            frameLayout.setVisibility(View.INVISIBLE);
            textDot.setVisibility(View.INVISIBLE);
            textSno.setVisibility(View.INVISIBLE);
        }
    }


    // 드로어 메뉴 이미지 설정 및 활성화
    private void initActionbarMenu() {

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.btn_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    // 광고 아이템 삽입
    private void initAdvertisementItem() {


        Call<List<AdvertisementResult>> advertisementResultCall = networkService.getAdvertisemetnResult();
        advertisementResultCall.enqueue(new Callback<List<AdvertisementResult>>() {

            @Override
            public void onResponse(Call<List<AdvertisementResult>> call, Response<List<AdvertisementResult>> response) {

                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (!response.body().get(i).getTitle().equals("")) {
                            advertisementResultList.add(new AdvertisementResultList(response.body().get(i)));
                        }
                    }

                    AdvertisementViewPagerAdapter adapter = new AdvertisementViewPagerAdapter(getLayoutInflater(), getApplicationContext(), advertisementResultList);
                    advertisementViewPager.setAdapter(adapter);

                    // for indicator
                    circleIndicator.setItemMargin(dpToPx(getApplicationContext(), 4));
                    circleIndicator.createDotPanel(advertisementResultList.size(), R.drawable.gray_circle, R.drawable.point_blue_circle);
                    advertisementViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            circleIndicator.selectDot(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<List<AdvertisementResult>> call, Throwable t) {
                Log.d("debug", t.getMessage().toString());

            }
        });

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == advertisementResultList.size()) {
                            currentPage = 0;
                        }
                        advertisementViewPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, 500, 3000);
    }


    private void initFragment() {

        // Main화면에 뿌려질 프래그먼트를 결정함
        if (intent.getSerializableExtra("code") == null) {
            // InputFoodNumberFragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.am_fragment, new InputFoodNumberFragment())
                    .commit();
        }

        else {

            // WaitingFoodNumberFragment
            registerData = new RegisterData(
                    String.valueOf(intent.getSerializableExtra("code")),
                    String.valueOf(intent.getSerializableExtra("num1")),
                    String.valueOf(intent.getSerializableExtra("num2")),
                    String.valueOf(intent.getSerializableExtra("num3")),
                    null,
                    null
            );

            onFragmentChanged(0, registerData, String .valueOf(intent.getSerializableExtra("name")));
        }

    }

    // Status Bar Tint
    private void initStatusBar(){
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#20000000"));
    }



    // 프래그먼트 교체
    public void onFragmentChanged(int index, final RegisterData registerData, final String cafename) {

        myFragmentManager = getSupportFragmentManager();

        // WaitingFoodNumberFragment로 교체 되는 경우 (식당 정보 및 입력한 번호를 전송)
        if (index == 0) {

            Call<RegisterResult> registerResultCall = networkService.getRegisterResult(registerData);
            registerResultCall.enqueue(new Callback<RegisterResult>() {

                @Override
                public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", registerData);
                    bundle.putString("cafename", cafename);
                    waitingFoodNumberFragment.setArguments(bundle);

                    android.support.v4.app.FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.am_fragment, waitingFoodNumberFragment, TAG_FRAGMENT);
                    fragmentTransaction.commit();

                }

                @Override
                public void onFailure(Call<RegisterResult> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "서버접속이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                }

            });


        }

        // InputFoodNumberFragment로 교체 되는 경우 (주문번호를 초기화)
        else if (index == 1) {

            resetNumModel = new ResetNumModel(FirebaseInstanceId.getInstance().getToken());
            Call<ResetNumModel> resetNumModelCall = networkService.getResetNumResult(resetNumModel);
            resetNumModelCall.enqueue(new Callback<ResetNumModel>() {

                @Override
                public void onResponse(Call<ResetNumModel> call, Response<ResetNumModel> response) {

                    if (response.body().getResult().equals("success")) {

                        Log.d("Token: ", resetNumModel.getFcmtoken());

                        android.support.v4.app.FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.am_fragment, inputFoodNumberFragment, TAG_FRAGMENT);
                        fragmentTransaction.commit();

                    }
                }

                @Override
                public void onFailure(Call<ResetNumModel> call, Throwable t) {

                    Toast.makeText(MainActivity.this, "서버접속이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 학생인증 바코드 통신체크 및 활성화
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            Call<ActiveBarcodeResult> activeBarcodeResultCall = networkService.getActiveBarcodeResult(new ActiveBarcodeData(DRAWER_OPENED, barcode));
            activeBarcodeResultCall.enqueue(new Callback<ActiveBarcodeResult>() {

                // 서버 통신이 정상적으로 이루어질 경우
                @Override
                public void onResponse(Call<ActiveBarcodeResult> call, Response<ActiveBarcodeResult> response) {

                    if (response.isSuccessful()) {
                        // 바코드 활성화
                        imgBarcode.setVisibility(View.VISIBLE);
                    } else {
                        // 바코드 비활성화
                        imgBarcode.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ActiveBarcodeResult> call, Throwable t) {
                    imgBarcode.setVisibility(View.INVISIBLE);
                }
            });
        }

        if(swipeTimer == null) {
            swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (currentPage == advertisementResultList.size()) {
                                currentPage = 0;
                            }
                            advertisementViewPager.setCurrentItem(currentPage++, true);
                        }
                    });
                }
            }, 500, 3000);
        }

    }


    // 홈버튼 눌렀을 때
    @Override
    protected void onPause() {
        super.onPause();
        // 생명주기 : OnUserLeaveHint() -> onStop()
        // 학생인증 바코드 비활성화
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            Call<ActiveBarcodeResult> activeBarcodeResultCall = networkService.getActiveBarcodeResult(new ActiveBarcodeData(DRAWER_CLOSED, barcode));
            activeBarcodeResultCall.enqueue(new Callback<ActiveBarcodeResult>() {

                @Override
                public void onResponse(Call<ActiveBarcodeResult> call, Response<ActiveBarcodeResult> response) {
                }

                @Override
                public void onFailure(Call<ActiveBarcodeResult> call, Throwable t) {

                }
            });
        }

        if(swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer = null;
        }

        Log.d("timer", "cancel");

    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            backPressCloseHandler.onBackPressedActivity();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.drawer_img_barcode:

                break;

            default:
                break;

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_inquire:
//                Toast.makeText(this, "점검중입니다.", Toast.LENGTH_SHORT).show();

                startActivity(inquireIntent);
                return true;

            case R.id.menu_app_info:

                startActivity(appInfoIntent);
                return true;

            case R.id.menu_logout:

                mDialog = new Dialog(MainActivity.this,
                        null,
                        "로그아웃 하시겠습니까?",
                        logoutCancel,
                        logoutConfirm,
                        "취소",
                        "확인");
                mDialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


    private View.OnClickListener logoutCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.cancel();
        }
    };

    private View.OnClickListener logoutConfirm = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            logoutData = new LogoutData(token);

            Call<LogoutResult> logoutResultCall = networkService.getLogoutResult(logoutData);
            logoutResultCall.enqueue(new Callback<LogoutResult>() {

                @Override
                public void onResponse(Call<LogoutResult> call, Response<LogoutResult> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getResult().equals("logout is successful")) {

                            // Delete auto login data
                            student.clear();
                            student.commit();
                        }

                        noReplayNoticeDate.clear();
                        noReplayNoticeDate.commit();

                        startActivity(loginIntent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<LogoutResult> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "서버 접속이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    /**
     * 픽셀을 DP 로 변환하는 메소드.
     * @param px 픽셀
     * @return 픽셀에서 dp 로 변환된 값.
     */
    private int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * DP 를 픽셀로 변환하는 메소드.
     * @param dp dp
     * @return dp 에서 변환된 픽셀 값.
     */
    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}