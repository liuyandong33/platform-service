package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.Order;
import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.SerialNumberGenerator;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            order.setTenantId(BigInteger.valueOf(Long.valueOf(tenantId)));
            order.setOrderNumber(SerialNumberGenerator.nextSerialNumber(10, sequenceMapper.nextValue("tenant_order_number")));
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            String agentId = parameters.get("agentId");
            Validate.notNull(agentId, "参数(agentId)不能为空！");
            order.setOrderNumber(SerialNumberGenerator.nextSerialNumber(10, sequenceMapper.nextValue("tenant_order_number")));
        }

        order.setCreateUserId(bigIntegerUserId);
        order.setLastUpdateUserId(bigIntegerUserId);
        orderMapper.insert(order);

        JsonArray orderDetailsJsonArray = GsonUtils.parseJsonArray(parameters.get("orderDetails"));
        int size = orderDetailsJsonArray.size();
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

        List<BigInteger> goodsIds = new ArrayList<BigInteger>();
        List<BigInteger>goodsSpecificationIds = new ArrayList<BigInteger>();
        for (int index = 0; index < size; index++) {
            goodsIds.add(orderDetailsJsonArray.get(index).getAsJsonObject().get("goodsId").getAsBigInteger());
            goodsSpecificationIds.add(orderDetailsJsonArray.get(index).getAsJsonObject().get("goodsSpecificationId").getAsBigInteger());
        }
        SearchModel goodsSearchModel = new SearchModel(true);
        goodsSearchModel.addSearchCondition("id", "=", goodsIds);
        List<Goods> goodses = goodsMapper.findAll(goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new LinkedHashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("id", "=", goodsSpecificationIds);
        List<GoodsSpecification> goodsSpecifications = goodsSpecificationMapper.findAll(goodsSpecificationSearchModel);
        Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new LinkedHashMap<BigInteger, GoodsSpecification>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
        }

        for (int index = 0; index < size; index++) {
            JsonObject orderDetailJsonObject = orderDetailsJsonArray.get(index).getAsJsonObject();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            BigInteger goodsId = orderDetailJsonObject.get("goodsId").getAsBigInteger();
            Goods goods = goodsMap.get(goodsId);
            orderDetail.setGoodsId(goods.getId());

            BigInteger goodsSpecificationId = orderDetailJsonObject.get("goodsSpecificationId").getAsBigInteger();
            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsSpecificationId);
            orderDetail.setGoodsSpecificationId(goodsSpecification.getId());
            orderDetail.setPrice(BigDecimal.ONE);
            orderDetail.setDiscountAmount(BigDecimal.ZERO);
            orderDetail.setRealPrice(BigDecimal.ZERO);
            orderDetail.setCreateUserId(bigIntegerUserId);
            orderDetail.setLastUpdateUserId(bigIntegerUserId);
            orderDetail.setLastUpdateRemark("保存订单！");
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertAll(orderDetails);

        order.setTotalAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayableAmount(BigDecimal.ZERO);
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
}
