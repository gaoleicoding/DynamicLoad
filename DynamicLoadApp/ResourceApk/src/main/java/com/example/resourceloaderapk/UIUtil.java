package com.example.resourceloaderapk;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.resourceloaderapk1.R;

public class UIUtil {
	
	public static String getTextString(Context ctx){
		return ctx.getResources().getString(R.string.grassland);
	}
	
	public static Drawable getImageDrawable(Context ctx){
		return ctx.getResources().getDrawable(R.drawable.skin1);
	}
	
	public static View getLayout(Context ctx){
		return LayoutInflater.from(ctx).inflate(R.layout.activity_main, null);
	}
	
	public static int getTextStringId(){
		return R.string.grassland;
	}
	
	public static int getImageDrawableId(){
		return R.drawable.skin1;
	}
	
	public static int getLayoutId(){
		return R.layout.activity_main;
	}

}
