package leu.doan_datdoan.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by MyPC on 23/06/2017.
 */

public class TypefaceFactory {
    private static TypefaceFactory typefaceFactory;
    private static HashMap<String,Typeface> fontCache = new HashMap<>();

    private TypefaceFactory() {

    }

    public static TypefaceFactory getInstance() {
        if(typefaceFactory == null){
            typefaceFactory = new TypefaceFactory();
        }
        return typefaceFactory;
    }

    public Typeface factoryTypeface(String typefaceName, Context context) {

        Typeface typeface = fontCache.get(typefaceName);

        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + typefaceName);
            fontCache.put(typefaceName,typeface);
        }

        return typeface;
    }
}
