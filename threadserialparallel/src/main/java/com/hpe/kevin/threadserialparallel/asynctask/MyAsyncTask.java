package com.hpe.kevin.threadserialparallel.asynctask;

import android.os.AsyncTask;
import android.widget.TextView;

import com.hpe.kevin.threadserialparallel.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hpe.kevin.threadserialparallel.MainActivity.logContent;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private String mName = "AsyncTask";
    private TextView mTextView;

    public MyAsyncTask(String name, TextView textView) {
        this.mName = name;
        this.mTextView = textView;
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     *
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param s The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MainActivity.logContent.append(s + " execute finish at " + df.format(new Date()) + "\n");
        mTextView.setText(MainActivity.logContent);
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
    protected String doInBackground(String... strings) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mName;
    }
}
