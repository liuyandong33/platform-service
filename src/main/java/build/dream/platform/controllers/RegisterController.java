package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.platform.models.register.RegisterAgentModel;
import build.dream.platform.models.register.RegisterTenantModel;
import build.dream.platform.services.RegisterService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BasicController {
    /**
     * 注册商户
     *
     * @return
     */
    @RequestMapping(value = "/registerTenant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = RegisterTenantModel.class, serviceClass = RegisterService.class, serviceMethodName = "registerTenant", error = "注册商户失败")
    public String registerTenant() {
        return null;
    }

    /**
     * 注册代理商
     *
     * @return
     */
    @RequestMapping(value = "/registerAgent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = RegisterAgentModel.class, serviceClass = RegisterService.class, serviceMethodName = "registerAgent", error = "注册代理商失败")
    public String registerAgent() {
        return null;
    }
}
