package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.models.user.BatchDeleteUserModel;
import build.dream.platform.models.user.BatchGetUsersModel;
import build.dream.platform.models.user.ObtainAllPrivilegesModel;
import build.dream.platform.models.user.ObtainUserInfoModel;
import build.dream.platform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BasicController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainUserInfo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainUserInfoModel obtainUserInfoModel = ApplicationHandler.instantiateObject(ObtainUserInfoModel.class, requestParameters);
            obtainUserInfoModel.validateAndThrow();
            return userService.obtainUserInfo(obtainUserInfoModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取用户信息失败", requestParameters);
    }

    /**
     * 批量获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/batchGetUsers", method = RequestMethod.GET)
    @ResponseBody
    public String batchGetUsers() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            BatchGetUsersModel batchGetUsersModel = ApplicationHandler.instantiateObject(BatchGetUsersModel.class, requestParameters);
            batchGetUsersModel.validateAndThrow();
            return userService.batchObtainUser(batchGetUsersModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询用户失败", requestParameters);
    }

    /**
     * 获取用户所有的权限
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllPrivileges", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAllPrivileges() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainAllPrivilegesModel obtainAllPrivilegesModel = ApplicationHandler.instantiateObject(ObtainAllPrivilegesModel.class, requestParameters);
            obtainAllPrivilegesModel.validateAndThrow();
            return userService.obtainAllPrivileges(obtainAllPrivilegesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取用户权限失败", requestParameters);
    }

    /**
     * 批量删除用户
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteUser", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteUser() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            BatchDeleteUserModel batchDeleteUserModel = ApplicationHandler.instantiateObject(BatchDeleteUserModel.class, requestParameters);
            batchDeleteUserModel.validateAndThrow();

            return userService.batchDeleteUser(batchDeleteUserModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "批量删除用户失败", requestParameters);
    }
}
