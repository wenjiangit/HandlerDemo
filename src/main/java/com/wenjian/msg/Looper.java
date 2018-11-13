package com.wenjian.msg;

/**
 * Description: Looper
 * Date: 2018/11/12
 *
 * @author jian.wen@ubtrobot.com
 */
public class Looper {

    private final MessageQueue queue;

    private static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();

    public Looper() {
        this.queue = new MessageQueue(true);
    }


    public static void loop() {
        Looper looper = myLooper();
        MessageQueue queue = looper.queue;
        for (; ; ) {
            Message message = queue.next();
            if (message == null) {
                //退出了
                System.out.println("loop 退出循环");
                return;
            }

            message.inUse = true;
            message.target.dispatchMessage(message);
            message.recycle();
        }
    }


    public static void prepare() {
        Looper looper = myLooper();
        if (looper != null) {
            throw new IllegalStateException("looper has initial");
        }
        sThreadLocal.set(new Looper());
    }


    public static Looper myLooper() {
        return sThreadLocal.get();
    }


    public MessageQueue myQueue() {
        return queue;
    }


}
