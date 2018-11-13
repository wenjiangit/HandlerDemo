package com.wenjian.msg;

/**
 * Description: HandlerThread
 * Date: 2018/11/13
 *
 * @author jian.wen@ubtrobot.com
 */
public class HandlerThread extends Thread {

    public HandlerThread() {
        super("HandlerThread");
    }

    private Looper looper;

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        synchronized (this) {
            looper = Looper.myLooper();
            notifyAll();
        }
        Looper.loop();
    }


    public Looper getLooper() {
        synchronized (this) {
            while (looper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return looper;
    }


    public void quit() {
        looper.myQueue().quit();
    }
}
