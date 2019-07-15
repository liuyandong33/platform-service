package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AlipayDeveloperAccount;
import build.dream.common.utils.AlipayUtils;
import build.dream.common.utils.JacksonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

            String appToAppAuthorizeUrl = AlipayUtils.generateAppToAppAuthorizeUrl(alipayDeveloperAccount.getAppId(), "https://www.baidu.com");
            apiRest = ApiRest.builder().data(appToAppAuthorizeUrl).message("生成授权链接成功！").successful(true).build();
        } catch (Exception e) {
            LogUtils.error("生成授权链接失败", this.getClass().getName(), "generateAppToAppAuthorizeUrl", e);
            apiRest = new ApiRest(e);
        }
        return JacksonUtils.writeValueAsString(apiRest);
    }
}
