package build.dream.platform.utils;

import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.saas.domains.OrderInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderUtils {
    public static Map<String, Object> buildOrderInfo(OrderInfo orderInfo, List<OrderDetail> orderDetails) {
        Map<String, Object> orderInfoMap = new HashMap<String, Object>();
        orderInfoMap.put("id", orderInfo.getId());
        orderInfoMap.put("orderNumber", orderInfo.getOrderNumber());
        orderInfoMap.put("orderType", orderInfo.getOrderType());
        orderInfoMap.put("orderStatus", orderInfo.getOrderStatus());
        orderInfoMap.put("tenantId", orderInfo.getTenantId());
        orderInfoMap.put("agentId", orderInfo.getAgentId());
        orderInfoMap.put("totalAmount", orderInfo.getTotalAmount());
        orderInfoMap.put("discountAmount", orderInfo.getDiscountAmount());
        orderInfoMap.put("payableAmount", orderInfo.getPayableAmount());
        orderInfoMap.put("paidAmount", orderInfo.getPaidAmount());
        orderInfoMap.put("paidType", orderInfo.getPaidType());
        orderInfoMap.put("details", buildOrderDetailInfos(orderDetails));
        return orderInfoMap;
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
