package org.sysoev.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sysoev.aspect.loging_lvl_service.LoggingLevelService;
import org.sysoev.config.HttpLoggingProperties;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private final LoggingLevelService levelService;

    public LoggingAspect(LoggingLevelService levelService) {
        this.levelService = levelService;
    }

    //Логируем по аннотации входные параметры
    @Before("@annotation(org.sysoev.aspect.anotasion.LogEnterValues)")
    public void logEnter(JoinPoint joinPoint) {
        levelService.log(log, "Вызвали метод "+ joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            levelService.log(log, "Детали входные параметры: " + arg);
        }
    }

    //Логируем успешную обработку сервисов
    @AfterReturning(pointcut = "@annotation(org.sysoev.aspect.anotasion.LogOutputValues)", returning = "result")
    public void recordSuccessfulExecution(JoinPoint joinPoint, Object result) {
        if (result != null) {
            levelService.log(log, "Успешно выполнен метод: " + joinPoint.getSignature().getName()
                    + "с результатом выполнения - " + result);
        } else {
            levelService.log(log, "Успешно выполнен метод: " + joinPoint.getSignature().getName());
        }
    }

    //Логируем ошибки в сервисах
    @AfterThrowing(pointcut = "@annotation(org.sysoev.aspect.anotasion.LogTrowing)", throwing = "exception")
    public void recordFailedExecution(JoinPoint joinPoint, Exception exception) {
        log.error("Метод - {},  выдал исключение - {}, в классе {}", joinPoint.getSignature().getName(), exception.getMessage(), exception.getClass());
    }

    //Замер времени выполнения
    @Around("@annotation(org.sysoev.aspect.anotasion.LogTimeMeasuring)")
    public Object timeDetected(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            levelService.log(log, "Метод: " + joinPoint.getSignature().getName()
                    +"отработал за: "+ (endTime - startTime));

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Произошла ошибка в методе: {}", joinPoint.getSignature().getName());
            throw e;
        }
    }

}