package com.example.wangguanghong.viewpagerindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wangguanghong on 2017/1/17.
 */

public class ViewPagerFragment extends Fragment {
    private String mTitle;
    private static final String BUNDLE_TITLE="title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        if(bundle!=null){
            mTitle=bundle.getString(BUNDLE_TITLE);
        }
        TextView textView=new TextView(getActivity());
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        return textView;
    }

    public static ViewPagerFragment newInstance(String title){
        Bundle bundle=new Bundle();
        bundle.putString(BUNDLE_TITLE,title);

        ViewPagerFragment viewPagerFragment=new ViewPagerFragment();
        viewPagerFragment.setArguments(bundle);

        return viewPagerFragment;
    }
}
