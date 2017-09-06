package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.Order;
import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
    public ApiRest saveOrder(Map<String, String> parameters) {
        Integer orderType = Integer.valueOf(parameters.get("orderType"));
        Validate.isTrue(orderType == Constants.ORDER_TYPE_TENANT_ORDER || orderType == Constants.ORDER_TYPE_AGENT_ORDER, "参数(orderType)只能为1或者2！");
        String userId = parameters.get("userId");

        Validate.notNull(userId, "参数(userId)不能为空！");
        BigInteger bigIntegerUserId = BigInteger.valueOf(Long.valueOf(userId));

        Order order = new Order();
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            String tenantId = parameters.get("tenantId");
            Validate.notNull(tenantId, "参数(tenantId)不能为空！");
            order.setOrderType(Constants.ORDER_TYPE_TENANT_ORDER);
            order.setTenantId(BigInteger.valueOf(Long.valueOf(tenantId)));
            order.setOrderNumber(SerialNumberGenerator.nextOrderNumber("TO",10, sequenceMapper.nextValue(SerialNumberGenerator.generatorTodaySequenceName("tenant_order_number"))));
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            String agentId = parameters.get("agentId");
            Validate.notNull(agentId, "参数(agentId)不能为空！");
            order.setOrderType(Constants.ORDER_TYPE_AGENT_ORDER);
            order.setAgentId(BigInteger.valueOf(Long.valueOf(agentId)));
            order.setOrderNumber(SerialNumberGenerator.nextOrderNumber("AO", 10, sequenceMapper.nextValue(SerialNumberGenerator.generatorTodaySequenceName("agent_order_number"))));
        }

        order.setOrderStatus(Constants.ORDER_STATUS_UNPAID);
        order.setCreateUserId(bigIntegerUserId);
        order.setLastUpdateUserId(bigIntegerUserId);
        orderMapper.insert(order);

        JSONArray orderDetailsJsonArray = JSONArray.fromObject(parameters.get("orderDetailsJson"));
        int size = orderDetailsJsonArray.size();
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

        List<BigInteger> goodsIds = new ArrayList<BigInteger>();
        List<BigInteger>goodsSpecificationIds = new ArrayList<BigInteger>();
        for (int index = 0; index < size; index++) {
            JSONObject orderDetailJSONObject = orderDetailsJsonArray.getJSONObject(index);
            goodsIds.add(BigInteger.valueOf(orderDetailJSONObject.getLong("goodsId")));
            goodsSpecificationIds.add(BigInteger.valueOf(orderDetailJSONObject.getLong("goodsSpecificationId")));
        }
        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsIds);
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

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal payableAmount = BigDecimal.ZERO;
        for (int index = 0; index < size; index++) {
            JSONObject orderDetailJSONObject = orderDetailsJsonArray.getJSONObject(index);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            BigInteger goodsId = BigInteger.valueOf(orderDetailJSONObject.getLong("goodsId"));
            Goods goods = goodsMap.get(goodsId);
            Validate.notNull(goods, "产品不存在！");
            orderDetail.setGoodsId(goods.getId());

            BigInteger goodsSpecificationId = BigInteger.valueOf(orderDetailJSONObject.getLong("goodsSpecificationId"));
            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsSpecificationId);
            Validate.notNull(goodsSpecification, "产品规格不存在！");
            orderDetail.setGoodsSpecificationId(goodsSpecification.getId());
            Integer amount = orderDetailJSONObject.getInt("amount");
            if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
                orderDetail.setPrice(goodsSpecification.getTenantPrice());
                orderDetail.setDiscountAmount(BigDecimal.ZERO);
                orderDetail.setPayableAmount(goodsSpecification.getTenantPrice().multiply(BigDecimal.valueOf(amount)));
            } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
                orderDetail.setPrice(goodsSpecification.getAgentPrice());
                orderDetail.setDiscountAmount(BigDecimal.ZERO);
                orderDetail.setPayableAmount(goodsSpecification.getAgentPrice().multiply(BigDecimal.valueOf(amount)));
            }
            orderDetail.setBranchId(BigInteger.valueOf(orderDetailJSONObject.getLong("branchId")));
            orderDetail.setAmount(amount);
            totalAmount = totalAmount.add(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getAmount())));
            discountAmount = discountAmount.add(orderDetail.getDiscountAmount());
            payableAmount = payableAmount.add(orderDetail.getPayableAmount());
            orderDetail.setCreateUserId(bigIntegerUserId);
            orderDetail.setLastUpdateUserId(bigIntegerUserId);
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

    @Transactional(rollbackFor = Exception.class)
    public ApiRest handlePaymentCallback(Map<String, String> parameters, Integer paidType) {
        String orderNumber = null;
        BigDecimal paidAmount = null;
        String lastUpdateRemark = null;
        if (paidType == Constants.ORDER_PAID_TYPE_WEI_XIN) {
            orderNumber = parameters.get("out_trade_no");
            paidAmount = new BigDecimal(parameters.get("total_fee")).divide(new BigDecimal(100));
            lastUpdateRemark = "支付宝支付回调！";
        } else if (paidType == Constants.ORDER_PAID_TYPE_ALI_PAY) {
            orderNumber = parameters.get("out_trade_no");
            paidAmount = new BigDecimal(parameters.get("total_amount"));
            lastUpdateRemark = "微信支付回调！";
        }
        SearchModel orderSearchModel = new SearchModel(true);
        orderSearchModel.addSearchCondition("order_number", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderNumber);
        Order order = orderMapper.find(orderSearchModel);
        Validate.notNull(order, "订单不存在！");
        Validate.isTrue(order.getOrderStatus() == Constants.ORDER_STATUS_UNPAID, "订单状态异常！");
        order.setOrderStatus(Constants.ORDER_STATUS_PAID);
        order.setLastUpdateRemark(lastUpdateRemark);
        order.setPaidAmount(order.getPaidAmount().add(paidAmount));
        order.setLastUpdateUserId(BigInteger.ZERO);
        order.setPaidType(paidType);
        orderMapper.update(order);

        List<Map<String, Object>> orderInfos = orderMapper.findOrderInfos(order.getId());
        for (Map<String, Object> orderInfo : orderInfos) {
            BigInteger orderId = ApplicationHandler.obtainBigIntegerFromMap(orderInfo, "orderId");
        }
        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("支付回调处理成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
