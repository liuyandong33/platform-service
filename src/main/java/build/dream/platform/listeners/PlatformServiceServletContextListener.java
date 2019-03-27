package build.dream.platform.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.RedisUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.jobs.JobScheduler;
import build.dream.platform.mappers.CommonMapper;
import build.dream.platform.services.AlipayService;
import build.dream.platform.services.NewLandService;
import build.dream.platform.services.TenantService;
import build.dream.platform.services.WeiXinService;
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

        // 缓存支付宝账号
        List<AlipayAccount> alipayAccounts = alipayService.findAllAlipayAccounts();
        Map<String, String> alipayAccountMap = new HashMap<String, String>();
        for (AlipayAccount alipayAccount : alipayAccounts) {
            alipayAccountMap.put(alipayAccount.getAppId(), GsonUtils.toJson(alipayAccount));
        }
        RedisUtils.delete(Constants.KEY_ALIPAY_ACCOUNTS);
        if (MapUtils.isNotEmpty(alipayAccountMap)) {
            RedisUtils.hmset(Constants.KEY_ALIPAY_ACCOUNTS, alipayAccountMap);
        }

        List<AlipayAuthorizerInfo> alipayAuthorizerInfos = alipayService.findAllAlipayAuthorizerInfos();
        Map<String, String> alipayAuthorizerInfoMap = new HashMap<String, String>();
        for (AlipayAuthorizerInfo alipayAuthorizerInfo : alipayAuthorizerInfos) {
            alipayAuthorizerInfoMap.put(alipayAuthorizerInfo.getTenantId() + "_" + alipayAuthorizerInfo.getBranchId(), GsonUtils.toJson(alipayAuthorizerInfo));
        }
        RedisUtils.delete(Constants.KEY_ALIPAY_AUTHORIZER_INFOS);
        if (MapUtils.isNotEmpty(alipayAuthorizerInfoMap)) {
            RedisUtils.hmset(Constants.KEY_ALIPAY_AUTHORIZER_INFOS, alipayAuthorizerInfoMap);
        }

        // 缓存微信支付账号
        List<WeiXinPayAccount> weiXinPayAccounts = weiXinService.obtainAllWeiXinPayAccounts();
        Map<String, String> weiXinPayAccountMap = new HashMap<String, String>();
        for (WeiXinPayAccount weiXinPayAccount : weiXinPayAccounts) {
            weiXinPayAccountMap.put(weiXinPayAccount.getTenantId() + "_" + weiXinPayAccount.getBranchId(), GsonUtils.toJson(weiXinPayAccount));
        }
        RedisUtils.delete(Constants.KEY_WEI_XIN_PAY_ACCOUNTS);
        if (MapUtils.isNotEmpty(weiXinPayAccountMap)) {
            RedisUtils.hmset(Constants.KEY_WEI_XIN_PAY_ACCOUNTS, weiXinPayAccountMap);
        }

        // 缓存商户信息
        List<Tenant> tenants = tenantService.obtainAllTenantInfos();
        Map<String, String> tenantInfos = new HashMap<String, String>();
        for (Tenant tenant : tenants) {
            String tenantInfo = GsonUtils.toJson(tenant);
            tenantInfos.put("_id_" + tenant.getId(), tenantInfo);
            tenantInfos.put("_code_" + tenant.getCode(), tenantInfo);
        }
        RedisUtils.delete(Constants.KEY_TENANT_INFOS);
        if (MapUtils.isNotEmpty(tenantInfos)) {
            RedisUtils.hmset(Constants.KEY_TENANT_INFOS, tenantInfos);
        }

        // 缓存微信授权token
        List<WeiXinAuthorizerToken> weiXinAuthorizerTokens = weiXinService.obtainAllWeiXinAuthorizerTokens();
        Map<String, String> weiXinAuthorizerTokenMap = new HashMap<String, String>();
        for (WeiXinAuthorizerToken weiXinAuthorizerToken : weiXinAuthorizerTokens) {
            weiXinAuthorizerTokenMap.put(weiXinAuthorizerToken.getComponentAppId() + "_" + weiXinAuthorizerToken.getAuthorizerAppId(), GsonUtils.toJson(weiXinAuthorizerToken));
        }

        RedisUtils.delete(Constants.KEY_WEI_XIN_AUTHORIZER_TOKENS);
        if (MapUtils.isNotEmpty(weiXinAuthorizerTokenMap)) {
            RedisUtils.hmset(Constants.KEY_WEI_XIN_AUTHORIZER_TOKENS, weiXinAuthorizerTokenMap);
        }

        // 缓存新大陆账号
        List<NewLandAccount> newLandAccounts = newLandService.obtainAllNewLandAccounts();
        Map<String, String> newLandAccountMap = new HashMap<String, String>();
        for (NewLandAccount newLandAccount : newLandAccounts) {
            newLandAccountMap.put(newLandAccount.getTenantId() + "_" + newLandAccount.getBranchId(), GsonUtils.toJson(newLandAccount));
        }
        RedisUtils.delete(Constants.KEY_NEW_LAND_ACCOUNTS);
        if (MapUtils.isNotEmpty(newLandAccountMap)) {
            RedisUtils.hmset(Constants.KEY_NEW_LAND_ACCOUNTS, newLandAccountMap);
        }

        // 启动所有定时任务
        jobScheduler.scheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
