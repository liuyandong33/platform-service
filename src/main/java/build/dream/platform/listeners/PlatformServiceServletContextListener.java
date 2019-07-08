package build.dream.platform.listeners;

import build.dream.common.beans.AlipayAccount;
import build.dream.common.beans.JDDJVenderInfo;
import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.mappers.CommonMapper;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.CommonRedisUtils;
import build.dream.common.utils.JacksonUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.jobs.JobScheduler;
import build.dream.platform.services.AlipayService;
import build.dream.platform.services.NewLandService;
import build.dream.platform.services.TenantService;
import build.dream.platform.services.WeiXinService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebListener
public class PlatformServiceServletContextListener extends BasicServletContextListener {
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private JobScheduler jobScheduler;
    @Autowired
    private WeiXinService weiXinService;
    @Autowired
    private NewLandService newLandService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        super.previousInjectionBean(servletContextEvent.getServletContext(), CommonMapper.class);

        // 缓存支付宝开发者账号
        List<AlipayDeveloperAccount> alipayDeveloperAccounts = alipayService.obtainAllAlipayDeveloperAccounts();
        Map<String, String> alipayDeveloperAccountMap = new HashMap<String, String>();
        for (AlipayDeveloperAccount alipayDeveloperAccount : alipayDeveloperAccounts) {
            alipayDeveloperAccountMap.put(alipayDeveloperAccount.getAppId(), JacksonUtils.writeValueAsString(alipayDeveloperAccount));
        }
        CommonRedisUtils.del(Constants.KEY_ALIPAY_DEVELOPER_ACCOUNTS);
        if (MapUtils.isNotEmpty(alipayDeveloperAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_ALIPAY_DEVELOPER_ACCOUNTS, alipayDeveloperAccountMap);
        }

        // 缓存支付宝账号
        List<AlipayAccount> alipayAccounts = alipayService.obtainAllAlipayAccounts();
        Map<String, String> alipayAccountMap = new HashMap<String, String>();
        for (AlipayAccount alipayAccount : alipayAccounts) {
            alipayAccountMap.put(alipayAccount.getAppId(), JacksonUtils.writeValueAsString(alipayAccount));
        }
        CommonRedisUtils.del(Constants.KEY_ALIPAY_ACCOUNTS);
        if (MapUtils.isNotEmpty(alipayAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_ALIPAY_ACCOUNTS, alipayAccountMap);
        }

        // 缓存微信支付账号
        List<WeiXinPayAccount> weiXinPayAccounts = weiXinService.obtainAllWeiXinPayAccounts();
        Map<String, String> weiXinPayAccountMap = new HashMap<String, String>();
        for (WeiXinPayAccount weiXinPayAccount : weiXinPayAccounts) {
            weiXinPayAccountMap.put(weiXinPayAccount.getTenantId() + "_" + weiXinPayAccount.getBranchId(), JacksonUtils.writeValueAsString(weiXinPayAccount));
        }
        CommonRedisUtils.del(Constants.KEY_WEI_XIN_PAY_ACCOUNTS);
        if (MapUtils.isNotEmpty(weiXinPayAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_WEI_XIN_PAY_ACCOUNTS, weiXinPayAccountMap);
        }

        // 缓存商户信息
        List<Tenant> tenants = tenantService.obtainAllTenantInfos();
        Map<String, String> tenantInfos = new HashMap<String, String>();
        Map<String, String> jddjVenderInfos = new HashMap<String, String>();
        for (Tenant tenant : tenants) {
            BigInteger tenantId = tenant.getId();
            String tenantCode = tenant.getCode();
            String tenantInfo = JacksonUtils.writeValueAsString(tenant);
            tenantInfos.put("_id_" + tenantId, tenantInfo);
            tenantInfos.put("_code_" + tenantCode, tenantInfo);

            String jddjVenderId = tenant.getJddjVenderId();
            String jddjAppKey = tenant.getJddjAppKey();
            String jddjAppSecret = tenant.getJddjAppSecret();

            if (StringUtils.isBlank(jddjVenderId) || StringUtils.isBlank(jddjAppKey) || StringUtils.isBlank(jddjAppSecret)) {
                continue;
            }

            JDDJVenderInfo jddjVenderInfo = JDDJVenderInfo.builder()
                    .tenantId(tenantId)
                    .tenantCode(tenantCode)
                    .partitionCode(tenant.getPartitionCode())
                    .venderId(jddjVenderId)
                    .appKey(jddjAppKey)
                    .appSecret(jddjAppSecret)
                    .build();
            jddjVenderInfos.put(jddjAppKey, JacksonUtils.writeValueAsString(jddjVenderInfo));
        }
        CommonRedisUtils.del(Constants.KEY_TENANT_INFOS);
        if (MapUtils.isNotEmpty(tenantInfos)) {
            CommonRedisUtils.hmset(Constants.KEY_TENANT_INFOS, tenantInfos);
        }

        CommonRedisUtils.del(Constants.KEY_JDDJ_VENDER_INFOS);
        if (MapUtils.isNotEmpty(jddjVenderInfos)) {
            CommonRedisUtils.hmset(Constants.KEY_JDDJ_VENDER_INFOS, jddjVenderInfos);
        }

        // 缓存微信授权token
        List<WeiXinAuthorizerToken> weiXinAuthorizerTokens = weiXinService.obtainAllWeiXinAuthorizerTokens();
        Map<String, String> weiXinAuthorizerTokenMap = new HashMap<String, String>();
        for (WeiXinAuthorizerToken weiXinAuthorizerToken : weiXinAuthorizerTokens) {
            weiXinAuthorizerTokenMap.put(weiXinAuthorizerToken.getComponentAppId() + "_" + weiXinAuthorizerToken.getAuthorizerAppId(), JacksonUtils.writeValueAsString(weiXinAuthorizerToken));
        }

        CommonRedisUtils.del(Constants.KEY_WEI_XIN_AUTHORIZER_TOKENS);
        if (MapUtils.isNotEmpty(weiXinAuthorizerTokenMap)) {
            CommonRedisUtils.hmset(Constants.KEY_WEI_XIN_AUTHORIZER_TOKENS, weiXinAuthorizerTokenMap);
        }

        // 缓存新大陆账号
        List<NewLandAccount> newLandAccounts = newLandService.obtainAllNewLandAccounts();
        Map<String, String> newLandAccountMap = new HashMap<String, String>();
        for (NewLandAccount newLandAccount : newLandAccounts) {
            newLandAccountMap.put(newLandAccount.getTenantId() + "_" + newLandAccount.getBranchId(), JacksonUtils.writeValueAsString(newLandAccount));
        }
        CommonRedisUtils.del(Constants.KEY_NEW_LAND_ACCOUNTS);
        if (MapUtils.isNotEmpty(newLandAccountMap)) {
            CommonRedisUtils.hmset(Constants.KEY_NEW_LAND_ACCOUNTS, newLandAccountMap);
        }

        // 启动所有定时任务
        jobScheduler.scheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
