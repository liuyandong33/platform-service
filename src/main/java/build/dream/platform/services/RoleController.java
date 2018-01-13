package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.role.ListRolePrivilegesModel;
import build.dream.platform.models.role.ListRolesModel;
import build.dream.platform.models.role.SaveRolePrivilegesModel;
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
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ListRolesModel listRolesModel = ApplicationHandler.instantiateObject(ListRolesModel.class, requestParameters);
            listRolesModel.validateAndThrow();
            apiRest = roleService.listRoles(listRolesModel);
        } catch (Exception e) {
            LogUtils.error("获取角色列表失败", controllerSimpleName, "listRoles", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/listRolePrivileges")
    @ResponseBody
    public String listRolePrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ListRolePrivilegesModel listRolePrivilegesModel = ApplicationHandler.instantiateObject(ListRolePrivilegesModel.class, requestParameters);
            listRolePrivilegesModel.validateAndThrow();
            apiRest = roleService.listRolePrivileges(listRolePrivilegesModel);
        } catch (Exception e) {
            LogUtils.error("获取角色权限列表失败", controllerSimpleName, "listRolePrivileges", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/saveRolePrivileges")
    @ResponseBody
    public String saveRolePrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveRolePrivilegesModel saveRolePrivilegesModel = ApplicationHandler.instantiateObject(SaveRolePrivilegesModel.class, requestParameters);
            saveRolePrivilegesModel.validateAndThrow();
            apiRest = roleService.saveRolePrivileges(saveRolePrivilegesModel);
        } catch (Exception e) {
            LogUtils.error("保存角色权限失败", controllerSimpleName, "saveRolePrivileges", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
