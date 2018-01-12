package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.user.ObtainAllPrivilegesModel;
import build.dream.platform.services.UserService;
import org.apache.commons.lang.Validate;
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
            apiRest = new ApiRest(e);
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
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/obtainAllPrivileges")
    @ResponseBody
    public String obtainAllPrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainAllPrivilegesModel obtainAllPrivilegesModel = ApplicationHandler.instantiateObject(ObtainAllPrivilegesModel.class, requestParameters);
            obtainAllPrivilegesModel.validateAndThrow();
            apiRest = userService.obtainAllPrivileges(obtainAllPrivilegesModel);
        } catch (Exception e) {
            LogUtils.error("查询权限失败", controllerSimpleName, "findAllAppAuthorities", e.getClass().getSimpleName(), e.getMessage(), requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
