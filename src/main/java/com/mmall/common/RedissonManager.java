package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Jane on 2018/1/31.
 */
@Component
@Slf4j
public class RedissonManager {
    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    private static String redis_1_Ip = PropertiesUtil.getProperty(Const.REDIS_SERVER_1_IP);
    private static Integer redis_1_Port = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_SERVER_1_PORT));
    private static String redis_2_Ip = PropertiesUtil.getProperty(Const.REDIS_SERVER_2_IP);
    private static Integer redis_2_Port = Integer.parseInt(PropertiesUtil.getProperty(Const.REDIS_SERVER_2_PORT));

    @PostConstruct
    private void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redis_1_Ip).append(":").append(redis_1_Port).toString());

            redisson = (Redisson)Redisson.create(config);
            log.info("初始化Redisson success");
        } catch (Exception e) {
            log.error("初始化Redisson error");
        }

    }


}
