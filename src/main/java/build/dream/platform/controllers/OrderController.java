package build.dream.platform.controllers;

import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.MethodCaller;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.order.*;
import build.dream.platform.services.OrderService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrder() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveOrderModel saveOrderModel = ApplicationHandler.instantiateObject(SaveOrderModel.class, requestParameters);
            String goodsInfos = requestParameters.get("goodsInfos");
            saveOrderModel.setGoodsInfos(goodsInfos);
            saveOrderModel.validateAndThrow();
            return orderService.saveOrder(saveOrderModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存订单失败", requestParameters);
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainOrderInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainOrderInfo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            String orderInfoId = requestParameters.get("orderInfoId");
            ApplicationHandler.notEmpty(orderInfoId, "orderInfoId");

            return orderService.obtainOrderInfo(NumberUtils.createBigInteger(orderInfoId));
        };
        return ApplicationHandler.callMethod(methodCaller, "获取订单信息失败", requestParameters);
    }

    /**
     * 分页获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllOrderInfos", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAllOrderInfos() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ObtainAllOrderInfosModel obtainAllOrderInfosModel = ApplicationHandler.instantiateObject(ObtainAllOrderInfosModel.class, requestParameters);
            obtainAllOrderInfosModel.validateAndThrow();
            return orderService.obtainAllOrderInfos(obtainAllOrderInfosModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取订单信息失败", requestParameters);
    }

    /**
     * 批量删除订单
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteOrders", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteOrders() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            BatchDeleteOrdersModel batchDeleteOrdersModel = ApplicationHandler.instantiateObject(BatchDeleteOrdersModel.class, requestParameters);
            batchDeleteOrdersModel.validateAndThrow();
            return orderService.batchDeleteOrders(batchDeleteOrdersModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "批量删除订单失败", requestParameters);
    }

    /**
     * 删除订单
     *
     * @return
     */
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOrder() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            DeleteOrderModel deleteOrderModel = ApplicationHandler.instantiateObject(DeleteOrderModel.class, requestParameters);
            deleteOrderModel.validateAndThrow();
            return orderService.deleteOrder(deleteOrderModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "删除订单失败", requestParameters);
    }

    /**
     * 发起支付
     *
     * @return
     */
    @RequestMapping(value = "/doPay", method = RequestMethod.POST)
    @ResponseBody
    public String doPay() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            DoPayModel doPayModel = ApplicationHandler.instantiateObject(DoPayModel.class, requestParameters);
            doPayModel.validateAndThrow();

            return orderService.doPay(doPayModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "发起支付失败", requestParameters);
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
