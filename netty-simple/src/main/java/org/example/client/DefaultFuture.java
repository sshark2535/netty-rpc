package org.example.client;


import org.example.utill.Request;
import org.example.utill.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    public static ConcurrentHashMap<Long,DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long,DefaultFuture>();
    final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Response response;
    public DefaultFuture(Request request){
        allDefaultFuture.put(request.getId(), this);
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
}
