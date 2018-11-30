package mx.com.vialogika.mistclient.Utils;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mx.com.vialogika.mistclient.EdoApostamientos;
import mx.com.vialogika.mistclient.EdoClients;
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
                return EdoClients.newInstance("Hello","Hello");
            case 3:
                return EdoClients.newInstance("hello","hello");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
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
                title = "Clientes";
                break;
            case 3:
                title = "Proveedores";
                break;
        }
        return title;
    }
}
