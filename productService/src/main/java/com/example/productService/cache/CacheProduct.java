package com.example.productService.cache;


import com.example.productService.mapper.ProductMapper;
import com.example.productService.repository.CategoryRepository;
import com.example.productService.repository.ProductRepository;
import com.example.productService.service.RedisCacheService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class CacheProduct {

    @Autowired
    private ProductMapper productMapper;

    static int size = 10;
    public static String keyQueueProduct = "ProductService:queueProduct";
    public static String keyProductDetails = "ProductService:productDetails:";
    public static String keyCategory = "ProductService:queueRelatedProducts:";
    public static String keyCategoryDetail = "ProductService:relatedProducts:";
    private final ProductRepository productRepository;
    private final RedisCacheService redisService;
    private final CategoryRepository categoryRepository;


//    @Scheduled(cron = "0 0 16 * * ?")
//    @Scheduled(fixedRate = 2000)
//    @PostConstruct
    public void initCacheProductsAll(){
        log.info("Start...");
        int page = 0;
        while (true){
            var list = productRepository.findByDeletedAtIsNull(PageRequest.of(page,5)).getContent()
                    .stream().map(productMapper::toDTO).collect(Collectors.toList());
            if (list==null || list.isEmpty()){
                break;
            }
            redisService.setListProducts(keyQueueProduct, list);
            redisService.setListProducts(keyCategory,list);
            page+=1;
        }
        log.info("Cache initialization finished.");
    }
//
//    @PostConstruct
//    @Scheduled(fixedRate = 5000)
//    public void inintCacheRelatedProducts(){
//        log.info("Start...");
//            var listCategoryId = categoryRepository.getCategoryId();
//            listCategoryId.forEach(id ->
//                    redisService.setListCategoryId(keyCategory + id, productRepository.listProductIdByCategory(id))
//            );
//        log.info("Successfully...");
//    }


}
