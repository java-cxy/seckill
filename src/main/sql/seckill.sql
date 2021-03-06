# 秒杀执行存储过程
# console ; 转换为 $$（换行符转换）
DELIMITER $$;
# 定义存储过程
# 参数 in：输入参数  out：输出参数
# row_count()函数：返回上一条修改类型sql（delete，insert，update）的影响行数
# row_count()返回值解释： 0：未修改数据  >0：表示修改的行数   <0：表示sql错误/未执行修改sql
CREATE PROCEDURE execute_seckill
  (IN v_seckill_id BIGINT, IN v_phone BIGINT,
   IN v_kill_time TIMESTAMP, OUT r_result INT)
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed
    (seckill_id, user_phone, create_time)
    VALUES (v_seckill_id, v_phone, v_kill_time);
    SELECT row_count()
    INTO insert_count;
    IF (insert_count = 0)
    THEN
      #说明没有修改数据：返回-1（重复秒杀）
      ROLLBACK;
      SET r_result = -1;
    ELSEIF (insert_count < 0)
      THEN
        #sql语句执行出错，返回-2（数据异常）
        ROLLBACK;
        SET r_result = -2;
    ELSE
      UPDATE seckill
      SET number = number - 1
      WHERE seckill_id = v_seckill_id
            AND end_time > v_kill_time
            AND start_time < v_kill_time
            AND number > 0;
      SELECT row_count()
      INTO insert_count;
      IF (insert_count = 0)
      THEN
        ROLLBACK;
        SET r_result = 0;
      ELSEIF (insert_count < 0)
        THEN
          ROLLBACK;
          SET r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END;
$$;

# 存储过程定义结束

DELIMITER ;

# 设置一个值用于测试存储过程
SET @r_result = -3;

# 执行存储过程
CALL execute_seckill(1000,15243695874,now(),@r_result);

# 获取测试结果
SELECT @r_result;

# 存储过程
# 1：存储过程优化行级锁所持有的时间
# 2：不要过度依赖存储过程
# 3：简单的逻辑可以应用存储过程
# 4：QPS对一个秒杀商品达到6000/qps