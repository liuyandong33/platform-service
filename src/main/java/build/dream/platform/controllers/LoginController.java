package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.platform.models.goods.ListGoodsInfosModel;
import build.dream.platform.services.LoginService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BasicController {
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListGoodsInfosModel.class, serviceClass = LoginService.class, serviceMethodName = "login", error = "登录失败")
    public String login() {
        return null;
    }
}
