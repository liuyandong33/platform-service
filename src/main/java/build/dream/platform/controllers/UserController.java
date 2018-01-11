package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.UserService;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BasicController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/obtainUserInfo")
    @ResponseBody
    public String obtainUserInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String loginName = requestParameters.get("loginName");
            Validate.notNull(loginName, "参数(loginName)不能为空！");
            apiRest = userService.obtainUserInfo(loginName);
        } catch (Exception e) {
            LogUtils.error("获取用户信息失败", controllerSimpleName, "obtainUserInfo", e, requestParameters);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/findAllUsers")
    @ResponseBody
    public String findAllUsers() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            apiRest = userService.findAllUsers(requestParameters);
        } catch (Exception e) {
            LogUtils.error("查询用户失败", controllerSimpleName, "findAllUsers", e, requestParameters);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/findAllAppPrivileges")
    @ResponseBody
    public String findAllAppPrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String userId = requestParameters.get("userId");
            Validate.notNull(userId, ApplicationHandler.obtainParameterErrorMessage("userId"));
            apiRest = userService.findAllAppPrivileges(NumberUtils.createBigInteger(userId));
        } catch (Exception e) {
            LogUtils.error("查询APP权限失败", controllerSimpleName, "findAllAppAuthorities", e.getClass().getSimpleName(), e.getMessage(), requestParameters);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }
}
