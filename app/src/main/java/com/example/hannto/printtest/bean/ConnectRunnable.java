package com.example.hannto.printtest.bean;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class ConnectRunnable implements Runnable {
    private int finishSize=0;
    private String DEBUG_TAG = "ConnectRunnable";
    private static ScheduledExecutorService pool;// 线程池对象
    public ConnectRunnable(){
    }
    @Override
    public void run() {
        if(pool==null){
            pool = Executors.newScheduledThreadPool(20);
        }
        for (int i = 2; i < 255; ++i) {
            final int index = i;
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String ipaddress = "192.168.1." + index;
                        NetPrinter printer = new NetPrinter();
                        printer.Open(ipaddress, NetPrinter.POS_OPEN_NETPORT);
                        if (printer.IFOpen) {
                            Log.e(DEBUG_TAG, "connect success" + ipaddress);

                        } else {
                            Log.d(DEBUG_TAG, "connect fail" + ipaddress);
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                    } finally {
                        synchronized (this) {
                            finishSize++;
                            Log.i(DEBUG_TAG, "finishSize:" + finishSize);
                        }
                    }
                }
            });
        }
//        while (true) {
//            synchronized (this) {
//                Log.e(DEBUG_TAG, "finishSize:" + finishSize);
//                if (finishSize == 253) {
////                    isFinish=true;
////                    lastFinishTime=System.currentTimeMillis();
//
//                    Log.e(DEBUG_TAG, "finishSize:" + finishSize);
//                }
//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
