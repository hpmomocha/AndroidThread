package com.hpe.kevin.androidthread.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PictureDownloadTask extends AsyncTask<String,String,Bitmap> {
    //上下文对象
    private Context context;
    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView textView;

    public PictureDownloadTask(Context context, ImageView imageView, ProgressBar progressBar, TextView textView) {
        this.context = context;
        this.imageView = imageView;
        this.progressBar = progressBar;
        this.textView = textView;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param strings The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Bitmap doInBackground(String... strings) {
        publishProgress("正在下载图片");
        return getBitmapFromUrl(strings[0]);
    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        textView.setText("异步开始");
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     *
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param bitmap The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        textView.setText("下载结束");
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        textView.setText(values[0]);
    }

    private Bitmap getBitmapFromUrl(String urlString)  {
//        从Android6.0开始，谷歌不推荐再使用HttpClient
        //网络请求：
        /*创建HttpClient的实例*/
        HttpClient httpClient = new DefaultHttpClient();
        HttpHost proxy = new HttpHost("proxy.houston.hp.com",8080, null);
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        /*创建连接方法的实例，HttpGet()的构造中传入url地址*/
        HttpGet httpGet = new HttpGet(urlString);
        try{
            /*调用创建好的HttpClient的实例的execute方法来发送创建好的HttpGet或HttpPost请求，并返回HttpResponse对象*/
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                /*返回实体对象*/
                HttpEntity entity = httpResponse.getEntity();
                byte [] data = EntityUtils.toByteArray(entity);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                return bitmap;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }
}
