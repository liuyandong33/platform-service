package build.dream.platform.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.order.*;
import build.dream.platform.services.OrderService;
import org.apache.commons.lang.math.NumberUtils;
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
    @ApiRestAction(error = "保存订单失败")
    public String saveOrder() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        SaveOrderModel saveOrderModel = ApplicationHandler.instantiateObject(SaveOrderModel.class, requestParameters);
        String goodsInfos = requestParameters.get("goodsInfos");
        saveOrderModel.setGoodsInfos(goodsInfos);
        saveOrderModel.validateAndThrow();
        return GsonUtils.toJson(orderService.saveOrder(saveOrderModel));
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainOrderInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取订单信息失败")
    public String obtainOrderInfo() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        String orderInfoId = requestParameters.get("orderInfoId");
        ApplicationHandler.notEmpty(orderInfoId, "orderInfoId");

        return GsonUtils.toJson(orderService.obtainOrderInfo(NumberUtils.createBigInteger(orderInfoId)));
    }

    /**
     * 分页获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllOrderInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "获取订单信息失败")
    public String obtainAllOrderInfos() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        ObtainAllOrderInfosModel obtainAllOrderInfosModel = ApplicationHandler.instantiateObject(ObtainAllOrderInfosModel.class, requestParameters);
        obtainAllOrderInfosModel.validateAndThrow();
        return GsonUtils.toJson(orderService.obtainAllOrderInfos(obtainAllOrderInfosModel));
    }

    /**
     * 批量删除订单
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteOrders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "批量删除订单失败")
    public String batchDeleteOrders() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        BatchDeleteOrdersModel batchDeleteOrdersModel = ApplicationHandler.instantiateObject(BatchDeleteOrdersModel.class, requestParameters);
        batchDeleteOrdersModel.validateAndThrow();
        return GsonUtils.toJson(orderService.batchDeleteOrders(batchDeleteOrdersModel));
    }

    /**
     * 删除订单
     *
     * @return
     */
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "删除订单失败")
    public String deleteOrder() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        DeleteOrderModel deleteOrderModel = ApplicationHandler.instantiateObject(DeleteOrderModel.class, requestParameters);
        deleteOrderModel.validateAndThrow();
        return GsonUtils.toJson(orderService.deleteOrder(deleteOrderModel));
    }

    /**
     * 发起支付
     *
     * @return
     */
    @RequestMapping(value = "/doPay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "发起支付失败")
    public String doPay() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        DoPayModel doPayModel = ApplicationHandler.instantiateObject(DoPayModel.class, requestParameters);
        doPayModel.validateAndThrow();

        return GsonUtils.toJson(orderService.doPay(doPayModel));
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
