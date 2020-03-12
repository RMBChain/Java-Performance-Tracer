//package com.minirmb.jds.web.test;
//
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import lombok.extern.slf4j.Slf4j;
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//public class MongoTest {
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@Test
//	public void find() throws Exception {
//		List<User> users = mongoTemplate.findAll(User.class);
//		users.forEach(u -> {
//			log.info(u.toString());
//		});
//	}
//
//	@Test
//	public void contextLoads() {
//		User user = new User();
//		user.setId(11);
//		user.setName("凌康");
//		user.setSex("男");
//		mongoTemplate.insert(user);
//		user.setId(12);
//		user.setName("李白");
//		user.setSex("男");
//		mongoTemplate.insert(user);
//		log.info("插入成功！");
//	}
//}