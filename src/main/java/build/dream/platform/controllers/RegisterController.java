package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.register.RegisterTenantModel;
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
            RegisterTenantModel registerTenantModel = ApplicationHandler.instantiateObject(RegisterTenantModel.class, requestParameters);
            registerTenantModel.validateAndThrow();
            apiRest = registerService.registerTenant(registerTenantModel);
        } catch (Exception e) {
            LogUtils.error("注册商户失败", controllerSimpleName, "registerTenant", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
