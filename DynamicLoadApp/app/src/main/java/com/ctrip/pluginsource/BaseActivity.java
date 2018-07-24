package com.ctrip.pluginsource;

import java.lang.reflect.Method;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class BaseActivity extends Activity{
	
	protected AssetManager mAssetManager;//��Դ������  
	protected Resources mResources;//��Դ  
	protected Theme mTheme;//����  
	private static final int MY_PERMISSION_REQUEST_CODE = 10000;
	//    BottomNavigationView bottomNavigationView;
	String[] permissionArray = new String[]{
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	protected void loadResources(String dexPath) {  
        try {  
            AssetManager assetManager = AssetManager.class.newInstance();  
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);  
            addAssetPath.invoke(assetManager, dexPath);  
            mAssetManager = assetManager;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        Resources superRes = super.getResources();  
        superRes.getDisplayMetrics();  
        superRes.getConfiguration();  
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),superRes.getConfiguration());  
        mTheme = mResources.newTheme();  
        mTheme.setTo(super.getTheme());
    }  
	
	@Override  
	public AssetManager getAssets() {  
	    return mAssetManager == null ? super.getAssets() : mAssetManager;  
	}  
	
	@Override  
	public Resources getResources() {  
	    return mResources == null ? super.getResources() : mResources;  
	}  
	
	@Override  
	public Theme getTheme() {  
	    return mTheme == null ? super.getTheme() : mTheme;  
	}
	public void checkPermission() {
		/**
		 * 第 1 步: 检查是否有相应的权限
		 */
		boolean isAllGranted = checkPermissionAllGranted(
				permissionArray
		);
		// 如果这权限全都拥有, 则显示HomeFragment
		if (isAllGranted) {
//            switchFragment(0);
			return;
		}

		/**
		 * 第 2 步: 请求权限
		 */
		// 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
		ActivityCompat.requestPermissions(
				this,
				permissionArray,
				MY_PERMISSION_REQUEST_CODE
		);
	}

	/**
	 * 检查是否拥有指定的所有权限
	 */
	private boolean checkPermissionAllGranted(String[] permissions) {
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				// 只要有一个权限没有被授予, 则直接返回 false
				return false;
			}
		}
		return true;
	}

	/**
	 * 第 3 步: 申请权限结果返回处理
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == MY_PERMISSION_REQUEST_CODE) {
			boolean isAllGranted = true;
			// 判断是否所有的权限都已经授予了
			for (int grant : grantResults) {
				if (grant != PackageManager.PERMISSION_GRANTED) {
					isAllGranted = false;
					break;
				}
			}

			if (isAllGranted) {
				// 如果所有的权限都授予了, 则显示HomeFragment
//                switchFragment(0);
			} else {
				// 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
//                getAppDetailSettingIntent(this);
				toast("App正常使用需要授权" );

			}
		}
	}

	public void toast(String content) {
		Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
	}

}
