package com.inu.cafeteria.Network;

import com.google.gson.JsonElement;
import com.inu.cafeteria.EmergencyTemporary.VersionResult;
import com.inu.cafeteria.Model.ActiveBarcodeData;
import com.inu.cafeteria.Model.ActiveBarcodeResult;
import com.inu.cafeteria.Model.AdvertisementResult;
import com.inu.cafeteria.Model.CafeCodeResult;
import com.inu.cafeteria.Model.ErrorMsgData;
import com.inu.cafeteria.Model.ErrorMsgResult;
import com.inu.cafeteria.Model.LoginData;
import com.inu.cafeteria.Model.LoginResult;
import com.inu.cafeteria.Model.LogoutData;
import com.inu.cafeteria.Model.LogoutResult;
import com.inu.cafeteria.Model.MessageResult;
import com.inu.cafeteria.Model.PushModel;
import com.inu.cafeteria.Model.RegisterData;
import com.inu.cafeteria.Model.RegisterResult;
import com.inu.cafeteria.Model.ResetNumModel;
import com.inu.cafeteria.Model.WaitData;
import com.inu.cafeteria.Model.WaitResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

    // 로그인
    @POST("/login")
    Call<LoginResult> getLoginResult(@Body LoginData data);

    // 로그아웃
    @POST("/logout")
    Call<LogoutResult> getLogoutResult(@Body LogoutData data);

    // 바코드 활성화
    @POST("/activeBarcode")
    Call<ActiveBarcodeResult> getActiveBarcodeResult(@Body ActiveBarcodeData data);

    // 공지사항
    @GET("/notice.json")
    Call<MessageResult> getMessageResult();

    // 에러메세지
    @POST("/errormsg")
    Call<ErrorMsgResult> getErrorMsgResult(@Body ErrorMsgData data);

    // 광고
    @GET("/ads.json")
    Call<List<AdvertisementResult>> getAdvertisemetnResult();

    // 식당메뉴
    @GET("/food/{date}")
    Call<JsonElement> getFoodMenu(@Path("date") String date);

    // 식당정보
    @GET("/cafecode.json")
    Call<JsonElement> getCafeCode();



    // 음식번호 입력
    @POST("/registerNumber")
    Call<RegisterResult> getRegisterResult(@Body RegisterData data);


    // 푸시
    @POST("/pushNumber")
    Call<PushModel> getPushResult(@Body PushModel data);


    // 음식번호 지우기
    @POST("/resetNumber")
    Call<ResetNumModel> getResetNumResult(@Body ResetNumModel data);


    // 기다리고 있는 번호가 있는지 판별
    @POST("/isNumberWait")
    Call<WaitResult> getWaitResult(@Body WaitData data);



    // temp
    @GET("/version.json")
    Call<VersionResult> getVersionREsult();
}
