package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.Order;
import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.*;
import build.dream.platform.models.order.ObtainAllOrderInfosModel;
import build.dream.platform.models.order.SaveOrderModel;
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

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveOrder(SaveOrderModel saveOrderModel) {
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

        BigInteger userId = saveOrderModel.getUserId();
        order.setCreateUserId(userId);
        order.setLastUpdateUserId(userId);
        orderMapper.insert(order);

        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, saveOrderModel.getGoodsIds());
        List<Goods> goodses = goodsMapper.findAll(goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new LinkedHashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, saveOrderModel.getGoodsSpecificationIds());
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new LinkedHashMap<BigInteger, GoodsSpecification>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal payableAmount = BigDecimal.ZERO;
        List<SaveOrderModel.OrderDetailModel> orderDetailModels = saveOrderModel.getOrderDetailModels();
        for (SaveOrderModel.OrderDetailModel orderDetailModel : orderDetailModels) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            BigInteger goodsId = orderDetailModel.getGoodsId();
            Goods goods = goodsMap.get(goodsId);
            Validate.notNull(goods, "产品不存在！");
            orderDetail.setGoodsId(goods.getId());
            orderDetail.setGoodsName(goods.getName());

            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(orderDetailModel.getGoodsSpecificationId());
            Validate.notNull(goodsSpecification, "产品规格不存在！");
            orderDetail.setGoodsSpecificationId(goodsSpecification.getId());
            orderDetail.setGoodsSpecificationName(goodsSpecification.getName());
            Integer amount = orderDetailModel.getAmount();
            if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
                orderDetail.setPrice(goodsSpecification.getTenantPrice());
                orderDetail.setDiscountAmount(BigDecimal.ZERO);
                orderDetail.setPayableAmount(goodsSpecification.getTenantPrice().multiply(BigDecimal.valueOf(amount)));
                orderDetail.setBranchId(orderDetailModel.getBranchId());
            } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
                orderDetail.setPrice(goodsSpecification.getAgentPrice());
                orderDetail.setDiscountAmount(BigDecimal.ZERO);
                orderDetail.setPayableAmount(goodsSpecification.getAgentPrice().multiply(BigDecimal.valueOf(amount)));
            }
            orderDetail.setAmount(amount);
            totalAmount = totalAmount.add(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getAmount())));
            discountAmount = discountAmount.add(orderDetail.getDiscountAmount());
            payableAmount = payableAmount.add(orderDetail.getPayableAmount());
            orderDetail.setCreateUserId(userId);
            orderDetail.setLastUpdateUserId(userId);
            orderDetail.setLastUpdateRemark("保存订单详情！");
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertAll(orderDetails);

        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayableAmount(payableAmount);
        order.setPaidAmount(BigDecimal.ZERO);
        order.setLastUpdateRemark("保存订单！");
        orderMapper.update(order);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("order", order);
        data.put("orderDetails", orderDetails);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("保存订单成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    public ApiRest obtainOrderInfo(BigInteger orderId) {
        SearchModel orderSearchModel = new SearchModel(true);
        orderSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderId);
        Order order = orderMapper.find(orderSearchModel);
        Validate.notNull(order, "订单不存在！");

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition("order_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.findAll(orderDetailSearchModel);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("order", order);
        data.put("orderDetails", orderDetails);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("获取订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    public ApiRest obtainAllOrderInfos(ObtainAllOrderInfosModel obtainAllOrderInfosModel) {
        PagedSearchModel orderPagedSearchModel = new PagedSearchModel(true);
        orderPagedSearchModel.setOffsetAndMaxResults(obtainAllOrderInfosModel.getPage(), obtainAllOrderInfosModel.getRows());
        List<Order> orders = orderMapper.findAllPaged(orderPagedSearchModel);

        SearchModel orderSearchModel = new SearchModel(true);
        long total = orderMapper.count(orderSearchModel);

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

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Order order : orders) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("order", order);
            row.put("orderDetail", orderDetailsMap.get(order.getId()));
            rows.add(row);
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
}
