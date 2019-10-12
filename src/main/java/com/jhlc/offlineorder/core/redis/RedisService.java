package com.jhlc.offlineorder.core.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jhlc.offlineorder.conmmon.utils.CommonUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author staconfree
 * @date 2017/4/2
 */
@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, ?> redisTemplate;

    public boolean set(final String key, final String value) {
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                connection.set(serializer.serialize(key), serializer.serialize(value));
                return true;
            }
        });
        return result;
    }

    public String get(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }

    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public <T> boolean setList(String key, List<T> list) {
        String value = JSON.toJSONString(list, SerializerFeature.WriteMapNullValue);
        return set(key, value);
    }

    public <T> List<T> getList(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            List<T> list = JSON.parseArray(json, clz);
            return list;
        }
        return null;
    }

    public long lpush(final String key, Object obj) {
        final String value = JSON.toJSONString(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    public long rpush(final String key, Object obj) {
        final String value = JSONObject.toJSONString(obj);
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
                return count;
            }
        });
        return result;
    }

    public String lpop(final String key) {
        String result = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] res = connection.lPop(serializer.serialize(key));
                return serializer.deserialize(res);
            }
        });
        return result;
    }

    public Long del(final String key) {
        long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                long count = connection.del(serializer.serialize(key));
                return count;
            }
        });
        return result;
    }

    /**
     * 传入一个key值，判断其是否存在，时间长60
     *
     * @param key
     */
    public synchronized boolean isExistKey(String key) {
        String redisSign = get(key);
        if (CommonUtils.strIsNull(redisSign)) {
            return false;
        } else {
            set(key, "1");
            expire(key, 300);
            return true;
        }
    }

    /**
     *redis高版本支持
     */
    public boolean setIfAbsent(final String key, final Serializable value, final long exptime) {
        boolean b = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
            RedisSerializer keySerializer = redisTemplate.getKeySerializer();
            Object obj = connection.execute("set", keySerializer.serialize(key),
                    valueSerializer.serialize(value),
                    SafeEncoder.encode("NX"),
                    SafeEncoder.encode("EX"),
                    Protocol.toByteArray(exptime));
            return obj != null;
        });
        return b;
    }

    public boolean addZset(String key,String value,double a){
        ZSetOperations<String, String> vo = (ZSetOperations<String, String>) redisTemplate.opsForZSet();
        return vo.add(key,value,a);
    }
    public Set<String> getZsetAll(String key){
        ZSetOperations<String, String> vo = (ZSetOperations<String, String>) redisTemplate.opsForZSet();
        return vo.range(key,0,-1);
    }
    public long getZsetSize(String key){
        ZSetOperations<String, String> vo = (ZSetOperations<String, String>) redisTemplate.opsForZSet();
        return vo.zCard(key);
    }
    public void removeZset(String key,String... value){
        ZSetOperations<String, String> vo = (ZSetOperations<String, String>) redisTemplate.opsForZSet();
        vo.remove(key,value);
    }
    public void removeZsetForAll(String key){
        ZSetOperations<String, String> vo = (ZSetOperations<String, String>) redisTemplate.opsForZSet();
        vo.removeRange(key,0,-1);
    }
}
