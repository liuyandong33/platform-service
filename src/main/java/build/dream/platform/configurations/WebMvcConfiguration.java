package build.dream.platform.configurations;

import build.dream.platform.interceptors.AccessLogHandlerInterceptor;
import build.dream.platform.interceptors.ThreadNameHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private AccessLogHandlerInterceptor accessLogHandlerInterceptor;
    @Autowired
    private ThreadNameHandlerInterceptor threadNameHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(accessLogHandlerInterceptor).addPathPatterns("/**");
        registry.addInterceptor(threadNameHandlerInterceptor).addPathPatterns("/**");
    }
}
