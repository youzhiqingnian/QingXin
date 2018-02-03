package com.qingxin.medical.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.qingxin.medical.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        a
//        setContentView(R.layout.activity_main2);

//        initViewPager();

    }

//    private void initViewPager() {
//
//        UltraViewPager ultraViewPager = (UltraViewPager)findViewById(R.id.ultra_viewpager);
//        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
////initialize UltraPagerAdapter，and add child view to UltraViewPager
//        PagerAdapter adapter = new UltraPagerAdapter(false);
//        ultraViewPager.setAdapter(adapter);
//
////initialize built-in indicator
//        ultraViewPager.initIndicator();
////set style of indicators
//        ultraViewPager.getIndicator()
//                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                .setFocusResId(R.mipmap.vp_selected)
//                .setNormalResId(R.mipmap.vp_unselected)
//                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
////set the alignment
//        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
////construct built-in indicator, and add it to  UltraViewPager
//        ultraViewPager.getIndicator().build();
//
////set an infinite loop
//        ultraViewPager.setInfiniteLoop(true);
////enable auto-scroll mode
//        ultraViewPager.setAutoScroll(2000);
//
//
//
//    }
}
