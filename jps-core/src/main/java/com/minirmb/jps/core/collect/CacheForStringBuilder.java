package com.minirmb.jps.core.collect;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用CacheForStringBuffer是为了防止创建太多的StringBuilder对象。减轻gc的压力
 *
 *
 */
public class CacheForStringBuilder {
    private static Queue<StringBuilder> cache = new ConcurrentLinkedQueue<>();

    private static ReentrantLock lock = new ReentrantLock() ;
    public static void put(StringBuilder ele){
        ele.delete(0,ele.length());
        cache.offer(ele);
    }

    public static StringBuilder get(){
        //StringBuilder result = null;
        lock.lock();
        StringBuilder result = cache.size() > 0 ? cache.poll() : new StringBuilder();
        lock.unlock();
        return result;
    }
}
