package com.mmall.common;

import com.google.common.collect.Lists;
import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.List;

/**
 * Created by Jane on 2018/1/29.
 */
public class RedisShardedPool {

    //sharded jedis连接池
    private static ShardedJedisPool pool;
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

    private static String redis_1_Ip = PropertiesUtil.getProperty(Const.REDIS_SERVER_1_IP);

    private static Integer redis_1_Port = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_SERVER_1_PORT));

    private static String redis_2_Ip = PropertiesUtil.getProperty(Const.REDIS_SERVER_2_IP);

    private static Integer redis_2_Port = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_SERVER_2_PORT));

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

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redis_1_Ip, redis_1_Port, redisTimeout);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redis_2_Ip, redis_2_Port, redisTimeout);
        List<JedisShardInfo> jedisShardInfoList = Lists.newArrayList();
        jedisShardInfoList.add(jedisShardInfo1);
        jedisShardInfoList.add(jedisShardInfo2);

        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static{
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis shardedJedis){
        pool.returnResource(shardedJedis);
    }

    public static void returnBrokenResource(ShardedJedis shardedJedis){
        pool.returnBrokenResource(shardedJedis);
    }



}
