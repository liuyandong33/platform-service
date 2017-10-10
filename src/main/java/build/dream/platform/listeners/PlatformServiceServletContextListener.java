package build.dream.platform.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.saas.domains.Configuration;
import build.dream.common.saas.domains.SystemPartition;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.services.ConfigurationService;
import build.dream.platform.services.SystemPartitionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@WebListener
public class PlatformServiceServletContextListener extends BasicServletContextListener {
    private static final String PLATFORM_SERVICE_SERVLET_CONTEXT_LISTENER_SIMPLE_NAME = "PlatformServiceServletContextListener";
    @Autowired
    private SystemPartitionService systemPartitionService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
        try {
            String deploymentEnvironment = PropertyUtils.getProperty(Constants.DEPLOYMENT_ENVIRONMENT);
            List<SystemPartition> systemPartitions = systemPartitionService.findAllByDeploymentEnvironment(deploymentEnvironment);
            if (CollectionUtils.isNotEmpty(systemPartitions)) {
                SystemPartitionUtils.loadSystemPartitions(systemPartitions, deploymentEnvironment);
            }

            List<Configuration> configurations = configurationService.findAllByDeploymentEnvironment(deploymentEnvironment);
            if (CollectionUtils.isNotEmpty(configurations)) {
                ConfigurationUtils.loadConfigurations(configurations);
            }

            SearchModel systemUserSearchModel = new SearchModel(true);
            systemUserSearchModel.addSearchCondition("user_type", Constants.SQL_OPERATION_SYMBOL_EQUALS, BigInteger.valueOf(3));
            List<SystemUser> systemUsers = systemUserMapper.findAll(systemUserSearchModel);
            CommonUtils.loadServiceSystemUsers(systemUsers);
        } catch (IOException e) {
            LogUtils.error("初始化数据失败", PLATFORM_SERVICE_SERVLET_CONTEXT_LISTENER_SIMPLE_NAME, "contextInitialized", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
