package com.hpe.kevin.intentservicedemo.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public class DownloadService extends IntentService {
    private static int CONNECT_TIMEOUT=5000;
    private static int READ_TIMEOUT = 6000;

    private static final String TAG = "DownloadService";
    private static final String key = "key";
    private File destFile;
    private float fileLength; //文件总长度
    private float downloadLength;//文件当前下载

    //Service运行在主线程，所以这里不需要配置Looper
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownloadChanger.getInstance().setPostChange(msg.what);
        }
    };

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public DownloadService() {
        // 这里必须有一个空参数的构造实现父类的构造,否则会报异常
        // java.lang.InstantiationException: java.lang.Class<***.DownloadService> has no zero argument constructor
        super(TAG);
    }

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(key, url);
        return intent;
    }
    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     *               This may be null if the service is being restarted after
     *               its process has gone away; see
     *               {@link Service#onStartCommand}
     *               for details.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "onHandleIntent");
        String url = intent.getStringExtra(key);
        downloadLength = 0f;
        downloadFile(url);
    }

    private void downloadFile(String url) {
        HttpURLConnection mConnection  = null;
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.houston.hp.com", 8080));
            mConnection = (HttpURLConnection) new URL(url).openConnection(proxy);
            // HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
            mConnection.setRequestMethod("GET");
            mConnection.setConnectTimeout(CONNECT_TIMEOUT);
            mConnection.setReadTimeout(READ_TIMEOUT);
            int responseCode = mConnection.getResponseCode();
            fileLength = mConnection.getContentLength();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                fos = new FileOutputStream(destFile);
                inputStream = mConnection.getInputStream();
                byte[] bytes = new byte[2048];
                int len = -1;
                while ((len = inputStream.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                    downloadLength = downloadLength + len;
                    mHandler.sendEmptyMessage((int) (downloadLength*100/fileLength));
                }
                fos.close();
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mConnection != null) {
                mConnection.disconnect();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        // 设置文件下载后的保存路径
        destFile = new File(getCacheDir() + File.separator + "IntentService.png");
    }

    /**
     * You should not override this method for your IntentService. Instead,
     * override {@link #onHandleIntent}, which the system calls when the IntentService
     * receives a start request.
     *
     * @param intent
     * @param flags
     * @param startId
     * @see Service#onStartCommand
     */
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if (destFile.exists()) {
            destFile.delete();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
