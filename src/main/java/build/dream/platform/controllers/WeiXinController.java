package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.weixin.DeleteWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.FindWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.SaveWeiXinOpenPlatformApplicationModel;
import build.dream.platform.services.WeiXinOpenPlatformApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/weiXin")
public class WeiXinController extends BasicController {
    @Autowired
    private WeiXinOpenPlatformApplicationService weiXinOpenPlatformApplicationService;

    @RequestMapping(value = "/saveWeiXinOpenPlatformApplication")
    @ResponseBody
    public String saveWeiXinOpenPlatformApplication() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveWeiXinOpenPlatformApplicationModel saveWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(SaveWeiXinOpenPlatformApplicationModel.class, requestParameters);
            saveWeiXinOpenPlatformApplicationModel.validateAndThrow();
            apiRest = weiXinOpenPlatformApplicationService.saveWeiXinOpenPlatformApplication(saveWeiXinOpenPlatformApplicationModel);
        } catch (Exception e) {
            LogUtils.error("保存微信公众平台应用失败", controllerSimpleName, "findWeiXinOpenPlatformApplication", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

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
}
