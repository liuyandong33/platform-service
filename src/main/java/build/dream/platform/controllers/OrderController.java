package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.LogUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.order.*;
import build.dream.platform.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BasicController {
    @Autowired
    private OrderService orderService;

    /**
     * 保存订单信息
     *
     * @return
     */
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveOrderModel.class, serviceClass = OrderService.class, serviceMethodName = "saveOrder", error = "保存订单失败")
    public String saveOrder() {
        return null;
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainOrderInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainOrderInfoModel.class, serviceClass = OrderService.class, serviceMethodName = "obtainOrderInfo", error = "获取订单信息失败")
    public String obtainOrderInfo() {
        return null;
    }

    /**
     * 分页获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllOrderInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainAllOrderInfosModel.class, serviceClass = OrderService.class, serviceMethodName = "obtainAllOrderInfos", error = "获取订单信息失败")
    public String obtainAllOrderInfos() {
        return null;
    }

    /**
     * 批量删除订单
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteOrders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = BatchDeleteOrdersModel.class, serviceClass = OrderService.class, serviceMethodName = "batchDeleteOrders", error = "批量删除订单失败")
    public String batchDeleteOrders() {
        return null;
    }

    /**
     * 删除订单
     *
     * @return
     */
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DeleteOrderModel.class, serviceClass = OrderService.class, serviceMethodName = "deleteOrder", error = "删除订单失败")
    public String deleteOrder() {
        return null;
    }

    /**
     * 发起支付
     *
     * @return
     */
    @RequestMapping(value = "/doPay", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DoPayModel.class, serviceClass = OrderService.class, serviceMethodName = "doPay", error = "发起支付失败")
    public String doPay() {
        return null;
    }

    /**
     * 支付宝支付回调
     *
     * @return
     */
    @RequestMapping(value = "/alipayCallback", method = RequestMethod.POST)
    @ResponseBody
    public String alipayCallback() {
        String result = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String orderNumber = requestParameters.get("out_trade_no");
            ApplicationHandler.notBlank(orderNumber, "out_trade_no");

            result = orderService.handleCallback(orderNumber, Constants.PAID_TYPE_ALIPAY);
        } catch (Exception e) {
            LogUtils.error("处理支付宝支付回调失败", className, "alipayCallback", e, requestParameters);
            result = Constants.FAILURE;
        }
        return result;
    }

    /**
     * 微信支付回调
     *
     * @return
     */
    @RequestMapping(value = "/weiXinPayCallback", method = RequestMethod.POST)
    @ResponseBody
    public String weiXinPayCallback() {
        String result = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String orderNumber = requestParameters.get("out_trade_no");
            ApplicationHandler.notBlank(orderNumber, "out_trade_no");

            result = orderService.handleCallback(orderNumber, Constants.PAID_TYPE_WEI_XIN);
        } catch (Exception e) {
            LogUtils.error("处理微信支付回调失败", className, "weiXinPayCallback", e, requestParameters);
            result = Constants.FAILURE;
        }
        return result;
    }
}
