package leu.doan_datdoan.fragment.cuahang;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.adapters.ViewPagerAdapter;
import leu.doan_datdoan.animation.DeptAnimation;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuaHang_TrangChuFragment extends Fragment {

    @BindView(R.id.viewpager_cuahang_trangchu)
    ViewPager viewPager;
    @BindView(R.id.tablayout_cuahang_trangchu)
    TabLayout tabLayout;

    private CuaHang cuaHang;
    private ViewPagerAdapter viewPagerAdapter;
    private Activity activity;
    private List<DonHang> donHangs;
    private List<DonHang> donHangPheDuyetList;
    private List<DonHang> donHangDangShipList;
    private List<DonHang> donHangHuyList;
    private List<DonHang> donHangLichSuList;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cua_hang_trang_chu, container, false);
        donHangLichSuList = new ArrayList<>();
        donHangPheDuyetList = new ArrayList<>();
        cuaHang = (CuaHang) getArguments().getSerializable(CuaHangActivity.KEY_BUNDLE_CUAHANG);
        ((CuaHangActivity)activity).showingDialog();

        setupUI(v);
        return v;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPagePosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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

        loadingData();
    }

    private void loadingData() {
        RetrofitFactory.getInstance().createService(CuaHangService.class).layDonHangTuCuaHang(cuaHang.getId()).enqueue(new Callback<List<DonHang>>() {
            @Override
            public void onResponse(Call<List<DonHang>> call, Response<List<DonHang>> response) {
                if(response.isSuccessful()){
                    donHangs = response.body();
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    setupTabsIcon();
                    ((CuaHangActivity)activity).dismissingDialog();

                }else{
                    Toast.makeText(activity,"Lỗi vui lòng ấn lại trang chủ!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DonHang>> call, Throwable t) {
                Toast.makeText(activity,"Lỗi vui lòng ấn lại trang chủ!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTabsIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_assignment_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_check_box_white_24dp);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        CuaHang_PheDuyetFragment cuaHang_pheDuyetFragment = new CuaHang_PheDuyetFragment();
        CuaHang_LichSuFragment cuaHang_lichSuFragment = new CuaHang_LichSuFragment();

        Bundle b = new Bundle();
        b.putSerializable(CuaHangActivity.KEY_BUNDLE_CUAHANG,cuaHang);
        cuaHang_lichSuFragment.setArguments(b);
        cuaHang_pheDuyetFragment.setArguments(b);

        for(int i = 0;i <donHangs.size();i++){
            if(!donHangs.get(i).getTrangThai().equals("Đã xong")){
                donHangPheDuyetList.add(donHangs.get(i));
            }else{
                donHangLichSuList.add(donHangs.get(i));
            }
        }

        cuaHang_pheDuyetFragment.setDonHangs(donHangPheDuyetList);
        cuaHang_pheDuyetFragment.setContext(activity);
        cuaHang_lichSuFragment.setDonHangs(donHangLichSuList);
        cuaHang_lichSuFragment.setContext(activity);

        viewPagerAdapter.addFragment(cuaHang_pheDuyetFragment, "Đơn hàng chờ");
        viewPagerAdapter.addFragment(cuaHang_lichSuFragment, "Đơn đã duyệt");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(true,new DeptAnimation());
    }

}
