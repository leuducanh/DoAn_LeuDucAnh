package leu.doan_datdoan.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import leu.doan_datdoan.R;
import leu.doan_datdoan.utils.TypefaceFactory;


/**
 * Created by MyPC on 23/06/2017.
 */

public class CustomFontEditText extends android.support.v7.widget.AppCompatEditText {
    public CustomFontEditText(Context context) {
        super(context);
    }

    public CustomFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(AttributeSet attributeSet){
        if(attributeSet != null){
            TypedArray typedArray = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomFontEditText);
            String fontName = typedArray.getString(R.styleable.CustomFontEditText_fontNameEd);

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
