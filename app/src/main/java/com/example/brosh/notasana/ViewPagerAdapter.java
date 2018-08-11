package com.example.brosh.notasana;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.brosh.notasana.Friends.FriendsTab;
import com.example.brosh.notasana.Tasks.TaskTab;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private Fragment[] childFragments;

    public ViewPagerAdapter(FragmentManager fm){

        super(fm);

        childFragments = new Fragment[]{
                new TaskTab(),
                new FriendsTab(),
                new GroceryTab()
        };

    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Object test = null;
        try {
            test = Class.forName(getItem(position).getClass().getName()).newInstance();
        } catch (InstantiationException e) {
            System.err.println(e);
        } catch (IllegalAccessException e) {
            System.err.println(e);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
        String title = ((Feature) test).getTitle();

        return title.subSequence(title.lastIndexOf(".")+1, title.length());
    }
}
