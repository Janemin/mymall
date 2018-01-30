package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Jane on 2018/1/26.
 */
public class RedisPool {
    //jedis连接池
    private static JedisPool pool;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_MAX_TOTAL, Const.DEFAULT_REDIS_MAX_TOTAL));
    //连接池中最大空闲Jedis实例个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_MAX_IDLE, Const.DEFAULT_REDIS_MAX_IDLE));
    //连接池中最小空闲Jedis实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_MIN_IDLE, Const.DEFAULT_REDIS_MIN_IDLE));
    //borrow Jedis实例时，是否进行验证操作，赋值为true，则得到的Jedis实例可用
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty(Const.REDIS_TEST_BORROW, Const.DEFAULT_REDIS_TEST_BORROW));
    //return Jedis实例时，是否进行验证操作，赋值为true，则放回连接池的Jedis实例可用
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty(Const.REDIS_TEST_RETURN, Const.DEFAULT_REDIS_TEST_RETURN));

    private static String redisIp = PropertiesUtil.getProperty(Const.REDIS_SERVER_1_IP);

    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_SERVER_1_PORT));

    private static Integer redisTimeout = 1000*2;

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽时，是否阻塞，false会抛出异常，true阻塞直到超时
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, redisTimeout);
    }

    static{
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis){
            pool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis){
            pool.returnBrokenResource(jedis);
    }
}
