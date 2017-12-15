package build.dream.platform.controllers;

import build.dream.common.api.ApiRest;
import build.dream.common.controllers.BasicController;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.models.order.ObtainAllOrderInfosModel;
import build.dream.platform.models.order.SaveOrderModel;
import build.dream.platform.services.OrderService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BasicController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrder() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            SaveOrderModel saveOrderModel = ApplicationHandler.instantiateObject(SaveOrderModel.class, requestParameters);
            String orderDetails = requestParameters.get("orderDetails");
            ApplicationHandler.notEmpty(orderDetails, "orderDetails");
            List<SaveOrderModel.OrderDetailModel> orderDetailModels = GsonUtils.jsonToList(orderDetails, SaveOrderModel.OrderDetailModel.class);
            saveOrderModel.setOrderDetailModels(orderDetailModels);
            saveOrderModel.validateAndThrow();
            apiRest = orderService.saveOrder(saveOrderModel);
        } catch (Exception e) {
            LogUtils.error("保存订单失败", controllerSimpleName, "saveOrder", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

    @RequestMapping(value = "/obtainOrderInfo", method = RequestMethod.GET)
    @ResponseBody
    public String obtainOrderInfo() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String orderId = requestParameters.get("orderId");
            ApplicationHandler.notEmpty(orderId, "orderId");

            apiRest = orderService.obtainOrderInfo(NumberUtils.createBigInteger(orderId));
        } catch (Exception e) {
            LogUtils.error("获取订单信息失败", controllerSimpleName, "obtainOrderInfo", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }

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

    @RequestMapping(value = "/deleteOrders", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteOrders() {
        ApiRest apiRest = null;
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        try {
            String orderIds = requestParameters.get("orderIds");
            ApplicationHandler.notEmpty(orderIds, "orderIds");

            List<BigInteger> bigIntegerOrderIds = new ArrayList<BigInteger>();
            String[] orderIdArray = orderIds.split(",");
            for (String orderId : orderIdArray) {
                bigIntegerOrderIds.add(NumberUtils.createBigInteger(orderId));
            }
            apiRest = orderService.deleteOrders(bigIntegerOrderIds);
        } catch (Exception e) {
            LogUtils.error("删除订单失败", controllerSimpleName, "deleteOrders", e, requestParameters);
            apiRest = new ApiRest(e);
        }
        return GsonUtils.toJson(apiRest);
    }
}
