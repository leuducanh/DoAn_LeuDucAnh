package leu.doan_datdoan.fragment.khachhang;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.KhachHangActivity;
import leu.doan_datdoan.activity.XemCuaHangActivity;
import leu.doan_datdoan.adapters.ViewPagerAdapter;
import leu.doan_datdoan.anim.DeptAnimation;
import leu.doan_datdoan.events.khachhang.KhachHang_OnClickChonCuaHangEvent;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.model.KhachHang;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrangChuKhachHangFragment extends Fragment {

    @BindView(R.id.tablayout_khachhang_trangchu)
    public TabLayout tabLayout;
    @BindView(R.id.viewpager_khachhang_trangchu)
    public ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;
    private KhachHang kh;
    private KhachHangActivity khachHangActivity;

    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void setKhachHangActivity(KhachHangActivity khachHangActivity) {
        this.khachHangActivity = khachHangActivity;
    }

    public TrangChuKhachHangFragment() {
        // Required empty public constructor
    }

    public void setKh(KhachHang kh) {
        this.kh = kh;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trang_chu_khach_hang, container, false);
        setupView(v);

            EventBus.getDefault().register(this);

        kh = (KhachHang) getArguments().getSerializable(KhachHangActivity.KEY_BUNDLE_KHACHHANG);

        return v;
    }

    public void setupView(View view) {
        ButterKnife.bind(this, view);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPagePosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    hideKeyboard();
                }

                FragmentLifecycle fragmentLifecycleOfReleasedFragment = (FragmentLifecycle)viewPagerAdapter.getItem(currentPagePosition);
                fragmentLifecycleOfReleasedFragment.onPauseFragment();


                FragmentLifecycle fragmentLifecycleOfSelectedFragment = (FragmentLifecycle)viewPagerAdapter.getItem(position);
                fragmentLifecycleOfSelectedFragment.onResumeFragment();

                currentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabsIcon();
    }

    private void setupTabsIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.near_me);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search_black_24dp);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        KhachHang_NearMeViewPagerFragment khachHang_nearMeViewPagerFragment = new KhachHang_NearMeViewPagerFragment();
        KhachHang_SearchViewPagerFragment khachHang_searchViewPagerFragment = new KhachHang_SearchViewPagerFragment();

        viewPagerAdapter.addFragment(khachHang_nearMeViewPagerFragment, "Gần bạn");
        viewPagerAdapter.addFragment(khachHang_searchViewPagerFragment, "Tìm kiếm");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true,new DeptAnimation());
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(viewPager.getWindowToken(),0);
    }

    @Subscribe(sticky = true)
    public void onKhachHang_ChonCuaHang(KhachHang_OnClickChonCuaHangEvent khachHang_onClickChonCuaHangEvent){
        Log.d("abc","abc");
        Intent i = new Intent(getActivity(), XemCuaHangActivity.class);
        i.putExtra(XemCuaHangActivity.KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_CUAHANGKEY, khachHang_onClickChonCuaHangEvent.getCuaHang());
        i.putExtra(XemCuaHangActivity.KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_KHACHHANGKEY,kh);

        EventBus.getDefault().removeStickyEvent(khachHang_onClickChonCuaHangEvent);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
