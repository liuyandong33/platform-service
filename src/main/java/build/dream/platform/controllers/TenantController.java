package build.dream.platform.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/tenant")
public class TenantController {

    @RequestMapping(value = "/index")
    @ResponseBody
    public String index() {
        return "hello world";
    }
}
