package com.song.client.core;

import com.song.client.param.ClientRequest;
import com.song.client.param.Response;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public static ConcurrentHashMap<Long,DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long,DefaultFuture>();
    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Response response;

    private long timeout = 2;
    private long startTime = System.currentTimeMillis();
    public DefaultFuture(ClientRequest request){
        allDefaultFuture.put(request.getId(), this);
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getStartTime() {
        return startTime;
    }


    public Response get(){
        lock.lock();
        try{
            while(!done()){
                condition.await();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return response;
    }
    public Response get(long time){
        this.setTimeout(time);
        lock.lock();
        try{
            while(!done()){
                condition.await(time, TimeUnit.SECONDS);
                if((System.currentTimeMillis()-startTime)>time){
                    System.out.println("----------------------请求超时----------------------");
                    break;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return response;
    }

    public static void receive(Response response){
        long resId = response.getId();
        DefaultFuture df = allDefaultFuture.get(resId);
        if(df != null){
            Lock lock = df.lock;
            lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                allDefaultFuture.remove(resId);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }
    }

    public boolean done() {
        if(this.response!=null)
            return true;
        else
            return false;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // 另起一个线程，对allDefaultFuture进行遍历，把超时的移除掉
    static class FutureThread extends Thread{
        public void run(){
            Set<Long> ids = allDefaultFuture.keySet();
            for(Long id:ids){
                DefaultFuture df = allDefaultFuture.get(id);
                if(df == null){
                    allDefaultFuture.remove(id);
                } else if (df.timeout<System.currentTimeMillis()-df.getStartTime()) {
                    Response response = new Response();
                    response.setId(id);
                    response.setCode("33333");
                    response.setContent("请求超时");
                    receive(response);// 如果超时直接给自己response
                }
            }
        }

    }
    static{
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }
}
