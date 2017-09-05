package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
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

    @RequestMapping(value = "/aliPayCallback")
    @ResponseBody
    public String aliPayCallback() {
        String returnValue = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String tradeStatus = requestParameters.get("trade_status");
            if (Constants.TRADE_FINISHED.equals(tradeStatus) || Constants.TRADE_SUCCESS.equals(tradeStatus)) {
                ApiRest apiRest = orderService.handlePaymentCallback(requestParameters, Constants.ORDER_PAID_TYPE_ALI_PAY);
                Validate.isTrue(apiRest.isSuccessful(), apiRest.getError());
                returnValue = Constants.ALI_PAY_CALLBACK_SUCCESS_RETURN_VALUE;
            } else {
                returnValue = Constants.ALI_PAY_CALLBACK_FAILURE_RETURN_VALUE;
            }
        } catch (Exception e) {
            LogUtils.error("处理支付宝支付回调失败", controllerSimpleName, "aliPayCallback", e, requestParameters);
            returnValue = Constants.ALI_PAY_CALLBACK_FAILURE_RETURN_VALUE;
        }
        return returnValue;
    }

    @RequestMapping(value = "/weiXinPayCallback")
    @ResponseBody
    public String weiXinPayCallback() {
        String returnValue = null;
        Map<String, String> requestParameters = null;
        try {
            requestParameters = WebUtils.xmlInputStreamToMap(ApplicationHandler.getHttpServletRequest().getInputStream());

            String resultCode = requestParameters.get("result_code");
            String returnCode = requestParameters.get("return_code");
            if (Constants.SUCCESS.equals(resultCode) && Constants.SUCCESS.equals(returnCode)) {
                ApiRest apiRest = orderService.handlePaymentCallback(requestParameters, Constants.ORDER_PAID_TYPE_WEI_XIN);
                Validate.isTrue(apiRest.isSuccessful(), apiRest.getError());
                returnValue = Constants.WEI_XIN_PAY_CALLBACK_SUCCESS_RETURN_VALUE;
            } else {
                returnValue = Constants.WEI_XIN_PAY_CALLBACK_FAILURE_RETURN_VALUE;
            }
        } catch (Exception e) {
            LogUtils.error("处理微信支付回调失败", controllerSimpleName, "weiXinPayCallback", e, requestParameters);
            returnValue = Constants.WEI_XIN_PAY_CALLBACK_FAILURE_RETURN_VALUE;
        }
        return returnValue;
    }
}
