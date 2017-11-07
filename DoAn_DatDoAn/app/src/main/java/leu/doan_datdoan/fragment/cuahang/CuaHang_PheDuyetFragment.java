package leu.doan_datdoan.fragment.cuahang;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.adapters.cuahang.CuaHang_RvPheDuyetAdapter;
import leu.doan_datdoan.events.CuaHang_OnClickChiTietPheDuyetMatHang;
import leu.doan_datdoan.fragment.FragmentLifecycle;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.DonHang;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuaHang_PheDuyetFragment extends Fragment implements FragmentLifecycle,CuaHang_RvPheDuyetAdapter.OnItemRvPheDuyetClick{

    @BindView(R.id.rvpheduyet_cuahang_pheduyetfragment)
    RecyclerView recyclerView;
    @BindView(R.id.tvsoluong_cuahang_pheduyetfragment)
    TextView tv;

    private CuaHang_RvPheDuyetAdapter adapter;
    private List<DonHang> donHangs;
    private Context context;

    private CuaHang cuaHang;
    public void setContext(Context context) {
        this.context = context;
    }

    public void setDonHangs(List<DonHang> donHangs) {
        this.donHangs = donHangs;
    }


    public CuaHang_PheDuyetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cua_hang_phe_duyet, container, false);

        cuaHang = (CuaHang) getArguments().getSerializable(CuaHangActivity.KEY_BUNDLE_CUAHANG);
        Log.d("abcd","abc " + cuaHang.getTen());
        setupUI(v);
        return v;
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);

        adapter = new CuaHang_RvPheDuyetAdapter(donHangs,context);
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        int sl = 0;
        for(int i = 0;i < donHangs.size();i++){
            if(donHangs.get(i).getTrangThai().equals("Đang chờ phê duyệt")){
                sl++;
            }
        }
        tv.setText(sl + " đơn hàng chờ phê duyệt.");
    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void hienThongTinPheDuyet(int viTri) {
        ((CuaHangActivity)context).showingDialog();
        EventBus.getDefault().postSticky(new CuaHang_OnClickChiTietPheDuyetMatHang(donHangs.get(viTri),cuaHang));
        ((CuaHangActivity)context).openChiTietDuyetDonFragment();
    }
}
