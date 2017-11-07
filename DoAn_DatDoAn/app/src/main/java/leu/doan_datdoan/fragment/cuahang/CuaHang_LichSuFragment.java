package leu.doan_datdoan.fragment.cuahang;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.adapters.cuahang.CuaHang_RvPheDuyetAdapter;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.model.DonHang;


public class CuaHang_LichSuFragment extends Fragment implements FragmentLifecycle {

    @BindView(R.id.rvlichsu_cuahang_lichsufragment)
    RecyclerView recyclerView;

    List<DonHang> donHangs;
    private Context context;
    private CuaHang_RvPheDuyetAdapter adapter;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDonHangs(List<DonHang> donHangs) {
        this.donHangs = donHangs;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cua_hang_lich_su, container, false);

        setupUI(v);
        return v;
    }
    private void setupUI(View v) {
        ButterKnife.bind(this, v);

        adapter = new CuaHang_RvPheDuyetAdapter(donHangs,context);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }
}
