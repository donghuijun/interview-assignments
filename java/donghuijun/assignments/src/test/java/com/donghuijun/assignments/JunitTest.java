package com.donghuijun.assignments;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.donghuijun.assignments.controller.AssignmentsController;
import com.donghuijun.assignments.utils.RedisUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssignmentsApplication.class)
@WebAppConfiguration
public class JunitTest {
	
	public static final Logger logger = LoggerFactory.getLogger(JunitTest.class);
	
	
	@Autowired
	private AssignmentsController controller;
	@Autowired
	private RedisUtils redisUtils;
	
	@Before
	public void init(){
		redisUtils.increment("short_url_serial_number", 1000L);
	}
	@Test
	public void test1(){
		try {
			for(int i=0;i<10000;i++){
				String shortUrl = this.controller.convertToShortUrl("https://www.baidu.com?keyword=java&go");
				logger.info("shortUrl={}", shortUrl);
				String longUrl = this.controller.findLongUrl(shortUrl);
				logger.info("longUrl={}",longUrl);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
}

