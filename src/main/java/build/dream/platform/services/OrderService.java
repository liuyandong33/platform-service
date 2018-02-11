package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.OrderDetail;
import build.dream.common.saas.domains.OrderInfo;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.*;
import build.dream.platform.models.order.*;
import build.dream.platform.utils.OrderUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UniversalMapper universalMapper;

    /**
     * 保存订单
     *
     * @param saveOrderModel
     * @return
     */
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

        OrderInfo orderInfo = new OrderInfo();
        Integer orderType = saveOrderModel.getOrderType();
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            orderInfo.setOrderType(Constants.ORDER_TYPE_TENANT_ORDER);
            orderInfo.setTenantId(saveOrderModel.getTenantId());
            orderInfo.setOrderNumber(SerialNumberGenerator.nextOrderNumber("TO", 10, sequenceMapper.nextValue(SerialNumberGenerator.generatorTodaySequenceName("tenant_order_number"))));
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            orderInfo.setOrderType(Constants.ORDER_TYPE_AGENT_ORDER);
            orderInfo.setAgentId(saveOrderModel.getAgentId());
            orderInfo.setOrderNumber(SerialNumberGenerator.nextOrderNumber("AO", 10, sequenceMapper.nextValue(SerialNumberGenerator.generatorTodaySequenceName("agent_order_number"))));
        }

        orderInfo.setOrderStatus(Constants.ORDER_STATUS_UNPAID);
        orderInfo.setCreateUserId(userId);
        orderInfo.setLastUpdateUserId(userId);
        orderInfoMapper.insert(orderInfo);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (SaveOrderModel.GoodsInfo goodsInfo : goodsInfos) {
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            Validate.notNull(goods, "产品不存在！");

            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsInfo.getGoodsSpecificationId());
            Validate.notNull(goodsSpecification, "产品规格不存在！");

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderInfoId(orderInfo.getId());
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

        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setDiscountAmount(discountAmount);
        orderInfo.setPayableAmount(totalAmount.subtract(discountAmount));
        orderInfo.setPaidAmount(BigDecimal.ZERO);
        orderInfo.setLastUpdateRemark("保存订单信息！");
        orderInfoMapper.update(orderInfo);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(OrderUtils.buildOrderInfo(orderInfo, orderDetails));
        apiRest.setMessage("保存订单成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 获取订单信息
     *
     * @param orderInfoId
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainOrderInfo(BigInteger orderInfoId) {
        SearchModel orderInfoSearchModel = new SearchModel(true);
        orderInfoSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderInfoId);
        OrderInfo orderInfo = orderInfoMapper.find(orderInfoSearchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderInfoId);
        List<OrderDetail> orderDetails = orderDetailMapper.findAll(orderDetailSearchModel);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(OrderUtils.buildOrderInfo(orderInfo, orderDetails));
        apiRest.setMessage("获取订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 分页获取订单信息
     *
     * @param obtainAllOrderInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAllOrderInfos(ObtainAllOrderInfosModel obtainAllOrderInfosModel) {
        SearchModel orderSearchModel = new SearchModel(true);
        long total = orderInfoMapper.count(orderSearchModel);

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (total > 0) {
            PagedSearchModel orderInfoPagedSearchModel = new PagedSearchModel(true);
            orderInfoPagedSearchModel.setPage(obtainAllOrderInfosModel.getPage());
            orderInfoPagedSearchModel.setRows(obtainAllOrderInfosModel.getRows());
            if (obtainAllOrderInfosModel.getTenantId() != null) {
                orderInfoPagedSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, obtainAllOrderInfosModel.getTenantId());
            }
            if (obtainAllOrderInfosModel.getAgentId() != null) {
                orderInfoPagedSearchModel.addSearchCondition("agent_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, obtainAllOrderInfosModel.getAgentId());
            }
            List<OrderInfo> orderInfos = orderInfoMapper.findAllPaged(orderInfoPagedSearchModel);

            if (CollectionUtils.isNotEmpty(orderInfos)) {
                List<BigInteger> orderIds = new ArrayList<BigInteger>();
                for (OrderInfo orderInfo : orderInfos) {
                    orderIds.add(orderInfo.getId());
                }

                SearchModel orderDetailSearchModel = new SearchModel(true);
                orderDetailSearchModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_IN, orderIds);
                List<OrderDetail> orderDetails = orderDetailMapper.findAll(orderDetailSearchModel);
                Map<BigInteger, List<OrderDetail>> orderDetailsMap = new HashMap<BigInteger, List<OrderDetail>>();
                for (OrderDetail orderDetail : orderDetails) {
                    List<OrderDetail> orderDetailList = orderDetailsMap.get(orderDetail.getOrderInfoId());
                    if (orderDetailList == null) {
                        orderDetailList = new ArrayList<OrderDetail>();
                        orderDetailsMap.put(orderDetail.getOrderInfoId(), orderDetailList);
                    }
                    orderDetailList.add(orderDetail);
                }

                for (OrderInfo orderInfo : orderInfos) {
                    rows.add(OrderUtils.buildOrderInfo(orderInfo, orderDetailsMap.get(orderInfo.getId())));
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

    /**
     * 批量删除订单
     *
     * @param batchDeleteOrdersModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest batchDeleteOrders(BatchDeleteOrdersModel batchDeleteOrdersModel) {
        List<BigInteger> orderInfoIds = batchDeleteOrdersModel.getOrderInfoIds();
        BigInteger userId = batchDeleteOrdersModel.getUserId();

        UpdateModel orderInfoUpdateModel = new UpdateModel(true);
        orderInfoUpdateModel.setTableName("order_info");
        orderInfoUpdateModel.addContentValue("deleted", 1);
        orderInfoUpdateModel.addContentValue("last_update_user_id", userId);
        orderInfoUpdateModel.addContentValue("last_update_remark", "删除订单信息！");
        orderInfoUpdateModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, orderInfoIds);
        universalMapper.universalUpdate(orderInfoUpdateModel);

        UpdateModel orderDetailUpdateModel = new UpdateModel(true);
        orderDetailUpdateModel.setTableName("order_detail");
        orderDetailUpdateModel.addContentValue("deleted", 1);
        orderDetailUpdateModel.addContentValue("last_update_user_id", userId);
        orderDetailUpdateModel.addContentValue("last_update_remark", "删除订单详情信息！");
        orderDetailUpdateModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_IN, orderInfoIds);
        universalMapper.universalUpdate(orderDetailUpdateModel);
        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("删除订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 删除订单
     *
     * @param deleteOrderModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest deleteOrder(DeleteOrderModel deleteOrderModel) {
        BigInteger orderInfoId = deleteOrderModel.getOrderInfoId();
        BigInteger userId = deleteOrderModel.getUserId();
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderInfoId);
        OrderInfo orderInfo = orderInfoMapper.find(searchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        orderInfo.setDeleted(true);
        orderInfo.setLastUpdateUserId(userId);
        orderInfo.setLastUpdateRemark("删除订单信息！");
        orderInfoMapper.update(orderInfo);

        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName("order_detail");
        updateModel.addContentValue("deleted", 1);
        updateModel.addContentValue("last_update_user_id", userId);
        updateModel.addContentValue("last_update_remark", "删除订单详情信息！");
        updateModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, orderInfoId);
        universalMapper.universalUpdate(updateModel);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("删除订单信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 发起支付
     *
     * @param doPayModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest doPay(DoPayModel doPayModel) throws IOException {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, doPayModel.getOrderInfoId());
        OrderInfo orderInfo = orderInfoMapper.find(searchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("tenantId", "0");
        requestParameters.put("branchId", "0");
        requestParameters.put("userId", doPayModel.getUserId().toString());

        ApiRest apiRest = null;
        String notifyUrl = SystemPartitionUtils.getUrl(Constants.SERVICE_NAME_PLATFORM, "order", "");
        int paidScene = doPayModel.getPaidScene();
        if (paidScene == Constants.PAID_SCENE_WEI_XIN_PUBLIC_ACCOUNT || paidScene == Constants.PAID_SCENE_WEI_XIN_H5 || paidScene == Constants.PAID_SCENE_WEI_XIN_APP || paidScene == Constants.PAID_SCENE_WEI_XIN_NATIVE) {
            requestParameters.put("body", "订单支付");
            requestParameters.put("outTradeNo", orderInfo.getOrderNumber());
            requestParameters.put("totalFee", String.valueOf(orderInfo.getPayableAmount().multiply(BigDecimal.valueOf(100)).longValue()));
            requestParameters.put("spbillCreateIp", ApplicationHandler.getRemoteAddress());
            requestParameters.put("notifyUrl", notifyUrl);

            String tradeType = null;
            if (paidScene == Constants.PAID_SCENE_WEI_XIN_PUBLIC_ACCOUNT) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_H5) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_MWEB;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_APP) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_APP;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_NATIVE) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_NATIVE;
            }
            requestParameters.put("tradeType", tradeType);
            apiRest = ProxyUtils.doPostWithRequestParameters(Constants.SERVICE_NAME_OUT, "weiXinPay", "unifiedOrder", requestParameters);
        } else if (paidScene == Constants.PAID_SCENE_ALIPAY_MOBILE_WEBSITE || paidScene == Constants.PAID_SCENE_ALIPAY_PC_WEBSITE || paidScene == Constants.PAID_SCENE_ALIPAY_APP) {
            requestParameters.put("subject", "订单支付");
            requestParameters.put("outTradeNo", orderInfo.getOrderNumber());
            requestParameters.put("totalAmount", orderInfo.getPayableAmount().toEngineeringString());
            requestParameters.put("productCode", orderInfo.getOrderNumber());
            requestParameters.put("notifyUrl", notifyUrl);
            if (paidScene == Constants.PAID_SCENE_ALIPAY_MOBILE_WEBSITE) {
                apiRest = ProxyUtils.doPostWithRequestParameters(Constants.SERVICE_NAME_OUT, "alipay", "alipayTradeWapPay", requestParameters);
            } else if (paidScene == Constants.PAID_SCENE_ALIPAY_PC_WEBSITE) {
                apiRest = ProxyUtils.doPostWithRequestParameters(Constants.SERVICE_NAME_OUT, "alipay", "alipayTradePagePay", requestParameters);
            } else if (paidScene == Constants.PAID_SCENE_ALIPAY_APP) {
                apiRest = ProxyUtils.doPostWithRequestParameters(Constants.SERVICE_NAME_OUT, "alipay", "alipayTradeAppPay", requestParameters);
            }
        }
        Validate.isTrue(apiRest.isSuccessful(), apiRest.getError());
        return new ApiRest(apiRest.getData(), "发起支付成功！");
    }
}
