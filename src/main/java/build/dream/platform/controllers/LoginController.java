package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.models.login.LoginModel;
import build.dream.platform.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BasicController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "登录失败")
    public String login() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        LoginModel loginModel = ApplicationHandler.instantiateObject(LoginModel.class, requestParameters);
        loginModel.validateAndThrow();
        return GsonUtils.toJson(loginService.login(loginModel));
    }
}
