package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.jddj.ListJDDJCodesModel;
import build.dream.platform.models.jddj.SaveJDDJInfoModel;
import build.dream.platform.services.JDDJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/jddj")
public class JDDJController {
    @Autowired
    private JDDJService jddjService;

    /**
     * 分页查询京东到家Code
     *
     * @return
     */
    @RequestMapping(value = "/listJDDJCodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListJDDJCodesModel.class, serviceClass = JDDJService.class, serviceMethodName = "listJDDJCodes", error = "分页查询京东到家code失败！")
    public String listJDDJCodes() {
        return null;
    }

    /**
     * 保存京东到家商家信息
     *
     * @return
     */
    @RequestMapping(value = "/saveJDDJInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveJDDJInfoModel.class, serviceClass = JDDJService.class, serviceMethodName = "saveJDDJInfo", error = "保存京东到家信息失败！")
    public String saveVenderId() {
        return null;
    }

    /**
     * 缓存京东到家token
     *
     * @return
     */
    @RequestMapping(value = "/cacheJDDJTokens")
    @ResponseBody
    public String cacheJDDJTokens() {
        jddjService.cacheJDDJTokens();
        return Constants.SUCCESS;
    }
}
