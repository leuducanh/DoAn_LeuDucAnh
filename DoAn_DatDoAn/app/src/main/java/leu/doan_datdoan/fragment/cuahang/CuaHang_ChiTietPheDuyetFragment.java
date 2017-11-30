package leu.doan_datdoan.fragment.cuahang;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.adapters.cuahang.CuaHang_RvChiTietPheDuyetAdapter;
import leu.doan_datdoan.events.cuahang.CuaHang_OnClickChiTietPheDuyetMatHang;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.Util;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.donhang.DonHangService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuaHang_ChiTietPheDuyetFragment extends Fragment implements OnMapReadyCallback {
    public static final String DONHANG_PHEDUYET = "Phê duyệt";
    public static final String DONHANG_CHOPHEDUYET = "Đang chờ phê duyệt";
    public static final String DONHANG_DATIMDCSHIP = "Đã tìm được ship";
    public static final String DONHANG_DANGSHIP = "Đang ship";
    public static final String DONHANG_DAXONG = "Đã xong";

    @BindView(R.id.ivtran_cuahang_chitietpheduyetfragment)
    ImageView ivTran;
    @BindView(R.id.tvtencuahang_cuahang_chitietdonhangfragment)
    TextView tvTenCuaHang;
    @BindView(R.id.tvtennguoidat_cuahang_chitietpheduyetfragment)
    TextView tvNguoiDat;
    @BindView(R.id.tvdiachinguoidat_cuahang_chitietpheduyetfragment)
    TextView tvDiaChi;
    @BindView(R.id.tvsdtnguoidat_cuahang_chitietpheduyetfragment)
    TextView tvSdt;
    @BindView(R.id.ivgoidien_cuahang_chitietpheduyetfragment)
    ImageView ivGoiDien;
    @BindView(R.id.tvghichu_cuahang_chitietpheduyetfragment)
    TextView tvGhiChu;
    @BindView(R.id.tvtienhang_cuahang_chitietpheduyetfragment)
    TextView tvTienHang;
    @BindView(R.id.tvtienvanchuyen_cuahang_chitietpheduyetfragment)
    TextView tvTienVanChuyen;
    @BindView(R.id.tvtongtien_cuahang_chitietpheduyetfragment)
    TextView tvTongTien;
    @BindView(R.id.btnpheduyet_cuahang_chitietpheduyetfragment)
    Button btnPheDuyet;
    @BindView(R.id.btnbatdaugiao_cuahang_chitietpheduyetfragment)
    Button btnBatDauGiaoHang;
    @BindView(R.id.btn_tim_cuahang_chitietpheduyetfragment)
    Button btnTimShipper;
    @BindView(R.id.btnhuy_cuahang_chitietpheduyetfragment)
    Button btnHuyShip;
    @BindView(R.id.btnhuydon_cuahang_chitietpheduyetfragment)
    Button btnHuyDon;
    @BindView(R.id.rvhangdat_chitietpheduyetfragment)
    RecyclerView recyclerView;
    @BindView(R.id.nsvmain_cuahang_chitietpheduyetfragment)
    NestedScrollView nsv;
    @BindView(R.id.tvbiensoxe_cuahang_chitietpheduyetfragment)
    TextView tvBienSo;
    @BindView(R.id.tvsdtvanchuyen_cuahang_chitietpheduyetfragment)
    TextView tvSdtVanChuyen;
    @BindView(R.id.ivgoidienvanchuyen_cuahang_chitietpheduyetfragment)
    ImageView ivGoiDienVanChuyen;
    @BindView(R.id.rl3_cuahang_chitietpheduyetfragment)
    RelativeLayout rl3;

    GoogleMap map;
    private SupportMapFragment supportMapFragment;

    private CuaHang cuaHang;
    private DonHang donHang;
    private Activity activity;

    private CuaHang_RvChiTietPheDuyetAdapter adapter;
    private Marker markerCuaHang;
    private Marker markerDatHang;

    public CuaHang_ChiTietPheDuyetFragment() {
        // Required empty public constructor
    }

    public CuaHang_ChiTietPheDuyetFragment(Activity activity) {
        this.activity = activity;
    }

    private static final int PERMISSIONS_REQUEST_PHONE = 123;

    @OnClick(R.id.ivgoidienvanchuyen_cuahang_chitietpheduyetfragment)
    public void onClickGoiDienVanChuyen(){
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + donHang.getNguoiVanChuyen().getDienThoai()));
            startActivity(intent);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_PHONE);
        }
    }


    @OnClick(R.id.ivgoidien_cuahang_chitietpheduyetfragment)
    public void onClickIvGoiDien() {

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvSdtVanChuyen.getText().toString()));
            startActivity(intent);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_PHONE);
        }

    }

    @OnClick(R.id.btnpheduyet_cuahang_chitietpheduyetfragment)
    public void onClickPheDuyetDonHang() {
        ((CuaHangActivity) activity).showingDialog();
        donHang.setTrangThai(DONHANG_PHEDUYET);
        RetrofitFactory.getInstance().createService(DonHangService.class).capNhapDonHang(donHang.getId(),donHang ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    btnPheDuyet.setVisibility(View.GONE);
                    btnHuyDon.setVisibility(View.GONE);
                    btnBatDauGiaoHang.setVisibility(View.GONE);
                    btnHuyShip.setVisibility(View.GONE);
                    btnTimShipper.setVisibility(View.VISIBLE);

                    ((CuaHangActivity) activity).dismissingDialog();
                } else {
                    ((CuaHangActivity) activity).dismissingDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ((CuaHangActivity) activity).dismissingDialog();
            }
        });
    }

    @OnClick(R.id.btnhuydon_cuahang_chitietpheduyetfragment)
    public void onClickHuyDonHang() {
        ((CuaHangActivity) activity).showingDialog();
        RetrofitFactory.getInstance().createService(DonHangService.class).huyDonHang(donHang.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ((CuaHangActivity) activity).dismissingDialog();
                    ((CuaHangActivity) activity).onBackPressed();
                } else {
                    ((CuaHangActivity) activity).dismissingDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ((CuaHangActivity) activity).dismissingDialog();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cua_hang_chi_tiet_phe_duyet, container, false);
        setupUI(v);


        return v;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);

        adapter = new CuaHang_RvChiTietPheDuyetAdapter(donHang.getHangDatList(), activity);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

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

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_cuahang_chitietpheduyetfragment);
        supportMapFragment.getMapAsync(this);
    }


    @Subscribe(sticky = true)
    public void onCickChiTietPheduyetMatHang(CuaHang_OnClickChiTietPheDuyetMatHang cuaHang_onClickChiTietPheDuyetMatHang) {
        cuaHang = cuaHang_onClickChiTietPheDuyetMatHang.getCuaHang();
        donHang = cuaHang_onClickChiTietPheDuyetMatHang.getDonHang();
        donHang.setCuaHang(cuaHang);
        tvNguoiDat.setText(donHang.getTenNguoiDat());
        tvDiaChi.setText(donHang.getDiaChi());
        tvGhiChu.setText(donHang.getGhiChu());
        tvSdt.setText(donHang.getSdt());
        tvTenCuaHang.setText(cuaHang.getTen());

        tvTienHang.setText(Util.formatConcurrency(donHang.getGiaHang()) + " đ");
        tvTienVanChuyen.setText(Util.formatConcurrency(donHang.getGiaVanChuyen()) + " đ");
        tvTongTien.setText(Util.formatConcurrency(donHang.getGiaHang() + donHang.getGiaVanChuyen()) + " đ");

        if (donHang.getTrangThai().equals(DONHANG_CHOPHEDUYET)) {
            setViewChoPheDuyet();
        } else if (donHang.getTrangThai().equals(DONHANG_PHEDUYET)) {
            setViewChoPheDuyet();
        } else if (donHang.getTrangThai().equals(DONHANG_DATIMDCSHIP)){
            setViewTimDuocShiper();
        } else if (donHang.getTrangThai().equals(DONHANG_DANGSHIP)){
            setViewShipLayHang();
        }

        EventBus.getDefault().removeStickyEvent(cuaHang_onClickChiTietPheDuyetMatHang);
    }

    public void setGoneTatCaCacView(){
        btnBatDauGiaoHang.setVisibility(View.GONE);
        btnHuyShip.setVisibility(View.GONE);
        btnTimShipper.setVisibility(View.GONE);
        btnPheDuyet.setVisibility(View.GONE);
        btnHuyDon.setVisibility(View.GONE);
        rl3.setVisibility(View.GONE);
    }

    public void setViewChoPheDuyet(){
      setGoneTatCaCacView();
        btnPheDuyet.setVisibility(View.VISIBLE);
        btnHuyDon.setVisibility(View.VISIBLE);
    }
    public void setViewPheDuyet(){
        setGoneTatCaCacView();
        btnTimShipper.setVisibility(View.VISIBLE);
    }
    public void setViewTimDuocShiper(){
        setGoneTatCaCacView();
        btnBatDauGiaoHang.setVisibility(View.VISIBLE);
        btnHuyShip.setVisibility(View.VISIBLE);
    }
    public void setViewShipLayHang(){
        setGoneTatCaCacView();
    }

    private void updateCamera() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(markerCuaHang.getPosition());
        builder.include(markerDatHang.getPosition());
        LatLngBounds bounds = builder.build();

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

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        ((CuaHangActivity) activity).dismissingDialog();
        pinMarkers();
        updateCamera();
    }

    private void pinMarkers() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(donHang.getLat(), donHang.getLng()));

        // This is optional, only if you want a custom image for your pin icon
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        markerDatHang = map.addMarker(markerOptions);
        markerOptions.position(new LatLng(cuaHang.getLat(), cuaHang.getLng()));

        // This is optional, only if you want a custom image for your pin icon
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerCuaHang = map.addMarker(markerOptions);
    }

//    public void setEnableThongTinShip(){
//
//    }
}
