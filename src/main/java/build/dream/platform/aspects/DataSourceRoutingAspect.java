package build.dream.platform.aspects;

import build.dream.common.annotations.DataSourceType;
import build.dream.platform.configurations.DataSourceContextHolder;
import build.dream.platform.constants.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

@Aspect
@Component
@Order(value = 1)
public class DataSourceRoutingAspect {
    @Before(value = "execution(public * build.dream.platform.services.*.*(..))")
    public void setDataSource(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DataSourceType dataSourceType = method.getAnnotation(DataSourceType.class);
        if (dataSourceType != null) {
            joinPoint.getTarget().getClass().getAnnotation(DataSourceType.class);
        }

        if (dataSourceType != null) {
            int type = dataSourceType.type();
            if (type == Constants.DATA_SOURCE_TYPE_READ) {
                DataSourceContextHolder.setDataSourceType(Constants.SECONDARY_DATA_SOURCE);
            } else if (type == Constants.DATA_SOURCE_TYPE_WRITE) {
                DataSourceContextHolder.setDataSourceType(Constants.PRIMARY_DATA_SOURCE);
            }
        } else {
            DataSourceContextHolder.setDataSourceType(Constants.PRIMARY_DATA_SOURCE);
        }
        System.out.println(UUID.randomUUID().toString());
    }
}
