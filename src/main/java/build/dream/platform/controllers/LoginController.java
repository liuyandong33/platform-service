package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.LoginService;
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
            ApplicationHandler.notBlank(loginName, "loginName");

            String password = requestParameters.get("password");
            ApplicationHandler.notBlank(password, "password");

            String sessionId = requestParameters.get("sessionId");
            ApplicationHandler.notBlank(sessionId, "sessionId");
            apiRest = loginService.login(loginName, password, sessionId);
        } catch (Exception e) {
            LogUtils.error("登录失败", controllerSimpleName, "login", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
