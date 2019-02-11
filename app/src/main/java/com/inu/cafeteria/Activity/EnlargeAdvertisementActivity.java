package com.inu.cafeteria.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.inu.cafeteria.Adapter.EnlargeAdvertisementViewPagerAdapter;
import com.inu.cafeteria.Model.AdvertisementResultList;
import com.inu.cafeteria.R;
import com.inu.cafeteria.Utility.CircleIndicator;

import java.util.ArrayList;

public class EnlargeAdvertisementActivity extends AppCompatActivity {

    private ViewPager viewPagerForAdvertisement;
    private CircleIndicator indicatorForAdvertisement;
    private ImageButton buttonForLink;
    private String link;
    private ArrayList<AdvertisementResultList> advertisementResultLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_advertisement);

        viewPagerForAdvertisement = (ViewPager) findViewById(R.id.aea_viewpager_advertisement);
        indicatorForAdvertisement = (CircleIndicator) findViewById(R.id.aea_indicator_advertisement);
        buttonForLink = (ImageButton) findViewById(R.id.aea_button_link);


        // get ad data
        Intent intent = getIntent();
        advertisementResultLists = (ArrayList<AdvertisementResultList>) intent.getSerializableExtra("AdData");
        int position = intent.getIntExtra("position", 1);

        // init link when first page initiate
        link = advertisementResultLists.get(position).getUrl();
        if(!link.equals("")) {
            buttonForLink.setVisibility(View.VISIBLE);
            buttonForLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                }
            });
        }


        EnlargeAdvertisementViewPagerAdapter adapter = new EnlargeAdvertisementViewPagerAdapter(getLayoutInflater(), getApplicationContext(), advertisementResultLists);
        viewPagerForAdvertisement.setAdapter(adapter);

        // for indicator
        indicatorForAdvertisement.setItemMargin(dpToPx(getApplicationContext(), 4));
        indicatorForAdvertisement.createDotPanel(advertisementResultLists.size(), R.drawable.gray_circle, R.drawable.point_blue_circle);
        viewPagerForAdvertisement.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorForAdvertisement.selectDot(position);
                link = advertisementResultLists.get(position).getUrl();
                if(link.equals("")) {
                    buttonForLink.setVisibility(View.INVISIBLE);
                } else {
                    buttonForLink.setVisibility(View.VISIBLE);
                    buttonForLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // for scale up and down page animation when scroll the view pager
        MyPageTransFormer transFormer = new MyPageTransFormer();
        viewPagerForAdvertisement.setPageTransformer(false, transFormer);
    }

    private class MyPageTransFormer implements ViewPager.PageTransformer {
        private float standardSize = 1.0f;
        @Override
        public void transformPage(View page, float position) {
            position -= 0.258427;
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setScaleY(standardSize - 0.2f);
                page.setScaleX(standardSize - 0.2f);
            } else if (position < 0) { // [-1,0]
                // when moving to the left page
                page.setScaleY(standardSize + (position / 5));
                page.setScaleX(standardSize + (position / 5));
            } else if (position == 0) { // (0,1]
                // page generating
                page.setScaleY(standardSize);
                page.setScaleX(standardSize);
            } else if(position < 1) {
                page.setScaleY(standardSize - (position / 5));
                page.setScaleX(standardSize - (position / 5));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setScaleY(standardSize - 0.2f);
                page.setScaleX(standardSize - 0.2f);
            }
        }
    }

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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fast_fadeout, R.anim.fast_fadeout);
    }
}
