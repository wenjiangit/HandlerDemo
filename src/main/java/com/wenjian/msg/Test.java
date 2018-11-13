package com.wenjian.msg;

import java.io.IOException;

/**
 * Description: Test
 * Date: 2018/11/12
 *
 * @author jian.wen@ubtrobot.com
 */
public class Test {

    public static void main(String[] args) throws IOException {

        Looper.prepare();

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("收到消息: " + msg);
            }
        };

        EventThread eventThread = new EventThread(handler);
        eventThread.start();

        Looper.loop();

    }


    private static class EventThread extends Thread {

        private final Handler handler;

        private static int key;

        public EventThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            while (true) {

                if (key % 2 == 0) {
                    int temp = ++key;
                    handler.postDelay(new DelayTask(temp),20000);
                } else {
                    handler.sendEmptyMessage(++key);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class DelayTask implements Runnable {

        private int key;

        public DelayTask(int key) {
            this.key = key;
        }

        @Override
        public void run() {
            System.out.println("延时任务执行了," + key);
        }
    }


}
