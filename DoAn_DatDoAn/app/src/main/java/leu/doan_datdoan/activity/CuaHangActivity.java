package leu.doan_datdoan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.victor.loading.rotate.RotateLoading;

import leu.doan_datdoan.R;
import leu.doan_datdoan.fragment.cuahang.CuaHang_ChiTietPheDuyetFragment;
import leu.doan_datdoan.fragment.cuahang.CuaHang_MatHangFragment;
import leu.doan_datdoan.fragment.cuahang.CuaHang_NhapHangFragment;
import leu.doan_datdoan.fragment.cuahang.CuaHang_ThongTinCuaHangFragment;
import leu.doan_datdoan.fragment.cuahang.CuaHang_TrangChuFragment;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.taikhoan.TaiKhoanService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuaHangActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TRANGCHU_CUAHANG_FRAGMEN_TAG = "trangchu_cuahang_fragment_tag";
    private static final String THONGTIN_CUAHANG_FRAGMEN_TAG = "thongtin_cuahang_fragment_tag";
    private static final String NHAPHANG_CUAHANG_FRAGMEN_TAG = "nhaphang_cuahang_fragment_tag";
    public static final String KEY_BUNDLE_DATAOCUAHANG = "dataocuahang";
    public static final String KEY_BUNDLE_IDTAIKHOAN = "idtaikhoan";
    public static final String KEY_BUNDLE_CUAHANG = "cuahang";
    RotateLoading rlLoading;
    Dialog dialog;

    NavigationView navigationView;

    private CuaHang cuaHang;
    private boolean daTaoCuaHang = true;
    private int idTaiKhoan = 2;

    private boolean dangLamMatHang = false;
    private boolean dangDuyetDonHang = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cua_hang);
        FirebaseApp.initializeApp(this);

        dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_loading,null,false);
        rlLoading = (RotateLoading) v.findViewById(R.id.rl_loading);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_cuahang);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        daTaoCuaHang = i.getBooleanExtra(KEY_BUNDLE_DATAOCUAHANG,false);
        idTaiKhoan = i.getIntExtra(KEY_BUNDLE_IDTAIKHOAN,0);

        if(daTaoCuaHang){
            RetrofitFactory.getInstance().createService(TaiKhoanService.class).layCuaHangBangIdTaiKhoan(idTaiKhoan).enqueue(new Callback<CuaHang>() {
                @Override
                public void onResponse(Call<CuaHang> call, Response<CuaHang> response) {
                    if(response.isSuccessful()){
                        cuaHang = response.body();
                        navigationView.setCheckedItem(R.id.nav_cuahang_trangchu);
                        onNavigationItemSelected(navigationView.getMenu().getItem(0));
                    }
                }

                @Override
                public void onFailure(Call<CuaHang> call, Throwable t) {

                }
            });
        }else{
            enableOtherItem(false);
            navigationView.setCheckedItem(R.id.nav_cuahang_thongtin);
            onNavigationItemSelected(navigationView.getMenu().getItem(1));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enableOtherItem(boolean flag) {
        final Menu menu = navigationView.getMenu();
        menu.getItem(0).setEnabled(flag);
        menu.getItem(2).setEnabled(flag);
        menu.getItem(3).setEnabled(flag);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(dangLamMatHang){
                dangLamMatHang = false;
                CuaHang_NhapHangFragment cuaHang_nhapHangFragment = new CuaHang_NhapHangFragment();
                FragmentManager manager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_BUNDLE_CUAHANG,cuaHang);
                cuaHang_nhapHangFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_cuahang_nav_for_fragment,cuaHang_nhapHangFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            if(dangDuyetDonHang){
                dangDuyetDonHang = false;
                CuaHang_TrangChuFragment cuaHangTrangChuFragment = new CuaHang_TrangChuFragment();
                FragmentManager manager = getSupportFragmentManager();
                cuaHangTrangChuFragment.setActivity(this);
                Bundle bundle = new Bundle();
                if(daTaoCuaHang)bundle.putSerializable(KEY_BUNDLE_CUAHANG,cuaHang);
                cuaHangTrangChuFragment.setArguments(bundle);

                manager.beginTransaction()
                        .replace(R.id.rl_cuahang_nav_for_fragment,cuaHangTrangChuFragment,TRANGCHU_CUAHANG_FRAGMEN_TAG)
                        .commit();
            }

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


        if (id == R.id.nav_cuahang_trangchu) {
            // Handle the camera action
            CuaHang_TrangChuFragment cuaHangTrangChuFragment = new CuaHang_TrangChuFragment();

            cuaHangTrangChuFragment.setActivity(this);
            Bundle bundle = new Bundle();
            if(daTaoCuaHang)bundle.putSerializable(KEY_BUNDLE_CUAHANG,cuaHang);
            cuaHangTrangChuFragment.setArguments(bundle);

            manager.beginTransaction()
                    .replace(R.id.rl_cuahang_nav_for_fragment,cuaHangTrangChuFragment)
                    .commit();
        } else if (id == R.id.nav_cuahang_thongtin) {
            CuaHang_ThongTinCuaHangFragment cuaHangThongTinCuaHangFragment = new CuaHang_ThongTinCuaHangFragment();

            cuaHangThongTinCuaHangFragment.setActivityAndContext(this,getApplicationContext());
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_BUNDLE_IDTAIKHOAN,idTaiKhoan);
            bundle.putBoolean(KEY_BUNDLE_DATAOCUAHANG,daTaoCuaHang);
            if(daTaoCuaHang)bundle.putSerializable(KEY_BUNDLE_CUAHANG,cuaHang);
            cuaHangThongTinCuaHangFragment.setArguments(bundle);

            manager.beginTransaction()
                    .replace(R.id.rl_cuahang_nav_for_fragment,cuaHangThongTinCuaHangFragment,THONGTIN_CUAHANG_FRAGMEN_TAG)
                    .commit();

        } else if (id == R.id.nav_cuahang_nhaphang) {
            CuaHang_NhapHangFragment cuaHang_nhapHangFragment = new CuaHang_NhapHangFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_BUNDLE_CUAHANG,cuaHang);
            cuaHang_nhapHangFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.rl_cuahang_nav_for_fragment,cuaHang_nhapHangFragment);
            fragmentTransaction.commit();
        }else if(id == R.id.nav_cuahang_thoat){
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    public void openMatHangFragment(){
        dangLamMatHang = true;
        FragmentManager manager = getSupportFragmentManager();
        CuaHang_MatHangFragment cuaHang_matHangFragment = new CuaHang_MatHangFragment();
        cuaHang_matHangFragment.setC(getApplicationContext());
        cuaHang_matHangFragment.setActivity(this);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_cuahang_nav_for_fragment,cuaHang_matHangFragment);
        fragmentTransaction.commit();
    }

    public void openChiTietDuyetDonFragment(){
        dangDuyetDonHang = true;
        FragmentManager manager = getSupportFragmentManager();
        CuaHang_ChiTietPheDuyetFragment cuaHang_chiTietPheDuyetFragment = new CuaHang_ChiTietPheDuyetFragment(this);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.rl_cuahang_nav_for_fragment,cuaHang_chiTietPheDuyetFragment);
        fragmentTransaction.commit();
    }

    public void setDaTaoCuaHang(boolean daTaoCuaHang){
        this.daTaoCuaHang = daTaoCuaHang;
        RetrofitFactory.getInstance().createService(TaiKhoanService.class).layCuaHangBangIdTaiKhoan(idTaiKhoan).enqueue(new Callback<CuaHang>() {
            @Override
            public void onResponse(Call<CuaHang> call, Response<CuaHang> response) {
                if(response.isSuccessful()){
                    cuaHang = response.body();
                    enableOtherItem(true);
                }
            }

            @Override
            public void onFailure(Call<CuaHang> call, Throwable t) {

            }
        });
    }

    public void setCuaHang(CuaHang ch){
        cuaHang = ch;
    }

    public void showingDialog() {
        rlLoading.start();
        dialog.show();
    }

    public void dismissingDialog() {
        rlLoading.stop();
        dialog.dismiss();
    }
}
