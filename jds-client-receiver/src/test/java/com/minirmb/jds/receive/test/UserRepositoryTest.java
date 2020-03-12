package com.minirmb.jds.receive.test;
//package cn.merryyou.java.repository;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.spooner.domain.User;
//import com.spooner.repository.UserRepository;
//
///**
// * Created on 2018/3/23.
// *
// * @author zlf
// * @since 1.0
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void saveTest() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        user.setName("spooner");
//        user.setUrl("http://myou.cn");
//        User result = userRepository.save(user);
//        log.info(result.toString());
//        Assert.assertNotNull(user.getId());
//    }
//
//    @Test
//    public void findOneTest() throws Exception{
//        User user = userRepository.findOne(1l);
//        log.info(user.toString());
//        Assert.assertNotNull(user);
//        Assert.assertTrue(1l==user.getId());
//    }
//}