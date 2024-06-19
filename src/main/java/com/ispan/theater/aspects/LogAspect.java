    package com.ispan.theater.aspects;

    import org.aspectj.lang.JoinPoint;
    import org.aspectj.lang.annotation.After;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.Pointcut;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Component;

    @Aspect
    @Component
    public class LogAspect {
        private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
        @Pointcut("execution(* com.ispan.theater.service.*.*(..))")
        public void allServiceMethods() {}

        @After("allServiceMethods()")
        public void afterAllServiceMethods(JoinPoint joinPoint) {
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();

            StringBuilder logMessage = new StringBuilder();
            logMessage.append("Method '").append(methodName).append("' executed with arguments: ");
            for (Object arg : args) {
                logMessage.append(arg).append(", ");
            }
            if (args.length > 0) {
                logMessage.delete(logMessage.length() - 2, logMessage.length());
            }
            logger.info(logMessage.toString());
        }

    }
