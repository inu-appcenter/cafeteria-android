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

/**
 * 식당을 지정하고 대기번호를 추가하는 프래그먼트.
 */
public class InputFoodNumberFragment extends Fragment implements View.OnClickListener {

    private ViewGroup view;

    /**
     * 대기번호 몇개나 썼나
     */
    private int editInputCount;

    /**
     * 식당 옆으로 넘겨서 보는 cover flow
     */
    private CoverFlowView coverFlowView;
    private TextView textCode;
    private List<CoverflowModel> coverflowList;

    /**
     * cover flow 뷰에 사용할 어댑터
     */
    CoverFlowAdapter coverFlowAdapter;

    /**
     * 입력한 대기번호 배열. 최대 3개라 3개임.
     */
    private String[] loadData = {"", "", ""};

    /**
     * 대기번호 입력할 리사이클러뷰와 그 어댑터, 그리고 레이아웃 매니저
     */
    RecyclerView recyclerInputFoodNumber;
    RecyclerView.Adapter adapterInputFoodNumber;
    RecyclerView.LayoutManager layoutManager;

    /**
     * 번호 추가, 삭제, 입력 버튼
     */
    private Button btnInput;
    private Button btnPlus;
    private Button btnMinus;

    /**
     * 서버에 보낼 대기번호 등록 데이터
     */
    RegisterData registerData;

    /**
     * 주문번호 초기화하고 돌아갈 프래그먼트
     */
    InputFoodNumberFragment fragment;
    Bundle bundle;

    /**
     * 음식 메뉴 보여줄 슬라이딩 drawer
     */
    SlidingDrawer foodMenuSlidingDrawer;
    ListView foodMenuListView;

    /**
     * retrofit 객체
     */
    NetworkService networkService;


    /**
     * 생성자이다. 사실 안쓴다.
     */
    public InputFoodNumberFragment() {
        // 오잉 생성자가 비어있네
    }

    /**
     * 진입점.
     * @param inflater 다 아는 그거
     * @param container 이것도 다 아는거
     * @param savedInstanceState 뻔한거
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.fragment_input_food_number, container, false);

        // 객체 초기화
        initView();

        // 리스너 등록
        initEvent();

        // 식당 목록과 번호 목록 초기화
        initListDataAndAction();

        // 식단 불러오기
        loadFoodMenu();

        // 식당에 따른 입력버튼 제한 설정
        initInputLock();

        return view;
    }

    /**
     * 필드 초기화.
     */
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

    /**
     * 클릭 리스너 등록.
     */
    private void initEvent() {
        btnInput.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
    }

    /**
     * 대기번호 목록과 식당 목록 UI 초기화.
     */
    private void initListDataAndAction() {
        /*
         * 번호 입력 부분
         */

        // 대기번호 입력 리사이클러뷰 레이아웃 매니저와 어댑터 만들어서 적용
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerInputFoodNumber.setLayoutManager(layoutManager);
        adapterInputFoodNumber = new InputFoodNumberRecyclerAdapter(getActivity(), editInputCount, loadData);
        recyclerInputFoodNumber.setAdapter(adapterInputFoodNumber);

        // 번호 목록 사이 간격은 10.
        recyclerInputFoodNumber.addItemDecoration(new CustomRecyclerDecoration(10));


        /*
         * 식당 목록 부분
         */

        // 식당 목록이 들어갈 리스트
        coverflowList = new ArrayList<>();

        // 의존성 주입받음. ApplicationController에서 가져옴.
        for (int i = 0; i < ApplicationController.getInstance().getCoverflowTitles().length; i++) {
            CoverflowModel channelBean = new CoverflowModel();
            channelBean.setImg(ApplicationController.getInstance().getCoverflowImages()[i]);
            channelBean.setIsMenu((ApplicationController.getInstance().getCoverflowIsMenu()[i]));
            coverflowList.add(channelBean);
        }

        // 식당 이름을 나타낼 텍스트뷰는 textCode를 사용.
        coverFlowView.setTextView(textCode);

        // 식당 타이틀 목록은 ApplicationController로부터 공수.
        coverFlowView.setCafeList(ApplicationController.getInstance().getCoverflowTitles());

        // 어댑터 만들어서 설정.
        coverFlowAdapter = new CoverFlowAdapter(getActivity(), coverflowList);
        coverFlowView.setAdapter(coverFlowAdapter);
    }

    /**
     * 식단 가져와 표시하기.
     */
    private void loadFoodMenu() {
        Call<JsonElement> foodMenuCall = networkService.getFoodMenu(getTodayFormatyyyyMMdd());
        foodMenuCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) { //status가 200일 경우
                    HashMap<String, String> hashMap = ApplicationController.getInstance().getSlidingdrawer_cafecode();
                    ArrayList<String> arrayList = ApplicationController.getInstance().getSlidingdrawer_cafename();

                    /*
                     * 식당별로 음식 메뉴 데이터 설정해줌.
                     */
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

    /**
     * 입력 버튼을 누를 수 있는 식당 설정하기.
     */
    private void initInputLock() {
        coverFlowView.setCoverflowIsAlarm(ApplicationController.getInstance().getCoverflowIsAlarm());
        coverFlowView.setSubmitButton(btnInput);
    }

    /**
     * 다양한 버튼 처리를 여기서.
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fifn_btn_submit: {
                /*
                 * 입력 버튼!
                 */

                // 버튼이 눌렸으면 일단 키보드를 내린다.
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);


                // 비어있는 input 칸의 개수를 센다.
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
            }

            case R.id.fifn_btn_plus: {
                /*
                 * 번호 추가 버튼!
                 */

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
            }

            case R.id.fifn_btn_minus: {
                /*
                 * 번호 삭제 버튼!
                 */

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
            }

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