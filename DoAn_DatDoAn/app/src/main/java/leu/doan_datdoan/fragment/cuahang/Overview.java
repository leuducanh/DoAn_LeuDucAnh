package leu.doan_datdoan.fragment.cuahang;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import leu.doan_datdoan.R;
import leu.doan_datdoan.activity.CuaHangActivity;
import leu.doan_datdoan.adapters.cuahang.OverviewRvAdapter;
import leu.doan_datdoan.custom_views.CustomMarkerView;
import leu.doan_datdoan.model.CuaHang;
import leu.doan_datdoan.model.ThongKe;
import leu.doan_datdoan.model.ThongKeLoaiHang;
import leu.doan_datdoan.model.ThongKeMatHang;
import leu.doan_datdoan.network.RetrofitFactory;
import leu.doan_datdoan.network.cuahang.CuaHangService;
import leu.doan_datdoan.utils.LabelFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Overview extends Fragment implements DatePickerDialog.OnDateSetListener{


    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.rlpiechart)
    RelativeLayout rlPieChart;

    @BindView(R.id.rvloaihangpiechart_overview)
    RecyclerView rvMoney;
    @BindView(R.id.radiochedo_overview)
    RadioRealButtonGroup radioGroup;
    @BindView(R.id.lnngay_overview)
    LinearLayout lnNgay;
    @BindView(R.id.lnthangnam_overview)
    LinearLayout lnThangNam;
    @BindView(R.id.spnthang_overview)
    Spinner spnThang;
    @BindView(R.id.spnnam_overview)
    Spinner spnNam;
    @BindView(R.id.btnchonngay_overview)
    Button btnChonNgay;
    @BindView(R.id.btnthongke_overview)
    Button btnThongKe;
    @BindView(R.id.tvngaychon_overview)
    TextView tvNgayChon;
    @BindView(R.id.nsv_cuahang_overview)
    NestedScrollView nsv;
    @BindView(R.id.barcharmathang_overview)
    BarChart barChart;
