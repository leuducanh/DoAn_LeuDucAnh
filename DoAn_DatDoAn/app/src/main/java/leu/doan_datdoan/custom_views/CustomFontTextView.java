package leu.doan_datdoan.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import leu.doan_datdoan.R;
import leu.doan_datdoan.utils.TypefaceFactory;


/**
 * Created by MyPC on 22/06/2017.
 */

public class CustomFontTextView extends android.support.v7.widget.AppCompatTextView{

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomFontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet){
        if(attributeSet != null){
            TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomFontTextView);
            String fontName = typedArray.getString(R.styleable.CustomFontTextView_fontName);

            try{
                if(fontName != null){
                    setTypeface(TypefaceFactory.getInstance().factoryTypeface(fontName,getContext()));
                }
            }catch (Exception e){

            }

            typedArray.recycle();
        }
    }
}
