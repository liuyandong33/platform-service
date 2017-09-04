package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.services.OrderService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BasicController {
    @Autowired
    private OrderService orderService;

    // TODO 修改为POST请求
    @RequestMapping(value = "/saveOrder")
    @ResponseBody
    public String saveOrder() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ValidateUtils.notNull(requestParameters, "orderType", "orderDetailsJson");
            apiRest = orderService.saveOrder(requestParameters);
        } catch (Exception e) {
            LogUtils.error("保存订单失败", controllerSimpleName, "saveOrder", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
