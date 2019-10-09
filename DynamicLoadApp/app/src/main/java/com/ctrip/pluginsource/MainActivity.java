package com.ctrip.pluginsource;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends BaseActivity {

    private TextView title, subTitle;
    private RelativeLayout rlBg;
    private ImageView img;
    protected File fileRelease = null; //�ͷ�Ŀ¼

    //把插件APK放到sd卡，然后在这里加载
    final File pluginFile = new File(Environment.getExternalStorageDirectory().getPath(), "ResourceApk.apk");
    final String pluginPath = pluginFile.getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态获取权限
        checkPermission();
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subtitle);
        rlBg = findViewById(R.id.background);
        img = findViewById(R.id.img);


        fileRelease = getDir("dex", 0);

        findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (pluginFile.exists()) {
                    showPluginRes();

                } else {
                    Toast.makeText(MainActivity.this, "插件ResourceApk.apk不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void showPluginRes() {
        Drawable bg = Util.getPluginResources(MainActivity.this, pluginPath).getDrawable(Util.getResId(MainActivity.this, pluginPath, Util.getPluginPackagename(MainActivity.this, pluginPath), "home_theme_bg"));
        rlBg.setBackgroundDrawable(bg);
        String titleStr = Util.getPluginResources(MainActivity.this, pluginPath).getString(Util.getStringId(MainActivity.this, pluginPath, Util.getPluginPackagename(MainActivity.this, pluginPath), "home_theme_title"));
        title.setText(titleStr);
        Drawable iconCao = Util.getPluginResources(MainActivity.this, pluginPath).getDrawable(Util.getResId(MainActivity.this, pluginPath, Util.getPluginPackagename(MainActivity.this, pluginPath), "home_theme_icon"));
        img.setBackgroundDrawable(iconCao);
        String subtitle = Util.getPluginResources(MainActivity.this, pluginPath).getString(Util.getStringId(MainActivity.this, pluginPath, Util.getPluginPackagename(MainActivity.this, pluginPath), "home_theme_subtitle"));
        subTitle.setText(subtitle);
    }


}
