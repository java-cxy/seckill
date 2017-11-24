package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * Created by LSM on 2017/11/22.
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test，junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        Date killedTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killedTime);
        System.out.println(updateCount);
        /**
         21:32:01.323 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@4fdfa676] will not be managed by Spring
         21:32:01.354 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==>  Preparing: UPDATE seckill SET number = number - 1 WHERE seckill_id = ? AND start_time <= ? AND end_time >= ? AND number > 0;
         21:32:01.417 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2017-11-22 21:32:00.505(Timestamp), 2017-11-22 21:32:00.505(Timestamp)
         21:32:01.421 [main] DEBUG o.s.dao.SeckillDao.reduceNumber - <==    Updates: 0
         21:32:01.422 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@5dda6f9]
         0
         */

    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         1000元秒杀iphone6
         Seckill{
         seckillId=1000,
         name='1000元秒杀iphone6',
         nunber=0,
         startTime=Wed Nov 22 00:00:00 CST 2017,
         endTime=Wed Nov 22 00:00:00 CST 2017,
         createTime=Wed Nov 22 17:28:35 CST 2017
         }
         **/
    }

    @Test
    public void queryAll() throws Exception {
        /**
         * java没有保存形参的记录：List<Seckill> queryAll(int offset, int limit) -> List<Seckill> queryAll(arg0, arg1);
         * 多个参数时需要加注解@Param("")
         */
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill:seckills) {
            System.out.println(seckill);
        }
        /**
         Seckill{seckillId=1000, name='1000元秒杀iphone6', nunber=0, startTime=Wed Nov 22 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}
         Seckill{seckillId=1001, name='500元秒杀ipad2', nunber=0, startTime=Wed Nov 22 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}
         Seckill{seckillId=1002, name='300元秒杀小米4', nunber=0, startTime=Wed Nov 22 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}
         Seckill{seckillId=1003, name='200元秒杀红米note', nunber=0, startTime=Wed Nov 22 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}
         */
    }

}