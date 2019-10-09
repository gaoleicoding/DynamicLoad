package com.ctrip.pluginapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    File file;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    //    BottomNavigationView bottomNavigationView;
    String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });
        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load();
            }
        });
        checkPermission();
        PluginManager.getInstance().setContext(this);


    }

    //事件绑定load
    private void load() {
        /**
         * 事先放置到SD卡根目录的plugin.apk
         * 现实场景中是有服务端下发
         */

        if(file.exists()){
            try {
                PluginManager.getInstance().loadPath(file.getAbsoluteFile().getPath());
                toast("加载插件apk成功" );
            }catch (Exception e){
            }
        }else{
            toast("插件plugin.apk不存在" );
        }

    }

    //事件绑定click
    private void click() {
        /**
         * 点击跳往插件app的activity，一律跳转到PRoxyActivity
         */
        if(file.exists()){
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("className", PluginManager.getInstance().getEntryName());
        startActivity(intent);
        }else{
            toast("插件plugin.apk不存在" );

        }

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
            file = new File(Environment.getExternalStorageDirectory().getPath() , "plugin.apk");
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
                 file = new File(Environment.getExternalStorageDirectory().getPath() , "plugin.apk");
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