package leu.doan_datdoan.fragment.khachhang;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.XemCuaHangActivity;
import leu.doan_datdoan.adapters.khachhang.KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter;
import leu.doan_datdoan.events.khachhang.KhachHang_OnPlacePickerEvent;
import leu.doan_datdoan.events.khachhang.OnClickBtnDatHang;
import leu.doan_datdoan.events.khachhang.OnGpsTracker;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.HangDat;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.model.Util;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.khachhang.KhachHangService;
import leu.doan_datdoan.network.thongtingiaohangservice.Leg;
import leu.doan_datdoan.network.thongtingiaohangservice.ThongTin;
import leu.doan_datdoan.network.thongtingiaohangservice.ThongTinGiaoHangService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHang_XacNhanDatHangFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter.OnItemClickRVMatHangAdaper {

    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1218;
    private static final int REQUEST_READ_PHONE_STATE = 615;


    @BindView(R.id.nsvmain_khachhang_xacnhandathangfragment)
    NestedScrollView nsv;
    @BindView(R.id.ivtran_khachhang_xacnhandathangfragment)
    ImageView ivTran;
    @BindView(R.id.tvtentaikhoan_khachhang_xacnhandathangfragment)
    TextView tvTenTaiKhoan;
    @BindView(R.id.tvtencuahang_khachhang_xacnhandathang)
    TextView tvTenCuaHang;
    @BindView(R.id.tvdiachicuahang_khachhang_xacnhandathang)
    TextView tvDiaChiCuaHang;
    @BindView(R.id.tvdiachinguoidat_khachhang_xacnhandathangfragment)
    TextView tvDiaChiNguoiDat;
    @BindView(R.id.tvphivanchuyen_khachhang_xacnhandathang)
    TextView tvPhiVanChuyen;
    @BindView(R.id.tvkhoangcach_khachhang_xacnhandathang)
    TextView tvKhoangCach;
    @BindView(R.id.lntimdiadiem_khachhang_xacnhandathang)
    LinearLayout lnTimDiaDiemKhac;
    @BindView(R.id.edtennguoidat_khachhang_xacnhandathangfragment)
    EditText edTenNguoiDat;
    @BindView(R.id.edsdt_khachhang_xacnhandathangfragment)
    EditText edSDT;
    @BindView(R.id.ivsdtkhac_khachhang_xacnhandathangfragment)
    ImageView ivSDTKhac;
    @BindView(R.id.edghichu_khachhang_xacnhandathangfragment)
    EditText edGhiChu;
    @BindView(R.id.rvhangdat_xacnhandathangfragment)
    RecyclerView rvHangDat;
    @BindView(R.id.rldathang_khachhang_xacnhandathangfragment)
    RelativeLayout rlXacNhanDatHang;
    @BindView(R.id.tvtongtien_khachhang_xacnhandathang)
    TextView tvTongTien;
    @BindView(R.id.tvtienhang_khachhang_xacnhandathang)
    TextView tvTienHang;

    GoogleMap map;

    private SupportMapFragment supportMapFragment;
    private KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter adapter;

    private Location myLocation;
    private int tienVanChuyen = 0;
    private int tienShipper = 0;
    private int soPhan;
    private DonHang donHang;
    private List<HangDat> hangDats;
    private List<Object> hangDatsForRv;
    private CuaHang cuaHang;
    private KhachHang khachHang;
    private Marker markerCuaHang;
    private Marker markerDatHang;

    private String mPhoneNumber;
    private String tenNguoiGui;
    private int tongGia;
    private int giaHang;
    private LocationManager manager;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean flagStopTrack = false;
    private double khoangCach;
    private Activity activity;

    public KhachHang_XacNhanDatHangFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.rldathang_khachhang_xacnhandathangfragment)
    public void datHang() {
        rlXacNhanDatHang.setVisibility(View.GONE);

        ktraThongTin();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void ktraThongTin() {
        mPhoneNumber = edSDT.getText().toString();
        tenNguoiGui = edTenNguoiDat.getText().toString();
        boolean flag = true;
        if (mPhoneNumber.equals("")) {
            edSDT.setError("Thiếu");
            flag = false;
        }
        if (tenNguoiGui.equals("")) {
            edTenNguoiDat.setError("Thiếu");
            flag = false;
        }
//        if (tienShipper == 0) {
//            flag = false;
//            Toast.makeText(getActivity(), "Vui lòng bật GPS", Toast.LENGTH_SHORT).show();
//        }
        if (flag) {
            ((XemCuaHangActivity)activity).showDialog();
            donHang.setDiaChi(tvDiaChiNguoiDat.getText().toString());
            donHang.setGhiChu(edGhiChu.getText().toString());
            donHang.setGiaHang(giaHang);
            donHang.setGiaVanChuyen(0);
            donHang.setLat(myLocation.getLatitude());
            donHang.setLng(myLocation.getLongitude());
            donHang.setTenNguoiDat(edTenNguoiDat.getText().toString());
            donHang.setSdt(edSDT.getText().toString());
            donHang.setTrangThai("Đang chờ phê duyệt");

            Iterator<HangDat> i = hangDats.iterator();
            while(i.hasNext()){
                HangDat h = i.next();
                if(h.getSoluong() == 0){
                    i.remove();
                }
            }
            donHang.setHangDatList(hangDats);

                RetrofitFactory.getInstance().createService(KhachHangService.class).taoDonHang(khachHang.getId(), donHang).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            ((XemCuaHangActivity)getActivity()).dissmissDialog();
                            getActivity().finish();
                        }else{
                            ((XemCuaHangActivity)activity).dissmissDialog();
                            rlXacNhanDatHang.setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(),"Lỗi " + response.code(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        rlXacNhanDatHang.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"Lỗi" ,Toast.LENGTH_SHORT).show();
                    }
                });
        }else{
            rlXacNhanDatHang.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.lntimdiadiem_khachhang_xacnhandathang)
    public void onClickTimDiaDiemKhac() {
        lnTimDiaDiemKhac.setClickable(false);
        ((XemCuaHangActivity) getActivity()).openPlacePicker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_khach_hang__xac_nhan_dat_hang, container, false);
        flagStopTrack = false;

        manager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getActivity().getSharedPreferences("SHARE_ACCOUNT", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_khachhang_xacnhandathangfragment);
        supportMapFragment.getMapAsync(this);
        setupUI(v);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return v;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);
        nsv.requestDisallowInterceptTouchEvent(true);

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

        int permissionCheck = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();
            mPhoneNumber = mPhoneNumber.substring(1, mPhoneNumber.length());
            edSDT.setText(mPhoneNumber + "");
        }



        ((XemCuaHangActivity) getActivity()).dissmissDialog();

    }

    @Subscribe(sticky = true)
    public void onReceivedDonHang(OnClickBtnDatHang onClickBtnDatHang) {

        tongGia = onClickBtnDatHang.getDonHang().getGiaHang() + onClickBtnDatHang.getDonHang().getGiaVanChuyen();
        donHang = onClickBtnDatHang.getDonHang();
        cuaHang = onClickBtnDatHang.getDonHang().getCuaHang();
        khachHang = onClickBtnDatHang.getDonHang().getKhachHang();
        tienVanChuyen = onClickBtnDatHang.getDonHang().getGiaVanChuyen();
        hangDats = donHang.getHangDatList();

        for (int i = 0; i < hangDats.size(); i++) {
            soPhan += hangDats.get(i).getSoluong();
            giaHang += hangDats.get(i).getMatHang().getGia() * hangDats.get(i).getSoluong();
        }
        setTien();
        hangDatsForRv = new ArrayList<>();

        for (int i = 0; i < hangDats.size(); i++) {
            hangDatsForRv.add(hangDats.get(i));

        }
        setTien();

        tvDiaChiCuaHang.setText(cuaHang.getDiaDiem());
        tvTenCuaHang.setText(cuaHang.getTen());
        tvTenTaiKhoan.setText(khachHang.getTen());

        LinearLayoutManager linearLayoutManagerver = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);

        linearLayoutManagerver.setAutoMeasureEnabled(true);
        adapter = new KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter(hangDatsForRv, this.getActivity());

        adapter.notifyDataSetChanged();
        adapter.setonItemClickRVMatHangAdaper(this);
        rvHangDat.setAdapter(adapter);
        rvHangDat.setLayoutManager(linearLayoutManagerver);
        rvHangDat.setNestedScrollingEnabled(false);
        checkThonTinGiaoHang();
        EventBus.getDefault().removeStickyEvent(onClickBtnDatHang);
    }

    private void setTien() {
        if (giaHang != 0 && tienShipper != 0) {
            tvTienHang.setText("Tổng tiền hàng: " + Util.formatConcurrency(giaHang) + " đ");
//            tvTongTien.setText("Tổng tiền phải trả: " + Util.formatConcurrency(tongGia) + " đ");
//            tvPhiVanChuyen.setText(" Phí vận chuyển: " + tienShipper + " đ");
        }
    }

    @Subscribe(sticky = true)
    public void onReceiveTracker(OnGpsTracker onGpsTracker) {

        if (!flagStopTrack && map != null) {
            myLocation = onGpsTracker.getLocation();

            checkThonTinGiaoHang();

            if (map != null) {
                pinDatHangMarkers();
            }

            updateCamera();
            flagStopTrack = true;
        }

    }

    private void updateCamera() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(markerCuaHang.getPosition());
        builder.include(markerDatHang.getPosition());
        LatLngBounds bounds = builder.build();

