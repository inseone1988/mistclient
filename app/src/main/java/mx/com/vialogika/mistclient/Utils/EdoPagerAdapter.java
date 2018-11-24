package mx.com.vialogika.mistclient.Utils;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mx.com.vialogika.mistclient.EdoApostamientos;
import mx.com.vialogika.mistclient.EdoGroups;
import mx.com.vialogika.mistclient.EdoGuards;

public class EdoPagerAdapter extends FragmentPagerAdapter {

    public EdoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private void setupTitle(){

    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return EdoGuards.newInstance("Hello","Hello");
            case 1:
                return EdoApostamientos.newInstance("Hello","Hello");
            case 2:
                return EdoGroups.newInstance("Hello","Hello");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = "";
        switch(position){
            case 0 :
                title = "Guardias";
                break;
            case 1:
                title = "Apostamientos";
                break;
            case 2:
                title = "Grupos";
                break;
        }
        return title;
    }
}
