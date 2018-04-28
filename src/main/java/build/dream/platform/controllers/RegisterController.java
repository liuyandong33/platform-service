package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.models.register.RegisterAgentModel;
import build.dream.platform.models.register.RegisterTenantModel;
import build.dream.platform.services.RegisterService;
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

    /**
     * 注册商户
     *
     * @return
     */
    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET)
    @ResponseBody
    public String registerTenant() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            RegisterTenantModel registerTenantModel = ApplicationHandler.instantiateObject(RegisterTenantModel.class, requestParameters);
            registerTenantModel.validateAndThrow();
            return registerService.registerTenant(registerTenantModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "注册商户失败", requestParameters);
    }

    /**
     * 注册代理商
     *
     * @return
     */
    @RequestMapping(value = "/registerAgent", method = RequestMethod.GET)
    @ResponseBody
    public String registerAgent() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            RegisterAgentModel registerAgentModel = ApplicationHandler.instantiateObject(RegisterAgentModel.class, requestParameters);
            registerAgentModel.validateAndThrow();
            return registerService.registerAgent(registerAgentModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "注册代理商失败", requestParameters);
    }
}
