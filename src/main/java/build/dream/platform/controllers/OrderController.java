package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
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
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveOrderModel saveOrderModel = ApplicationHandler.instantiateObject(SaveOrderModel.class, requestParameters);
            String goodsInfos = requestParameters.get("goodsInfos");
            saveOrderModel.setGoodsInfos(goodsInfos);
            saveOrderModel.validateAndThrow();
            apiRest = orderService.saveOrder(saveOrderModel);
        } catch (Exception e) {
            LogUtils.error("保存订单失败", controllerSimpleName, "saveOrder", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String obtainOrderInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String orderInfoId = requestParameters.get("orderInfoId");
            ApplicationHandler.notEmpty(orderInfoId, "orderInfoId");

            apiRest = orderService.obtainOrderInfo(NumberUtils.createBigInteger(orderInfoId));
        } catch (Exception e) {
            LogUtils.error("获取订单信息失败", controllerSimpleName, "obtainOrderInfo", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 分页获取订单信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainAllOrderInfos", method = RequestMethod.GET)
    @ResponseBody
    public String obtainAllOrderInfos() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            ObtainAllOrderInfosModel obtainAllOrderInfosModel = ApplicationHandler.instantiateObject(ObtainAllOrderInfosModel.class, requestParameters);
            obtainAllOrderInfosModel.validateAndThrow();
            apiRest = orderService.obtainAllOrderInfos(obtainAllOrderInfosModel);
        } catch (Exception e) {
            LogUtils.error("获取订单信息失败", controllerSimpleName, "obtainAllOrderInfos", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 批量删除订单
     *
     * @return
     */
    @RequestMapping(value = "/batchDeleteOrders", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteOrders() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            BatchDeleteOrdersModel batchDeleteOrdersModel = ApplicationHandler.instantiateObject(BatchDeleteOrdersModel.class, requestParameters);
            batchDeleteOrdersModel.validateAndThrow();
            apiRest = orderService.batchDeleteOrders(batchDeleteOrdersModel);
        } catch (Exception e) {
            LogUtils.error("批量删除订单失败", controllerSimpleName, "batchDeleteOrders", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 删除订单
     *
     * @return
     */
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    @ResponseBody
    public String deleteOrder() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            DeleteOrderModel deleteOrderModel = ApplicationHandler.instantiateObject(DeleteOrderModel.class, requestParameters);
            deleteOrderModel.validateAndThrow();
            apiRest = orderService.deleteOrder(deleteOrderModel);
        } catch (Exception e) {
            LogUtils.error("删除订单失败", controllerSimpleName, "deleteOrder", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    /**
     * 发起支付
     *
     * @return
     */
    @RequestMapping(value = "/doPay", method = RequestMethod.POST)
    @ResponseBody
    public String doPay() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            DoPayModel doPayModel = ApplicationHandler.instantiateObject(DoPayModel.class, requestParameters);
            doPayModel.validateAndThrow();

            apiRest = orderService.doPay(doPayModel);
        } catch (Exception e) {
            LogUtils.error("发起支付失败", controllerSimpleName, "doPay", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/alipayCallback", method = RequestMethod.GET)
    @ResponseBody
    public String alipayCallback() {
        String result = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            result = orderService.handleCallback("123456", 1);
        } catch (Exception e) {
            LogUtils.error("处理支付宝支付回调失败", controllerSimpleName, "alipayCallback", e, requestParameters);
            result = Constants.FAILURE;
        }
        return result;
    }
}
