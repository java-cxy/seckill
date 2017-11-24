package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by LSM on 2017/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1001L;
        long phone = 18122711386L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println(insertCount);
        /**
         21:41:53.256 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@5be82d43] will not be managed by Spring
         21:41:53.293 [main] DEBUG o.s.d.S.insertSuccessKilled - ==>  Preparing: INSERT ignore INTO success_killed(seckill_id,user_phone) VALUES (?,?)
         21:41:53.345 [main] DEBUG o.s.d.S.insertSuccessKilled - ==> Parameters: 1000(Long), 18122711386(Long)
         21:41:53.352 [main] DEBUG o.s.d.S.insertSuccessKilled - <==    Updates: 1
         21:41:53.359 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@54afd745]
         1
         */
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1001L;
        long phone = 18122711386L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /**
         SuccessKilled{seckillId=1000, userPhone=18122711386, state=-1, createTime=Wed Nov 22 21:41:53 CST 2017}
         Seckill{seckillId=1000, name='1000元秒杀iphone6', nunber=0, startTime=Tue Nov 21 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}
         */
    }

}