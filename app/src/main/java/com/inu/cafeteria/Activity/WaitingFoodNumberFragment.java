package com.inu.cafeteria.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
import com.inu.cafeteria.Adapter.GridviewAdapter;
import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.Model.RegisterData;
import com.inu.cafeteria.R;
import com.inu.cafeteria.Utility.Dialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WaitingFoodNumberFragment extends Fragment {


    // when delete Temporary Code, go to DialogVibe line 44 ~ 45
    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Temporary Code //////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    // 클래스 외부에서 함수를 사용하기 위함
//    public static Fragment mFragment;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = (ViewGroup) inflater.inflate(R.layout.fragment_waiting_food_board, container, false);
//
//        return view;
//    }


    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// Temporary Code //////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////


    ApplicationController controller;

    ViewGroup view;

    // 다이얼로그
    Dialog mDialog;

    // 식당 관련 정보
    private String cafeteriaName, num1, num2, num3, code;
    private LinearLayout llBackImage;
    private LinearLayout llFoodNum;
    private TextView txFoodNum1, txFoodNum2, txFoodNum3, textCafeteriaName;
    private ImageButton tvInit;


    Bundle bundle;
    private RegisterData registerData;


    // 클래스 외부에서 함수를 사용하기 위함
    public static Fragment mFragment;



    // 전광판 관련한 소켓 통신 정보
    private String socketURL;
    private String receiveMessage;
    private String tagCafeteria;

    private Socket mSocket;

    GridView gridCompleteFoodNumber;
    GridviewAdapter gridviewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_waiting_food_board, container, false);

        bundle = getArguments();
        if(bundle != null){

            registerData = (RegisterData)bundle.get("data");
            num1 = registerData.getNum1();
            num2 = registerData.getNum2();
            num3 = registerData.getNum3();
            code = registerData.getCode();
            cafeteriaName = bundle.getString("cafename");
        }


        mFragment = this;

        initView();
        initEvent();
        setWaitNumber();

        initSocketView(view);
        initSocket();

        return view;
    }


    private void initView() {

        controller = (ApplicationController) getActivity().getApplication();

        // 뷰 초기화
        textCafeteriaName = (TextView) view.findViewById(R.id.fwfb_text_cafeteria_name);
        tvInit = (ImageButton) view.findViewById(R.id.fwfb_V_init);

        // 뷰 설정
        textCafeteriaName.setText(cafeteriaName);

        // 식당이미지 뷰
        llBackImage = view.findViewById(R.id.fwfb_LL_image);
        llBackImage.setBackgroundResource(controller.getInstance().getWaitingImages()[Integer.parseInt(code) % 7]);

        // 음식번호(num) 뷰
        llFoodNum = (LinearLayout) view.findViewById(R.id.fwfb_V_foodnum);

    }


    private void initEvent() {

        tvInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog = new Dialog((MainActivity) getActivity(),
                        null,
                        "대기번호를 초기화하면 알림이 오지\n않습니다. 초기화 하시겠습니까?",
                        initCancel,
                        initConfirm,
                        "취소",
                        "확인");
                mDialog.show();

            }
        });

    }


    // 기다리는 번호 레이아웃 설정
    private void setWaitNumber() {

        int margin = 10;
        int textSmallSize = 30;
        int textBigSize = 50;
        String textColor = "#FFFFFF";

        // 음식번호(num) 텍스트 : '(실제 입력한 번호)'
        if (num1 != null && !num1.equals("-1")) {
            txFoodNum1 = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(IntToDp(margin), 0, IntToDp(margin), 0); //left top right bottom
            txFoodNum1.setLayoutParams(params);
            txFoodNum1.setText(num1);
            txFoodNum1.setTextSize(textBigSize);
            txFoodNum1.setTextColor(Color.parseColor(textColor));
            txFoodNum1.setTag("txFoodNum1");
            llFoodNum.addView(txFoodNum1);
        }

        if (num2 != null && !num2.equals("-1")) {
            txFoodNum2 = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(IntToDp(margin), 0, IntToDp(margin), 0); //left top right bottom
            txFoodNum2.setLayoutParams(params);
            txFoodNum2.setText(num2);
            txFoodNum2.setTextSize(textSmallSize);
            txFoodNum2.setTextColor(Color.parseColor(textColor));
            txFoodNum2.setTag("txFoodNum2");
            llFoodNum.addView(txFoodNum2);
        }

        if (num3 != null && !num3.equals("-1")) {
            txFoodNum3 = new TextView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(IntToDp(margin), 0, IntToDp(margin), 0); //left top right bottom
            txFoodNum3.setLayoutParams(params);
            txFoodNum3.setText(num3);
            txFoodNum3.setTextSize(textSmallSize);
            txFoodNum3.setTextColor(Color.parseColor(textColor));
            txFoodNum3.setTag("txFoodNum3");
            llFoodNum.addView(txFoodNum3);
        }

    }



    private int IntToDp(int margin) {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int)(10 * dm.density);
    }


    private View.OnClickListener initCancel = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            mDialog.cancel();
        }
    };


    private View.OnClickListener initConfirm = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            mDialog.cancel();

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.onFragmentChanged(1, null, null);
        }
    };



    // 완료된 푸시 TextView 설정
    public boolean matchCompleteFoodNumber(String number) {

        String completeColor = "#F26C4F";
        TextView textView1, textView2, textView3;
        textView1 = (TextView)llFoodNum.findViewWithTag("txFoodNum1");
        textView2 = (TextView)llFoodNum.findViewWithTag("txFoodNum2");
        textView3 = (TextView)llFoodNum.findViewWithTag("txFoodNum3");

        if(textView1 != null && textView1.getText().toString().equals(number)) {
            textView1.setTextColor(Color.parseColor(completeColor));
            return true;
        }
        if(textView2 != null && textView2.getText().toString().equals(number)) {
            textView2.setTextColor(Color.parseColor(completeColor));
            return true;
        }
        if(textView3 != null && textView3.getText().toString().equals(number)) {
            textView3.setTextColor(Color.parseColor(completeColor));
            return true;
        }
        return false;
    }



    private void initSocketView(View view) {

        socketURL = controller.getBaseUrl();

        gridCompleteFoodNumber = (GridView) view.findViewById(R.id.fcfb_grid_complete_food);
        gridviewAdapter = new GridviewAdapter(getContext());
        gridCompleteFoodNumber.setAdapter(gridviewAdapter);
        tagCafeteria = registerData.getCode();
    }


    private void initSocket() {

        try {
            mSocket = IO.socket(socketURL);
        } catch (URISyntaxException e) {

        }
        //mSocket.connect();
        //mSocket.on(tagCafeteria, onNewMessage);
    }

    private void attemptSend(String tag, String sendMessage) {
        sendMessage = "전송할 데이터";
        if (TextUtils.isEmpty(sendMessage)) {
            return;
        }

        mSocket.emit(tag, sendMessage);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    try {
                        receiveMessage = data.getString("msg");
                    } catch (JSONException e) {
                        return;
                    }

                    //Log.i("debug", receiveMessage);
                    // 소켓통신으로 온 완료된 음식번호 중 사용자가 대기중이었던 번호를 제외하고 미니전광판에 띄움
                    if (!matchCompleteFoodNumber(receiveMessage)) {
                        gridviewAdapter.add(receiveMessage);
                        gridviewAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    };


    public void addCompleteFood(String number) {
        if (mSocket.connected()) {
            gridviewAdapter.add(number);
            gridviewAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mSocket.connect();
        mSocket.on(tagCafeteria, onNewMessage);
    }


    @Override
    public void onStop() {
        super.onStop();
        mSocket.disconnect();
        mSocket.off(tagCafeteria, onNewMessage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSocket.disconnect();
        mSocket.off(tagCafeteria, onNewMessage);
    }

}