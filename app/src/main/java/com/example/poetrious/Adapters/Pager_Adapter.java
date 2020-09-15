package com.example.poetrious.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.poetrious.Fragment.Followers;
import com.example.poetrious.Fragment.Following;

import java.util.ArrayList;
import java.util.List;

public class Pager_Adapter extends FragmentStatePagerAdapter {
  int noOfTabs;
    private List<String> mFragmentTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    public Pager_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
      this.  noOfTabs=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                Followers followers=new Followers();
                return  followers;
            case 1:
                Following following=new Following();
                return  following;
            default:
               return null;
        }

    }
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
    @Override
    public int getCount() {
        return noOfTabs;
    }
}
