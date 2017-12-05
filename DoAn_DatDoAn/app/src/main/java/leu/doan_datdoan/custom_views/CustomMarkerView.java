package leu.doan_datdoan.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import leu.doan_datdoan.R;


/**
 * Created by MyPC on 07/07/2017.
 */

public class CustomMarkerView extends MarkerView {
    private TextView tvContent;


    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Locale locale = new Locale("da", "DK");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setCurrency(Currency.getInstance("VND"));
        String number = numberFormat.format(highlight.getY());
        tvContent.setText(number);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(0,40);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        super.draw(canvas, posX, posY);
    }
}
