package build.dream.platform.listeners;

import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.PropertyUtils;
import build.dream.common.utils.SystemPartitionUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.SystemPartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.List;

@WebListener
public class PlatformServiceServletContextListener implements ServletContextListener {
    private static final String PLATFORM_SERVICE_SERVLET_CONTEXT_LISTENER_SIMPLE_NAME = "PlatformServiceServletContextListener";
    @Autowired
    private SystemPartitionService systemPartitionService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
        try {
            String deploymentEnvironment = PropertyUtils.getProperty(Constants.DEPLOYMENT_ENVIRONMENT);
            List<SystemPartition> systemPartitions = systemPartitionService.findAllByDeploymentEnvironment(deploymentEnvironment);
            SystemPartitionUtils.loadSystemPartition(systemPartitions, deploymentEnvironment);
        } catch (IOException e) {
            LogUtils.error("初始化分区数据失败", PLATFORM_SERVICE_SERVLET_CONTEXT_LISTENER_SIMPLE_NAME, "contextInitialized", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
