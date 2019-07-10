package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.weixin.HandleAuthCallbackModel;
import build.dream.platform.models.weixin.ObtainWeiXinMiniProgramsModel;
import build.dream.platform.models.weixin.ObtainWeiXinPublicAccountModel;
import build.dream.platform.models.weixin.SaveWeiXinPayAccountModel;
import build.dream.platform.services.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/weiXin")
public class WeiXinController {
    @Autowired
    private WeiXinService weiXinService;

    /**
     * 获取已授权的微信公众平台账号
     *
     * @return
     */
    @RequestMapping(value = "/obtainWeiXinPublicAccount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainWeiXinPublicAccountModel.class, serviceClass = WeiXinService.class, serviceMethodName = "obtainWeiXinPublicAccount", error = "获取微信公众号失败")
    public String obtainWeiXinPublicAccount() {
        return null;
    }

    /**
     * 获取已授权的微信小程序
     *
     * @return
     */
    @RequestMapping(value = "/obtainWeiXinMiniPrograms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainWeiXinMiniProgramsModel.class, serviceClass = WeiXinService.class, serviceMethodName = "obtainWeiXinMiniPrograms", error = "获取微信小程序失败")
    public String obtainWeiXinMiniPrograms() {
        return null;
    }

    /**
     * 保存微信支付账号
     *
     * @return
     */
    @RequestMapping(value = "/saveWeiXinPayAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveWeiXinPayAccountModel.class, serviceClass = WeiXinService.class, serviceMethodName = "saveWeiXinPayAccount", error = "保存微信支付账号失败")
    public String saveWeiXinPayAccount() {
        return null;
    }


    /**
     * 处理授权回调
     *
     * @return
     */
    @RequestMapping(value = "/handleAuthCallback", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = HandleAuthCallbackModel.class, serviceClass = WeiXinService.class, serviceMethodName = "handleAuthCallback", error = "处理授权回调失败")
    public String handleAuthCallback() {
        return null;
    }

    @RequestMapping(value = "/cacheWeiXinPayAccounts")
    @ResponseBody
    public String cacheWeiXinPayAccounts() {
        weiXinService.cacheWeiXinPayAccounts();
        return Constants.SUCCESS;
    }

    @RequestMapping(value = "/cacheWeiXinAuthorizerTokens")
    @ResponseBody
    public String cacheWeiXinAuthorizerTokens() {
        weiXinService.cacheWeiXinAuthorizerTokens();
        return Constants.SUCCESS;
    }
}
