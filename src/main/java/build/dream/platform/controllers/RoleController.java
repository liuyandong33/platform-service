package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.models.role.ListRolePrivilegesModel;
import build.dream.platform.models.role.ListRolesModel;
import build.dream.platform.models.role.SaveRolePrivilegesModel;
import build.dream.platform.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BasicController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/listRoles")
    @ResponseBody
    public String listRoles() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListRolesModel listRolesModel = ApplicationHandler.instantiateObject(ListRolesModel.class, requestParameters);
            listRolesModel.validateAndThrow();
            return roleService.listRoles(listRolesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取角色列表失败", requestParameters);
    }

    @RequestMapping(value = "/listRolePrivileges")
    @ResponseBody
    public String listRolePrivileges() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListRolePrivilegesModel listRolePrivilegesModel = ApplicationHandler.instantiateObject(ListRolePrivilegesModel.class, requestParameters);
            listRolePrivilegesModel.validateAndThrow();
            return roleService.listRolePrivileges(listRolePrivilegesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取角色权限列表失败", requestParameters);
    }

    @RequestMapping(value = "/saveRolePrivileges")
    @ResponseBody
    public String saveRolePrivileges() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveRolePrivilegesModel saveRolePrivilegesModel = ApplicationHandler.instantiateObject(SaveRolePrivilegesModel.class, requestParameters);
            saveRolePrivilegesModel.validateAndThrow();
            return roleService.saveRolePrivileges(saveRolePrivilegesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存角色权限失败", requestParameters);
    }
}
