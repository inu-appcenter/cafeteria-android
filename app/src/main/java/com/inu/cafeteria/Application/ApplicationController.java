package com.inu.cafeteria.Application;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.inu.cafeteria.Network.NetworkService;
import com.inu.cafeteria.R;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {

    // 먼저 어플리케이션 인스턴스 객체를 하나 선언
    private static ApplicationController instance;
    private static String baseUrl = Private.serverUrl; /* 비밀이지롱~ */
    private static String nation = "korea";

    // WaitingFoodNumberActivity 뷰 식당 이미지
    private int[] waitingImages = {
            R.drawable.there_is_no_img,
            R.drawable.img_45_corner_2,
            R.drawable.img_cafeteria,
            R.drawable.img_kimbab_2,
            R.drawable.img_sodam_2,
            R.drawable.img_cafedream_2_2,
            R.drawable.img_miyu_2,
            R.drawable.img_cafedream_1_2,
            R.drawable.img_domitory,
            R.drawable.there_is_no_img,
            R.drawable.there_is_no_img,
    };

    // 식당 이미지 변경이 필요할 경우 이부분만 수정하면 됨 (InputFoodNumberActivity 뷰 식당 이미지)
    private int[] coverflowImages = {
            R.drawable.there_is_no_img,
            R.drawable.img_45_corner,
            R.drawable.img_cafeteria,
            R.drawable.img_kimbab,
            R.drawable.img_sodam,
            R.drawable.img_cafedream_2,
            R.drawable.img_miyu,
            R.drawable.img_cafedream_1,
            R.drawable.img_domitory,
            R.drawable.there_is_no_img,
            R.drawable.img_school_staff,
    };



    // 식당 이름 및 변경이 필요한 경우 이 부분을 수정
    private String[] coverflowTitles = {
            "제2기숙사",
            "학생식당",
            "카페테리아(27호관)",
            "김밥천국",
            "소담국밥",
            "카페드림(도서관)",
            "미유카페",
            "카페드림(학식)",
            "제1기숙사",
            "사범대 학생식당",
            "교직원식당",
    };

    private String[] coverflowCodes = {
            "11",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
    };

    private int[] coverflowIsMenu = {
            0,
            1,
            2,
            -1,
            -1,
            -1,
            -1,
            -1,
            4,
            3,
            5,
    };

    private boolean[] coverflowIsAlarm ={
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false,
    };


    // InputFoodNumberFragment에 표시되는 메뉴의 정보를 받아오기 위한 코드
    private HashMap<String, String> slidingdrawer_cafecode;
    private ArrayList<String> slidingdrawer_cafename;


    public static String getNation() {
        return nation;
    }

    public static void setNation(String nation) {
        ApplicationController.nation = nation;
    }

    public int[] getCoverflowImages() {
        return coverflowImages;
    }

    public int[] getWaitingImages() { return waitingImages; }

    public String[] getCoverflowTitles() {
        return coverflowTitles;
    }

    public String[] getCoverflowCodes() {
        return coverflowCodes;
    }

    public int[] getCoverflowIsMenu() { return coverflowIsMenu; }

    public boolean[] getCoverflowIsAlarm() {
        return coverflowIsAlarm;
    }

    public HashMap<String, String> getSlidingdrawer_cafecode() { return slidingdrawer_cafecode; }
    public ArrayList<String> getSlidingdrawer_cafename() { return slidingdrawer_cafename; }

    public String getBaseUrl() {
        return baseUrl;
    }


    // 네트워크 서비스 객체 선언
    private NetworkService networkService;


    // 인스턴스 객체 반환  왜? static 안드에서 static 으로 선언된 변수는 매번 객체를 새로 생성하지 않아도 다른 액티비티에서
    // 자유롭게 사용가능합니다.
    public static ApplicationController getInstance() {
        return instance;
    }

    // 네트워크서비스 객체 반환
    public NetworkService getNetworkService() {
        return networkService;
    }


    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    //인스턴스 객체 초기화
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;
        buildService();
        initialFoodMenuInformationSetting();

    }

    private void initialFoodMenuInformationSetting() {
        slidingdrawer_cafecode = new HashMap<>();
        slidingdrawer_cafename = new ArrayList<>();

        // load cafeteria information
        Call<JsonElement> cafeCodeResultCall = networkService.getCafeCode();
        cafeCodeResultCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    JsonArray info = response.body().getAsJsonArray();
                    for(int i = 0; i < info.size(); i++) {
                        JsonObject obj = info.get(i).getAsJsonObject();
                        if(obj.get("menu").getAsInt() != -1) {
                            slidingdrawer_cafecode.put(obj.get("name").getAsString(), obj.get("no").getAsString());
                            slidingdrawer_cafename.add(obj.get("name").getAsString());
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    public void buildService() {
        // Maven에서 포함한 PersistentCookieJar를 통해 쿠키 세팅
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


        // 여기서 실제로 retrofit 객체 레퍼런스가 대입된다.
        networkService = retrofit.create(NetworkService.class);
    }

}
