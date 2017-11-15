package leu.doan_datdoan.adapters.khachhang;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.network.LoadPicture;


/**
 * Created by MyPC on 17/10/2017.
 */

public class KhachHang_NearMeViewPagerFrament_RVAdapter extends RecyclerView.Adapter<KhachHang_NearMeViewPagerFrament_RVAdapter.CuaHangViewHolder>{

    private List<CuaHang> cuaHangList;
    private Context context;
    private KhachHang_OnClickCuaHang khachHang_onClickCuaHang;

    public static interface  KhachHang_OnClickCuaHang{
        public void onClick(int postion);
    }

    public KhachHang_NearMeViewPagerFrament_RVAdapter(List<CuaHang> cuaHangList, Context context) {
        this.cuaHangList = cuaHangList;
        this.context = context;
    }

    public void setOnItemClickListener(KhachHang_OnClickCuaHang khachHang_onClickCuaHang) {
        this.khachHang_onClickCuaHang= khachHang_onClickCuaHang;
    }

    public List<CuaHang> getCuaHangList() {
        return cuaHangList;
    }

    @Override
    public CuaHangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_khachang_rv_nearme_view_pager, parent, false);
        KhachHang_NearMeViewPagerFrament_RVAdapter.CuaHangViewHolder cuaHangViewHolder = new KhachHang_NearMeViewPagerFrament_RVAdapter.CuaHangViewHolder(view);
         return cuaHangViewHolder;
    }

    @Override
    public void onBindViewHolder(CuaHangViewHolder holder, int position) {
        holder.setData(cuaHangList.get(position));
    }

    @Override
    public int getItemCount() {
        return cuaHangList.size();
    }


    public void setNewList(List<CuaHang> list){
        cuaHangList = list;
        notifyDataSetChanged();
    }


    public class CuaHangViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivcuahang_khachhang_nearme_viewpager_rv)
    ImageView img;
    @BindView(R.id.tvtencuahang_khachhang_nearme_viewpager_rv)
    TextView tvTen;
    @BindView(R.id.tvdiachi_khachhang_nearme_viewpager_rv)
    TextView tvDiaChi;

    View view;

    public CuaHangViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        view = itemView;
    }

    public void setData(CuaHang data) {
        if (data != null) {
            tvTen.setText(data.getTen());
            tvDiaChi.setText(data.getDiaDiem());
            LoadPicture.load(context,data.getUrl(),img);
            view.setTag(data);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    khachHang_onClickCuaHang.onClick(getAdapterPosition());
                }
            });
        }
    }
}
}
