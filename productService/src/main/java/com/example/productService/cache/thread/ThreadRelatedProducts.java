package com.example.productService.cache.thread;

import com.example.productService.service.ProductService;
import com.example.productService.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.example.productService.cache.CacheProduct.*;


@RequiredArgsConstructor
@Slf4j
public class ThreadRelatedProducts implements Runnable{

    private final RedisCacheService redisService;
    private final ProductService productService;

    @Override
    public void run() {
        try {
            var start = System.currentTimeMillis();
            log.info("start thread ... " + Thread.currentThread().getId());
            while (true) {
                if (redisService.checkExistsKey(keyCategory)) {
                    var product = redisService.getAfterDeleteListProduct(keyCategory);
                    redisService.setValue(keyCategoryDetail + product.getId(), productService.getProductRelated(product).toString());
                } else {
                    Thread.sleep(100);
                }
//                log.info(Thread.currentThread().getId() + " finished! - Time finished: " + (System.currentTimeMillis() - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
