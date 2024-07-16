package com.example.productService.service;

import com.example.productService.dto.ProductDTO;
import com.example.productService.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Service
public class RedisCacheService {
//    @Autowired
//    private RedisTemplate redisTemplate;

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ListOperations<String, Object> listOperations;
    private ObjectMapper objectMapper;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate,ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.listOperations = redisTemplate.opsForList();
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setTimout(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key,timeout, timeUnit);
    }

    public void setValueWithTimeout(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key,timeout, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setListProducts(String key, List<ProductDTO> list) {
        try {
            for (ProductDTO productDTO: list) {
                listOperations.leftPush(key, productDTO);
            }
//            redisTemplate.opsForList().leftPushAll(key,list);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkExistsKey(String key){
        boolean check = false;
        try {
            check = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }
    public void lPushAll(String key, List<String> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public void deleteKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting key from Redis", e);
        }
    }


    public <T> T getAfterDeleteListProduct(String key, Class<T> tClass) {
        try {
            if (checkExistsKey(key)){
                deleteKey(key);
            }
            var product = listOperations.rightPop(key, Duration.ofSeconds(5));
            return objectMapper.convertValue(product, tClass);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ProductDTO getAfterDeleteListProduct(String key) {
        try {
            Object obj = rPop(key);
            ProductDTO product = objectMapper.convertValue(obj,ProductDTO.class);
                return product ;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
