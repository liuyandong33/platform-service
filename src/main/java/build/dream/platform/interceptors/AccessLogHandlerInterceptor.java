package build.dream.platform.interceptors;

import build.dream.common.annotations.NotSaveAccessLog;
import build.dream.common.domains.saas.RequestLog;
import build.dream.common.domains.saas.ResponseLog;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.DatabaseHelper;
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
import java.math.BigInteger;
import java.util.*;

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
            RequestLog requestLog = new RequestLog();
            requestLog.setUuid(uuid);
            requestLog.setDeploymentEnvironment(DEPLOYMENT_ENVIRONMENT);
            requestLog.setPartitionCode(PARTITION_CODE);
            requestLog.setServiceName(SERVICE_NAME);
            requestLog.setClassName(handlerMethod.getBeanType().getName());
            requestLog.setMethodName(method.getName());
            requestLog.setRequestTime(new Date());
            requestLog.setRequestParameters(GsonUtils.toJson(ApplicationHandler.getRequestParameters(request)));
            requestLog.setHeaders(GsonUtils.toJson(obtainHeaders(ApplicationHandler.getRequestHeaders(request))));
            requestLog.setCookies(GsonUtils.toJson(ApplicationHandler.getCookies(request)));
            requestLog.setCreatedUserId(BigInteger.ONE);
            requestLog.setUpdatedUserId(BigInteger.ONE);
            DatabaseHelper.insert(requestLog);
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
            responseLog.setResponseTime(new Date());
            responseLog.setResponseContent((String) request.getAttribute(Constants.RESPONSE_CONTENT));
            responseLog.setHeaders(GsonUtils.toJson(obtainHeaders(ApplicationHandler.getResponseHeaders(response))));
            responseLog.setCreatedUserId(BigInteger.ONE);
            responseLog.setUpdatedUserId(BigInteger.ONE);
            DatabaseHelper.insert(responseLog);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    private Map<String, Object> obtainHeaders(Map<String, List<String>> headers) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, List<String>> responseHeader : headers.entrySet()) {
            String key = responseHeader.getKey();
            List<String> value = responseHeader.getValue();
            if (value.size() > 1) {
                map.put(key, value);
            } else {
                map.put(key, value.get(0));
            }
        }
        return map;
    }
}
