package com.ctrip.pluginsource;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends BaseActivity {

	private TextView title;
	private RelativeLayout background;
	protected DexClassLoader classLoader = null;
	protected File fileRelease = null; //�ͷ�Ŀ¼
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//动态获取权限
		checkPermission();
		setContentView(R.layout.activity_main);

		title = (TextView)findViewById(R.id.title);
		background = (RelativeLayout) findViewById(R.id.background);

		//把插件APK放到sd卡，然后在这里加载
		File file1 = new File(Environment.getExternalStorageDirectory().getPath() , "ResourceLoaderApk1.apk");
		final String filePath1 = file1.getAbsoluteFile().getPath();

		fileRelease = getDir("dex", 0);
		
		findViewById(R.id.btn1).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				classLoader = new DexClassLoader(filePath1, fileRelease.getAbsolutePath(), null, getClassLoader());
				loadResources(filePath1);
				setContent();
				//setContent2();
			}});

	}

	@SuppressLint("NewApi")
	private void setContent(){
		try{
			Class clazz = classLoader.loadClass("com.example.resourceloaderapk.UIUtil");
			Method method = clazz.getMethod("getTextString", Context.class);
			String str = (String)method.invoke(null, this);
			title.setText(str);
			method = clazz.getMethod("getImageDrawable", Context.class);
			Drawable drawable = (Drawable)method.invoke(null, this);
			background.setBackground(drawable);
			method = clazz.getMethod("getLayout", Context.class);
			View view = (View)method.invoke(null, this);
		}catch(Exception e){
			Log.i("Loader", "error:"+Log.getStackTraceString(e));
		}
	}

	//另一种加载插件资源的方式
	private void setContent2(){
		int stringId = getTextStringId();
		int drawableId = getImgDrawableId();
		int layoutId = getLayoutId();
		Log.i("Loader", "stringId:"+stringId+",drawableId:"+drawableId+",layoutId:"+layoutId);
		title.setText(stringId);
		background.setBackgroundResource(drawableId);
	}
	
	@SuppressLint("NewApi")
	private int getTextStringId(){
		try{
			Class clazz = classLoader.loadClass("com.example.resourceloaderapk1.R$string");
			Field field = clazz.getField("grassland");
			int resId = (int)field.get(null);
			return resId;
		}catch(Exception e){
			Log.i("Loader", "error:"+Log.getStackTraceString(e));
		}
		return 0;
	}
	
	@SuppressLint("NewApi")
	private int getImgDrawableId(){
		try{
			Class clazz = classLoader.loadClass("com.example.resourceloaderapk1.R$drawable");
			Field field = clazz.getField("skin1");
			int resId = (int)field.get(null);
			return resId;
		}catch(Exception e){
			Log.i("Loader", "error:"+Log.getStackTraceString(e));
		}
		return 0;
	}
	
	@SuppressLint("NewApi")
	private int getLayoutId(){
		try{
			Class clazz = classLoader.loadClass("com.example.resourceloaderapk1.R$layout");
			Field field = clazz.getField("activity_main");
			int resId = (int)field.get(null);
			return resId;
		}catch(Exception e){
			Log.i("Loader", "error:"+Log.getStackTraceString(e));
		}
		return 0;
	}
	
	@SuppressLint("NewApi")
	private void printResourceId(){
		try{
			Class clazz = classLoader.loadClass("com.example.resourceloaderapk.UIUtil");
			Method method = clazz.getMethod("getTextStringId", null);
			Object obj = method.invoke(null, null);
			Log.i("Loader", "stringId:"+obj);
			Log.i("Loader", "newId:"+R.string.app_name);
			method = clazz.getMethod("getImageDrawableId", null);
			obj = method.invoke(null, null);
			Log.i("Loader", "drawableId:"+obj);
			method = clazz.getMethod("getLayoutId", null);
			obj = method.invoke(null, null);
			Log.i("Loader", "layoutId:"+obj);
			Log.i("Loader", "newId:"+R.layout.activity_main);
		}catch(Exception e){
			Log.i("Loader", "error:"+Log.getStackTraceString(e));
		}
	}
	
	private void printRField(){
		Class clazz = R.id.class;
		Field[] fields = clazz.getFields();
		for(Field field : fields){
			Log.i("Loader", "fields:"+field);
		}
		Class clazzs = R.layout.class;
		Field[] fieldss = clazzs.getFields();
		for(Field field : fieldss){
			Log.i("Loader", "fieldss:"+field);
		}
	}
	
}
