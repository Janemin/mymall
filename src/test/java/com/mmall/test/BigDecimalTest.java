package com.mmall.test;

import com.mmall.vo.CartProductVo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by Jane on 2018/1/20.
 */
public class BigDecimalTest {

    @Test
    public void test1(){
        BigDecimal b1 = new BigDecimal(0.05);
        BigDecimal b2 = new BigDecimal(0.01);
        System.out.println(b1.add(b2));
        //0.06000000000000000298372437868010820238851010799407958984375

        BigDecimal b3 = new BigDecimal("0.05");
        BigDecimal b4 = new BigDecimal("0.01");
        System.out.println(b3.add(b4));
        //0.06


    }
}
