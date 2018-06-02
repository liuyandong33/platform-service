package build.dream.platform.test;

import build.dream.common.annotations.Action;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CallActionAspect {
    @Around(value = "execution(public * build.dream.platform.controllers.*.*(..)) && @annotation(action)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, Action action) {
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            LogUtils.error(action.error(), proceedingJoinPoint.getTarget().getClass().getName(), proceedingJoinPoint.getSignature().getName(), throwable, ApplicationHandler.getRequestParameters());
            returnValue = GsonUtils.toJson(new ApiRest(throwable));
        }
        return returnValue;
    }
}
