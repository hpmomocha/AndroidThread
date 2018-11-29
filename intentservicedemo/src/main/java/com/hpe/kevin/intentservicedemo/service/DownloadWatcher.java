package com.hpe.kevin.intentservicedemo.service;

import java.util.Observable;
import java.util.Observer;

// 观察者类
public abstract class DownloadWatcher implements Observer {
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            int progress = Integer.parseInt(arg.toString());
            notifyUpdate(progress);
        }
    }

    public abstract void notifyUpdate(int progress);
}
