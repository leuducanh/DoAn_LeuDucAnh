package leu.doan_datdoan.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import leu.doan_datdoan.R;


/**
 * Created by MyPC on 24/10/2017.
 */
public class ScreenManager {
    public static void openFragment(FragmentManager fragmentManager, Fragment fragment, int layoutID, boolean addToBackStack, boolean mainPlayer) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mainPlayer) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom);
        } else {
        }
        fragmentTransaction.replace(layoutID, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, int layoutID, boolean addToBackStack, boolean mainPlayer) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mainPlayer) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_bottom, 0, 0, R.anim.exit_to_bottom);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        }
        fragmentTransaction.add(layoutID, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public static void showFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(fragment);

        fragmentTransaction.commit();
    }

    public static void hideFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.show(fragment);

        fragmentTransaction.commit();
    }


    public static void backFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }
}
