package leu.doan_datdoan.fragment.khachhang;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.XemCuaHangActivity;
import leu.doan_datdoan.adapters.khachhang.KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter;
import leu.doan_datdoan.adapters.khachhang.KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter;
import leu.doan_datdoan.events.khachhang.OnClickBtnDatHang;
import leu.doan_datdoan.events.khachhang.OnGpsTracker;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.HangDat;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.model.LoaiHang;
import leu.doan_datdoan.model.MatHang;
import leu.doan_datdoan.model.Util;
import leu.doan_datdoan.network.LoadPicture;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHang_XemChiTietCuaHangFragment extends Fragment implements KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter.OnItemClickRVLoaiHangAdapter,
        KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter.OnItemClickRVMatHangAdaper,
        KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter.MoveToRVMatHangAdapter,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int HANGDADAT_KEY = 301;
    public static final int TONGGIA_KEY = 302;
    public static final int SOPHAN_KEY = 303;

    @BindView(R.id.stickysv_khachhang_xemchitietcuahangfragment)
    StickyScrollView stickyScrollView;
    @BindView(R.id.rvloaihang_khachhang_xemchitietcuahangfragment)
    RecyclerView rvLoaiHang;
    @BindView(R.id.rvmathang_khachhang_xemchitietcuahangfragment)
    RecyclerView rvMatHang;
    @BindView(R.id.tvtonggia_khachhang_xemchitietcuahangfragment)
    TextView tvTongGia;
    @BindView(R.id.btndathang_khachhang_xemchitietcuahangfragment)
    Button btnDatHang;
    @BindView(R.id.tvxoagiohang_khachhang_xemchitietcuahangfragment)
    TextView tvXoa;
    @BindView(R.id.tvdiachicuahang_khachhang_xemchitietcuahangfragment)
    TextView tvDiaChi;
    @BindView(R.id.tvtencuahang_khachhang_xemchitietcuahangfragment)
    TextView tvTenCuaHang;
    @BindView(R.id.tvkhoangcanhcuahang_khachhang_xemchitietcuahangfragment)
    TextView tvKhoangCach;
    @BindView(R.id.tvhoatdongcuahang_khachhang_xemchitietcuahangfragment)
    TextView tvHoatDong;
    @BindView(R.id.tvphigiaohang_khachhang_xemchitietcuahangfragment)
    TextView tvPhiGiaoHang;
    @BindView(R.id.rltinhkhoangcach_khachhang_xemchitietcuahangfragment)
    RelativeLayout rlHoatDongCuaHang;
    @BindView(R.id.ivcuahang_khachhang_xemchitietcuahangfragment)
    ImageView iv;


    TextView tvQuaGio;
    TextView tvTimDiaDiemKhac;

    KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter loaiHangAdapter;
    KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter matHangAdapter;

    List<HangDat> hangDaDat;
    ArrayList<LoaiHang> loaiHangs = new ArrayList<>();
    ArrayList<MatHang> matHangs = new ArrayList<>();
    List<Object> dsRVMatHang;

    HashMap<Integer, View> mapScrollToView;
    boolean flag = false;

    private int idCuaHang;
    private int soPhan;
    private int tongGia;
    private int phiGiaoHang;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private CuaHang cuaHang;
    private KhachHang khachHang;
    private Dialog dialog;

    public KhachHang_XemChiTietCuaHangFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btndathang_khachhang_xemchitietcuahangfragment)
    public void onClickDatHang() {
        hangDaDat = new ArrayList<>();
        for (int i = 0; i < dsRVMatHang.size(); i++) {
            if (dsRVMatHang.get(i) instanceof HangDat && ((HangDat) dsRVMatHang.get(i)).getSoluong() > 0) {
                hangDaDat.add((HangDat) dsRVMatHang.get(i));
            }
        }

        ((XemCuaHangActivity)getActivity()).showDialog();
        EventBus.getDefault().postSticky(new OnClickBtnDatHang(new DonHang(khachHang,cuaHang,phiGiaoHang,tongGia,hangDaDat)));
        ((XemCuaHangActivity) getActivity()).toXacNhanDatHangFragment();
    }

    @OnClick(R.id.tvxoagiohang_khachhang_xemchitietcuahangfragment)
    public void onClickTvXoa(){
        for(int i = 0;i < dsRVMatHang.size();i++){
            if(dsRVMatHang.get(i) instanceof HangDat){
                ((HangDat)dsRVMatHang.get(i)).setSoluong(0);
            }
        }
        matHangAdapter.notifyDataSetChanged();
        tvXoa.setVisibility(GONE);
        btnDatHang.setVisibility(GONE);

        soPhan = 0;
        tongGia = 0;

        tvTongGia.setText(soPhan + " phần - " + Util.formatConcurrency(tongGia));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_khach_hang_xem_chi_tiet_cua_hang, container, false);

        Bundle b = getArguments();
        cuaHang = (CuaHang) b.getSerializable(XemCuaHangActivity.KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_CUAHANGKEY);
        khachHang = (KhachHang) b.getSerializable(XemCuaHangActivity.KHACHHANG_XEMCHITIETCUAHANG_FRAGMENT_KHACHHANGKEY);

        final Context context = this.getContext();
        phiGiaoHang = 5000;
        soPhan = 0;
        tongGia = 0;
        mapScrollToView = new HashMap<>();

        RetrofitFactory.getInstance().createService(CuaHangService.class).layToanBoLoaiHangTuCuaHang(cuaHang.getId()).enqueue(new Callback<List<LoaiHang>>() {
            @Override
            public void onResponse(Call<List<LoaiHang>> call, Response<List<LoaiHang>> response) {

                if(response.isSuccessful()){
                    loaiHangs = (ArrayList<LoaiHang>) response.body();
                    chuyenDoiSangMatHangList();
                    setupUI(v);
                    LoadPicture.load(getActivity(),cuaHang.getUrl(),iv);
                    setupLocation();
                }

                ((XemCuaHangActivity)getActivity()).dissmissDialog();
            }

            @Override
            public void onFailure(Call<List<LoaiHang>> call, Throwable t) {
                ((XemCuaHangActivity)getActivity()).dissmissDialog();
            }
        });

        return v;
    }

    private void setupLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
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

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        final Context context = this.getContext();
        final Activity activity = this.getActivity();
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this.getContext())
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this.getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {
                    Toast.makeText(this.getActivity(), "Xin hãy cập quyền!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);


        dialog = new Dialog(this.getContext());

        View dialogView = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_khachhang_xemchitietcuahang_thoigian, null, false);

        tvQuaGio = (TextView) dialogView.findViewById(R.id.tvquagio_khachhang_xemchitietcuahangfragment);
        tvTimDiaDiemKhac = (TextView) dialogView.findViewById(R.id.tvtimdiadiemkhac_xemchitietcuahangfragment);
        tvTimDiaDiemKhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        kiemTraThoiGian();


        tvTenCuaHang.setText(cuaHang.getTen());
        tvPhiGiaoHang.setText(phiGiaoHang + " đ/Km");
        tvDiaChi.setText(cuaHang.getDiaDiem());

        tvTongGia.setText(soPhan + " phần - " + Util.formatConcurrency(tongGia));

        loaiHangAdapter = new KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter(loaiHangs, this.getActivity());
        loaiHangAdapter.setOnItemClickRVLoaiHangAdapter(this);
        matHangAdapter = new KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter(dsRVMatHang, this.getActivity());
        matHangAdapter.setonItemClickRVMatHangAdaper(this);
        matHangAdapter.setMoveToRVMatHangAdapter(this);
        LinearLayoutManager linearLayoutManagerver = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);

                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;

                if (matHangAdapter.getItemCount() > itemsShown) {
                    flag = true;
                }
            }
        };
        linearLayoutManagerver.setAutoMeasureEnabled(true);
        LinearLayoutManager linearLayoutManagerhori = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvLoaiHang.setLayoutManager(linearLayoutManagerhori);
        rvMatHang.setLayoutManager(linearLayoutManagerver);

        rvMatHang.setItemAnimator(null);
        rvLoaiHang.setItemAnimator(null);
        rvLoaiHang.setAdapter(loaiHangAdapter);
        rvMatHang.setAdapter(matHangAdapter);
        rvMatHang.setNestedScrollingEnabled(false);
    }

    private void kiemTraThoiGian() {
        String[] moCua = cuaHang.getThoiGianMoCua().split(" ");
        String[] dongCua = cuaHang.getThoiGianDongCua().split(" ");

        boolean flag = false;

        String yeuCau = "Nếu đặt đơn hàng có thể được xử lí nhanh nhất vào ";

        Date currentTime = Calendar.getInstance().getTime();
        int gio = currentTime.getHours();
        int phut = currentTime.getMinutes();

        int gioMoCua = Integer.parseInt(moCua[0]);
        int phutMoCua = Integer.parseInt(moCua[1]);

        if (gio < gioMoCua || (gio == gioMoCua && phut < phutMoCua)) {
            yeuCau += gioMoCua + " giờ " + phutMoCua + " phút hôm nay.";
            flag = true;
        } else {
            int gioDongCua = Integer.parseInt(dongCua[0]);
            int phutDongCua = Integer.parseInt(dongCua[1]);

            if (gio > gioDongCua || (gio == gioDongCua && phut > phutDongCua)) {
                yeuCau += gioMoCua + " giờ " + phutMoCua + " phút ngày mai";
                flag = true;
            }
        }

        if (flag) {
            tvHoatDong.setTextColor(Color.RED);
            tvHoatDong.setText("Đóng cửa");
            tvQuaGio.setText(yeuCau);
            dialog.show();
        }

    }

    private void finish() {
        finish();
    }



    public void chuyenDoiSangMatHangList() {
        dsRVMatHang = new ArrayList<>();
        for (int i = 0; i < loaiHangs.size(); i++) {
            dsRVMatHang.add(loaiHangs.get(i).getTen());
            for (int j = 0; j < loaiHangs.get(i).getMatHangs().size(); j++) {
                dsRVMatHang.add(new HangDat(loaiHangs.get(i).getMatHangs().get(j), 0));
            }
        }


        if(matHangAdapter != null){
            matHangAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemLoaiHangClick(final int position) {
        loaiHangAdapter.setChoosenPosition(position);

        if (flag) {
            stickyScrollView.post(new Runnable() {
                public void run() {
                    stickyScrollView.smoothScrollTo(0, mapScrollToView.get(position).getTop() + rlHoatDongCuaHang.getHeight());
                }
            });
        }
    }


    @Override
    public void onButtonClick(View v, int position) {
        HangDat hangDat = (HangDat) dsRVMatHang.get(position);

        switch (v.getId()) {
            case R.id.ivcong: {
                ((HangDat) dsRVMatHang.get(position)).setSoluong(hangDat.getSoluong() + 1);
                matHangAdapter.notifyItemChanged(position);
                soPhan += 1;
                if(soPhan == 1){
                    tvXoa.setVisibility(View.VISIBLE);
                    btnDatHang.setVisibility(View.VISIBLE);
                }
                tongGia += hangDat.getMatHang().getGia();
                tvTongGia.setText(soPhan + " phần - " + Util.formatConcurrency(tongGia));
                break;
            }
            case R.id.ivtru: {
                ((HangDat) dsRVMatHang.get(position)).setSoluong(hangDat.getSoluong() - 1);
                matHangAdapter.notifyItemChanged(position);
                soPhan -= 1;
                if(soPhan == 0){
                    tvXoa.setVisibility(GONE);
                    btnDatHang.setVisibility(GONE);
                }
                tongGia -= hangDat.getMatHang().getGia();
                tvTongGia.setText(soPhan + " phần - " + Util.formatConcurrency(tongGia));
                break;
            }
        }

    }

    @Override
    public void onItemMatHangClick(int position) {

    }

    @Override
    public void setView(View v, int position) {
        mapScrollToView.put(position, v);
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
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
        if (ContextCompat.checkSelfPermission(this.getContext(),
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

        tvKhoangCach.setText((double) Math.round(distFrom(location.getLatitude(), location.getLongitude(), cuaHang.getLat(), cuaHang.getLng()) / 100) / 10 + "km");

        EventBus.getDefault().postSticky(new OnGpsTracker(location));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}
