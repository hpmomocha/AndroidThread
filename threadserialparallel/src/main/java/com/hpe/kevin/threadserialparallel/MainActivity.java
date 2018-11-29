package com.hpe.kevin.threadserialparallel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hpe.kevin.threadserialparallel.asynctask.MyAsyncTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static SpannableStringBuilder logContent = new SpannableStringBuilder();
    @BindView(R.id.textView)
    public TextView textView;

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

    public void executeThreadSerial(View view) {
        logContent.clear();
        logContent.append("串行执行开始...\n");
        textView.setText(logContent);
        new MyAsyncTask("AsyncTask#1", textView).execute("");
        new MyAsyncTask("AsyncTask#2", textView).execute("");
        new MyAsyncTask("AsyncTask#3", textView).execute("");
        new MyAsyncTask("AsyncTask#4", textView).execute("");
        new MyAsyncTask("AsyncTask#5", textView).execute("");
    }

    public void executeThreadParallel(View view) {
        logContent.clear();
        logContent.append("并行执行开始...\n");
        textView.setText(logContent);
        new MyAsyncTask("AsyncTask#1", textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#2", textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#3", textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#4", textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        new MyAsyncTask("AsyncTask#5", textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }
}
