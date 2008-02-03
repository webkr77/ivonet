package nl.ivonet.samples.aop;


public class MainNoAOP {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		//final Test test = (Test) applicationContext.getBean("testbean");
		final Test test = new TestImpl();
		test.test_1();
		test.test_2();
		test.test_3();
		test.test_4("In test_4");
		test.test_5();
	}

}
