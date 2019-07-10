package build.dream.platform.controllers;

import build.dream.platform.constants.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/newLand")
public class NewLandController {
    @RequestMapping(value = "/cacheNewLandAccounts")
    @ResponseBody
    public String cacheNewLandAccounts() {
        return Constants.SUCCESS;
    }
}
