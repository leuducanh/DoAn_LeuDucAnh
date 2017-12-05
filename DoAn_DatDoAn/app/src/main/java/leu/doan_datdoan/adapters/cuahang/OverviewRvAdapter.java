package leu.doan_datdoan.adapters.cuahang;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import leu.doan_datdoan.R;

/**
 * Created by MyPC on 09/07/2017.
 */

public class OverviewRvAdapter extends RecyclerView.Adapter<OverviewRvAdapter.OverviewRvViewholder> {

    private Context context;
    private PieData pieData;


    public OverviewRvAdapter(Context context, PieData pieData) {
        this.context = context;
        this.pieData = pieData;
    }

    @Override
    public OverviewRvViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_overview_rv,parent,false);

        OverviewRvViewholder overviewRvViewholder = new OverviewRvViewholder(v);
        return overviewRvViewholder;
    }

    @Override
    public void onBindViewHolder(OverviewRvViewholder holder, int position) {
        holder.setData(pieData.getDataSet().getEntryForIndex(position),pieData.getDataSet().getEntryForIndex(position).getY(),pieData.getDataSet().getColor(position));
    }

    @Override
    public int getItemCount() {
        return pieData.getDataSet().getEntryCount();
    }

    public class OverviewRvViewholder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvloaihang_piechart)
        TextView tvLoaiHang;
        @BindView(R.id.tvtien_piechart)
        TextView tvTien;
        @BindView(R.id.tvpercent_piechart)
        TextView tvPercent;

        View itemView;
        public OverviewRvViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.itemView = itemView;
        }

        public void setData(PieEntry entryForIndex,float percen,int color){
            tvLoaiHang.setText(entryForIndex.getLabel());

            Locale locale = new Locale("da", "DK");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            numberFormat.setCurrency(Currency.getInstance("VND"));
            String number = numberFormat.format(entryForIndex.getY());

            tvTien.setText(number);

            Drawable mDrawable = context.getResources().getDrawable(R.drawable.corner_item_overview_rv);
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            tvPercent.setBackground(mDrawable);
            tvPercent.setText(percen + "%");
        }
    }
}
