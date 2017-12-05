package leu.doan_datdoan.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import leu.doan_datdoan.R;
import leu.doan_datdoan.events.khachhang.OnGpsTracker;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.NguoiVanChuyen;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.nguoivanchuyen.NguoiVanChuyenService;
import leu.doan_datdoan.network.taikhoan.TaiKhoanService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NguoiVanChuyenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION= 5556;
    private BroadcastReceiver broadcastReceiver;
    public static final String NVC_NVCBROADCAST = "nvc";
    public static final String KEY_BUNDLE_DATAONVC = "KEY_NVC";
    public static final String KEY_BUNDLE_IDTAIKHOAN = "KEY_IDTAIKHOAN_NVC";
    public static final int REQUEST_READ_PHONE_STATE = 306;
    @BindView(R.id.ivtran_nvcfragment)
    ImageView ivTran;
    @BindView(R.id.tvdccuahang_nvc)
    TextView tvDcCuaHang;
    @BindView(R.id.tvnguoidat_nvcfragment)
    TextView tvTen;
    @BindView(R.id.tvtencuahang_nvc)
    TextView tvTenCuaHang;
    @BindView(R.id.tvdiachinguoidat_nvc)
    TextView tvDiaChi;
    @BindView(R.id.btnnhandon_nvc)
    Button btnNhanDon;
    @BindView(R.id.btnhuydon_nvc)
    Button btnHuyDon;
    @BindView(R.id.btnnhanhang_nvc)
    Button btnNhanHang;
    @BindView(R.id.btngiaohang_nvc)
    Button btnGiaoHang;
    @BindView(R.id.nsvmain_nvcfragment)
    NestedScrollView nsv;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private EditText edBienSo;
    private EditText edSdt;
    private Button btnSave;
    private Dialog dialog;
    private boolean daTao = true;
    private int idTaiKhoan = 2;
    private NguoiVanChuyen nvc;
    private Location myLocation;
    private Location mLastLocation;
    private DonHang dh;

    private LocationManager manager;

    private Marker markerCuaHang;
    private Marker markerDatHang;
    private boolean firstTime = true;
    private BehaviorSubject<Location> subject;
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;


    private void setTrangThaiBtnCoDon(int state){
        btnHuyDon.setVisibility(state);
        btnNhanDon.setVisibility(state);
    }

    private void setTrangThaiBtnXuLiDon(int state){
        btnGiaoHang.setVisibility(state);
        btnNhanHang.setVisibility(state);
    }

    private void setThongTin(){
        tvTen.setText(dh.getTenNguoiDat());
        tvDiaChi.setText(dh.getDiaChi());
        tvTenCuaHang.setText(dh.getCuaHang().getTen());
        tvDcCuaHang.setText(dh.getCuaHang().getDiaDiem());

        pinDatHangMarkers();
        pinCuaHangMarkers();

        updateCamera(markerCuaHang.getPosition(),markerDatHang.getPosition());
    }

    private void clearThongTin(){
        tvTen.setText("Tên người đặt");
        tvDiaChi.setText("Địa chỉ người đặt");
        tvTenCuaHang.setText("Tên cửa hàng");
        tvDcCuaHang.setText("Địa chỉ cửa hàng");

        moveToCurrent();
    }

    public void pinCuaHangMarkers() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(dh.getCuaHang().getLat(), dh.getCuaHang().getLng())).title(dh.getCuaHang().getTen());

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerCuaHang = map.addMarker(markerOptions);

    }

    public void pinDatHangMarkers() {

        if (markerDatHang != null) {
            markerDatHang.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(dh.getLat(), dh.getLng()));

        // This is optional, only if you want a custom image for your pin icon
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        markerDatHang = map.addMarker(markerOptions);
    }

    private void updateCamera(LatLng a,LatLng b) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(a);
        builder.include(b);
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
        map.animateCamera(cu);
    }

    private void moveToCurrent(){
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        map.animateCamera(cameraUpdate);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_van_chuyen);
        Intent i = getIntent();
        daTao = i.getBooleanExtra(KEY_BUNDLE_DATAONVC, false);
        idTaiKhoan = i.getIntExtra(KEY_BUNDLE_IDTAIKHOAN, 0);
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nvcfragment);
        supportMapFragment.getMapAsync(this);
        setUpUI();
        setUpEvent();

        if (daTao) {
            RetrofitFactory.getInstance().createService(TaiKhoanService.class).layNVCBangIdTaiKhoan(idTaiKhoan).enqueue(new Callback<NguoiVanChuyen>() {
                @Override
                public void onResponse(Call<NguoiVanChuyen> call, Response<NguoiVanChuyen> response) {
                    if(response.isSuccessful()){
                        nvc = response.body();

                        setupLocation();
                    }
                }

                @Override
                public void onFailure(Call<NguoiVanChuyen> call, Throwable t) {

                }
            });

        } else {
            dialog.show();

            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            } else {
                TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                mPhoneNumber = mPhoneNumber.substring(1, mPhoneNumber.length());
                edSdt.setText(mPhoneNumber + "");
            }

        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Gson g = new Gson();
                String json  = intent.getStringExtra("donhang");
                DonHang dh = g.fromJson(json,DonHang.class);

            }
        };

        this.registerReceiver(this.broadcastReceiver, new IntentFilter(NVC_NVCBROADCAST));
    }

    private void setUpEvent() {

        subject = BehaviorSubject.create();

        ((Observable<Location>)subject).debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query->{

                });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitFactory.getInstance().createService(TaiKhoanService.class).taoNguoiVanChuyenChoTaiKhoan(idTaiKhoan,new NguoiVanChuyen(0,edBienSo.getText().toString(),edSdt.getText().toString(),0,0,1)).enqueue(new Callback<NguoiVanChuyen>() {
                    @Override
                    public void onResponse(Call<NguoiVanChuyen> call, Response<NguoiVanChuyen> response) {
                        if(response.isSuccessful()){
                            nvc = response.body();
                            setupLocation();
                        }

                        if(nvc != null){
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<NguoiVanChuyen> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void setupLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 98;

    private void checkLocationPermission() {
        final Context context = getApplicationContext();
        final Activity activity = this;
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Cần quyền truy cập vị trí!")
                        .setMessage("Phần mềm này cần quyền truy cập vị trí. Xin hãy cấp quyền.")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void setUpUI() {
        ButterKnife.bind(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_nvc, null, false);
        edBienSo = (EditText) v.findViewById(R.id.edbienso_dialognvc);
        edSdt = (EditText) v.findViewById(R.id.edsdt_dialognvc);
        btnSave = (Button) v.findViewById(R.id.btnSave_dialognvc);
        dialog = new Dialog(this);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ivTran.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        nsv.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        nsv.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        nsv.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[],
//                                           int[] grantResults) {
//        switch (requestCode) {
//
//
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    if (ContextCompat.checkSelfPermission(this,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                    }
//
//                } else {
//                    Toast.makeText(this, "Xin hãy cập quyền!", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//
//    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if(firstTime){
            moveToCurrent();
            setTrangThai(2);
        }else{
            setToaDo();
        }

        subject.onNext(mLastLocation);
    }

    public void setTrangThai(int trangThai){
        RetrofitFactory.getInstance().createService(NguoiVanChuyenService.class).capNhapTrangThai(nvc.getId(),trangThai).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    showMess("Lỗi ko cập nhập được trạng thái online");
                    finish();
                }
                setToaDo();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showMess("Lỗi ko cập nhập được trạng thái online");
                finish();
            }
        });
    }

    private void setToaDo() {
        RetrofitFactory.getInstance().createService(NguoiVanChuyenService.class).capNhapToaDo(nvc.getId(),mLastLocation.getLatitude(),mLastLocation.getLongitude()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                firstTime = false;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void showMess(String m){
        Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        final Activity activity = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getApplicationContext());
        alertDialogBuilder.setTitle("Quyền truy cập vị trí!");

        alertDialogBuilder
                .setMessage("Bạn chưa cấp quyền truy cập vị trí không thể giao hàng!!!")
                .setCancelable(false)
                .setPositiveButton("Cấp quyền", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(
                                activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                });

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            buildAlertMessageNoGps();
                        }
                        map.setMyLocationEnabled(true);
                    }

                } else {
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                        @Override
                        public void onShow(DialogInterface dialog) {
                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                        }
                    });

                    alertDialog.show();
                }
                return;
            }
            case REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_PHONE_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                        String mPhoneNumber = tMgr.getLine1Number();
                        edSdt.setText(mPhoneNumber + "");
                    }
                    return;
                }
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn cần bật GPS để có thể xác định vị trí?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 123);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            }
        });
        alert.show();
    }
}
