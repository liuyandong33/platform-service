package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {
    private static final String REGISTER_CONTROLLER_SIMPLE_NAME = "RegisterController";
    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/registerTenant", method = RequestMethod.POST)
    public String registerTenant() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            Tenant tenant = ApplicationHandler.instantiateDomain(Tenant.class, requestParameters);
            apiRest = new ApiRest(registerService.registerTenant(tenant), "注册商户成功！");
        } catch (Exception e) {
            LogUtils.error("注册商户失败", REGISTER_CONTROLLER_SIMPLE_NAME, "registerTenant", e.getClass().getSimpleName(), e.getMessage(), requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
