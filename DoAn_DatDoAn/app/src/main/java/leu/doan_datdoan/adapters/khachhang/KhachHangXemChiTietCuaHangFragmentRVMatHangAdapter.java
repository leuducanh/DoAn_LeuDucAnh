package leu.doan_datdoan.adapters.khachhang;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.model.HangDat;
import leu.doan_datdoan.network.LoadPicture;

/**
 * Created by MyPC on 21/10/2017.
 */

public class KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> rvList;
    private Context context;
    private static final int TYPE_MATHANG = 1;
    private static final int TYPE_LOAIHANG = 2;

    private OnItemClickRVMatHangAdaper onItemClickRVMatHangAdaper;
    private MoveToRVMatHangAdapter moveToRVMatHangAdapter;
    private int count = 0;

    public static interface OnItemClickRVMatHangAdaper {
        void onButtonClick(View v, int position);

        void onItemMatHangClick(int position);
    }

    public static interface MoveToRVMatHangAdapter {
        void setView(View v, int position);
    }

    public KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter(List<Object> rvList, Context context) {
        this.context = context;
        this.rvList = rvList;
    }

    public void setonItemClickRVMatHangAdaper(KhachHangXemChiTietCuaHangFragmentRVMatHangAdapter.OnItemClickRVMatHangAdaper onItemClickRVMatHangAdaper) {
        this.onItemClickRVMatHangAdaper = onItemClickRVMatHangAdaper;
    }

    public void setMoveToRVMatHangAdapter(MoveToRVMatHangAdapter moveToRVMatHangAdapter) {
        this.moveToRVMatHangAdapter = moveToRVMatHangAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_LOAIHANG) {
            view = layoutInflater.inflate(R.layout.item_khachhang_chitietcuahangfragment_rvmathang_loaihang, parent, false);
            holder = new HolderLoaiHangRVMatHang(view);
        } else {
            view = layoutInflater.inflate(R.layout.item_khachhang_chitietcuahangfragment_rvmathang, parent, false);
            holder = new HolderMatHangRVMatHang(view);
        }
//        if (onClickListener != null) {
//            view.setOnClickListener(onClickListener);
//        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == TYPE_MATHANG) {
            ((HolderMatHangRVMatHang) holder).setData((HangDat) rvList.get(position));
        } else {
            ((HolderLoaiHangRVMatHang) holder).setData((String) rvList.get(position), count);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            HangDat hangDat = (HangDat) payloads.get(0);
            ((HolderMatHangRVMatHang) holder).setData(hangDat);
        }
    }

    @Override
    public int getItemCount() {
        return rvList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (rvList.get(position) instanceof String) {
            return TYPE_LOAIHANG;
        } else {
            return TYPE_MATHANG;
        }
    }

    public class HolderMatHangRVMatHang extends RecyclerView.ViewHolder {

        @BindView(R.id.ivmathang_khachhang_xemchitietcuahang_rvmathang)
        ImageView iv;
        @BindView(R.id.tvtenmathang_khachhang_xemchitietcuahang_rvmathang)
        TextView tvTenMatHang;
        @BindView(R.id.tvmotamathang_khachhang_xemchitietcuahang_rvmathang)
        TextView tvMoTaMatHang;
        @BindView(R.id.tvgia_khachhang_xemchitietcuahang_rvmathang)
        TextView tvGiaMatHang;
        @BindView(R.id.ivcong)
        ImageView ivCong;
        @BindView(R.id.ivtru)
        ImageView ivTru;
        @BindView(R.id.tvsoluong_khachhang_xemchitietcuahang_rvmathang)
        TextView tvSoLuong;

        View view;

        public HolderMatHangRVMatHang(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(HangDat data) {
            if (data != null) {
                if (data.getSoluong() > 0) {
                    tvSoLuong.setText(data.getSoluong() + "");
                    ivTru.setVisibility(View.VISIBLE);
                } else {
                    tvSoLuong.setText(0 + "");
                    ivTru.setVisibility(View.GONE);
                }
                tvTenMatHang.setText(data.getMatHang().getTen());
                tvGiaMatHang.setText(data.getMatHang().getGia() + "");

                Locale locale = new Locale("vi", "VN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                tvGiaMatHang.setText(fmt.format(data.getMatHang().getGia()));

                tvMoTaMatHang.setText(data.getMatHang().getMota());
                LoadPicture.load(context,data.getMatHang().getTenAnh(),iv);
                addEvent();
            }
        }

        private void addEvent() {
            ivCong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickRVMatHangAdaper.onButtonClick(v, getAdapterPosition());
                }
            });
            ivTru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickRVMatHangAdaper.onButtonClick(v, getAdapterPosition());
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickRVMatHangAdaper.onItemMatHangClick(getAdapterPosition());
                }
            });
        }
    }

    public class HolderLoaiHangRVMatHang extends RecyclerView.ViewHolder {
        @BindView(R.id.tvloaihang_khachhang_xemchitietcuahang_rvmathang)
        TextView tvLoaiHang;


        View v;

        public HolderLoaiHangRVMatHang(View itemView) {
            super(itemView);
            v = itemView;
            ButterKnife.bind(this, v);
        }

        public void setData(String ten, int position) {
            tvLoaiHang.setText(ten);
            if(moveToRVMatHangAdapter != null){
                moveToRVMatHangAdapter.setView(v, count);
            }
            count++;
        }
    }
}
