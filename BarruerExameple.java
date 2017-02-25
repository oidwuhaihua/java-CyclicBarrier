package com.bmkj.dealer.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by rebby on 2017/2/25.
 */
@SuppressWarnings("all")
public class BarruerExameple {
    public static  String[] locations ={"邵阳", "上海", "广州","深圳",
                                 "New York","Albany"};
    public static CyclicBarrier barrier;

    public static void main(String[] args) {
        Thread walker = new Thread(getRunnable("walker",5));
        Thread bus = new Thread(getRunnable("bus",4));
        Thread train = new Thread(getRunnable("train",3));
        Thread plane = new Thread(getRunnable("plane",2));
        barrier = new CyclicBarrier(4);
        walker.start();
        bus.start();
        train.start();
        plane.start();
    }


    public static Runnable getRunnable(final String type,final int secs){

        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread " + type + " starting trip");
                for (int i = 0 ;i<locations.length; i++){


                        try {
                            Thread.sleep(secs * 1000);//旅行个所需要的时间
                            System.out.println("Thread " + type + " 到达 : " + locations[i]);
                            System.out.println("Thread " + type + " 坐等其他朋友...");
                            barrier.await();
                            System.out.println("Thread " + type + " 一起离开: " + locations[i]);
                            }
                            catch(InterruptedException ie) {
                                System.out.println("Thread " + type + " interrupted");
                            } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }

                }
                System.out.println("Thread " + type + " finished trip!");
            }
        };

    }
}

