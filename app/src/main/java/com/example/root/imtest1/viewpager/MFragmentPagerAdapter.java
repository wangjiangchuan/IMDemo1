package com.example.root.imtest1.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by cuicui on 16/5/8.
 */
public class MFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;

    //Fragment的list
    private List<Fragment> mListFragment;

    public MFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mListFragment = list;

    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }


}
