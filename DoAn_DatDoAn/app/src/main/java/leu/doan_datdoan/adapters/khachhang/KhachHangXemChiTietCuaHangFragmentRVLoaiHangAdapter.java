package leu.doan_datdoan.adapters.khachhang;

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
import leu.doan_datdoan.model.LoaiHang;

/**
 * Created by MyPC on 21/10/2017.
 */

public class KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter extends RecyclerView.Adapter<KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter.Holder> {
    private List<LoaiHang> loaiHangs;
    private int choosenPosition = 0;

    private Context context;
    private OnItemClickRVLoaiHangAdapter onItemClickRVLoaiHangAdapter;
    public static interface OnItemClickRVLoaiHangAdapter{
        public void onItemLoaiHangClick(int position);
    }

    public void setOnItemClickRVLoaiHangAdapter(OnItemClickRVLoaiHangAdapter onItemClickRVLoaiHangAdapter) {
        this.onItemClickRVLoaiHangAdapter = onItemClickRVLoaiHangAdapter;
    }

    public KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter(List<LoaiHang> loaiHangs, Context context) {
        this.loaiHangs = loaiHangs;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_khachhang_chitietcuahangfragment_rvloaihang, parent, false);
        KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter.Holder loaiHangViewHolder = new KhachHangXemChiTietCuaHangFragmentRVLoaiHangAdapter.Holder(view);
        return loaiHangViewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setData(loaiHangs.get(position).getTen(),position);
    }

    @Override
    public int getItemCount() {
        return loaiHangs.size();
    }

    public void setChoosenPosition(int choosenPosition){
        this.choosenPosition = choosenPosition;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvloaihang_khachhang_xemchitietcuahang_rvloaihang)
        TextView tvLoaiHang;

        View view;

        public Holder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,view);
        }

        public void setData(String data,int position) {
            if (data != null) {
                if(position == choosenPosition){
                    tvLoaiHang.setTextColor(Color.RED);
                }else{
                    tvLoaiHang.setTextColor(Color.BLACK);
                }

                tvLoaiHang.setText(data);
                view.setTag(data);

                addEvent();
            }
        }

        private void addEvent() {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickRVLoaiHangAdapter.onItemLoaiHangClick(getAdapterPosition());
                }
            });
        }
    }
}