//    @BindView(R.id.imgtouch_overview)
//    ImageView img;

    private ArrayAdapter<Integer> dataAdapter1;
    private ArrayAdapter<Integer> dataAdapter;
    private DatePickerDialog dialog;
    OverviewRvAdapter overviewRvAdapter;
    private ArrayList<TextView> textViewArrayList;

    private CuaHang cuaHang;
    private int ngay = 0;
    private int thang = 0;
    private int nam = 0;
    private int cheDo = 0;
    private int currentYear;
    private ThongKe thongKe;
    private ArrayList<Integer> colors;
    private ArrayList<String> labels;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ngay = dayOfMonth;
        thang = month + 1;
        nam = year;
        setTvNgayChon();
    }

    public static interface click {
        public void onClick();
    }

    List<ThongKeLoaiHang> thongKeLoaiHangs;
    List<ThongKeMatHang> thongKeMatHangs;

    List<PieEntry> entriesDraw;


    public Overview() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        textViewArrayList = new ArrayList<>();
        thongKeLoaiHangs = new ArrayList<>();
        labels = new ArrayList<>();
        cuaHang = (CuaHang) getArguments().getSerializable(CuaHangActivity.KEY_BUNDLE_CUAHANG);
        setupUI(v);
        addEvents();

        drawPieChart();
        return v;
    }

    private void addEvents() {
        nsv.requestDisallowInterceptTouchEvent(true);
        rlPieChart.requestDisallowInterceptTouchEvent(true);




        radioGroup.setFocusable(false);
        radioGroup.setOnPositionChangedListener(new RadioRealButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
                cheDo = currentPosition + 1;

                switch (cheDo){
                    case 1:{
                        lnThangNam.setVisibility(View.GONE);
                        lnNgay.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 2:{
                        lnNgay.setVisibility(View.GONE);
                        lnThangNam.setVisibility(View.VISIBLE);
                        spnThang.setAdapter(dataAdapter);
                        spnNam.setAdapter(dataAdapter1);

                        if(((Spinner) spnThang).getSelectedView()!=null){
                            ((Spinner) spnThang).getSelectedView().setEnabled(true);
                        }

                        spnThang.setEnabled(true);

                        spnThang.setSelection(thang - 1);

                        spnNam.setSelection(currentYear-nam);
                        break;
                    }
                    case 3:{
                        lnNgay.setVisibility(View.GONE);
                        lnThangNam.setVisibility(View.VISIBLE);
                        if(((Spinner) spnThang).getSelectedView()!=null){
                            ((Spinner) spnThang).getSelectedView().setEnabled(false);
                        }
                        spnThang.setEnabled(false);
                        spnNam.setAdapter(dataAdapter1);

                        spnNam.setSelection(currentYear-nam);

                        break;
                    }
                }

                btnThongKe.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUI(View v) {
        ButterKnife.bind(this, v);


        colors = new ArrayList<>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        Date currentTime = Calendar.getInstance().getTime();

        ngay = currentTime.getDate();
        thang = currentTime.getMonth() +1;
        nam = currentTime.getYear()+1900;
        currentYear = nam;


                dialog = new DatePickerDialog(getContext(),this,currentTime.getYear()+1900,currentTime.getMonth(),currentTime.getDate());
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 1;i <= 12;i++){
            list.add(i);
        }

       dataAdapter = new ArrayAdapter<Integer>(this.getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        ArrayList<Integer> list1 = new ArrayList<>();
        for(int i = currentTime.getYear()+1900;i >= 1900;i--){
            list1.add(i);
        }

         dataAdapter1 = new ArrayAdapter<Integer>(this.getActivity(),
                android.R.layout.simple_spinner_item, list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thang = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnNam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nam = list1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
        setTvNgayChon();
    }

    private void setTvNgayChon(){
        tvNgayChon.setText(ngay + "/" + thang + "/" +nam);
    }

    private void request(){

        RetrofitFactory.getInstance().createService(CuaHangService.class).thongKeCuaHang(cuaHang.getId(),ngay,thang,nam,cheDo).enqueue(new Callback<ThongKe>() {
            @Override
            public void onResponse(Call<ThongKe> call, Response<ThongKe> response) {
                if(response.isSuccessful()){
                    thongKeLoaiHangs = response.body().getThongKeLoaiHangs();
                    thongKeMatHangs = response.body().getThongKeMatHangs();
                    drawPieChart();
                    drawBarChart();
                }else{
                    showM("Lỗi");
                }
            }

            @Override
            public void onFailure(Call<ThongKe> call, Throwable t) {
                showM("Lỗi");
            }
        });
    }

    private void showM(String m){
        Toast.makeText(getContext(),m,Toast.LENGTH_SHORT).show();
    }

    public void clearPieChart() {
        pieChart.clear();
    }
    public void clearBarChart(){
        barChart.clear();
    }

    public void drawBarChart(){
        clearBarChart();

        if(thongKeMatHangs.size() > 0){
            Log.d("abcde","123a");
            List<BarEntry> entries = convertToListBarEntry();

            Description description = new Description();
            description.setText("");
            description.setTextSize(12);
            description.setTextAlign(Paint.Align.RIGHT);
            barChart.setDescription(description);

            BarDataSet set = new BarDataSet(entries,"Thống kê mặt hàng");
            set.setValueTextColor(Color.BLACK);
            set.setColors(colors);
            BarData data = new BarData(set);
            data.setBarWidth(0.9f);
            barChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
            barChart.setData(data);
            barChart.setFitBars(true);

            XAxis xAxis = barChart.getXAxis();
            YAxis yAxisL = barChart.getAxisLeft();
            YAxis yAxisR = barChart.getAxisRight();

            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(4);
            xAxis.setDrawGridLines(false);
            yAxisR.setDrawGridLines(false);
            yAxisR.setDrawAxisLine(false);
            yAxisR.setDrawLabels(false);
            barChart.setDoubleTapToZoomEnabled(false);
            barChart.setPinchZoom(true);
            barChart.invalidate();
        }
    }

    private List<BarEntry> convertToListBarEntry() {
        List<BarEntry> barEntries = new ArrayList<>();
        labels.clear();

        float i = 0f;
        for(ThongKeMatHang tk : thongKeMatHangs){
            barEntries.add(new BarEntry(i,tk.getSoLuong()));
            labels.add(tk.getMatHang());
            i++;
        }

        return barEntries;

    }

    public void drawPieChart() {
        clearPieChart();
        if (thongKeLoaiHangs.size() > 0) {
//            List<PieEntry> entries = convertToListPieEntry(thongKeLoaiHangs);

            List<PieEntry> entries = convertToListPieEntry();
            PieDataSet pieDataSet = new PieDataSet(entries,"Loại hàng");
            pieDataSet.setSelectionShift(15f);
            pieDataSet.setSliceSpace(3f);

            pieDataSet.setValueTextColors(colors);
            pieDataSet.setValueLineWidth(.5f);
            pieDataSet.setColors(colors);
            pieDataSet.setValueLinePart1OffsetPercentage(100f);
            pieDataSet.setValueLinePart1Length(1.0f);
            pieDataSet.setValueLinePart2Length(0.8f);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData pieData = new PieData();
            pieData.addDataSet(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);

            pieChart.setData(pieData);
            pieChart.setDrawCenterText(false);
            Description description = new Description();
            description.setText("Thống kê loại hàng");

            pieChart.setDescription(description);

            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);

            pieChart.setHighlightPerTapEnabled(true);
            pieChart.setRotationAngle(0);
            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            pieChart.setHoleRadius(7);
            pieChart.setTransparentCircleRadius(10);
            pieChart.setUsePercentValues(true);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setExtraOffsets(13, 13, 13, 13);
            pieChart.calculateOffsets();
            CustomMarkerView customMarkerView = new CustomMarkerView(getContext(), R.layout.marker_view);
            pieChart.setMarker(customMarkerView);

//            for(int i = 0;i <  pieChart.getData().getDataSet().getEntryCount();i++){
//                PieEntry p =  pieChart.getData().getDataSet().getEntryForIndex(i);
//
//                PercentFormatter f = new PercentFormatter();
//                Log.d("abc"," " + p.getX() + " " + p.getY() + " " + p.getLabel());
//                Log.d("    ",(float)Math.round(p.getY()/(pieChart.getData().getYValueSum())*1000)/10 + "");
//
//            }

            overviewRvAdapter = new OverviewRvAdapter(getContext(), pieChart.getData());
            rvMoney.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMoney.setAdapter(overviewRvAdapter);
            nsv.setNestedScrollingEnabled(false);
            overviewRvAdapter.notifyDataSetChanged();
        } else {
            overviewRvAdapter = new OverviewRvAdapter(getContext(), new PieData(new PieDataSet(new ArrayList<PieEntry>(), "")));
            rvMoney.setAdapter(overviewRvAdapter);
            overviewRvAdapter.notifyDataSetChanged();
        }

        pieChart.invalidate();
    }

    private List<PieEntry> convertToListPieEntry() {
        List<PieEntry> entries = new ArrayList<>();

        for(ThongKeLoaiHang tk : thongKeLoaiHangs){
            entries.add(new PieEntry(tk.getTongGia(),tk.getLoaiHang()));
        }

        return entries;
    }

//    private List<PieEntry> convertToListPieEntry(List<IncomeExpensesMoney> list) {
//        float total = 0;
//        float sum = 0;
//
//        HashMap<String, Long> categoryMap = new HashMap<>();
//        for (int i = 0; i < list.size(); i++) {
//            total += list.get(i).getMoney();
//
//            Long currentMoney = categoryMap.get(list.get(i).getCategory().getName());
//            long money = list.get(i).getMoney();
//
//            if (currentMoney == null) {
//                categoryMap.put(list.get(i).getCategory().getName(), money);
//            } else {
//                categoryMap.put(list.get(i).getCategory().getName(), currentMoney + money);
//            }
//        }
//        List<PieEntry> entries = new ArrayList<>();
//
//        Set<String> categories = categoryMap.keySet();
//        Iterator<String> iterCategories = categories.iterator();
//
//        int count = 0;
//        while (iterCategories.hasNext()) {
//            String category = iterCategories.next();
////            count++;
////            if(count == entries.size()){
////                entries.add(new PieEntry(total-sum,category));
////                break;
////            }
////            float temp =  roundTwoDigitsDecimal((float)categoryMap.get(category)/total*100);
////            sum += temp;
////
//////      float temp = categoryMap.get(category);
////
////      entries.add(new PieEntry(temp, category));
//            entries.add(new PieEntry((float) categoryMap.get(category), category));
//        }
//
//        return entries;
//    }

    private float roundTwoDigitsDecimal(float d) {
        d = (float) ((int) (Math.round(d * 100))) / 100;
        return d;
    }

}
