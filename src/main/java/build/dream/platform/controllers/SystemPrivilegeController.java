package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.GsonUtils;
import build.dream.platform.services.SystemPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/systemPrivilege")
public class SystemPrivilegeController {
    @Autowired
    private SystemPrivilegeService systemPrivilegeService;

    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("systemPrivilege/index");
        return modelAndView;
    }

    @RequestMapping(value = "/findAll")
    @ResponseBody
    public String findAll() {
        ApiRest apiRest = systemPrivilegeService.findAll();
        return GsonUtils.toJson(apiRest);
    }
}
