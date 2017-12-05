package leu.doan_datdoan.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.events.khachhang.KhachHang_OnPlacePickerEvent;
import leu.doan_datdoan.fragment.khachhang.KhachHang_XacNhanDatHangFragment;
import leu.doan_datdoan.fragment.khachhang.KhachHang_XemChiTietCuaHangFragment;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.utils.ScreenManager;


public class XemCuaHangActivity extends AppCompatActivity{

    public static final String KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_CUAHANGKEY = "CUAHANG";
    public static final String KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_KHACHHANGKEY = "KHACHHANG";
    public static final int PLACE_PICKER_REQUEST = 8732;

    @BindView(R.id.toolb_khachhang_xemchitietcuahang)
    Toolbar toolbar;

    private AVLoadingIndicatorView loadingRotate;
    private Dialog dialog;

    private Intent intent;
    private CuaHang cuaHang;
    private KhachHang khachHang;

    private KhachHang_XacNhanDatHangFragment khachHang_xacNhanDatHangFragment;
    private KhachHang_XemChiTietCuaHangFragment khachHang_xemChiTietCuaHangFragment;
    private boolean dangXacNhan = false;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        View v = getLayoutInflater().inflate(R.layout.dialog_chuyen,null,false);
        dialog = new Dialog(XemCuaHangActivity.this);
        dialog.setContentView(R.layout.dialog_chuyen);
        loadingRotate = (AVLoadingIndicatorView) dialog.findViewById(R.id.rotatechuyen);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        showDialog();

        khachHang_xemChiTietCuaHangFragment =  new KhachHang_XemChiTietCuaHangFragment();

        intent = getIntent();
        cuaHang = (CuaHang) intent.getSerializableExtra(KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_CUAHANGKEY);
        khachHang = (KhachHang)intent.getSerializableExtra(KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_KHACHHANGKEY);
        setupUI();


        Bundle bundle = new Bundle();
        bundle.putSerializable(KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_CUAHANGKEY,cuaHang);
        bundle.putSerializable(KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_KHACHHANGKEY,khachHang);
        khachHang_xemChiTietCuaHangFragment.setArguments(bundle);
        ScreenManager.openFragment(getSupportFragmentManager(),khachHang_xemChiTietCuaHangFragment,R.id.rl_khachhang_dathangactivity,false,false);

    }

    private void setupUI() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cuaHang.getTen());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        }else{
            finish();
        }
    }

    public void toXacNhanDatHangFragment(){
        khachHang_xacNhanDatHangFragment = new KhachHang_XacNhanDatHangFragment();
        khachHang_xacNhanDatHangFragment.setActivity(this);
        ScreenManager.addFragment(getSupportFragmentManager(),khachHang_xacNhanDatHangFragment,R.id.rl_khachhang_dathangactivity,true,false);
        ScreenManager.showFragment(getSupportFragmentManager(),khachHang_xacNhanDatHangFragment);
        dangXacNhan = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getApplicationContext());

                    khachHang_xacNhanDatHangFragment.onPlacePickerEvent(new KhachHang_OnPlacePickerEvent(place));


//
//                    if(place !=null){
//                        tvDiaChiNguoiDat.setText(place.getAddress());
//                        myLocation.setLatitude(place.getLatLng().latitude);
//                        myLocation.setLongitude(place.getLatLng().longitude);
//                        pinDatHangMarkers();
//                    }else{
//                        lnTimDiaDiemKhac.setClickable(true);
//                    }

                }
                break;
        }

                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }

                khachHang_xemChiTietCuaHangFragment.buildGoogleApiClient();


        }

    public void openPlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn cần bật GPS để có thể xác định vị trí giao hàng?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 123);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            }
        });
        alert.show();
    }

    public void showDialog(){
        loadingRotate.show();
        dialog.show();
    }

    public void dissmissDialog(){
        loadingRotate.hide();
        dialog.dismiss();
    }
}
