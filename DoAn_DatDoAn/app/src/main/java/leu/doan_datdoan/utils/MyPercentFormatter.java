package leu.doan_datdoan.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by MyPC on 30/07/2017.
 */

public class MyPercentFormatter extends PercentFormatter {
    private PercentFormatter percentFormatter;

    public MyPercentFormatter() {
    }

    public MyPercentFormatter(PercentFormatter percentFormatter) {
        this.percentFormatter = percentFormatter;
    }

    public MyPercentFormatter(DecimalFormat format, PercentFormatter percentFormatter) {
        super(format);
        this.percentFormatter = percentFormatter;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value < 1) return "";
        return percentFormatter.getFormattedValue(value,null);
    }
}
