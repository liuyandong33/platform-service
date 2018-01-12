package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/privilege")
public class PrivilegeController extends BasicController {
    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("privilege/index");
        return modelAndView;
    }

    @RequestMapping(value = "/listBackgroundPrivileges")
    @ResponseBody
    public String listBackgroundPrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            apiRest = privilegeService.listBackgroundPrivileges();
        } catch (Exception e) {
            LogUtils.error("获取后台权限列表失败", controllerSimpleName, "listBackgroundPrivileges", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/listAppPrivileges")
    @ResponseBody
    public String listAppPrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            apiRest = privilegeService.listAppPrivileges();
        } catch (Exception e) {
            LogUtils.error("获取APP权限列表失败", controllerSimpleName, "listAppPrivileges", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/listPosPrivileges")
    @ResponseBody
    public String listPosPrivileges() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            apiRest = privilegeService.listPosPrivileges();
        } catch (Exception e) {
            LogUtils.error("获取POS权限列表失败", controllerSimpleName, "listPosPrivileges", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
