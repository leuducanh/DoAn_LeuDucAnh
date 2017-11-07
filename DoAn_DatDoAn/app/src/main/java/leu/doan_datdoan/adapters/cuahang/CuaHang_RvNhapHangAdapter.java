package leu.doan_datdoan.adapters.cuahang;

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
import leu.doan_datdoan.model.MatHang;
import leu.doan_datdoan.network.LoadPicture;

/**
 * Created by MyPC on 01/11/2017.
 */

public class CuaHang_RvNhapHangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> rvList;
    private Context context;
    private static final int TYPE_MATHANG = 1;
    private static final int TYPE_LOAIHANG = 2;

    private OnItemClickRVNhapHangAdaper onItemClickRVMatHangAdaper;

    public static interface OnItemClickRVNhapHangAdaper {
        void onItemClick(int position);
    }

    public CuaHang_RvNhapHangAdapter(List<Object> rvList, Context context) {
        this.context = context;
        this.rvList = rvList;
    }

    public void setonItemClickRVNhapHangAdaper(OnItemClickRVNhapHangAdaper onItemClickRVMatHangAdaper) {
        this.onItemClickRVMatHangAdaper = onItemClickRVMatHangAdaper;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_LOAIHANG) {
            view = layoutInflater.inflate(R.layout.cuahang_itemloaihang_rvnhaphang, parent, false);
            holder = new HolderLoaiHangRVNhapHang(view);
        } else {
            view = layoutInflater.inflate(R.layout.cuahang_itemmathang_rvnhaphang, parent, false);
            holder = new HolderMatHangRVNhapHang(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == TYPE_MATHANG) {
            ((HolderMatHangRVNhapHang) holder).setData((MatHang) rvList.get(position));
        } else {
            ((HolderLoaiHangRVNhapHang) holder).setData((String) rvList.get(position));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            MatHang matHang = (MatHang) payloads.get(0);
            ((HolderMatHangRVNhapHang) holder).setData(matHang);
        }
    }

    public void setRvList(List<Object> rvList) {
        this.rvList = rvList;
        notifyDataSetChanged();
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

    public class HolderMatHangRVNhapHang extends RecyclerView.ViewHolder {

        @BindView(R.id.ivmathang_cuahang_rvnhaphang)
        ImageView iv;
        @BindView(R.id.tvtenmathang_cuahang_rvnhaphang)
        TextView tvTenMatHang;
        @BindView(R.id.tvmotamathang_cuhang_rvnhaphang)
        TextView tvMoTaMatHang;
        @BindView(R.id.tvgia_cuahang_rvnhaphang)
        TextView tvGiaMatHang;

        View view;

        public HolderMatHangRVNhapHang(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(MatHang data) {
            if (data != null) {
                tvTenMatHang.setText(data.getTen());
                tvMoTaMatHang.setText(data.getMota());

                Locale locale = new Locale("vi", "VN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                tvGiaMatHang.setText(fmt.format(data.getGia())+"");

                LoadPicture.load(context,data.getTenAnh(),iv);
                addEvent();
            }
        }

        private void addEvent() {

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickRVMatHangAdaper.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public class HolderLoaiHangRVNhapHang extends RecyclerView.ViewHolder {
        @BindView(R.id.tvloaihang_cuahang_nhaphangfragment)
        TextView tvLoaiHang;


        View v;

        public HolderLoaiHangRVNhapHang(View itemView) {
            super(itemView);
            v = itemView;
            ButterKnife.bind(this, v);
        }

        public void setData(String ten) {
            tvLoaiHang.setText(ten);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickRVMatHangAdaper.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
