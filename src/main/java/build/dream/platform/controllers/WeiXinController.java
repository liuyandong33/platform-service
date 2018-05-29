package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.models.weixin.DeleteWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.FindWeiXinOpenPlatformApplicationModel;
import build.dream.platform.models.weixin.ObtainWeiXinPublicAccountModel;
import build.dream.platform.models.weixin.SaveWeiXinPublicAccountModel;
import build.dream.platform.services.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
