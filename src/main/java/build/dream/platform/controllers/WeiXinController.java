package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.common.utils.SystemPartitionUtils;
import build.dream.common.utils.WeChatUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.weixin.*;
import build.dream.platform.services.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;

@Controller
@RequestMapping(value = "/weiXin")
public class WeiXinController extends BasicController {
    @Autowired
    private WeiXinService weiXinService;

    @RequestMapping(value = "/deleteWeiXinOpenPlatformApplication")
    @ResponseBody
    public String deleteWeiXinOpenPlatformApplication() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            DeleteWeiXinOpenPlatformApplicationModel deleteWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(DeleteWeiXinOpenPlatformApplicationModel.class, requestParameters);
            deleteWeiXinOpenPlatformApplicationModel.validateAndThrow();
            return weiXinService.deleteWeiXinOpenPlatformApplication(deleteWeiXinOpenPlatformApplicationModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "删除微信公众平台应用失败", requestParameters);
    }

    @RequestMapping(value = "/findWeiXinOpenPlatformApplication")
    @ResponseBody
    public String findWeiXinOpenPlatformApplication() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            FindWeiXinOpenPlatformApplicationModel findWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(FindWeiXinOpenPlatformApplicationModel.class, requestParameters);
            findWeiXinOpenPlatformApplicationModel.validateAndThrow();
            return weiXinService.findWeiXinOpenPlatformApplication(findWeiXinOpenPlatformApplicationModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询微信公众平台应用失败", requestParameters);
    }

    @RequestMapping(value = "/saveWeiXinPublicAccount")
    @ResponseBody
    public String saveWeiXinPublicAccount() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveWeiXinPublicAccountModel saveWeiXinPublicAccountModel = ApplicationHandler.instantiateObject(SaveWeiXinPublicAccountModel.class, requestParameters);
            saveWeiXinPublicAccountModel.validateAndThrow();

            return weiXinService.saveWeiXinPublicAccount(saveWeiXinPublicAccountModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存微信公众号失败", requestParameters);
    }

    @RequestMapping(value = "/obtainWeiXinPublicAccount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String obtainWeiXinPublicAccount() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainWeiXinPublicAccountModel obtainWeiXinPublicAccountModel = ApplicationHandler.instantiateObject(ObtainWeiXinPublicAccountModel.class, requestParameters);
            obtainWeiXinPublicAccountModel.validateAndThrow();
            return weiXinService.obtainWeiXinPublicAccount(obtainWeiXinPublicAccountModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取微信公众号失败", requestParameters);
    }

    @RequestMapping(value = "/obtainUserInfo", method = RequestMethod.GET)
    public String obtainUserInfo() throws InstantiationException, IllegalAccessException, ParseException, NoSuchFieldException, IOException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainUserInfoModel obtainUserInfoModel = ApplicationHandler.instantiateObject(ObtainUserInfoModel.class, requestParameters);
        obtainUserInfoModel.validateAndThrow();

        String appId = obtainUserInfoModel.getAppId();
        String scope = obtainUserInfoModel.getScope();
        String redirectUri = obtainUserInfoModel.getRedirectUri();
        String state = obtainUserInfoModel.getState();

        String outsideUrl = SystemPartitionUtils.getOutsideUrl(Constants.SERVICE_NAME_OUT, "weiXin", "obtainUserInfo");
        String authorizeUrl = WeChatUtils.generateAuthorizeUrl(appId, scope, outsideUrl + "?redirectUri=" + URLEncoder.encode(redirectUri, Constants.CHARSET_NAME_UTF_8), state);
        return "redirect:" + authorizeUrl;
    }

    @RequestMapping(value = "/obtainUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public String oauthCallback() {
        return null;
    }
}
