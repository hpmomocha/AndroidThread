package com.hpe.kevin.intentservicedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpe.kevin.intentservicedemo.service.DownloadChanger;
import com.hpe.kevin.intentservicedemo.service.DownloadService;
import com.hpe.kevin.intentservicedemo.service.DownloadWatcher;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private String url = "https://images.pexels.com/photos/573299/pexels-photo-573299.jpeg?cs=srgb&dl=adolescence-attractive-beautiful-573299.jpg&fm=jpg";

    @BindView(R.id.tvProgress)
    public TextView tvProgress;

    @BindView(R.id.imageView)
    public ImageView imageView;

    @BindView(R.id.button)
    public Button button;

    private DownloadWatcher mWatcher = new DownloadWatcher() {
        @Override
        public void notifyUpdate(int progress) {
            tvProgress.setText(progress + "%");
            if (progress == 100) {
                button.setText("开始");
                Bitmap bm = BitmapFactory.decodeFile(getCacheDir() + File.separator + "IntentService.png");
                // 以下解决图片太大不显示的问题
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                if(bm.getWidth() <= screenWidth){
                    imageView.setImageBitmap(bm);
                }else{
                    Bitmap bmp= Bitmap.createScaledBitmap(bm, screenWidth, bm.getHeight() * screenWidth / bm.getWidth(), true);
                    imageView.setImageBitmap(bmp);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DownloadChanger.getInstance().addObserver(mWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start(View view) {
        button.setText("下载中");
        startService(DownloadService.newIntent(this, url));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadChanger.getInstance().deleteObservers();
    }
}
