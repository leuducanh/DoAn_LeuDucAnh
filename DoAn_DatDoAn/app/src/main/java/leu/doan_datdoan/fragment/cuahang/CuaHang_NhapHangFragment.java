package leu.doan_datdoan.fragment.cuahang;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.victor.loading.rotate.RotateLoading;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.adapters.cuahang.CuaHang_RvNhapHangAdapter;
import leu.doan_datdoan.anim.MyBounceInterpolator;
import leu.doan_datdoan.events.CuaHang_OnClickFabMatHang;
import leu.doan_datdoan.events.CuaHang_OnClickMatHang;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.LoaiHang;
import leu.doan_datdoan.model.MatHang;
import leu.doan_datdoan.network.LoadPicture;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuaHang_NhapHangFragment extends Fragment implements CuaHang_RvNhapHangAdapter.OnItemClickRVNhapHangAdaper{

    @BindView(R.id.rvnhaphang_cuahang_cuahangnhaphangfragment)
    RecyclerView rv;
    @BindView(R.id.fabnhaphang_cuahang_cuahangnhaphangfragment)
    FloatingActionButton fab;

    private List<Object> rvList = new ArrayList<>();

    private CuaHang cuaHang;
    private CuaHang_RvNhapHangAdapter adapter;
    private Dialog mathangDialog;

     EditText edTen;
    EditText edMota;
     EditText edGia;
    ImageView ivAnh;
     RelativeLayout rlCamera;
    ImageView ivAnhCuaHang;

    List<LoaiHang>loaiHangs;

    @OnClick(R.id.fabnhaphang_cuahang_cuahangnhaphangfragment)
    public void onClickFabNhapHang(){
        EventBus.getDefault().postSticky(new CuaHang_OnClickFabMatHang(cuaHang.getUrl(),loaiHangs,cuaHang.getId()));
        fab.setClickable(false);
        ((CuaHangActivity)getActivity()).openMatHangFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cua_hang_nhap_hang, container, false);
        cuaHang = (CuaHang) getArguments().getSerializable(CuaHangActivity.KEY_BUNDLE_CUAHANG);

        Log.d("abcde",cuaHang + "");

        setupUI(v);
        return v;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this,v);
        fab.setClickable(false);

        RetrofitFactory.getInstance().createService(CuaHangService.class).layToanBoLoaiHangTuCuaHang(cuaHang.getId()).enqueue(new Callback<List<LoaiHang>>() {
            @Override
            public void onResponse(Call<List<LoaiHang>> call, Response<List<LoaiHang>> response) {

                if(response.isSuccessful()){
                    loaiHangs = response.body();
                    chuyenDoiSangMatHangList(loaiHangs);
                    fab.setClickable(true);
                }

            }

            @Override
            public void onFailure(Call<List<LoaiHang>> call, Throwable t) {

            }
        });

        adapter = new CuaHang_RvNhapHangAdapter(rvList,getContext());
        adapter.setonItemClickRVNhapHangAdaper(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(null);
    }

    public void chuyenDoiSangMatHangList(List<LoaiHang> loaiHangs) {
        rvList = new ArrayList<>();
        for (int i = 0; i < loaiHangs.size(); i++) {
            rvList.add(loaiHangs.get(i).getTen());
            for (int j = 0; j < loaiHangs.get(i).getMatHangs().size(); j++) {
                rvList.add(loaiHangs.get(i).getMatHangs().get(j));
            }
        }
        if(adapter != null){
            adapter.setRvList(rvList);
        }
    }



    @Override
    public void onItemClick(int position) {
        Object i = rvList.get(position);
        int pos = 0;
        if(i instanceof MatHang){
            LoaiHang lh = null;
            for(int j = 1;j <= position;j++){
                if(rvList.get(j) instanceof String){
                    pos++;
                }
            }


            EventBus.getDefault().postSticky(new CuaHang_OnClickMatHang((MatHang)i,cuaHang.getUrl(),loaiHangs.get(pos).getTen(),loaiHangs.get(pos).getId()));
            ((CuaHangActivity)getActivity()).openMatHangFragment();
        }else{

        }
    }
}
