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

/**
 * 등록된 대기번호 표시하는 프래그먼트.
 * 번호는 소켓으로 날아온다.
 */
public class WaitingFoodNumberFragment extends Fragment {

    /**
     * 의존성 주입 인스턴스
     */
    ApplicationController controller;

    /**
     * 기타 UI
     */
    ViewGroup view;
    Dialog mDialog;
    private String cafeteriaName, num1, num2, num3, code;
    private LinearLayout llBackImage;
    private LinearLayout llFoodNum;
    private TextView txFoodNum1, txFoodNum2, txFoodNum3, textCafeteriaName;
    private ImageButton tvInit;

    /**
     * retrofit 아닌 bundle로부터 받은 번호 데이터들.
     */
    Bundle bundle;
    private RegisterData registerData;

    /** 클래스 외부에서 함수를 사용하기 위함
     * ##주의## 이렇게 하면 메모리 누수 생김...
     */
    public static Fragment mFragment;

    /**
     * 번호 수신받기 위한 소켓 정보.
     */
    private String socketURL;
    private String receiveMessage;
    private String tagCafeteria;

    /**
     * 그리고 소켓.
     */
    private Socket mSocket;

    /**
     * 완료된 번호를 표시할 그리드뷰와, 그 친구인 어댑터.
     */
    GridView gridCompleteFoodNumber;
    GridviewAdapter gridviewAdapter;


    /**
     * 진입점.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

    /**
     * 필드 객체 초기화
     */
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

    /**
     * 클릭 리스너 등록
     */
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

    /**
     * 기다리는 번호 레이아웃을 설정해준다.
     * 수동으로..
     */
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

    /**
     * 수신한 대기번호가 등록된 번호라면 색칠해주고 true를 반환.
     * 등록된 3개 번호 중 하나와도 일치하지 않을 때,
     * 즉 관심없는 번호일 때에는 아무것도 안하고 false 반환
     * @param number 날아온 대기번호
     * @return 기다리는 번호였으면 true, 아니면 false
     */
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

    /**
     * 소켓통신과 관련된 완료번호 UI를 초기화.
     * @param view 이 프래그먼트 뷰
     */
    private void initSocketView(View view) {

        socketURL = controller.getBaseUrl();

        gridCompleteFoodNumber = (GridView) view.findViewById(R.id.fcfb_grid_complete_food);
        gridviewAdapter = new GridviewAdapter(getContext());
        gridCompleteFoodNumber.setAdapter(gridviewAdapter);
        tagCafeteria = registerData.getCode();
    }

    /**
     * 소켓을 초기화하고 연결.
     */
    private void initSocket() {

        try {
            mSocket = IO.socket(socketURL);
        } catch (URISyntaxException e) {

        }

        /*
         * 지금은 작동을 안하는 모양이다.... 주석처리되어있었네..
         */

        //mSocket.connect();
        //mSocket.on(tagCafeteria, onNewMessage);
    }

    /**
     * 소켓으로다가 보낸다.
     * @param tag 무엇
     * @param sendMessage 무엇에 해당하는 메시지
     */
    private void attemptSend(String tag, String sendMessage) {
        sendMessage = "전송할 데이터";
        if (TextUtils.isEmpty(sendMessage)) {
            return;
        }

        mSocket.emit(tag, sendMessage);
    }

    /**
     * 소켓으로 대기번호 데이터가 날아올 때 취할 행동.
     *
     * 기다리는 번호가 있으면 처리하고, 그렇지 않으면 하단의 완료번호 목록에 추가함.
     */
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

                    // 소켓통신으로 온 완료된 음식번호 중 사용자가 대기중이었던 번호를 제외하고 미니전광판에 띄움
                    if (!matchCompleteFoodNumber(receiveMessage)) {
                        gridviewAdapter.add(receiveMessage);
                        gridviewAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    };

    /**
     * 완료된 대기번호 목록에 하나를 추가해준다.
     * @param number 추가할 것
     */
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

    private int IntToDp(int margin) {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int)(10 * dm.density);
    }
}