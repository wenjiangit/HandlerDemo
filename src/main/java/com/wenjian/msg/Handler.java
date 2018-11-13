package com.wenjian.msg;

/**
 * Description: Handler
 * Date: 2018/11/12
 *
 * @author jian.wen@ubtrobot.com
 */
public class Handler {

    private final Looper looper;

    public Handler(Looper looper) {
        this.looper = looper;
    }

    public Handler() {
        Looper looper = Looper.myLooper();
        if (looper == null) {
            throw new RuntimeException("please call Loop.prepare first");
        }
        this.looper = looper;
    }

    public void sendMessage(Message message) {
        sendMessageAtTime(message, System.currentTimeMillis());
    }

    public void sendEmptyMessage(int what) {
        Message message = Message.obtain();
        message.what = what;
        sendMessageAtTime(message, System.currentTimeMillis());
    }


    private void sendMessageAtTime(Message message, long time) {
        message.when = time;
        message.target = this;
        looper.myQueue().enqueueMessage(message);
    }

    public void post(Runnable runnable) {
        this.postDelay(runnable, 0);
    }

    public void postDelay(Runnable runnable,long delay) {
        Message message = Message.obtain();
        message.callback = runnable;
        sendMessageAtTime(message, System.currentTimeMillis() + delay);
    }


    public void dispatchMessage(Message message) {
        if (message.callback != null) {
            message.callback.run();
        } else {
            handleMessage(message);
        }
    }


    public void handleMessage(Message msg) {

    }

}
