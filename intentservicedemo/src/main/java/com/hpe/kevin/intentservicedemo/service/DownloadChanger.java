package com.hpe.kevin.intentservicedemo.service;

import java.util.Observable;

// 被观察者类
public class DownloadChanger extends Observable {
    private static DownloadChanger mInstance;

    /**
     * Construct an Observable with zero Observers.
     */
    public DownloadChanger() {
        super();
    }

    public static DownloadChanger getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadChanger();
        }
        return mInstance;
    }

    public void setPostChange(int progress) {
        // 调用方法通知观察者去获取更新数据
        setChanged();
        notifyObservers(progress);
    }
}
