package com.example.productService;

import com.example.productService.cache.thread.ThreadCacheProductAll;
import com.example.productService.cache.thread.ThreadRelatedProducts;
import com.example.productService.service.ProductService;
import com.example.productService.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ProductServiceApplication {
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private ProductService productService;

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	@Bean
	public Integer init() {
		ExecutorService executorService = Executors.newFixedThreadPool(6);
		// cache product
		for (int i=0; i<3; i++) {
			ThreadCacheProductAll process =  new ThreadCacheProductAll(redisCacheService);
			executorService.execute(process);
		}

		for (int i=0; i<3; i++) {
			ThreadRelatedProducts process =  new ThreadRelatedProducts(redisCacheService,productService);
			executorService.execute(process);
		}
		return 1;
	}

}
