package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.weixin.DeleteWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.FindWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.ObtainWeiXinPublicAccountModel;
import build.dream.platform.models.weixin.SaveWeiXinPublicAccountModel;
import build.dream.platform.services.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/weiXin")
public class WeiXinController extends BasicController {
    @Autowired
    private WeiXinService weiXinService;

    @RequestMapping(value = "/deleteWeiXinOpenPlatformApplication")
    @ResponseBody
    public String deleteWeiXinOpenPlatformApplication() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            DeleteWeiXinOpenPlatformApplicationModel deleteWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(DeleteWeiXinOpenPlatformApplicationModel.class, requestParameters);
            deleteWeiXinOpenPlatformApplicationModel.validateAndThrow();
            apiRest = weiXinService.deleteWeiXinOpenPlatformApplication(deleteWeiXinOpenPlatformApplicationModel);
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
            apiRest = weiXinService.find(findWeiXinOpenPlatformApplicationModel);
        } catch (Exception e) {
            LogUtils.error("查询微信公众平台应用失败", controllerSimpleName, "findWeiXinOpenPlatformApplication", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/saveWeiXinPublicAccount")
    @ResponseBody
    public String saveWeiXinPublicAccount() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveWeiXinPublicAccountModel saveWeiXinPublicAccountModel = ApplicationHandler.instantiateObject(SaveWeiXinPublicAccountModel.class, requestParameters);
            saveWeiXinPublicAccountModel.validateAndThrow();

            apiRest = weiXinService.saveWeiXinPublicAccount(saveWeiXinPublicAccountModel);
        } catch (Exception e) {
            LogUtils.error("保存微信公众号失败", controllerSimpleName, "saveWeiXinPublicAccount", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/obtainWeiXinPublicAccount")
    @ResponseBody
    public String obtainWeiXinPublicAccount() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainWeiXinPublicAccountModel obtainWeiXinPublicAccountModel = ApplicationHandler.instantiateObject(ObtainWeiXinPublicAccountModel.class, requestParameters);
            obtainWeiXinPublicAccountModel.validateAndThrow();

            apiRest = weiXinService.obtainWeiXinPublicAccount(obtainWeiXinPublicAccountModel);
        } catch (Exception e) {
            LogUtils.error("获取微信公众号失败", controllerSimpleName, "obtainWeiXinPublicAccount", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
