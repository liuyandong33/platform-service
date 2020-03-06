package build.dream.platform.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.common.mappers.CommonMapper;
import build.dream.platform.jobs.JobScheduler;
import build.dream.platform.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

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
    @Autowired
    private UserService userService;
    @Autowired
    private JDDJService jddjService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private OauthClientDetailService oauthClientDetailService;
    @Autowired
    private RsaKeyPairService rsaKeyPairService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        super.previousInjectionBean(servletContextEvent.getServletContext(), CommonMapper.class);

        // 缓存支付宝开发者账号
        alipayService.cacheAlipayDeveloperAccounts();

        // 缓存支付宝账号
        alipayService.cacheAlipayAccounts();

        // 缓存微信支付账号
        weiXinService.cacheWeiXinPayAccounts();

        // 缓存微信授权token
        weiXinService.cacheWeiXinAuthorizerTokens();

        // 缓存商户信息
        tenantService.cacheTenantInfos();

        // 缓存新大陆账号
        newLandService.cacheNewLandAccounts();

        // 缓存用户信息
        userService.cacheUserInfos();

        // 缓存京东到家token
        jddjService.cacheJDDJTokens();

        // 缓存代理商信息
        agentService.cacheAgentInfos();

        // 缓存oauth客户端信息
        oauthClientDetailService.cacheOauthClientDetails();

        // 缓存rsa秘钥对
        rsaKeyPairService.cacheRsaKeyPairs();

        // 启动所有定时任务
        jobScheduler.scheduler();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
