package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;
import com.iottivebeacontool.iottivebeacontool.widget.CustomTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iottive on 7/20/17.
 */

public class NearByBeaconFragment extends Fragment {
    private Activity near_bcn_activity;
    private CustomTabLayout tabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter pager_adapter = null;
    private UnregisteredFragment unregisteredFragment = new UnregisteredFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.near_by_beacon, container, false);
        near_bcn_activity=getActivity();
        initUi(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        near_bcn_activity=getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        near_bcn_activity = getActivity();
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        if(near_bcn_activity instanceof MainActivity){
            MainActivity myactivity = (MainActivity) near_bcn_activity;
            myactivity.setToolbar(getString(R.string.beacon_near_me),View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initUi(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
       setupViewPager(mViewPager);
        tabLayout = (CustomTabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        pager_adapter = new ViewPagerAdapter(getChildFragmentManager());
        pager_adapter.addFrag(unregisteredFragment, getString(R.string.unregistered));
        pager_adapter.addFrag(new RegisteredFragment(), getString(R.string.registered));

        viewPager.setAdapter(pager_adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position >= getCount()) {
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void onRefreshData(final ArrayList<NearByBeaconModel> bcnList) {
        Log.d("onRefreshDatam Size :", bcnList.size() + "");
        if (unregisteredFragment != null) {
          // unregisteredFragment.onRefreshData(bcnList);
        }
    }

}
