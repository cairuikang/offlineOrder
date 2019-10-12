package com.jhlc.offlineorder.core.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Created by xiexiaoqing1 on 2017/3/8.
 */
public class HaKeyRedisSerializer implements RedisSerializer {

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        try {
            return obj == null?null:String.valueOf(obj).getBytes("UTF-8");
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try {
            return bytes == null?null:new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
