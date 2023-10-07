package com.lwd.anchor;

import android.os.Handler;
import android.os.Looper;

/**
 * @author: liweidong
 * @data: 2023/9/27
 */
public class ThreadUtils {

    public static boolean isMainThread(){
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void runOnUiThread(Runnable r){
        if (isMainThread()){
            r.run();
        }else{
            LazyHolder.sUiThreadHander.post(r);
        }
    }

    public static void runOnUiThreadAtFront(Runnable r){
        if (isMainThread()){
            r.run();
        }else{
            LazyHolder.sUiThreadHander.postAtFrontOfQueue(r);
        }
    }

    public static void removeCallbacks(Runnable r){
        if (r != null){
            LazyHolder.sUiThreadHander.removeCallbacks(r);
        }
    }

    public static void checkAtMainThread(){
        LazyHolder.sUiThreadHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("not main thread");
            }
        }, 500);
    }

    public static class LazyHolder {
        public static final Handler sUiThreadHander = new Handler(Looper.getMainLooper());
    }

}
