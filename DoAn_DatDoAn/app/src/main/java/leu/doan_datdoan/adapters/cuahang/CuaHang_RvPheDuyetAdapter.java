package leu.doan_datdoan.adapters.cuahang;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.model.DonHang;

/**
 * Created by MyPC on 05/11/2017.
 */

public class CuaHang_RvPheDuyetAdapter extends RecyclerView.Adapter<CuaHang_RvPheDuyetAdapter.HolderDonHangRVPheDuyet> {

    private List<DonHang> donHangs;
    private Context context;
    private OnItemRvPheDuyetClick onClickListener;

    public static interface OnItemRvPheDuyetClick{
        public void hienThongTinPheDuyet(int viTri);
    }

    public CuaHang_RvPheDuyetAdapter(List<DonHang> donHangs, Context context) {
        this.donHangs = donHangs;
        this.context = context;
    }

    public void setOnClickListener(OnItemRvPheDuyetClick onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public HolderDonHangRVPheDuyet onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_cuahang_rvpheduyet, parent, false);
       HolderDonHangRVPheDuyet cuaHangViewHolder = new HolderDonHangRVPheDuyet(view);
        return cuaHangViewHolder;
    }

    @Override
    public void onBindViewHolder(HolderDonHangRVPheDuyet holder, int position) {
        holder.setData(donHangs.get(position));
    }

    @Override
    public int getItemCount() {
        return donHangs.size();
    }

    public class HolderDonHangRVPheDuyet extends RecyclerView.ViewHolder {

        @BindView(R.id.tvdiachi_cuahang_item_rvpheduyet)
        TextView tvDiaChiNguoiMua;
        @BindView(R.id.tvten_cuahang_item_rvpheduyet)
        TextView tvTenNguoiMua;
        @BindView(R.id.tvtrangthai_cuahang_itenrvpheduyet)
        TextView tvTrangThai;

        View view;

        public HolderDonHangRVPheDuyet(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(DonHang data) {
            if (data != null) {
                tvDiaChiNguoiMua.setText(data.getDiaChi());
                tvTenNguoiMua.setText(data.getTenNguoiDat());
                tvTrangThai.setText(data.getTrangThai());
                if(!data.getTrangThai().equals("Đang chờ phê duyệt"))tvTrangThai.setTextColor(Color.BLUE);
                addEvent();
            }
        }

        private void addEvent() {

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                onClickListener.hienThongTinPheDuyet(getAdapterPosition());
                }
            });
        }
    }
}
