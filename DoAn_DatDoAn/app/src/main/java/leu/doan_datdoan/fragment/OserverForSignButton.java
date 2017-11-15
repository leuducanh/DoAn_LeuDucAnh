package leu.doan_datdoan.fragment;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 08/07/2017.
 */

public class OserverForSignButton {
    private List<View> views;
    private static OserverForSignButton oserverForSignButton;

    public static OserverForSignButton getInstance(){
        if(oserverForSignButton == null){
            oserverForSignButton = new OserverForSignButton();
        }

        return oserverForSignButton;
    }

    public void add(View view){
        if(views == null){
            views = new ArrayList<>();
        }

        views.add(view);
    }

    public void turnOffButtons(){
        if(views != null){
            for(int i = 0;i < views.size();i++){
                views.get(i).setClickable(false);
            }
        }
    }

    public void turnOnButtons(){
        if(views != null){
            for(int i = 0;i < views.size();i++){
                views.get(i).setClickable(true);
            }
        }
    }
}
