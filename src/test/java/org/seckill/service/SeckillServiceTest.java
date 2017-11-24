package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by LSM on 2017/11/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    //slf4j日志输出
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        logger.info("seckills={}", seckills);
        /**
         * seckills=[Seckill{seckillId=1000, name='1000元秒杀iphone6', nunber=0, startTime=Tue Nov 21 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}, Seckill{seckillId=1001, name='500元秒杀ipad2', nunber=0, startTime=Tue Nov 21 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}, Seckill{seckillId=1002, name='300元秒杀小米4', nunber=0, startTime=Tue Nov 21 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}, Seckill{seckillId=1003, name='200元秒杀红米note', nunber=0, startTime=Tue Nov 21 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}]
         */
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
        /**
         *seckill=Seckill{seckillId=1000, name='1000元秒杀iphone6', nunber=0, startTime=Tue Nov 21 00:00:00 CST 2017, endTime=Wed Nov 22 00:00:00 CST 2017, createTime=Wed Nov 22 17:28:35 CST 2017}
         */
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
        /**
         *  exposer=Exposer{
         *  exposed=true,
         *  md5='6c69ad34cb4e68ba98a713fdc1797896',
         *  seckillId=1000, now=0, start=0, end=0}
         */
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000L;
        long userPhone = 13925648752L;
        String md5 = "6c69ad34cb4e68ba98a713fdc1797896";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
            logger.info("seckillExecution={}", seckillExecution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }

        /**
         * seckillExecution=SeckillExecution{
         * seckillId=1000, state=0, stateInfo='秒杀成功',
         *
         * successKilled=SuccessKilled{
         * seckillId=1000,
         * userPhone=13925648752,
         * state=0, createTime=Thu Nov 23 05:39:56 CST 2017}}
         */
    }

    //集成最后两个业务测试
    @Test
    public void testSeckillLogic() throws Exception {
        long id = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long userPhone = 13925648753L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
                logger.info("seckillExecution={}", seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开始
            logger.warn("exposer={}", exposer);
        }
    }

    /**
     * 测试存储过程来执行秒杀
     */
    @Test
    public void testExecuteSeckillProcedure() {
        long id = 1000L;
        long phone = 17254693824L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(id, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }

}