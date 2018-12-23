package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.models.eleme.HandleTenantAuthorizeCallbackModel;
import build.dream.platform.models.eleme.SaveElemeBranchMappingModel;
import build.dream.platform.models.eleme.VerifyTokenModel;
import build.dream.platform.services.ElemeService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping(value = "/eleme")
public class ElemeController {
    /**
     * 饿了么授权回调
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/handleTenantAuthorizeCallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = HandleTenantAuthorizeCallbackModel.class, serviceClass = ElemeService.class, serviceMethodName = "handleTenantAuthorizeCallback", error = "处理商户授权失败")
    public String handleTenantAuthorizeCallback() {
        return null;
    }

    /**
     * 保存门店映射
     *
     * @return
     */
    @RequestMapping(value = "/saveElemeBranchMapping", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveElemeBranchMappingModel.class, serviceClass = ElemeService.class, serviceMethodName = "saveElemeBranchMapping", error = "保存门店映射失败")
    public String saveElemeBranchMapping() {
        return null;
    }

    /**
     * 验证token是否有效
     *
     * @return
     */
    @RequestMapping(value = "/verifyToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = VerifyTokenModel.class, serviceClass = ElemeService.class, serviceMethodName = "verifyToken", error = "验证token是否有效失败")
    public String verifyToken() {
        return null;
    }
}
