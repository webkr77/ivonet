package nl.ivonet.samples.aop;

public class TestImpl implements Test {

	public void test_1() {
		System.out.println("In test_1");

	}
	public String test_2() {
		System.out.println("In test_2");
		return "";
	}
	
	public void test_3() {
		System.out.println("In test_3");
	}
	public void test_4(String string) {
		System.out.println(string);
		
	}
	public void test_5() throws Exception {
		System.out.println("Ik gooi helemaal niks maar zou het kunnen");
	}
	
}
