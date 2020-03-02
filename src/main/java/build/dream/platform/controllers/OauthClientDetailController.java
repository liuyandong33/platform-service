package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.oauthclientdetail.SaveOauthClientDetailModel;
import build.dream.platform.services.OauthClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/oauthClientDetail")
public class OauthClientDetailController {
    @Autowired
    private OauthClientDetailService oauthClientDetailService;

    /**
     * 保存特价商品活动
     *
     * @return
     */
    @RequestMapping(value = "/saveOauthClientDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveOauthClientDetailModel.class, serviceClass = OauthClientDetailService.class, serviceMethodName = "saveOauthClientDetail", error = "保存失败")
    public String saveOauthClientDetail() {
        return null;
    }

    /**
     * 缓存客户端信息
     *
     * @return
     */
    @RequestMapping(value = "/cacheOauthClientDetails")
    @ResponseBody
    public String cacheOauthClientDetails() {
        try {
            oauthClientDetailService.cacheOauthClientDetails();
            return Constants.SUCCESS;
        } catch (Exception e) {
            return Constants.FAILURE;
        }
    }
}
