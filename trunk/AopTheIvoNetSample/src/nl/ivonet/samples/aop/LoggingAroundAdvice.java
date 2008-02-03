package nl.ivonet.samples.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public class LoggingAroundAdvice {
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("=================================================");
		System.out.println("calling " + pjp.getSignature().getName());
		final Object result = pjp.proceed();
		System.out.println("Returned from " + pjp.getSignature().getName());
		System.out.println("=================================================");
		return result;
	}
}
