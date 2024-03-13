package com.ctrip.dynamic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 *项目详情可参考：https://blog.csdn.net/colinandroid/article/details/79431502
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title, subTitle;
    private RelativeLayout rlBg;
    private ImageView img;

    //把插件APK放到sd卡，然后在这里加载
    private final String pluginName = "plugin.apk";
    private final File pluginFile = new File(Environment.getExternalStorageDirectory().getPath(), pluginName);
    private final String pluginPath = pluginFile.getAbsolutePath();

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    String[] permissionArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subtitle);
        rlBg = findViewById(R.id.background);
        img = findViewById(R.id.img);
        Button btnLoadPlugin = findViewById(R.id.btn_load_plugin);
        Button btnGotoPlugin = findViewById(R.id.btn_goto_plugin);
        Button btnSwitchTheme = findViewById(R.id.btn_switch_theme);
        btnLoadPlugin.setOnClickListener(this);
        btnGotoPlugin.setOnClickListener(this);
        btnSwitchTheme.setOnClickListener(this);

        checkPermission();
        PluginManager.getInstance().setContext(this);

    }

    /**
     * 事先放置到SD卡根目录的plugin.apk，现实场景中是有服务端下发
     */
    private void loadPlugin() {
        if (pluginFile.exists()) {
            try {
                PluginManager.getInstance().loadPath(pluginFile.getAbsoluteFile().getPath());
                toast("加载插件apk成功");
            } catch (Exception e) {
            }
        } else {
            toast(String.format("插件%s不存在", pluginName));
        }
    }

    /**
     * 点击跳往插件app的activity，先跳转到PRoxyActivity
     */
    private void gotoPlugin() {

        if (pluginFile.exists()) {
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.putExtra("className", PluginManager.getInstance().getEntryName());
            startActivity(intent);
        } else {
            toast(String.format("插件%s不存在", pluginName));
        }

    }

    public void checkPermission() {
        // 检查是否有相应的权限
        boolean isAllGranted = checkPermissionAllGranted(
                permissionArray
        );
        // 如果这权限全都拥有, 则显示HomeFragment
        if (isAllGranted) {
            return;
        }
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                permissionArray,
                MY_PERMISSION_REQUEST_CODE
        );
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

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
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                toast("App需要使用存储卡权限，请授权后使用");
            }
        }
    }

    public void toast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    private void showPluginRes() {
        Drawable bg = ResourceUtil.getPluginResources(this, pluginPath).getDrawable(ResourceUtil.getResId(this, pluginPath, ResourceUtil.getPluginPackagename(this, pluginPath), "home_theme_bg"));
        rlBg.setBackgroundDrawable(bg);
        Drawable iconCao = ResourceUtil.getPluginResources(this, pluginPath).getDrawable(ResourceUtil.getResId(this, pluginPath, ResourceUtil.getPluginPackagename(this, pluginPath), "home_theme_icon"));
        img.setBackgroundDrawable(iconCao);
        String titleStr = ResourceUtil.getPluginResources(this, pluginPath).getString(ResourceUtil.getStringId(this, pluginPath, ResourceUtil.getPluginPackagename(this, pluginPath), "home_theme_title"));
        title.setText(titleStr);
        String subtitle = ResourceUtil.getPluginResources(this, pluginPath).getString(ResourceUtil.getStringId(this, pluginPath, ResourceUtil.getPluginPackagename(this, pluginPath), "home_theme_subtitle"));
        subTitle.setText(subtitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load_plugin:
                loadPlugin();
                break;
            case R.id.btn_goto_plugin:
                gotoPlugin();
                break;
            case R.id.btn_switch_theme:
                if (pluginFile.exists()) {
                    showPluginRes();
                } else {
                    Toast.makeText(MainActivity.this, String.format("插件%s不存在", pluginName), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}