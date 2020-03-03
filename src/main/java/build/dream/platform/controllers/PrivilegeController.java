package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.JacksonUtils;
import build.dream.platform.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = "/listBackgroundPrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取后台权限列表失败")
    public String listBackgroundPrivileges() {
        return JacksonUtils.writeValueAsString(privilegeService.listBackgroundPrivileges());
    }

    @RequestMapping(value = "/listAppPrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取APP权限列表失败")
    public String listAppPrivileges() {
        return JacksonUtils.writeValueAsString(privilegeService.listAppPrivileges());
    }

    @RequestMapping(value = "/listPosPrivileges", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取POS权限列表失败")
    public String listPosPrivileges() {
        return JacksonUtils.writeValueAsString(privilegeService.listPosPrivileges());
    }
}
