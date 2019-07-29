package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AlipayDeveloperAccount;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/alipay")
public class AlipayController {
    @Autowired
    private AlipayService alipayService;

    @RequestMapping(value = "/cacheAlipayDeveloperAccounts")
    @ResponseBody
    public String cacheAlipayDeveloperAccounts() {
        alipayService.cacheAlipayDeveloperAccounts();
        return Constants.SUCCESS;
    }

    @RequestMapping(value = "/cacheAlipayAccounts")
    @ResponseBody
    public String cacheAlipayAccounts() {
        alipayService.cacheAlipayAccounts();
        return Constants.SUCCESS;
    }

    @RequestMapping(value = "/generateAppToAppAuthorizeUrl")
    @ResponseBody
    public String generateAppToAppAuthorizeUrl() {
        ApiRest apiRest = null;
        try {
            AlipayDeveloperAccount alipayDeveloperAccount = AlipayUtils.obtainAlipayDeveloperAccount("2016121304213325");
            ValidateUtils.notNull(alipayDeveloperAccount, "支付宝开发者账号不存在！");

            Map<String, String> params = new HashMap<String, String>();
            params.put("tenantId", "100");
            params.put("branchId", "200");
            String redirectUri = CommonUtils.getOutsideUrl(Constants.SERVICE_NAME_PLATFORM, "alipay", "callback") + "?" + WebUtils.buildQueryString(params);
            String appToAppAuthorizeUrl = AlipayUtils.generateAppToAppAuthorizeUrl(alipayDeveloperAccount.getAppId(), redirectUri);
            apiRest = ApiRest.builder().data(appToAppAuthorizeUrl).message("生成授权链接成功！").successful(true).build();
        } catch (Exception e) {
            LogUtils.error("生成授权链接失败", this.getClass().getName(), "generateAppToAppAuthorizeUrl", e);
            apiRest = new ApiRest(e);
        }
        return JacksonUtils.writeValueAsString(apiRest);
    }

    @RequestMapping(value = "/callback")
    @ResponseBody
    public String callback() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        return JacksonUtils.writeValueAsString(alipayService.handleCallback(requestParameters));
    }
}
