package com.wenjian.msg;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Description: MessageQueue
 * Date: 2018/11/12
 *
 * @author jian.wen@ubtrobot.com
 */
public class MessageQueue {

    private final PriorityBlockingQueue<Message> queue;

    private volatile boolean quitting = false;

    private final boolean allowQuit;

    MessageQueue(boolean allowQuit) {
        this.allowQuit = allowQuit;
        this.queue = new PriorityBlockingQueue<>(11, Comparator.comparingLong(o -> o.when));
    }

    public void enqueueMessage(Message message) {
        if (quitting) {
            throw new IllegalStateException("queue already quit");
        }
        queue.offer(message);
        synchronized (this) {
            notifyAll();
        }
    }

    public Message next() {
        if (quitting) {
            return null;
        }

        long nextPollTimeoutMillis = 0;
        for (; ; ) {
            synchronized (this) {
                try {
                    wait(nextPollTimeoutMillis > 0 ? nextPollTimeoutMillis : Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long now = System.currentTimeMillis();

            final Message msg = queue.peek();
            if (msg == null) {
                nextPollTimeoutMillis = -1;
            } else {
                long when = msg.when;
                if (when > now) {
                    nextPollTimeoutMillis = Math.min(when - now, Integer.MAX_VALUE);
                } else {
                    return queue.poll();
                }
            }


            if (quitting) {
                return null;
            }
        }
    }


    public void quit() {
        if (!allowQuit) {
            throw new UnsupportedOperationException("cant quit this queue");
        }
        if (quitting) {
            return;
        }
        quitting = true;
        queue.clear();
        synchronized (this) {
            notifyAll();
        }
    }
}
