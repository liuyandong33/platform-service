package build.dream.platform.aspects;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.exceptions.ApiException;
import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class CallActionAspect {
    @Around(value = "execution(public * build.dream.platform.controllers.*.*(..)) && @annotation(apiRestAction)")
    public Object callApiRestAction(ProceedingJoinPoint proceedingJoinPoint, ApiRestAction apiRestAction) {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        Object returnValue = null;

        Throwable throwable = null;
        try {
            returnValue = callAction(proceedingJoinPoint, requestParameters, apiRestAction.modelClass(), apiRestAction.serviceName(), apiRestAction.serviceMethodName());
            returnValue = GsonUtils.toJson(returnValue);
        } catch (InvocationTargetException e) {
            throwable = e.getTargetException();
        } catch (ApiException e) {
            throwable = e;
        } catch (Throwable t) {
            throwable = new RuntimeException(apiRestAction.error());
        }

        if (throwable != null) {
            LogUtils.error(apiRestAction.error(), proceedingJoinPoint.getTarget().getClass().getName(), proceedingJoinPoint.getSignature().getName(), throwable, requestParameters);
            returnValue = GsonUtils.toJson(new ApiRest(throwable));
        }
        return returnValue;
    }

    public Object callAction(ProceedingJoinPoint proceedingJoinPoint, Map<String, String> requestParameters, Class<? extends BasicModel> modelClass, String serviceName, String serviceMethodName) throws Throwable {
        Object target = proceedingJoinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        Object returnValue = null;
        if (modelClass != BasicModel.class && StringUtils.isNotBlank(serviceName) && StringUtils.isNotBlank(serviceMethodName)) {
            BasicModel model = ApplicationHandler.instantiateObject(modelClass, requestParameters);
            model.validateAndThrow();

            Field field = targetClass.getDeclaredField(serviceName);
            Validate.notNull(field);
            field.setAccessible(true);

            Class<?> serviceClass = field.getType();

            Method method = serviceClass.getDeclaredMethod(serviceMethodName, modelClass);
            Validate.notNull(method);

            method.setAccessible(true);
            returnValue = method.invoke(field.get(target), model);
        } else {
            returnValue = proceedingJoinPoint.proceed();
        }
        return returnValue;
    }
}
