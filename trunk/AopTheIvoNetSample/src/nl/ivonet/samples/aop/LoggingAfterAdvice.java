package nl.ivonet.samples.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class LoggingAfterAdvice {
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		final Object res = pjp.proceed();
		System.out.println("adlfjhajdhfkajsd hfkajshdfklashdflkashdf");
		return res;
	}
}
