package com.example.resourceloaderapk1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;


public class UIUtil {

    public static String getTitle(Context ctx) {
        return ctx.getResources().getString(R.string.home_theme_title);
    }

    public static String getSubTitle(Context ctx) {
        return ctx.getResources().getString(R.string.home_theme_subtitle);
    }

    public static Drawable getImageDrawable(Context ctx) {
        return ctx.getResources().getDrawable(R.drawable.home_theme_bg);
    }

    public static View getLayout(Context ctx) {
        return LayoutInflater.from(ctx).inflate(R.layout.activity_main, null);
    }


}
