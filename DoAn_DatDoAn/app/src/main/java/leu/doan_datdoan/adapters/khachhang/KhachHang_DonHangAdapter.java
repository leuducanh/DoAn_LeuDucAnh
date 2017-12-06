package leu.doan_datdoan.adapters.khachhang;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.adapters.cuahang.CuaHang_RvChiTietPheDuyetAdapter;
import leu.doan_datdoan.adapters.cuahang.CuaHang_RvPheDuyetAdapter;
import leu.doan_datdoan.fragment.cuahang.CuaHang_ChiTietPheDuyetFragment;
import leu.doan_datdoan.model.DonHang;

/**
 * Created by MyPC on 06/12/2017.
 */

public class KhachHang_DonHangAdapter extends RecyclerView.Adapter<KhachHang_DonHangAdapter.HolderDonHangRVDonHang> {

    private List<DonHang> donHangs;
    private Context context;
    private OnItemRvDonHangClick onClickListener;

    public static interface OnItemRvDonHangClick {
        public void goiSDT(String sdt);
    }

    public void setDonHangs(List<DonHang> donHangs) {
        this.donHangs = donHangs;
        notifyDataSetChanged();
    }

    public KhachHang_DonHangAdapter(List<DonHang> donHangs, Context context) {
        this.donHangs = donHangs;
        this.context = context;
    }

    public void setOnClickListener(OnItemRvDonHangClick onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public HolderDonHangRVDonHang onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_rvdonhang_donhangfragment, parent, false);
        HolderDonHangRVDonHang holderDonHangRVDonHang = new HolderDonHangRVDonHang(view);
        return holderDonHangRVDonHang;
    }

    @Override
    public void onBindViewHolder(HolderDonHangRVDonHang holder, int position) {
        holder.setData(donHangs.get(position));
    }

    @Override
    public int getItemCount() {
        return donHangs.size();
    }

    public class HolderDonHangRVDonHang extends RecyclerView.ViewHolder {

        @BindView(R.id.tvdiachi_khachhang_item_rvdonhang)
        TextView tvDiaChi;
        @BindView(R.id.tvten_khachhang_item_rvdonhang)
        TextView tvTen;
        @BindView(R.id.tvtienhang_khachhang_item_rvdonhang)
        TextView tvTienHang;
        @BindView(R.id.tvtienship_khachhang_item_rvdonhang)
        TextView tvTienShip;
        @BindView(R.id.tvtienphaitra_khachhang_item_rvdonhang)
        TextView tvTienPhaiTra;
        @BindView(R.id.btngoicuahang_khachhang_item_rvdonhang)
        Button btnGoiCuaHang;
        @BindView(R.id.btngoiship_khachhang_item_rvdonhang)
        Button btnGoiShip;
        @BindView(R.id.tvtrangthai_khachhang_itenrvdonhang)
                TextView tvTrangThai;
        @BindView(R.id.tvngaydat_khachhang_item_rvdonhang)
                TextView tvNgay;

        View view;

        public HolderDonHangRVDonHang(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(DonHang data) {
            if (data != null) {
               tvDiaChi.setText(data.getCuaHang().getDiaDiem());
                tvTen.setText(data.getCuaHang().getTen());
                tvNgay.setText(data.getNgay());
                tvTrangThai.setText(data.getTrangThai());

                Locale locale = new Locale("vi", "VN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                tvTienHang.setText(fmt.format(data.getGiaHang()));

                boolean flag = false;
                switch (data.getTrangThai()) {
                    case (CuaHang_ChiTietPheDuyetFragment.DONHANG_PHEDUYET): {
                        tvTrangThai.setTextColor(Color.BLUE);
                        break;
                    }
                    case (CuaHang_ChiTietPheDuyetFragment.DONHANG_GIAOHANG): {
                        flag = true;
                        tvTrangThai.setTextColor(Color.RED);
                        break;
                    }
                    case (CuaHang_ChiTietPheDuyetFragment.DONHANG_HOANTHANH): {
                        flag = true;
                        tvTrangThai.setTextColor(Color.BLACK);
                        break;
                    }
                }

                if(flag){
                    Log.d("abcde",data.getTrangThai() + " " + data.getId());
                    btnGoiShip.setVisibility(View.VISIBLE);
                    tvTienShip.setText(fmt.format(data.getGiaVanChuyen()));
                    tvTienPhaiTra.setText(fmt.format(data.getGiaHang()+data.getGiaVanChuyen()));
                }else{
                    btnGoiShip.setVisibility(View.GONE);
                    tvTienShip.setText("Chưa biết.");
                    tvTienPhaiTra.setText("Chưa biết.");
                }

                btnGoiShip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.goiSDT(data.getSdtShip());
                    }
                });

                btnGoiCuaHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.goiSDT(data.getCuaHang().getDienthoai());
                    }
                });
            }
        }

    }

}
