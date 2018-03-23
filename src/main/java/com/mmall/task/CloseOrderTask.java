package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jane on 2018/1/31.
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

    //关闭tomcat之前调用(如果方法内操作特别多，关闭tomcat的时候会特别长。如果直接kill tomcat的进程，此方法无效)
    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

   // @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1(){
        int hour =Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
       // iOrderService.closeOrder(hour);
    }

   //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2(){
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout" , "50000"));

        Long setnxResult = RedisShardedPoolUtil.setnx(
                Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult !=null && setnxResult.intValue() == 1){
            //返回值为1.代表设置成功，获取锁
            /*在未设置锁的有效期时，如果tomcat shutdown了  会造成死锁现象*/
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("未获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
    }

   // @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3(){
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout" , "50000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(
                Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult !=null && setnxResult.intValue() == 1){
            //返回值为1.代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            //未获取到锁，判断时间戳，是否可以重置并获取到锁

            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(
                        Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
                //再次用当前时间戳getset，返回给定的key的旧值， -》旧值判断，是否可以获取锁
                //当key没有旧值，即key不存在时，返回nil（jedis中返回null） -》 获取锁
                if(getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                    //获取到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else {
                    log.info("未获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else {
                log.info("未获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV4(){
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        try {
            getLock = lock.tryLock(0, 5, TimeUnit.SECONDS);
            if(getLock){
                log.info("Redisson获得分布式锁:{}，ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
                int hour =Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
                iOrderService.closeOrder(hour);
            }else{
                log.info("Redisson未获得分布式锁:{}，ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson 获得分布式锁 error", e);
        }finally {
            if(!getLock){
                return;
            }
            lock.unlock();
            log.info("Redisson获得分布式锁释放");
        }

    }

    private void closeOrder(String lockName){
        log.info("============================================================================");
        RedisShardedPoolUtil.expire(lockName, 5);//有效期五秒，防止死锁
        log.info("获取锁{}，ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        int hour =Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放锁{}，ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        log.info("============================================================================");
    }


}
