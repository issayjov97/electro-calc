//package com.example.application.service;
//
//import com.example.application.persistence.entity.PatternEntity;
//import com.example.application.persistence.repository.PatternRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class MyRunner implements CommandLineRunner {
//
//    private static final Logger logger = LoggerFactory.getLogger(MyRunner.class);
//
//    private final PatternRepository patternRepository;
//    private final CacheManager      cacheManager;
//
//    public MyRunner(PatternRepository patternRepository, CacheManager cacheManager) {
//        this.patternRepository = patternRepository;
//        this.cacheManager = cacheManager;
//    }
//
////    @Override
////    public void run(String... args) throws Exception {
////        System.out.println("Helowewewe");
////         patternRepository.findDefaultPattern().forEach(it->{
////             logger.info("{}", it.getName());
////             cacheManager.getCache("patterns").put(it.getId().toString(), it);
////         });
////    }
//}