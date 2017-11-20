package com.dayang.dbx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vincent on 2017/11/22 0022.
 */
public class ObjectWaiter
{
    private final static Object waitLocker = new Object();

    public static void main(String[] args) throws InterruptedException {
      run();
    }

    public static void run()throws InterruptedException {
        ExecutorService tPools = Executors.newCachedThreadPool();

        for(int i=0;i<10;i++){
            Runnable run = new Thread(){
                public void run(){
                    synchronized (waitLocker) {
                        try {
                            System.out.println(Thread.currentThread().getId() + " release monitor.\n");
                            waitLocker.wait();
                            TimeUnit.SECONDS.sleep(2);
                            System.out.println(Thread.currentThread().getId() + " wait 2s.\n");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            tPools.execute(run);
        }

        TimeUnit.SECONDS.sleep(1);
        tPools.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (waitLocker){
                    waitLocker.notifyAll();
                }
            }
        });
    }
}
