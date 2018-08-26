package build.dream.platform.jobs;

import build.dream.common.beans.ComponentAccessToken;
import build.dream.common.saas.domains.WeiXinAuthorizerToken;
import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.utils.ValidateUtils;
import build.dream.common.utils.WeiXinUtils;
import build.dream.platform.services.WeiXinService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefreshWeiXinAuthorizerTokenJob implements Job {
    @Autowired
    private WeiXinService weiXinService;

    @Override
    public void execute(JobExecutionContext context) {
        List<WeiXinAuthorizerToken> weiXinAuthorizerTokens = weiXinService.findAllWeiXinAuthorizerTokens();
        Map<String, ComponentAccessToken> componentAccessTokenMap = new HashMap<String, ComponentAccessToken>();
        for (WeiXinAuthorizerToken weiXinAuthorizerToken : weiXinAuthorizerTokens) {
            try {
                String componentAppId = weiXinAuthorizerToken.getComponentAppId();
                ComponentAccessToken componentAccessToken = componentAccessTokenMap.get(componentAppId);
                if (componentAccessToken == null) {
                    WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = weiXinService.findWeiXinOpenPlatformApplication(componentAppId);
                    ValidateUtils.notNull(weiXinOpenPlatformApplication, "微信开放平台应用不存在！");

                    componentAccessToken = WeiXinUtils.obtainComponentAccessToken(weiXinOpenPlatformApplication.getAppId(), weiXinOpenPlatformApplication.getAppSecret());
                    componentAccessTokenMap.put(componentAppId, componentAccessToken);
                }
                weiXinService.refreshWeiXinAuthorizerToken(componentAccessToken.getComponentAccessToken(), componentAppId, weiXinAuthorizerToken.getAuthorizerAppId(), weiXinAuthorizerToken.getAuthorizerRefreshToken(), weiXinAuthorizerToken.getId());
            } catch (Exception e) {
                weiXinAuthorizerToken.setLastUpdateRemark("刷新token失败，删除本条记录-" + e.getMessage());
                weiXinAuthorizerToken.setDeleted(true);
                weiXinService.updateWeiXinAuthorizerToken(weiXinAuthorizerToken);
            }
        }
    }
}
