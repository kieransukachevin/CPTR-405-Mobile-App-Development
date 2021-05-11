package com.zybooks.asynctaskdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mDownloadProgressBar;
    private TextView mSummaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDownloadProgressBar = findViewById(R.id.progressBar);
        mSummaryTextView = findViewById(R.id.summaryTextView);

        DownloadUrlsTask task = new DownloadUrlsTask(this);
        task.execute("https://google.com/", "https://wikipedia.org/", "http://mit.edu/");
    }

    private boolean downloadUrl(String url) {
        try {
            // Put thread to sleep for one second
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // Ignore
        }

        return true;
    }

    private static class DownloadUrlsTask extends AsyncTask<String, Integer, Integer> {

        private final WeakReference<MainActivity> mActivity;

        public DownloadUrlsTask(MainActivity context) {
            mActivity = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mActivity.get().mDownloadProgressBar.setProgress(0);
        }

        @Override
        protected Integer doInBackground(String... urls) {
            int downloadSuccess = 0;
            for (int i = 0; i < urls.length; i++) {
                MainActivity mainActivity = mActivity.get();
                if (mainActivity == null || mainActivity.isFinishing()) return downloadSuccess;

                if (mainActivity.downloadUrl(urls[i])) {
                    downloadSuccess++;
                }
                publishProgress((i + 1) * 100 / urls.length);
            }
            return downloadSuccess;
        }

        protected void onProgressUpdate(Integer... progress) {
            MainActivity mainActivity = mActivity.get();
            if (mainActivity == null || mainActivity.isFinishing()) return;

            mainActivity.mDownloadProgressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Integer numDownloads) {
            MainActivity mainActivity = mActivity.get();
            if (mainActivity == null || mainActivity.isFinishing()) return;

            mainActivity.mSummaryTextView.setText("Downloaded " + numDownloads + " URLs");
        }
    }
}