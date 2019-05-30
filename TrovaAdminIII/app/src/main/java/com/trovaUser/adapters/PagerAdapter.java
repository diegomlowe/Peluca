package com.trovaUser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.trovaUser.view.CercanasFragment;
import com.trovaUser.view.FavoritosFragment;
import com.trovaUser.view.PromosFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {


    int numTabs;


    public PagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch(i){

            case 0:
                CercanasFragment cercanasFragment = new CercanasFragment();
                return cercanasFragment;

            case 1:
                PromosFragment promosFragment = new PromosFragment();
                return promosFragment;

            case 2:
                FavoritosFragment favoritosFragment = new FavoritosFragment();
                return favoritosFragment;

            default: return null;



        }


    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
