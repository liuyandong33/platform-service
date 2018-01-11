package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.role.ListBackgroundRolesModel;
import org.apache.commons.lang.Validate;
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

    @RequestMapping(value = "/listBackgroundRoles")
    @ResponseBody
    public String listBackgroundRoles() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ListBackgroundRolesModel listBackgroundRolesModel = ApplicationHandler.instantiateObject(ListBackgroundRolesModel.class, requestParameters);
            listBackgroundRolesModel.validateAndThrow();
            apiRest = roleService.listBackgroundRoles(listBackgroundRolesModel);
        } catch (Exception e) {
            LogUtils.error("获取后台角色列表失败", controllerSimpleName, "listBackgroundRoles", e, requestParameters);
            apiRest = new ApiRest();
            apiRest.setError(e.getMessage());
            apiRest.setSuccessful(false);
        }
        return GsonUtils.toJson(apiRest);
    }
}
