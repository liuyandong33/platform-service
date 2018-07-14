package build.dream.platform.aspects;

import build.dream.platform.configurations.DataSourceContextHolder;
import build.dream.platform.constants.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(value = 1)
public class DataSourceRoutingAspect {
    @Before(value = "execution(public * build.dream.platform.services.*.*(..))")
    public void setDataSource(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Transactional transactional = method.getAnnotation(Transactional.class);
        if (transactional == null) {
            transactional = joinPoint.getTarget().getClass().getAnnotation(Transactional.class);
        }
        if (transactional != null && transactional.readOnly()) {
            DataSourceContextHolder.setDataSourceType(Constants.SECONDARY_DATA_SOURCE);
        } else {
            DataSourceContextHolder.setDataSourceType(Constants.PRIMARY_DATA_SOURCE);
        }
    }
}
