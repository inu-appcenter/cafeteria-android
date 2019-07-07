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

/**
 * 메인!! 액티비티!!
 *
 * 액티비티 실행 순서:
 * SplashActivity -> (LoginActivity) -> MainActivity
 *
 * 루틴 실행 순서:
 * 1. 필드 초기화
 * 2. 클릭 리스너 설정
 * 3. actionBar 설정
 * 4. drawer 초기화
 * 5. 학생회원 확인
 * 6. 광고 표시
 * 7. 프래그먼트 설정
 * 8. 상태바 색상 설정
 *
 * 기능에 관한 상세 상호작용은 각 fragment에 정의되어 있음.
 * InputFoodNumberFragment와 WaitingFoodNumberFragment 참고.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * ApplicationController에서 가져올 retrofit 객체.
     */
    NetworkService networkService;

    /**
     * UI 객체들.
     */
    private Dialog mDialog;
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

    /**
     * 기기 화면 밝기 저장할 소수.
     */
    private float originBright;

    /**
     * 광고 띄울 때에 사용할 뷰페이저 UI 객체와 이를 도와줄 기타 등등 객체들.
     */
    private ArrayList<AdvertisementResultList> advertisementResultList;
    private CustomViewPager advertisementViewPager;
    private CircleIndicator circleIndicator;
    private Timer swipeTimer;
    private TimerTask swipeTimerTask;
    private int currentPage;

    /**
     * 뒤로가기 버튼에 반응해줄 녀석.
     */
    private BackPressCloseHandler backPressCloseHandler;

    /**
     * 로그아웃 요청 보낼 때에 쓸 객체.
     */
    LogoutData logoutData;

    /**
     * 대기번호 싹 지우는 요청 보낼 때에 쓸 객체.
     */
    ResetNumModel resetNumModel;

    /**
     * 대기번호 등록하는 요청 보낼 때에 쓸 객체.
     */
    RegisterData registerData;


    /**
     * 로그인 정보 포함 회원 정보를 담은 shared preference.
     */
    SharedPreferences studentInfo, checkNoReplayNoticeDate;
    SharedPreferences.Editor student, noReplayNoticeDate;
    String sno, token, barcode;

    /**
     * 여러 용도로 쓰일 인텐트들.
     */
    Intent intent, loginIntent, appInfoIntent, inquireIntent;


    /**
     * 프래그먼트 교체할 때에 쓸 매니저와, 바꿀 프래그먼트들
     */
    android.support.v4.app.FragmentManager myFragmentManager;
    InputFoodNumberFragment inputFoodNumberFragment;
    WaitingFoodNumberFragment waitingFoodNumberFragment;
    final static String TAG_FRAGMENT = "FRAGMENT";


    /**
     * 시작점.
     * @param savedInstanceState 안써욧
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // networkService 객체 가져오기
        initNetwork();

        // UI 포함 객체들 초기화
        initView();

        // 클릭 리스너 설정
        initEvent();

        // actionbar로 toolbar 사용
        initActionbar();

        // 바코드와 광고가 나오는 drawer 초기화.
        initDrawer();

        // 회원인지 확인하여 비회원일 때에는 drawer 레이아웃 일부 변경
        checkMember();

        // drawer에 띄울 광고 설정
        initAdvertisementItem();

        // 처음 띄울 프래그먼트 설정
        initFragment();

        // 상태바 색상 설정
        initStatusBar();
    }

    /**
     * networkService 객체 초기화.
     */
    private void initNetwork() {
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    /**
     * 객체 초기화.
     */
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
        // layoutForAdvertisementIndicator = (LinearLayout) findViewById(R.id.vd_container_indicator);

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

    /**
     * 바코드 이미지에 대한 클릭 이벤트 지정
     */
    private void initEvent() {
        imgBarcode.setOnClickListener(this);
    }

    /**
     * toolbar의 title을 없앤 채로 supportActionbar로 지정.
     */
    private void initActionbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    /**
     * 옆에서 밀면 나오는 바코드 달린 drawer 초기화.
     */
    private void initDrawer() {

        // 학번은 이미 아니까 바로 설정해줌
        textSno.setText(sno);

        // 토글시 동작 설정
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /**
             * Drawer가 닫힐 때!!
             * 바코드 비활성화해준다.
             * @param drawerView 닫힐 그 drawer!
             */
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                // 밝기 원래대로 돌려놓기
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.screenBrightness = originBright;
                getWindow().setAttributes(params);

                // 바코드 비활성화 (서버 통신)
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

                // 옵션 메뉴 새로 그리기를 종용!
                invalidateOptionsMenu();
            }

            /**
             * Drawer가 열릴 때!!
             * 바코드 다시 활성화해준다.
             * @param drawerView 열리는 drawer
             */
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

                        if (response.isSuccessful()) {
                            /*
                             * 서버 통신 결과가 성공인 경우.
                             */
                            if (response.body().getActive().equals("1")) {
                                /*
                                 * 적절한 응답이 왔으면 바코드 활성화
                                 */
                                imgBarcode.setVisibility(View.VISIBLE);
                            } else {
                                /*
                                 * 그렇지 않으면 비활성화
                                 */
                                imgBarcode.setVisibility(View.INVISIBLE);
                            }

                        }
                        else {
                            /*
                             * 결과가 실패이면 그냥 숨겨버림
                             */
                            imgBarcode.setVisibility(View.INVISIBLE);

                        }

                    }

                    @Override
                    public void onFailure(Call<ActiveBarcodeResult> call, Throwable t) {
                        /*
                         * 서버와 통신이 실패한 경우.
                         * 숨겨버림
                         */
                        imgBarcode.setVisibility(View.INVISIBLE);
                    }

                });

                // 옵션 메뉴 새로고치기
                invalidateOptionsMenu();
            }
        };

        // drawer 리스너로 등록
        drawerLayout.setDrawerListener(drawerToggle);

        // 바코드 만들어주기
        if (studentInfo != null) {
            CreateBarcode();
        }
    }

    /**
     * 적절한 바코드 생성.
     */
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

    /**
     * 회원 로그인인지 비회원 로그인인지 구분.
     * 비회원일 경우 drawer에서 UI 변경.
     */
    private void checkMember() {

        initActionbarMenu();

        if (barcode != null) {
            /*
             * 회원 로그인인 경우 아무것도 안함.
             */
        }
        else {
            /*
             * 비회원 로그인인 경우, UI 재설정.
             */

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

    /**
     * Drawer를 띄울 수 있는 메뉴 포함 액션 바 설정.
     */
    private void initActionbarMenu() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.btn_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 광고 끼워넣기.
     */
    private void initAdvertisementItem() {

        // 똑똑.. 광고 있나요
        Call<List<AdvertisementResult>> advertisementResultCall = networkService.getAdvertisemetnResult();
        advertisementResultCall.enqueue(new Callback<List<AdvertisementResult>>() {

            @Override
            public void onResponse(Call<List<AdvertisementResult>> call, Response<List<AdvertisementResult>> response) {

                if (response.body() != null) {
                    // 네 광고 있어요
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

        /*
         * 슥슥 넘어가는 광고
         */
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


    /**
     * 처음에 어떤 프래그먼트를 표시할지 결정함.
     */
    private void initFragment() {
        /*
         * MainActivity를 띄우는 놈은 SplashActivity거나 LoginActivity인데,
         * SplashActivity가 띄우는 경우에는 현재 등록된 대기번호를 표시해야 하는 경우가 있다.
         * 이는 intent에 담겨 bundle로 날아오는데, 여기서 code라는 extra에 식당 번호가 담긴다.
         * 이게 null이면 대기번호를 등록하는 프래그먼트를 띄우면 되고,
         * 그렇지 않다면 이미 존재하는 대기번호를 표시하기 위해 WaitingFoodNumberFragment 프래그먼트를
         * 띄우면 된다.
         */

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

    /**
     * 상태바 색상 설정.
     */
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

    /**
     * 프래그먼트 교체
     * @param index 0이면 주문번호 대기 화면으로 넘어가고, 1이면 번호 입력 화면으로 넘어감.
     * @param registerData 등록할 주문번호 정보
     * @param cafename 주문번호를 등록할 식당 정보
     */
    public void onFragmentChanged(int index, final RegisterData registerData, final String cafename) {

        // 프래그먼트 매니저 초기화
        myFragmentManager = getSupportFragmentManager();

        if (index == 0) {
            /*
             * WaitingFoodNumberFragment로 교체 되는 경우. (식당 정보 및 입력한 번호를 전송)
             * 입력 버튼 누른 후에 일어남.
             */

            // 입력된 번호가 들어있는 registerData를 가지고 서버 호출.
            Call<RegisterResult> registerResultCall = networkService.getRegisterResult(registerData);
            registerResultCall.enqueue(new Callback<RegisterResult>() {

                @Override
                public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                    /*
                     * 성공시 waitingFoodNumberFragment로 넘어가는데,
                     * registerData와 cafename를 가지고 넘어감.
                     */
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
                    /*
                     * 실패하면 프래그먼트를 교체하지 않고 토스트를 띄우고 끝냄.
                     */
                    Toast.makeText(MainActivity.this, "서버접속이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                }

            });

        }
        else if (index == 1) {
            /*
             * InputFoodNumberFragment로 교체되는 경우. (다시 번호 입력받음)
             * 주문번호 초기화할 때에 일어남.
             */

            // reset을 목적으로 resetNumModel를 만들어서 서버 호출.
            resetNumModel = new ResetNumModel(FirebaseInstanceId.getInstance().getToken());
            Call<ResetNumModel> resetNumModelCall = networkService.getResetNumResult(resetNumModel);
            resetNumModelCall.enqueue(new Callback<ResetNumModel>() {

                @Override
                public void onResponse(Call<ResetNumModel> call, Response<ResetNumModel> response) {

                    if (response.body().getResult().equals("success")) {
                        /*
                         * 주문번호 초기화 성공하면 inputFoodNumberFragment로 돌아감.
                         */
                        Log.d("Token: ", resetNumModel.getFcmtoken());

                        android.support.v4.app.FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.am_fragment, inputFoodNumberFragment, TAG_FRAGMENT);
                        fragmentTransaction.commit();
                    }
                }

                @Override
                public void onFailure(Call<ResetNumModel> call, Throwable t) {
                    /*
                     * 실패하면 토스트 띄우고 아무것도 안함.
                     */
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