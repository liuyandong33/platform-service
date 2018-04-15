package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
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
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainUserInfoModel obtainUserInfoModel = ApplicationHandler.instantiateObject(ObtainUserInfoModel.class, requestParameters);
            obtainUserInfoModel.validateAndThrow();
            apiRest = userService.obtainUserInfo(obtainUserInfoModel);
        } catch (Exception e) {
            LogUtils.error("获取用户信息失败", controllerSimpleName, "obtainUserInfo", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 批量获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/batchGetUsers", method = RequestMethod.GET)
    @ResponseBody
    public String batchGetUsers() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            BatchGetUsersModel batchGetUsersModel = ApplicationHandler.instantiateObject(BatchGetUsersModel.class, requestParameters);
            batchGetUsersModel.validateAndThrow();
            apiRest = userService.batchObtainUser(batchGetUsersModel);
        } catch (Exception e) {
            LogUtils.error("查询用户失败", controllerSimpleName, "batchGetUsers", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 获取用户所有的权限
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllPrivileges", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAllPrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainAllPrivilegesModel obtainAllPrivilegesModel = ApplicationHandler.instantiateObject(ObtainAllPrivilegesModel.class, requestParameters);
            obtainAllPrivilegesModel.validateAndThrow();
            apiRest = userService.obtainAllPrivileges(obtainAllPrivilegesModel);
        } catch (Exception e) {
            LogUtils.error("获取用户权限失败", controllerSimpleName, "obtainAllPrivileges", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 批量删除用户
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteUser", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteUser() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            BatchDeleteUserModel batchDeleteUserModel = ApplicationHandler.instantiateObject(BatchDeleteUserModel.class, requestParameters);
            batchDeleteUserModel.validateAndThrow();

            apiRest = userService.batchDeleteUser(batchDeleteUserModel);
        } catch (Exception e) {
            LogUtils.error("批量删除用户失败", controllerSimpleName, "batchDeleteUser", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
