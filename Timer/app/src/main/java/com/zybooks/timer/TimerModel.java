package com.zybooks.timer;

import android.os.SystemClock;
import java.util.Locale;

public class TimerModel {

    private long mTargetTime;
    private long mTimeLeft;
    private boolean mRunning;
    private long mDurationMillis;

    public TimerModel() {
        mRunning = false;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void start(long millisLeft) {
        mDurationMillis = millisLeft;
        mTargetTime = SystemClock.uptimeMillis() + mDurationMillis;
        mRunning = true;
    }

    public void start(int hours, int minutes, int seconds) {
        // Add 1 sec to duration so timer stays on current second longer
        mDurationMillis = (hours * 60 * 60 + minutes * 60 + seconds + 1) * 1000;
        mTargetTime = SystemClock.uptimeMillis() + mDurationMillis;

        mRunning = true;
    }

    public void stop() {
        mRunning = false;
    }

    public void pause() {
        mTimeLeft = mTargetTime - SystemClock.uptimeMillis();
        mRunning = false;
    }

    public void resume() {
        mTargetTime = SystemClock.uptimeMillis() + mTimeLeft;
        mRunning = true;
    }

    public long getRemainingMilliseconds() {
        if (mRunning) {
            return Math.max(0, mTargetTime - SystemClock.uptimeMillis());
        }
        return 0;
    }

    public int getRemainingSeconds() {
        if (mRunning) {
            return (int) ((getRemainingMilliseconds() / 1000) % 60);
        }
        return 0;
    }

    public int getRemainingMinutes() {
        if (mRunning) {
            return (int) (((getRemainingMilliseconds() / 1000) / 60) % 60);
        }
        return 0;
    }

    public int getRemainingHours() {
        if (mRunning) {
            return (int) (((getRemainingMilliseconds() / 1000) / 60) / 60);
        }
        return 0;
    }

    public int getProgressPercent() {
        if (mDurationMillis != 1000) {
            return Math.min(100, 100 - (int) ((getRemainingMilliseconds() - 1000) * 100 /
                    (mDurationMillis - 1000)));
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", getRemainingHours(),
                getRemainingMinutes(), getRemainingSeconds());
    }
}