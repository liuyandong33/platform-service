package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.Order;
import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.common.utils.UpdateModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.*;
import build.dream.platform.models.order.BatchDeleteOrdersModel;
import build.dream.platform.models.order.ObtainAllOrderInfosModel;
import build.dream.platform.models.order.SaveOrderModel;
import build.dream.platform.utils.OrderUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private SequenceMapper sequenceMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsSpecificationMapper goodsSpecificationMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UniversalMapper universalMapper;

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveOrder(SaveOrderModel saveOrderModel) {
        BigInteger userId = saveOrderModel.getUserId();
        List<BigInteger> goodsIds = new ArrayList<BigInteger>();
        List<BigInteger> goodsSpecificationIds = new ArrayList<BigInteger>();
        List<SaveOrderModel.GoodsInfo> goodsInfos = saveOrderModel.getGoodsInfos();
        for (SaveOrderModel.GoodsInfo goodsInfo : goodsInfos) {
            goodsIds.add(goodsInfo.getGoodsId());
            goodsSpecificationIds.add(goodsInfo.getGoodsSpecificationId());
        }

        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsInfos);
        List<Goods> goodses = goodsMapper.findAll(goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new LinkedHashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new LinkedHashMap<BigInteger, GoodsSpecification>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
        }

        Order order = new Order();
        Integer orderType = saveOrderModel.getOrderType();
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            order.setOrderType(Constants.ORDER_TYPE_TENANT_ORDER);
            order.setTenantId(saveOrderModel.getTenantId());
            order.setOrderNumber(SerialNumberGenerator.nextOrderNumber("TO",10, sequenceMapper.nextValue(SerialNumberGenerator.generatorTodaySequenceName("tenant_order_number"))));
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            order.setOrderType(Constants.ORDER_TYPE_AGENT_ORDER);
            order.setAgentId(saveOrderModel.getAgentId());
            order.setOrderNumber(SerialNumberGenerator.nextOrderNumber("AO", 10, sequenceMapper.nextValue(SerialNumberGenerator.generatorTodaySequenceName("agent_order_number"))));
        }

        order.setOrderStatus(Constants.ORDER_STATUS_UNPAID);
        order.setCreateUserId(userId);
        order.setLastUpdateUserId(userId);
        orderMapper.insert(order);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (SaveOrderModel.GoodsInfo goodsInfo : goodsInfos) {
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            Validate.notNull(goods, "产品不存在！");

            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsInfo.getGoodsSpecificationId());
            Validate.notNull(goodsSpecification, "产品规格不存在！");

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            orderDetail.setGoodsId(goods.getId());
            orderDetail.setGoodsName(goods.getName());
            orderDetail.setGoodsSpecificationId(goodsSpecification.getId());
            orderDetail.setGoodsSpecificationName(goodsSpecification.getName());
            Integer quantity = goodsInfo.getQuantity();
            if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
                orderDetail.setPrice(goodsSpecification.getTenantPrice().multiply(BigDecimal.valueOf(quantity)));
                orderDetail.setTotalAmount(goodsSpecification.getTenantPrice().multiply(BigDecimal.valueOf(quantity)));
                orderDetail.setDiscountAmount(BigDecimal.ZERO);
                orderDetail.setBranchId(goodsInfo.getBranchId());
            } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
                orderDetail.setPrice(goodsSpecification.getAgentPrice());
                orderDetail.setTotalAmount(goodsSpecification.getAgentPrice().multiply(BigDecimal.valueOf(quantity)));
                orderDetail.setDiscountAmount(BigDecimal.ZERO);
            }
            orderDetail.setPayableAmount(orderDetail.getTotalAmount().subtract(orderDetail.getDiscountAmount()));
            orderDetail.setQuantity(quantity);
            totalAmount = totalAmount.add(orderDetail.getTotalAmount());
            discountAmount = discountAmount.add(orderDetail.getDiscountAmount());
            orderDetail.setCreateUserId(userId);
            orderDetail.setLastUpdateUserId(userId);
            orderDetail.setLastUpdateRemark("保存订单详情信息！");
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertAll(orderDetails);

        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayableAmount(totalAmount.subtract(discountAmount));
        order.setPaidAmount(BigDecimal.ZERO);
        order.setLastUpdateRemark("保存订单信息！");
        orderMapper.update(order);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(OrderUtils.buildOrderInfo(order, orderDetails));
        apiRest.setMessage("保存订单成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest obtainOrderInfo(BigInteger orderId) {
        SearchModel orderSearchModel = new SearchModel(true);
        orderSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderId);
        Order order = orderMapper.find(orderSearchModel);
        Validate.notNull(order, "订单不存在！");

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition("order_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.findAll(orderDetailSearchModel);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(OrderUtils.buildOrderInfo(order, orderDetails));
        apiRest.setMessage("获取订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest obtainAllOrderInfos(ObtainAllOrderInfosModel obtainAllOrderInfosModel) {
        SearchModel orderSearchModel = new SearchModel(true);
        long total = orderMapper.count(orderSearchModel);

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (total > 0) {
            PagedSearchModel orderPagedSearchModel = new PagedSearchModel(true);
            orderPagedSearchModel.setPage(obtainAllOrderInfosModel.getPage());
            orderPagedSearchModel.setRows(obtainAllOrderInfosModel.getRows());
            List<Order> orders = orderMapper.findAllPaged(orderPagedSearchModel);

            if (CollectionUtils.isNotEmpty(orders)) {
                List<BigInteger> orderIds = new ArrayList<BigInteger>();
                for (Order order : orders) {
                    orderIds.add(order.getId());
                }

                SearchModel orderDetailSearchModel = new SearchModel(true);
                orderDetailSearchModel.addSearchCondition("order_id", Constants.SQL_OPERATION_SYMBOL_IN, orderIds);
                List<OrderDetail> orderDetails = orderDetailMapper.findAll(orderDetailSearchModel);
                Map<BigInteger, List<OrderDetail>> orderDetailsMap = new HashMap<BigInteger, List<OrderDetail>>();
                for (OrderDetail orderDetail : orderDetails) {
                    List<OrderDetail> orderDetailList = orderDetailsMap.get(orderDetail.getOrderId());
                    if (orderDetailList == null) {
                        orderDetailList = new ArrayList<OrderDetail>();
                        orderDetailsMap.put(orderDetail.getOrderId(), orderDetailList);
                    }
                    orderDetailList.add(orderDetail);
                }

                for (Order order : orders) {
                    rows.add(OrderUtils.buildOrderInfo(order, orderDetailsMap.get(order.getId())));
                }
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("rows", rows);
        data.put("total", total);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("获取订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest batchDeleteOrders(BatchDeleteOrdersModel batchDeleteOrdersModel) {
        List<BigInteger> orderIds = batchDeleteOrdersModel.getOrderIds();
        BigInteger userId = batchDeleteOrdersModel.getUserId();

        UpdateModel orderUpdateModel = new UpdateModel(true);
        orderUpdateModel.setTableName("order");
        orderUpdateModel.addContentValue("deleted", 1);
        orderUpdateModel.addContentValue("last_update_user_id", userId);
        orderUpdateModel.addContentValue("last_update_remark", "删除订单信息！");
        orderUpdateModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, orderIds);
        universalMapper.universalUpdate(orderUpdateModel);

        UpdateModel orderDetailUpdateModel = new UpdateModel(true);
        orderDetailUpdateModel.setTableName("order_detail");
        orderDetailUpdateModel.addContentValue("deleted", 1);
        orderDetailUpdateModel.addContentValue("last_update_user_id", userId);
        orderDetailUpdateModel.addContentValue("last_update_remark", "删除订单详情信息！");
        orderDetailUpdateModel.addSearchCondition("order_id", Constants.SQL_OPERATION_SYMBOL_IN, orderIds);
        universalMapper.universalUpdate(orderDetailUpdateModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("删除订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
