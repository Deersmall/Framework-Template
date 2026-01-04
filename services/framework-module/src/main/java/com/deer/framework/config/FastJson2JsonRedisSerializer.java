package com.deer.framework.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Redis使用FastJson序列化
 * 
 * @author soar
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz)
    {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException
    {
        if (t == null) {
            return new byte[0];
        }

        try {
            return JSON.toJSONString(t,
                    JSONWriter.Feature.WriteClassName,          // 写入类名（可选）
                    JSONWriter.Feature.PrettyFormat             // 格式化输出，使JSON可读
                    ).getBytes(DEFAULT_CHARSET);
        }catch (Exception ex){
            throw new SerializationException("无法进行序列化： " + ex.getMessage(), ex);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException
    {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        try {
            String str = new String(bytes, DEFAULT_CHARSET);
            return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
        }catch (Exception ex){
            throw new SerializationException("无法反序列化： " + ex.getMessage(), ex);
        }
    }
}
