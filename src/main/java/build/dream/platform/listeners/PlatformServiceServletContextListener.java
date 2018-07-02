package build.dream.platform.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.jobs.JobScheduler;
import build.dream.platform.mappers.CommonMapper;
import build.dream.platform.services.ConfigurationService;
import build.dream.platform.services.SystemPartitionService;
import build.dream.platform.services.SystemUserService;
import build.dream.platform.services.TenantSecretKeyService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebListener
public class PlatformServiceServletContextListener extends BasicServletContextListener {
    private static final String PLATFORM_SERVICE_SERVLET_CONTEXT_LISTENER_SIMPLE_NAME = "PlatformServiceServletContextListener";
    @Autowired
    private SystemPartitionService systemPartitionService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private TenantSecretKeyService tenantSecretKeyService;
    @Autowired
    private JobScheduler jobScheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        super.previousInjectionBean(servletContextEvent.getServletContext(), CommonMapper.class);
        try {
            SearchModel systemUserSearchModel = new SearchModel(true);
            systemUserSearchModel.addSearchCondition("user_type", Constants.SQL_OPERATION_SYMBOL_EQUAL, BigInteger.valueOf(3));
            List<SystemUser> systemUsers = systemUserService.findAll(systemUserSearchModel);
            CommonUtils.loadServiceSystemUsers(systemUsers);

            SearchModel tenantSecretKeySearchModel = new SearchModel(true);
            List<TenantSecretKey> tenantSecretKeys = tenantSecretKeyService.findAll(tenantSecretKeySearchModel);
            Map<String, String> tenantPublicKeys = new HashMap<String, String>();
            for (TenantSecretKey tenantSecretKey : tenantSecretKeys) {
                tenantPublicKeys.put(tenantSecretKey.getTenantId().toString(), tenantSecretKey.getPublicKey());
            }
            CacheUtils.delete(Constants.KEY_TENANT_PUBLIC_KEYS);
            if (MapUtils.isNotEmpty(tenantPublicKeys)) {
                CacheUtils.hmset(Constants.KEY_TENANT_PUBLIC_KEYS, tenantPublicKeys);
            }

            CacheUtils.set(Constants.KEY_PLATFORM_PRIVATE_KEY, ConfigurationUtils.getConfiguration(Constants.PLATFORM_PRIVATE_KEY));

            // 启动所有定时任务
            jobScheduler.scheduler();
        } catch (Exception e) {
            LogUtils.error("初始化数据失败", PLATFORM_SERVICE_SERVLET_CONTEXT_LISTENER_SIMPLE_NAME, "contextInitialized", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
