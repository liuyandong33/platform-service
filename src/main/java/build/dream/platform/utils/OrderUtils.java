package build.dream.platform.utils;

import build.dream.common.saas.domains.Order;
import build.dream.common.saas.domains.OrderDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderUtils {
    public static Map<String, Object> buildOrderInfo(Order order, List<OrderDetail> orderDetails) {
        Map<String, Object> orderInfo = new HashMap<String, Object>();
        orderInfo.put("id", order.getId());
        orderInfo.put("orderNumber", order.getOrderNumber());
        orderInfo.put("orderType", order.getOrderType());
        orderInfo.put("orderStatus", order.getOrderStatus());
        orderInfo.put("tenantId", order.getTenantId());
        orderInfo.put("agentId", order.getAgentId());
        orderInfo.put("totalAmount", order.getTotalAmount());
        orderInfo.put("discountAmount", order.getDiscountAmount());
        orderInfo.put("payableAmount", order.getPayableAmount());
        orderInfo.put("paidAmount", order.getPaidAmount());
        orderInfo.put("paidType", order.getPaidType());
        orderInfo.put("details", buildOrderDetailInfos(orderDetails));
        return orderInfo;
    }

    public static List<Map<String, Object>> buildOrderDetailInfos(List<OrderDetail> orderDetails) {
        List<Map<String, Object>> orderDetailInfos = new ArrayList<Map<String, Object>>();
        for (OrderDetail orderDetail : orderDetails) {
            Map<String, Object> orderDetailInfo = new HashMap<String, Object>();
            orderDetailInfo.put("goodsId", orderDetail.getGoodsId());
            orderDetailInfo.put("goodsName", orderDetail.getGoodsName());
            orderDetailInfo.put("goodsSpecificationId", orderDetail.getGoodsSpecificationId());
            orderDetailInfo.put("goodsSpecificationName", orderDetail.getGoodsSpecificationName());
            orderDetailInfo.put("branchId", orderDetail.getBranchId());
            orderDetailInfo.put("price", orderDetail.getBranchId());
            orderDetailInfo.put("quantity", orderDetail.getQuantity());
            orderDetailInfo.put("totalAmount", orderDetail.getTotalAmount());
            orderDetailInfo.put("discountAmount", orderDetail.getDiscountAmount());
            orderDetailInfo.put("payableAmount", orderDetail.getPayableAmount());
            orderDetailInfos.add(orderDetailInfo);
        }
        return orderDetailInfos;
    }
}
