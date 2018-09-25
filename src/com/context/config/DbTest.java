package com.context.config;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.demo.dynamic.service.DynamicService;

public class DbTest {

	  
		public static void main(String[] args)throws Exception {  
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { "classpath:com/context/config/applicationContext.xml" });
		DynamicService dynamicService = (DynamicService) appContext.getBean("dynamicService");
		System.out.println(dynamicService.getUserById(1));
		try {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			appContext.close();
		}

	}
}
