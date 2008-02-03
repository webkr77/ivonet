package nl.ivonet.samples.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	public static void main(String[] args) throws Exception {
		final ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		final Test test = (Test) appContext.getBean("testbean");
		test.test_1();
		test.test_2();
		test.test_3();
		test.test_4("In test_4");
		test.test_5();
	}	
}
