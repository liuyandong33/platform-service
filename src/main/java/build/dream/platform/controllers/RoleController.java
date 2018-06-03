package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.models.role.ListRolePrivilegesModel;
import build.dream.platform.models.role.ListRolesModel;
import build.dream.platform.models.role.SaveRolePrivilegesModel;
import build.dream.platform.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BasicController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/listRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取角色列表失败")
    public String listRoles() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ListRolesModel listRolesModel = ApplicationHandler.instantiateObject(ListRolesModel.class, requestParameters);
        listRolesModel.validateAndThrow();
        return GsonUtils.toJson(roleService.listRoles(listRolesModel));
    }

    @RequestMapping(value = "/listRolePrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取角色权限列表失败")
    public String listRolePrivileges() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ListRolePrivilegesModel listRolePrivilegesModel = ApplicationHandler.instantiateObject(ListRolePrivilegesModel.class, requestParameters);
        listRolePrivilegesModel.validateAndThrow();
        return GsonUtils.toJson(roleService.listRolePrivileges(listRolePrivilegesModel));
    }

    @RequestMapping(value = "/saveRolePrivileges", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "保存角色权限失败")
    public String saveRolePrivileges() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        SaveRolePrivilegesModel saveRolePrivilegesModel = ApplicationHandler.instantiateObject(SaveRolePrivilegesModel.class, requestParameters);
        saveRolePrivilegesModel.validateAndThrow();
        return GsonUtils.toJson(roleService.saveRolePrivileges(saveRolePrivilegesModel));
    }
}
