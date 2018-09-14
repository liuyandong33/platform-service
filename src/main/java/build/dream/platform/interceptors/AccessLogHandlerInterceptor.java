package build.dream.platform.interceptors;

import build.dream.common.annotations.NotSaveAccessLog;
import build.dream.common.saas.domains.AccessLog;
import build.dream.common.saas.domains.ResponseLog;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.constants.Constants;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

@Component
public class AccessLogHandlerInterceptor implements HandlerInterceptor {
    private static final String DEPLOYMENT_ENVIRONMENT = ConfigurationUtils.getConfiguration(Constants.DEPLOYMENT_ENVIRONMENT);
    private static final String PARTITION_CODE = ConfigurationUtils.getConfiguration(Constants.PARTITION_CODE);
    private static final String SERVICE_NAME = ConfigurationUtils.getConfiguration(Constants.SERVICE_NAME);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        NotSaveAccessLog notSaveAccessLog = AnnotationUtils.findAnnotation(method, NotSaveAccessLog.class);
        if (notSaveAccessLog == null) {
            String uuid = UUID.randomUUID().toString();
            request.setAttribute("uuid", uuid);
            AccessLog accessLog = new AccessLog();
            accessLog.setUuid(uuid);
            accessLog.setDeploymentEnvironment(DEPLOYMENT_ENVIRONMENT);
            accessLog.setPartitionCode(PARTITION_CODE);
            accessLog.setServiceName(SERVICE_NAME);
            accessLog.setClassName(handlerMethod.getBeanType().getName());
            accessLog.setMethodName(method.getName());
            accessLog.setAccessTime(new Date());
            accessLog.setRequestParameters(ApplicationHandler.getRequestParameters(request));
            accessLog.setHeaders(ApplicationHandler.getRequestHeaders(request));
            accessLog.setCookies(ApplicationHandler.getCookies(request));
            System.out.println(GsonUtils.toJson(accessLog));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        NotSaveAccessLog notSaveAccessLog = AnnotationUtils.findAnnotation(method, NotSaveAccessLog.class);
        if (notSaveAccessLog == null) {
            String uuid = (String) request.getAttribute("uuid");
            ResponseLog responseLog = new ResponseLog();
            responseLog.setUuid(uuid);
            responseLog.setResponseBody("");
            responseLog.setHeaders(ApplicationHandler.getResponseHeaders(response));
            System.out.println(GsonUtils.toJson(responseLog));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
