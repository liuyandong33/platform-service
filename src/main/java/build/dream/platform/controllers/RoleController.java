package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.platform.models.role.ListRolePrivilegesModel;
import build.dream.platform.models.role.ListRolesModel;
import build.dream.platform.models.role.SaveRolePrivilegesModel;
import build.dream.platform.services.RoleService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BasicController {
    /**
     * 分页查询角色
     *
     * @return
     */
    @RequestMapping(value = "/listRoles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListRolesModel.class, serviceClass = RoleService.class, serviceMethodName = "listRoles", error = "获取角色列表失败")
    public String listRoles() {
        return null;
    }

    /**
     * 查询角色拥有的权限
     *
     * @return
     */
    @RequestMapping(value = "/listRolePrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListRolePrivilegesModel.class, serviceClass = RoleService.class, serviceMethodName = "listRolePrivileges", error = "获取角色权限列表失败")
    public String listRolePrivileges() {
        return null;
    }

    /**
     * 保存角色权限
     *
     * @return
     */
    @RequestMapping(value = "/saveRolePrivileges", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveRolePrivilegesModel.class, serviceClass = RoleService.class, serviceMethodName = "saveRolePrivileges", error = "保存角色权限失败")
    public String saveRolePrivileges() {
        return null;
    }
}
