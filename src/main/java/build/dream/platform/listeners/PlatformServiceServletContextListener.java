package build.dream.platform.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.saas.domains.AlipayAccount;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.CacheUtils;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.jobs.JobScheduler;
import build.dream.platform.mappers.CommonMapper;
import build.dream.platform.services.AlipayService;
import build.dream.platform.services.TenantSecretKeyService;
import build.dream.platform.services.TenantService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebListener
public class PlatformServiceServletContextListener extends BasicServletContextListener {
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private TenantSecretKeyService tenantSecretKeyService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private JobScheduler jobScheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        super.previousInjectionBean(servletContextEvent.getServletContext(), CommonMapper.class);
        try {
            List<AlipayAccount> alipayAccounts = alipayService.findAllAlipayAccounts();
            Map<String, String> alipayAccountMap = new HashMap<String, String>();
            for (AlipayAccount alipayAccount : alipayAccounts) {
                alipayAccountMap.put(alipayAccount.getTenantId() + "_" + alipayAccount.getBranchId(), GsonUtils.toJson(alipayAccount));
            }

            CacheUtils.delete(Constants.KEY_ALIPAY_ACCOUNTS);
            if (MapUtils.isNotEmpty(alipayAccountMap)) {
                CacheUtils.hmset(Constants.KEY_ALIPAY_ACCOUNTS, alipayAccountMap);
            }

            List<TenantSecretKey> tenantSecretKeys = tenantSecretKeyService.findAll();
            Map<String, String> tenantPublicKeys = new HashMap<String, String>();
            for (TenantSecretKey tenantSecretKey : tenantSecretKeys) {
                tenantPublicKeys.put(tenantSecretKey.getTenantId().toString(), tenantSecretKey.getPublicKey());
            }
            CacheUtils.delete(Constants.KEY_TENANT_PUBLIC_KEYS);
            if (MapUtils.isNotEmpty(tenantPublicKeys)) {
                CacheUtils.hmset(Constants.KEY_TENANT_PUBLIC_KEYS, tenantPublicKeys);
            }

            CacheUtils.set(Constants.KEY_PLATFORM_PRIVATE_KEY, ConfigurationUtils.getConfiguration(Constants.PLATFORM_PRIVATE_KEY));

            CacheUtils.delete(Constants.KEY_TENANT_INFOS);
            List<Tenant> tenants = tenantService.obtainAllTenantInfos();
            Map<String, String> tenantInfos = new HashMap<String, String>();
            for (Tenant tenant : tenants) {
                String tenantInfo = GsonUtils.toJson(tenant);
                tenantInfos.put(tenant.getId().toString(), tenantInfo);
                tenantInfos.put(tenant.getCode(), tenantInfo);
            }
            if (MapUtils.isNotEmpty(tenantInfos)) {
                CacheUtils.hmset(Constants.KEY_TENANT_INFOS, tenantInfos);
            }

            // 启动所有定时任务
            jobScheduler.scheduler();
        } catch (Exception e) {
            LogUtils.error("初始化数据失败", this.getClass().getName(), "contextInitialized", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
