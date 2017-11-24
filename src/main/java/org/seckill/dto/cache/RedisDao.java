package org.seckill.dto.cache;


import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by LSM on 2017/11/24.
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    //连接redis方法
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    //在redis中获取Seckill对象
    public Seckill getSeckill(Long seckillId) {
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //redis并没有内部实现序列化操作
                //get -> byte[] -> 反序列化 -> Object(Seckill)
                //使用protostuff进行序列化操作，操作的pojo是一个正常拥有get，set的对象
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    //说明在缓存中找到该对象
                    //创建Seckill空对象
                    Seckill seckill = schema.newMessage();
                    //把bytes的数据赋值给seckill对象
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    //seckill被反序列化返回
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    //Seckill对象没有在redis中，查找db把数据加到redis
    public String putSeckill(Seckill seckill) {
        //set Object(Seckill) -> 序列化 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                //缓存器 LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;
                //成功返回ok，出错返回出错信息
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
