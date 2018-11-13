package com.wenjian.msg;

/**
 * Description: Message
 * Date: 2018/11/12
 *
 * @author jian.wen@ubtrobot.com
 */
public class Message {
    int what;
    long when;

    Object object;
    //下一个
    Message next;

    Runnable callback;

    boolean inUse;

    Handler target;

    private static final Object poolLock = new Object();

    private static Message sPool;

    private static int size;

    private static final int MAX_SIZE = 50;

    @Override
    public String toString() {
        return "Message{" +
                "what=" + what +
                ", object=" + object +
                ", next=" + next +
                ", callback=" + callback +
                ", when=" + when +
                ", inUse=" + inUse +
                ", target=" + target +
                '}';
    }

    public static Message obtain() {
        synchronized (poolLock) {
            Message p = Message.sPool;
            if (p != null) {
                sPool = p.next;
                p.next = null;
                size--;
                return p;
            }
        }
        return new Message();
    }


    public void recycle() {
     /*   if (inUse) {
            throw new IllegalStateException("");
        }*/

        inUse = false;
        object = null;
        target = null;
        callback = null;
        next = null;
        what = 0;
        when = 0;

        synchronized (poolLock) {
            if (size < MAX_SIZE) {
                next = sPool;
                sPool = this;
                size++;
            }
        }
    }

}
