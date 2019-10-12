package com.jhlc.offlineorder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 并发测试
 */
public class Test {
    static int poolnum = 1000;
//    static HttpClientUtil httpClientUtils = new HttpClientUtil();
    static CyclicBarrier c = new CyclicBarrier(poolnum);
    final static CountDownLatch latch = new CountDownLatch(poolnum);

    public static void main(String[] args) {
        ExecutorService newCachedThreadPool = Executors.newFixedThreadPool(poolnum);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < poolnum; i++) {
            LatchDemo latchDemo = new LatchDemo(latch, i + 1);
            newCachedThreadPool.execute(latchDemo);
        }

        try {
            //多线程运行结束前一直等待
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println("耗费时间：" + (end - begin));


    }

    static class LatchDemo implements Runnable {

        private CountDownLatch latch;
        private int num;

        public LatchDemo(CountDownLatch latch, int i) {
            this.latch = latch;
            num = i;
        }
        private static int i=0;
        @Override
        public void run() {

            try {
                System.out.println("线程准备完毕" + num);
                c.await();
//                httpClientUtils.postHttp("http://118.190.78.29:8082/test/async/d/feiyanshi?orderId=382880", null, null, null);
//                httpClientUtils.postHttp("http://118.190.78.29:8082/test/async/c/feiyanshi?orderId=382880", null, null, null);
//                httpClientUtils.postHttp("http://118.190.78.29:8082/test/feiyanshi?orderId=382880", null, null, null);
//                  httpClientUtils.postHttp("http://118.190.78.29:8082/test/async/d/yanshi?orderId=382880", null, null, null);
//                httpClientUtils.postHttp("http://118.190.78.29:8082/test/async/c/yanshi?orderId=382880", null, null, null);
//                httpClientUtils.postHttp("http://118.190.78.29:8082/test/yanshi?orderId=382880", null, null, null);
                i++;
                System.out.println("成功第"+i+"条");
            } catch (Exception e) {
                e.printStackTrace();
            }  finally {
                //保证肯定执行
                latch.countDown();
            }
        }
    }
}
