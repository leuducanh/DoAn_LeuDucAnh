package leu.doan_datdoan.adapters.cuahang;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.model.HangDat;
import leu.doan_datdoan.model.Util;
import leu.doan_datdoan.network.LoadPicture;

/**
 * Created by MyPC on 07/11/2017.
 */

public class CuaHang_RvChiTietPheDuyetAdapter extends RecyclerView.Adapter<CuaHang_RvChiTietPheDuyetAdapter.HolderhangdatRVChiTietPheDuyet> {
    private List<HangDat> hangdats;
    private Context context;

    public CuaHang_RvChiTietPheDuyetAdapter(List<HangDat> hangdats, Context context) {
        this.hangdats = hangdats;
        this.context = context;
    }

    @Override
    public HolderhangdatRVChiTietPheDuyet onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_cuahang_rvchitietduyetdon, parent, false);
        HolderhangdatRVChiTietPheDuyet holderhangdatRVChiTietPheDuyet = new HolderhangdatRVChiTietPheDuyet(view);
        return holderhangdatRVChiTietPheDuyet;
    }

    @Override
    public void onBindViewHolder(HolderhangdatRVChiTietPheDuyet holder, int position) {
        holder.setData(hangdats.get(position));
    }

    @Override
    public int getItemCount() {
        return hangdats.size();
    }

    public class HolderhangdatRVChiTietPheDuyet extends RecyclerView.ViewHolder {

        @BindView(R.id.tvtenmathang_cuahang_rvchitietpheduyet)
        TextView tvTen;
        @BindView(R.id.tvgia_cuahang_rvchitietduyetdon)
        TextView tvGia;
        @BindView(R.id.tvsoluong_cuahang_chitietduyetdonrv)
        TextView tvSoLuong;
        @BindView(R.id.ivmathang_cuahang_rvchitietduyetdon)
        ImageView iv;

        View view;

        public HolderhangdatRVChiTietPheDuyet(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(HangDat data) {
            if (data != null) {
                tvTen.setText(data.getMatHang().getTen());
                tvGia.setText(Util.formatConcurrency(data.getMatHang().getGia()));
                tvSoLuong.setText("x" + data.getSoluong());
                LoadPicture.load(context,data.getMatHang().getTenAnh(),iv);
            }
        }
    }
}
