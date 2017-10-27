package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import build.dream.common.controllers.BasicController;
import build.dream.common.saas.domains.WeiXinOpenPlatformApplication;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.ProxyUtils;
import build.dream.platform.models.weixin.DeleteWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.FindWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.ObtainOAuthAccessTokenModel;
import build.dream.platform.services.WeiXinOpenPlatformApplicationService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/weiXin")
public class WeiXinController extends BasicController {
    @Autowired
    private WeiXinOpenPlatformApplicationService weiXinOpenPlatformApplicationService;

    @RequestMapping(value = "/deleteWeiXinOpenPlatformApplication")
    @ResponseBody
    public String deleteWeiXinOpenPlatformApplication() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            DeleteWeiXinOpenPlatformApplicationModel deleteWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(DeleteWeiXinOpenPlatformApplicationModel.class, requestParameters);
            deleteWeiXinOpenPlatformApplicationModel.validateAndThrow();
            apiRest = weiXinOpenPlatformApplicationService.deleteWeiXinOpenPlatformApplication(deleteWeiXinOpenPlatformApplicationModel);
        } catch (Exception e) {
            LogUtils.error("删除微信公众平台应用失败", controllerSimpleName, "findWeiXinOpenPlatformApplication", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/findWeiXinOpenPlatformApplication")
    @ResponseBody
    public String findWeiXinOpenPlatformApplication() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            FindWeiXinOpenPlatformApplicationModel findWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(FindWeiXinOpenPlatformApplicationModel.class, requestParameters);
            findWeiXinOpenPlatformApplicationModel.validateAndThrow();
            apiRest = weiXinOpenPlatformApplicationService.find(findWeiXinOpenPlatformApplicationModel);
        } catch (Exception e) {
            LogUtils.error("查询微信公众平台应用失败", controllerSimpleName, "findWeiXinOpenPlatformApplication", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/obtainOAuthAccessToken")
    @ResponseBody
    public String obtainOAuthAccessToken() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainOAuthAccessTokenModel obtainOAuthAccessTokenModel = ApplicationHandler.instantiateObject(ObtainOAuthAccessTokenModel.class, requestParameters);
            obtainOAuthAccessTokenModel.validateAndThrow();

            WeiXinOpenPlatformApplication weiXinOpenPlatformApplication = weiXinOpenPlatformApplicationService.find(obtainOAuthAccessTokenModel.getAppId());
            Validate.notNull(weiXinOpenPlatformApplication, "微信开放平台应用不存在！");

            Map<String, String> obtainOAuthAccessTokenRequestParameters = new HashMap<String, String>();
            obtainOAuthAccessTokenRequestParameters.put("appId", weiXinOpenPlatformApplication.getAppId());
            obtainOAuthAccessTokenRequestParameters.put("appSecret", weiXinOpenPlatformApplication.getAppSecret());
            obtainOAuthAccessTokenRequestParameters.put("code", obtainOAuthAccessTokenModel.getCode());
            ApiRest obtainOAuthAccessTokenApiRest = ProxyUtils.doGetWithRequestParameters(Constants.SERVICE_NAME_OUT, "weiXin", "obtainOAuthAccessToken", obtainOAuthAccessTokenRequestParameters);
            Validate.isTrue(obtainOAuthAccessTokenApiRest.isSuccessful(), obtainOAuthAccessTokenApiRest.getError());

            apiRest = new ApiRest();
            apiRest.setData(obtainOAuthAccessTokenApiRest.getData());
            apiRest.setMessage("通过code换取网页授权access_token成功！");
            apiRest.setSuccessful(true);
        } catch (Exception e) {
            LogUtils.error("通过code换取网页授权access_token失败", controllerSimpleName, "obtainAccessToken", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
