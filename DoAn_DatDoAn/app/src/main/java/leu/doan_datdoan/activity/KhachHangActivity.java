package leu.doan_datdoan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;

import com.victor.loading.rotate.RotateLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import leu.doan_datdoan.R;
import leu.doan_datdoan.fragment.khachhang.KhachHang_DonHangFragment;
import leu.doan_datdoan.fragment.khachhang.ThongTinKhachHangFragment;
import leu.doan_datdoan.fragment.khachhang.TrangChuKhachHangFragment;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.taikhoan.TaiKhoanService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhachHangActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TRANGCHU_KHACHHANG_FRAGMEN_TAG = "trangchu_khachhang_fragment_tag";
    private static final String THONGTIN_KHACHHANG_FRAGMEN_TAG = "thongtin_khachhang_fragment_tag";
    public static final String KEY_BUNDLE_IDTAIKHOAN = "idtaikhoan";
    public static final String KEY_BUNDLE_DATAOKHACHHANG = "dataokhachhang";
    public static final String KEY_BUNDLE_USERNAME = "username";
    public static final String KEY_BUNDLE_KHACHHANG = "khachhang";



    @BindView(R.id.rl_loading)
    RotateLoading rlLoading;


    @BindView(R.id.iv_khachhang_search_viewpager_frament)
    ImageView ivSearch;

    Dialog dialog;

    NavigationView navigationView;

    private int idTaiKhoan;
    private boolean daTao = true;
    private String username;
    private KhachHang kh;
    private static final String DONHANG_KHACHHANG_FRAGMEN_TAG = "DONHANG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);

        Intent i = getIntent();
        idTaiKhoan = i.getIntExtra(KEY_BUNDLE_IDTAIKHOAN,0);
        daTao = i.getBooleanExtra(KEY_BUNDLE_DATAOKHACHHANG,true);
        username = i.getStringExtra(KEY_BUNDLE_USERNAME);

        layHoacTaoKhachHang();

        dialog = new Dialog(this);
        View v =getLayoutInflater().inflate(R.layout.dialog_loading,null,false);
        rlLoading = (RotateLoading) v.findViewById(R.id.rl_loading);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nav_khachhang_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_khachhang);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void layHoacTaoKhachHang() {
        if(daTao){
            RetrofitFactory.getInstance().createService(TaiKhoanService.class).layKhachHangBangIdTaiKhoan(idTaiKhoan).enqueue(new Callback<KhachHang>() {
                @Override
                public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                    if(response.isSuccessful()){
                        kh = response.body();
                        navigationView.getMenu().getItem(0).setChecked(true);
                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                    }
                }

                @Override
                public void onFailure(Call<KhachHang> call, Throwable t) {

                }
            });
        }else{
            RetrofitFactory.getInstance().createService(TaiKhoanService.class).khoiTaoKhachHang(idTaiKhoan,new KhachHang(0,username,"")).enqueue(new Callback<KhachHang>() {
                @Override
                public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                    if(response.isSuccessful()){
                        kh = response.body();
                        navigationView.getMenu().getItem(0).setChecked(true);
                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                    }
                }

                @Override
                public void onFailure(Call<KhachHang> call, Throwable t) {

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_khachhang);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        uncheckAllMenuItems(navigationView);
        item.setChecked(true);

        FragmentManager manager = getSupportFragmentManager();


        Bundle b = new Bundle();
        b.putSerializable(KEY_BUNDLE_KHACHHANG,kh);
        if (id == R.id.nav_khachhang_trangchu) {
            // Handle the camera action
            TrangChuKhachHangFragment trangChuKhachHangFragment = new TrangChuKhachHangFragment();

            trangChuKhachHangFragment.setArguments(b);
            trangChuKhachHangFragment.setKhachHangActivity(this);

            manager.beginTransaction()
                    .replace(R.id.rl_khachhang_nav_for_fragment,trangChuKhachHangFragment,TRANGCHU_KHACHHANG_FRAGMEN_TAG)
                    .commit();
        } else if (id == R.id.nav_khachhang_thongtin) {
            ThongTinKhachHangFragment thongTinKhachHangFragment = new ThongTinKhachHangFragment();
            thongTinKhachHangFragment.setArguments(b);
            manager.beginTransaction()
                    .replace(R.id.rl_khachhang_nav_for_fragment,thongTinKhachHangFragment,THONGTIN_KHACHHANG_FRAGMEN_TAG)
                    .commit();

        } else if (id == R.id.nav_khachhang_thoat) {
            finish();
        }else if (id == R.id.nav_khachhang_donhang){
            KhachHang_DonHangFragment khachHang_donHangFragment = new KhachHang_DonHangFragment();
            khachHang_donHangFragment.setArguments(b);
            khachHang_donHangFragment.setActivity(this);
            manager.beginTransaction()
                    .replace(R.id.rl_khachhang_nav_for_fragment,khachHang_donHangFragment,DONHANG_KHACHHANG_FRAGMEN_TAG)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_khachhang);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void uncheckAllMenuItems(NavigationView navigationView) {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

    private void showDialog() {
        rlLoading.start();
        dialog.show();
    }

    private void dismissDialog() {
        rlLoading.stop();
        dialog.dismiss();
    }

}
