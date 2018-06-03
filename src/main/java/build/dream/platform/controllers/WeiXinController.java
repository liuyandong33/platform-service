package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
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

    @RequestMapping(value = "/deleteWeiXinOpenPlatformApplication", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "删除微信公众平台应用失败")
    public String deleteWeiXinOpenPlatformApplication() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        DeleteWeiXinOpenPlatformApplicationModel deleteWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(DeleteWeiXinOpenPlatformApplicationModel.class, requestParameters);
        deleteWeiXinOpenPlatformApplicationModel.validateAndThrow();
        return GsonUtils.toJson(weiXinService.deleteWeiXinOpenPlatformApplication(deleteWeiXinOpenPlatformApplicationModel));
    }

    @RequestMapping(value = "/findWeiXinOpenPlatformApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "删除微信公众平台应用失败")
    public String findWeiXinOpenPlatformApplication() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        FindWeiXinOpenPlatformApplicationModel findWeiXinOpenPlatformApplicationModel = ApplicationHandler.instantiateObject(FindWeiXinOpenPlatformApplicationModel.class, requestParameters);
        findWeiXinOpenPlatformApplicationModel.validateAndThrow();
        return GsonUtils.toJson(weiXinService.findWeiXinOpenPlatformApplication(findWeiXinOpenPlatformApplicationModel));
    }

    @RequestMapping(value = "/saveWeiXinPublicAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "保存微信公众号失败")
    public String saveWeiXinPublicAccount() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        SaveWeiXinPublicAccountModel saveWeiXinPublicAccountModel = ApplicationHandler.instantiateObject(SaveWeiXinPublicAccountModel.class, requestParameters);
        saveWeiXinPublicAccountModel.validateAndThrow();

        return GsonUtils.toJson(weiXinService.saveWeiXinPublicAccount(saveWeiXinPublicAccountModel));
    }

    @RequestMapping(value = "/obtainWeiXinPublicAccount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取微信公众号失败")
    public String obtainWeiXinPublicAccount() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainWeiXinPublicAccountModel obtainWeiXinPublicAccountModel = ApplicationHandler.instantiateObject(ObtainWeiXinPublicAccountModel.class, requestParameters);
        obtainWeiXinPublicAccountModel.validateAndThrow();
        return GsonUtils.toJson(weiXinService.obtainWeiXinPublicAccount(obtainWeiXinPublicAccountModel));
    }
}
