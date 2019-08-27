package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.user.*;
import build.dream.platform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainUserInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainUserInfoModel.class, serviceClass = UserService.class, serviceMethodName = "obtainUserInfo", error = "获取用户信息失败")
    public String obtainUserInfo() {
        return null;
    }

    /**
     * 批量获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/batchGetUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = BatchGetUsersModel.class, serviceClass = UserService.class, serviceMethodName = "batchGetUsers", error = "获取用户信息失败")
    public String batchGetUsers() {
        return null;
    }

    /**
     * 获取用户所有的权限
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllPrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainAllPrivilegesModel.class, serviceClass = UserService.class, serviceMethodName = "obtainAllPrivileges", error = "获取用户权限失败")
    public String obtainAllPrivileges() {
        return null;
    }

    /**
     * 批量删除用户
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteUsers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = BatchDeleteUsersModel.class, serviceClass = UserService.class, serviceMethodName = "batchDeleteUsers", error = "批量删除用户失败")
    public String batchDeleteUser() {
        return null;
    }

    /**
     * 缓存用户信息
     *
     * @return
     */
    @RequestMapping(value = "/cacheUserInfos")
    @ResponseBody
    public String cacheUserInfos() {
        userService.cacheUserInfos();
        return Constants.SUCCESS;
    }

    /**
     * 新增用户
     *
     * @return
     */
    @RequestMapping(value = "/addUser")
    @ResponseBody
    @ApiRestAction(modelClass = AddUserModel.class, serviceClass = UserService.class, serviceMethodName = "addUser", error = "新增用户失败")
    public String addUser() {
        return null;
    }
}
