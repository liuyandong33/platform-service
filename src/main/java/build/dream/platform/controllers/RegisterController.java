package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.models.register.RegisterAgentModel;
import build.dream.platform.models.register.RegisterTenantModel;
import build.dream.platform.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "注册商户失败")
    public String registerTenant() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        RegisterTenantModel registerTenantModel = ApplicationHandler.instantiateObject(RegisterTenantModel.class, requestParameters);
        registerTenantModel.validateAndThrow();
        return GsonUtils.toJson(registerService.registerTenant(registerTenantModel));
    }

    /**
     * 注册代理商
     *
     * @return
     */
    @RequestMapping(value = "/registerAgent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "注册代理商失败")
    public String registerAgent() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        RegisterAgentModel registerAgentModel = ApplicationHandler.instantiateObject(RegisterAgentModel.class, requestParameters);
        registerAgentModel.validateAndThrow();
        return GsonUtils.toJson(registerService.registerAgent(registerAgentModel));
    }
}
