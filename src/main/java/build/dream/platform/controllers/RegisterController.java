package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.RegisterService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BasicController {
    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET)
    @ResponseBody
    public String registerTenant() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            Validate.notNull(requestParameters.get("name"), "商户名称不能为空！");
            Validate.notNull(requestParameters.get("mobile"), "手机号码不能为空！");
            Validate.notNull(requestParameters.get("email"), "邮箱不能为空！");
            Validate.notNull(requestParameters.get("linkman"), "联系人不能为空！");
            Validate.notNull(requestParameters.get("business"), "业态不能为空！");
            Validate.notNull(requestParameters.get("provinceCode"), "省编码不能为空！");
            Validate.notNull(requestParameters.get("cityCode"), "市编码不能为空！");
            Validate.notNull(requestParameters.get("districtCode"), "区编码不能为空！");
            Validate.notNull(requestParameters.get("password"), "密码不能为空！");
            apiRest = registerService.registerTenant(requestParameters);
        } catch (Exception e) {
            LogUtils.error("注册商户失败", controllerSimpleName, "registerTenant", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
