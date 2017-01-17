package com.example.wangguanghong.viewpagerindicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private List<ViewPagerFragment> mViewPagerFragments=new ArrayList<ViewPagerFragment>();
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;
    private ViewPagerIndicator mViewPagerIndicator;
    private List<String> mTitles= Arrays.asList("天下第一","天下第二","天下第三","天下第四","天下第五","天下第六","天下第七","天下第八","天下第九");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();
        mViewPagerIndicator.setItemCount(5);
        mViewPagerIndicator.setTitles(mTitles);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPagerIndicator.setViewPager(mViewPager,0);
        /**
         * 外部回调实现
         */
//        mViewPagerIndicator.setPageChangeListener(new ViewPagerIndicator.onPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Toast.makeText(getApplicationContext(),"滑到了"+position,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    private void initViews() {
        mViewPager= (ViewPager) findViewById(R.id.viewpager);
        mViewPagerIndicator= (ViewPagerIndicator) findViewById(R.id.viewpagerIndicator);
    }

    private void initDatas() {
        for(String title:mTitles){
            ViewPagerFragment fragment=ViewPagerFragment.newInstance(title);
            mViewPagerFragments.add(fragment);
        }
        mFragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mViewPagerFragments.get(position);
            }

            @Override
            public int getCount() {
                return mViewPagerFragments.size();
            }
        };
    }


}
