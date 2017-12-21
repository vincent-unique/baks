package com.dayang.dbx;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vincent on 2017/12/21 0021.
 */
public class ThreadLoop {

    private Thread worker;

    public void loop(){
        while (true){
            if(this.worker==null){
                System.out.println("The worker null and will be rebuild.");
                this.worker = new CoreTask();
                this.worker.start();
            }else if(!this.worker.isAlive()){
                this.worker.start();
            }
        }
    }

    public class CoreTask extends Thread{

        private final AtomicInteger counter = new AtomicInteger(0);
        public void run(){
            while (true){
                System.out.println("Counter:"+counter.get());
                if(counter.getAndIncrement()/5==0){
                    throw new RuntimeException("Thread RuntimeException.");
                }else {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new ThreadLoop().loop();
    }
}
