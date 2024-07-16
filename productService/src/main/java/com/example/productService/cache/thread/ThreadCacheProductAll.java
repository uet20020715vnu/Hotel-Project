package com.example.productService.cache.thread;

import com.example.productService.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import static com.example.productService.cache.CacheProduct.keyProductDetails;
import static com.example.productService.cache.CacheProduct.keyQueueProduct;


@RequiredArgsConstructor
@Slf4j
public class ThreadCacheProductAll implements Runnable{

    private final RedisCacheService redisService;

    @Override
    public void run() {
        try {
            var start = System.currentTimeMillis();
        log.info("start thread ... " + Thread.currentThread().getId());
        while (true) {
            if (redisService.checkExistsKey(keyQueueProduct)) {
                var product = redisService.getAfterDeleteListProduct(keyQueueProduct);
                redisService.setValue(keyProductDetails + product.getId(), product);
            } else {
                Thread.sleep(100);
            }
//            log.info(Thread.currentThread().getId() + " finished! - Time finished: " + (System.currentTimeMillis() - start));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
