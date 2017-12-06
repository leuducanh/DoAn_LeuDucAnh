package leu.doan_datdoan.fragment.khachhang;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.KhachHangActivity;
import leu.doan_datdoan.adapters.khachhang.KhachHang_DonHangAdapter;
import leu.doan_datdoan.model.DonHang;
import leu.doan_datdoan.model.KhachHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.donhang.DonHangService;
import leu.doan_datdoan.network.khachhang.KhachHangService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHang_DonHangFragment extends Fragment implements KhachHang_DonHangAdapter.OnItemRvDonHangClick{

    public static final String KHACHHANG_DONHANGBROADCASH = "KHACHHANG_DONHANGBROADCASH";
    @BindView(R.id.rvdonhang_donhangfragment)
    RecyclerView rv;

    private KhachHang_DonHangAdapter adapter;
    private List<DonHang> lst;

    private KhachHang kh;
    private BroadcastReceiver broadcastReceiver;
    private Activity activity;
    private List<Integer>lstId;

    public KhachHang_DonHangFragment() {
        // Required empty public constructor
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_khach_hang_don_hang, container, false);
        lst = new ArrayList<>();
        lstId = new ArrayList<>();
        kh = (KhachHang) getArguments().getSerializable(KhachHangActivity.KEY_BUNDLE_KHACHHANG);
        ButterKnife.bind(this,v);
        adapter = new KhachHang_DonHangAdapter(lst,getActivity());
        adapter.setOnClickListener(this);
        LinearLayoutManager linearLayoutManagerver = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManagerver);
        rv.setAdapter(adapter);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Gson g = new Gson();
                String json  = intent.getStringExtra("donhang");
                DonHang dh = g.fromJson(json,DonHang.class);
                if(lstId.contains(dh.getId())){
                    int i = lstId.indexOf(dh.getId());
                    lst.set(i,dh);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        activity.registerReceiver(this.broadcastReceiver, new IntentFilter(KHACHHANG_DONHANGBROADCASH));

        RetrofitFactory.getInstance().createService(KhachHangService.class).layDonHangTuKhachHang(kh.getId()).enqueue(new Callback<List<DonHang>>() {
            @Override
            public void onResponse(Call<List<DonHang>> call, Response<List<DonHang>> response) {
                if(response.isSuccessful()){
                    lst = response.body();
                    for(int i = 0;i < lst.size();i++){
                        lstId.add(lst.get(i).getId());
                    }
                    adapter.setDonHangs(lst);
                }
            }

            @Override
            public void onFailure(Call<List<DonHang>> call, Throwable t) {

            }
        });

        return v;
    }
    private static final int PERMISSIONS_REQUEST_PHONE = 537;
    @Override
    public void goiSDT(String sdt) {
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sdt));
            startActivity(intent);
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_PHONE);
        }
    }
}
