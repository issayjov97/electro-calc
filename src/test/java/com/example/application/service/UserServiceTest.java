package com.example.application.service;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static com.example.application.Values.TESTER;
import static com.example.application.config.EmbeddedCacheConfig.FIRMS_CACHE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private CacheManager cacheManager;
    private UserEntity user;
    private FirmEntity firm;

    @BeforeEach
    void before() {
        this.user = new UserEntity();
        this.firm = new FirmEntity();
        var firmsCache = cacheManager.getCache(FIRMS_CACHE);
        if (firmsCache != null) {
            firmsCache.clear();
        }
    }

    @Test
    void shouldReturnCachedValue() {
        user.setUsername(TESTER);
        user.setFirmEntity(firm);
        firm.setName("TEST");
        Mockito.when(userRepository.findBriefUserByUsername(TESTER)).thenReturn(Optional.of(user));

        var result_1 = userService.getUserFirm(TESTER);
        var result_2 = userService.getUserFirm(TESTER);

        Mockito.verify(userRepository, Mockito.times(1)).findBriefUserByUsername(TESTER);
        assertEquals(result_1, result_2);
    }

    @Test
    void shouldReturnNewValueWhenCacheIsEmpty() {
        user.setUsername(TESTER);
        user.setFirmEntity(firm);
        firm.setName("TEST");
        Mockito.when(userRepository.findBriefUserByUsername(TESTER)).thenReturn(Optional.of(user));

        var result_1 = userService.getUserFirm(TESTER);
        cacheManager.getCache(FIRMS_CACHE).clear();

        var result_2 = userService.getUserFirm(TESTER);

        Mockito.verify(userRepository, Mockito.times(2)).findBriefUserByUsername(TESTER);
        assertEquals(result_1, result_2);
    }

    @Test
    @WithMockUser(TESTER)
    void shouldEvictCacheWhenUserEntitySaved() {
        user.setUsername(TESTER);
        user.setPassword("PASSWORD");
        user.setFirmEntity(firm);
        firm.setName("TEST");
        Mockito.when(userRepository.findBriefUserByUsername(TESTER)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        userService.getUserFirm(TESTER);
        assertFalse(((ConcurrentMapCache) cacheManager.getCache(FIRMS_CACHE)).getNativeCache().isEmpty());

        userService.save(user);
        assertTrue(((ConcurrentMapCache) cacheManager.getCache(FIRMS_CACHE)).getNativeCache().isEmpty());

        Mockito.verify(userRepository, Mockito.times(1)).findBriefUserByUsername(TESTER);
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(user);
    }
}