//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng((cuaHang.getLat() + myLocation.getLatitude())/2 , (cuaHang.getLng() + myLocation.getLongitude())/2))      // Sets the center of the map
//                .zoom(10)                   // Sets the zoom
//                .bearing(angleBteweenCoordinate(cuaHang.getLat(),cuaHang.getLng(),myLocation.getLatitude(),myLocation.getLongitude())).build();                // -90 = west, 90 = east
//
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 80);
        map.animateCamera(cu);
    }

    private float angleBteweenCoordinate(double lat1, double long1, double lat2,
                                         double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng;

        return (float) brng;
    }

    private void checkThonTinGiaoHang() {
        if (cuaHang != null && myLocation != null) {

            final Context c = this.getContext();
            tvDiaChiNguoiDat.setText(getDiaChiNguoiDat(myLocation.getLatitude(), myLocation.getLongitude()));

            RetrofitFactory.getInstance().createService(ThongTinGiaoHangService.class).getThongTinGiaoHang(
                    cuaHang.getLat() + "," + cuaHang.getLng(),
                    myLocation.getLatitude() + "," + myLocation.getLongitude(),
                    "driving",
                    getString(R.string.directionAPI)).enqueue(new Callback<ThongTin>() {
                @Override
                public void onResponse(Call<ThongTin> call, Response<ThongTin> response) {

                    if (response.isSuccessful()) {
                        ThongTin t = response.body();
                        List<Leg> leg = t.getRoutes().get(0).getLegs();
                        khoangCach = (double) leg.get(0).getDistance().getValue() / 1000;
                        tvKhoangCach.setText(khoangCach + " Km ~ dự tính: " + Math.round((double) leg.get(0).getDuration().getValue() / 60) + " phút");
                        tienShipper = (int) Math.round(khoangCach * tienVanChuyen);
                        setTien();
                    }
                }

                @Override
                public void onFailure(Call<ThongTin> call, Throwable t) {

                }
            });
        }
    }

    private String getDiaChiNguoiDat(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("a123a","map");

        map = googleMap;

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
            map.setMyLocationEnabled(true);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (myLocation != null) {
            pinDatHangMarkers();
        }

        pinCuaHangMarkers();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        final Activity activity = this.getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this.getContext());
        alertDialogBuilder.setTitle("Quyền truy cập vị trí!");

        alertDialogBuilder
                .setMessage("Bạn chưa cấp quyền truy cập vị trí không thể giao hàng!!!")
                .setCancelable(false)
                .setPositiveButton("Cấp quyền", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                });

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this.getActivity(),
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

                    if (ContextCompat.checkSelfPermission(this.getActivity(),
                            Manifest.permission.READ_PHONE_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                        String mPhoneNumber = tMgr.getLine1Number();
                        edSDT.setText(mPhoneNumber + "");
                    }
                    return;
                }
            }

        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage("Bạn cần bật GPS để có thể xác định vị trí giao hàng?")
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

    public void onPlacePickerEvent(KhachHang_OnPlacePickerEvent khachHang_onPlacePickerEvent) {

        Place place = khachHang_onPlacePickerEvent.getPlace();

        if (place != null) {
            tvDiaChiNguoiDat.setText(place.getAddress());
            myLocation.setLatitude(place.getLatLng().latitude);
            myLocation.setLongitude(place.getLatLng().longitude);
            pinDatHangMarkers();
            checkThonTinGiaoHang();
            lnTimDiaDiemKhac.setClickable(true);
        }
    }

    @Override
    public void onButtonClick(View v, int position) {
        HangDat hangDat = (HangDat) hangDatsForRv.get(position);

        switch (v.getId()) {
            case R.id.ivcong: {
                ((HangDat) hangDatsForRv.get(position)).setSoluong(hangDat.getSoluong() + 1);
                adapter.notifyItemChanged(position);
                soPhan += 1;
                giaHang += hangDat.getMatHang().getGia();
                tvTienHang.setText("Tổng tiền hàng: " + Util.formatConcurrency(giaHang) + " đ");
                break;
            }
            case R.id.ivtru: {
                if (soPhan == 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                    builder.setMessage("Phải có ít nhất một mặt hàng mới có thể thanh toán !!!")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    ((HangDat) hangDatsForRv.get(position)).setSoluong(hangDat.getSoluong() - 1);
                    adapter.notifyItemChanged(position);
                    soPhan -= 1;
                    tongGia -= hangDat.getMatHang().getGia();
                    tvTienHang.setText(tongGia);
                }
                break;
            }
        }
    }

    @Override
    public void onItemMatHangClick(int position) {

    }

    public void pinCuaHangMarkers() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(cuaHang.getLat(), cuaHang.getLng())).title(cuaHang.getTen());

        // This is optional, only if you want a custom image for your pin icon
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerCuaHang = map.addMarker(markerOptions);

    }

    public void pinDatHangMarkers() {

        if (markerDatHang != null) {
            markerDatHang.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));

        // This is optional, only if you want a custom image for your pin icon
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        markerDatHang = map.addMarker(markerOptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
