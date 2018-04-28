package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
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
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> privilegeService.listBackgroundPrivileges();
        return ApplicationHandler.callMethod(methodCaller, "获取后台权限列表失败", requestParameters);
    }

    @RequestMapping(value = "/listAppPrivileges")
    @ResponseBody
    public String listAppPrivileges() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> privilegeService.listAppPrivileges();
        return ApplicationHandler.callMethod(methodCaller, "获取APP权限列表失败", requestParameters);
    }

    @RequestMapping(value = "/listPosPrivileges")
    @ResponseBody
    public String listPosPrivileges() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> privilegeService.listPosPrivileges();
        return ApplicationHandler.callMethod(methodCaller, "获取POS权限列表失败", requestParameters);
    }
}
