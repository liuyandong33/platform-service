package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.platform.models.weixin.*;
import build.dream.platform.services.WeiXinService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/weiXin")
public class WeiXinController extends BasicController {
    /**
     * 删除微信开放平台应用
     *
     * @return
     */
    @RequestMapping(value = "/deleteWeiXinOpenPlatformApplication", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DeleteWeiXinOpenPlatformApplicationModel.class, serviceClass = WeiXinService.class, serviceMethodName = "deleteWeiXinOpenPlatformApplication", error = "删除微信开放平台应用失败")
    public String deleteWeiXinOpenPlatformApplication() {
        return null;
    }

    /**
     * 获取微信开放平台应用
     *
     * @return
     */
    @RequestMapping(value = "/obtainWeiXinOpenPlatformApplication", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainWeiXinOpenPlatformApplicationModel.class, serviceClass = WeiXinService.class, serviceMethodName = "obtainWeiXinOpenPlatformApplication", error = "查询微信开放平台应用失败")
    public String obtainWeiXinOpenPlatformApplication() {
        return null;
    }

    /**
     * 保存微信公众平台账号
     *
     * @return
     */
    @RequestMapping(value = "/saveWeiXinPublicAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveWeiXinPublicAccountModel.class, serviceClass = WeiXinService.class, serviceMethodName = "saveWeiXinPublicAccount", error = "保存微信公众号失败")
    public String saveWeiXinPublicAccount() {
        return null;
    }

    /**
     * 获取微信公众平台账号
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
}
