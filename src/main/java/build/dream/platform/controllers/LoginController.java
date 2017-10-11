package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.LoginService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BasicController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public String login() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String loginName = requestParameters.get("loginName");
            Validate.notNull(loginName, ApplicationHandler.obtainParameterErrorMessage("loginName"));

            String password = requestParameters.get("password");
            Validate.notNull(password, ApplicationHandler.obtainParameterErrorMessage("password"));

            String sessionId = requestParameters.get("sessionId");
            Validate.notNull(password, ApplicationHandler.obtainParameterErrorMessage("sessionId"));
            apiRest = loginService.login(loginName, password, sessionId);
        } catch (Exception e) {
            LogUtils.error("登录失败", controllerSimpleName, "login", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
