package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.models.login.LoginModel;
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
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            LoginModel loginModel = ApplicationHandler.instantiateObject(LoginModel.class, requestParameters);
            loginModel.validateAndThrow();
            return loginService.login(loginModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "登录失败", requestParameters);
    }
}
