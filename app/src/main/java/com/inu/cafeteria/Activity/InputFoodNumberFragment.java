package com.inu.cafeteria.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.inu.cafeteria.Adapter.CoverFlowAdapter;
import com.inu.cafeteria.Adapter.InputFoodNumberRecyclerAdapter;
import com.inu.cafeteria.Application.ApplicationController;
import com.inu.cafeteria.CoverFlow.CoverFlowView;
import com.inu.cafeteria.CoverFlow.FoodMenuListViewAdpater;
import com.inu.cafeteria.Model.CoverflowModel;
import com.inu.cafeteria.Model.RegisterData;
import com.inu.cafeteria.Network.NetworkService;
import com.inu.cafeteria.R;
import com.inu.cafeteria.Utility.CustomRecyclerDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InputFoodNumberFragment extends Fragment implements View.OnClickListener {

    private ViewGroup view;
    private int editInputCount;


    // Coverflow 관련 뷰
    private CoverFlowView coverFlowView;
    private TextView textCode;
    private List<CoverflowModel> coverflowList;

    CoverFlowAdapter coverFlowAdapter;


    // 입력 값 저장을 위한 배열
    private String[] loadData = {"", "", ""};

    RecyclerView recyclerInputFoodNumber;
    RecyclerView.Adapter adapterInputFoodNumber;
    RecyclerView.LayoutManager layoutManager;

    private Button btnInput;
    private Button btnPlus;
    private Button btnMinus;

    RegisterData registerData;

    InputFoodNumberFragment fragment;
    Bundle bundle;

    //SlidingDrawer for Cafeteria Food Menu
    SlidingDrawer foodMenuSlidingDrawer;
    NetworkService networkService;
    ListView foodMenuListView;


    public InputFoodNumberFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_input_food_number, container, false);

        initView();
        initEvent();
        initListDataAndAction();
        loadFoodMenu();
        initInputLock();

        return view;
    }

    private void initInputLock() {
        coverFlowView.setCoverflowIsAlarm(ApplicationController.getInstance().getCoverflowIsAlarm());
        coverFlowView.setSubmitButton(btnInput);
    }


    private void initView() {

        editInputCount = 1;

        coverFlowView = (CoverFlowView) view.findViewById(R.id.fifn_coverflow);
        textCode = (TextView) view.findViewById(R.id.fifn_text_code);
        recyclerInputFoodNumber = (RecyclerView) view.findViewById(R.id.fifn_recycle_input_food_number);
        btnInput = (Button) view.findViewById(R.id.fifn_btn_submit);
        btnPlus = (Button) view.findViewById(R.id.fifn_btn_plus);
        btnMinus = (Button) view.findViewById(R.id.fifn_btn_minus);
        foodMenuSlidingDrawer = (SlidingDrawer) view.findViewById(R.id.fifn_slidingdrawer_foodmenu);
        networkService = ApplicationController.getInstance().getNetworkService();
        foodMenuListView = (ListView) view.findViewById(R.id.fifn_listview_foodmenu);

    }


    private void initEvent() {
        btnInput.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
    }


    private void initListDataAndAction() {


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerInputFoodNumber.setLayoutManager(layoutManager);
        adapterInputFoodNumber = new InputFoodNumberRecyclerAdapter(getActivity(), editInputCount, loadData);
        recyclerInputFoodNumber.setAdapter(adapterInputFoodNumber);
        // set the interval between items
        recyclerInputFoodNumber.addItemDecoration(new CustomRecyclerDecoration(10));

        coverflowList = new ArrayList<>();

        for (int i = 0; i < ApplicationController.getInstance().getCoverflowTitles().length; i++) {
            CoverflowModel channelBean = new CoverflowModel();
            channelBean.setImg(ApplicationController.getInstance().getCoverflowImages()[i]);
            channelBean.setIsMenu((ApplicationController.getInstance().getCoverflowIsMenu()[i]));
            coverflowList.add(channelBean);
        }

        coverFlowAdapter = new CoverFlowAdapter(getActivity(), coverflowList);
        // 스크롤할 때 식당이름을 나타내는 텍스트뷰를 넣는 부분
        coverFlowView.setTextView(textCode);
        // 스크롤할 때 변경되는 식당이름을 넣는 부분
        coverFlowView.setCafeList(ApplicationController.getInstance().getCoverflowTitles());
        coverFlowView.setAdapter(coverFlowAdapter);

    }


    private void loadFoodMenu() {
        Call<JsonElement> foodMenuCall = networkService.getFoodMenu(getTodayFormatyyyyMMdd());
        foodMenuCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) { //status가 200일 경우
                    HashMap<String, String> hashMap = ApplicationController.getInstance().getSlidingdrawer_cafecode();
                    ArrayList<String> arrayList = ApplicationController.getInstance().getSlidingdrawer_cafename();

                    JsonObject allCafeteriaMenuInfo = response.body().getAsJsonObject();//최상위 오브젝트 받아옴
                    coverFlowView.setCafeCode(ApplicationController.getInstance().getCoverflowCodes());
                    coverFlowView.setCoverflowIsMenu((ApplicationController.getInstance().getCoverflowIsMenu()));
                    coverFlowView.setFoodMenuView(foodMenuListView);
                    coverFlowView.setFoodMenu(allCafeteriaMenuInfo);
                    FoodMenuListViewAdpater foodMenuListViewAdpater = new FoodMenuListViewAdpater(getContext());
                    coverFlowView.setFoodMenuAdapter(foodMenuListViewAdpater);

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fifn_btn_submit:

//                Toast.makeText(getContext(), "현재 점검중입니다.", Toast.LENGTH_SHORT).show();
//                if(true) break;

                // 키보드 내림
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);


                // 비어있는 input 칸의 개수를 셈
                int editEmptyCount = 0;

                for (int i = 0; i < editInputCount; i++) {
                    EditText tmpEdit = (EditText) recyclerInputFoodNumber.getChildAt(i).findViewById(R.id.vrifni_edit_input_food_number);

                    if (tmpEdit.getText().toString().equals("")) {
                        editEmptyCount++;
                    }
                }

                // input 칸이 모두 비어 있는 경우
                if (editEmptyCount == editInputCount) {
                    Toast.makeText(getActivity(), "주문번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

                // 식당 이름과 식당 코드 정보 넘기기
                fragment = new InputFoodNumberFragment();
                bundle = new Bundle();
                bundle.putString("name", ApplicationController.getInstance().getCoverflowTitles()[coverFlowView.getTopViewPosition()]);
                bundle.putString("code", ApplicationController.getInstance().getCoverflowCodes()[coverFlowView.getTopViewPosition()]);


                // 대기 번호 입력 텍스트에 있는 값들을 넘김
                for (int i = 0; i < editInputCount; i++) {
                    EditText editText = (EditText) recyclerInputFoodNumber.getChildAt(i).findViewById(R.id.vrifni_edit_input_food_number);
                    String tmp = "";

                    if (!editText.getText().toString().equals("")) {
                        tmp = Integer.parseInt(editText.getText().toString()) + "";
                    }

                    //intent.putExtra("num" + (i+1), tmp.equals("") ? "-1" : tmp);
                    bundle.putString("num" + (i + 1), tmp.equals("") ? "-1" : tmp);
                }

                for (int i = editInputCount; i < 3; i++) {

                    //intent.putExtra("num" + (i+1), "-1");
                    bundle.putString("num" + (i + 1), "-1");
                }


                // 서버에 대기번호 등록하기
                registerData = new RegisterData(
                        bundle.getString("code"),
                        bundle.getString("num1"),
                        bundle.getString("num2"),
                        bundle.getString("num3"),
                        FirebaseInstanceId.getInstance().getToken(),
                        "android");


                fragment.setArguments(bundle);


                // MainActivity로 fragment 전환
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onFragmentChanged(0, registerData, bundle.getString("name"));

                break;


            // TODO
            case R.id.fifn_btn_plus:

                // 기존에 입력한 데이터 저장
                for (int i = 0; i < editInputCount; i++) {
                    EditText editText = (EditText) recyclerInputFoodNumber.getChildAt(i).findViewById(R.id.vrifni_edit_input_food_number);
                    loadData[i] = editText.getText().toString();
                }

                if (editInputCount < 3) {
                    adapterInputFoodNumber = new InputFoodNumberRecyclerAdapter(getActivity(), ++editInputCount, loadData);
                    recyclerInputFoodNumber.setAdapter(adapterInputFoodNumber);
                }

                // input 칸이 3개인 경우 plus 버튼 숨김
                if (editInputCount >= 3) {
                    btnPlus.setVisibility(View.INVISIBLE);
                } else if (btnMinus.getVisibility() == View.INVISIBLE) {
                    btnMinus.setVisibility(View.VISIBLE);
                }

                break;


            case R.id.fifn_btn_minus:

                for (int i = 0; i < editInputCount - 1; i++) {
                    EditText editText = (EditText) recyclerInputFoodNumber.getChildAt(i).findViewById(R.id.vrifni_edit_input_food_number);
                    loadData[i] = editText.getText().toString();
                }

                for (int i = editInputCount - 1; i < 3; i++) {
                    loadData[i] = "";
                }

                if (editInputCount > 1) {
                    adapterInputFoodNumber = new InputFoodNumberRecyclerAdapter(getActivity(), --editInputCount, loadData);
                    recyclerInputFoodNumber.setAdapter(adapterInputFoodNumber);
                }

                // input 칸이 1개인 경우 minus 버튼 숨김
                if (editInputCount <= 1) {
                    btnMinus.setVisibility(View.INVISIBLE);
                } else if (btnPlus.getVisibility() == View.INVISIBLE) {
                    btnPlus.setVisibility(View.VISIBLE);
                }

                break;

            default:
                break;


        }

    }

    private String getTodayFormatyyyyMMdd() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(calendar.getTime());
        return today;
    }


}