package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.order.*;
import build.dream.platform.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@Service
public class OrderService {
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
        List<Goods> goodses = DatabaseHelper.findAll(Goods.class, goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new LinkedHashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
        List<GoodsSpecification> goodsSpecifications = DatabaseHelper.findAll(GoodsSpecification.class, goodsSpecificationSearchModel);
        Map<BigInteger, GoodsSpecification> goodsSpecificationMap = new LinkedHashMap<BigInteger, GoodsSpecification>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationMap.put(goodsSpecification.getId(), goodsSpecification);
        }

        OrderInfo orderInfo = new OrderInfo();
        Integer orderType = saveOrderModel.getOrderType();
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            orderInfo.setOrderType(Constants.ORDER_TYPE_TENANT_ORDER);
            orderInfo.setTenantId(saveOrderModel.getTenantId());
            orderInfo.setOrderNumber(SerialNumberGenerator.nextOrderNumber("TO", 10, SequenceUtils.nextValue(SerialNumberGenerator.generatorTodaySequenceName("tenant_order_number"))));
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            orderInfo.setOrderType(Constants.ORDER_TYPE_AGENT_ORDER);
            orderInfo.setAgentId(saveOrderModel.getAgentId());
            orderInfo.setOrderNumber(SerialNumberGenerator.nextOrderNumber("AO", 10, SequenceUtils.nextValue(SerialNumberGenerator.generatorTodaySequenceName("agent_order_number"))));
        }

        orderInfo.setOrderStatus(Constants.ORDER_STATUS_UNPAID);
        orderInfo.setCreateUserId(userId);
        orderInfo.setLastUpdateUserId(userId);
        DatabaseHelper.insert(orderInfo);

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
                orderDetail.setPrice(goodsSpecification.getTenantPrice());
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
        DatabaseHelper.insertAll(orderDetails);

        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setDiscountAmount(discountAmount);
        orderInfo.setPayableAmount(totalAmount.subtract(discountAmount));
        orderInfo.setPaidAmount(BigDecimal.ZERO);
        orderInfo.setLastUpdateRemark("保存订单信息！");
        DatabaseHelper.update(orderInfo);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(OrderUtils.buildOrderInfo(orderInfo, orderDetails));
        apiRest.setMessage("保存订单成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 获取订单信息
     *
     * @param obtainOrderInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainOrderInfo(ObtainOrderInfoModel obtainOrderInfoModel) {
        BigInteger orderInfoId = obtainOrderInfoModel.getOrderInfoId();
        SearchModel orderInfoSearchModel = new SearchModel(true);
        orderInfoSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, orderInfoSearchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        List<OrderDetail> orderDetails = DatabaseHelper.findAll(OrderDetail.class, orderDetailSearchModel);

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
        int page = obtainAllOrderInfosModel.getPage();
        int rows = obtainAllOrderInfosModel.getRows();
        BigInteger tenantId = obtainAllOrderInfosModel.getTenantId();
        BigInteger agentId = obtainAllOrderInfosModel.getAgentId();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, 0));
        if (tenantId != null) {
            searchConditions.add(new SearchCondition("tenant_id", Constants.ELEME_MESSAGE_SCHEMA_FILE_PATH, tenantId));
        }

        if (agentId != null) {
            searchConditions.add(new SearchCondition("agent_id", Constants.ELEME_MESSAGE_SCHEMA_FILE_PATH, agentId));
        }
        SearchModel searchModel = new SearchModel();
        searchModel.setSearchConditions(searchConditions);
        long total = DatabaseHelper.count(OrderInfo.class, searchModel);

        List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
        if (total > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel();
            pagedSearchModel.setSearchConditions(searchConditions);
            pagedSearchModel.setPage(obtainAllOrderInfosModel.getPage());
            pagedSearchModel.setRows(obtainAllOrderInfosModel.getRows());
            List<OrderInfo> orderInfos = DatabaseHelper.findAllPaged(OrderInfo.class, pagedSearchModel);

            if (CollectionUtils.isNotEmpty(orderInfos)) {
                List<BigInteger> orderIds = new ArrayList<BigInteger>();
                for (OrderInfo orderInfo : orderInfos) {
                    orderIds.add(orderInfo.getId());
                }

                SearchModel orderDetailSearchModel = new SearchModel(true);
                orderDetailSearchModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_IN, orderIds);
                List<OrderDetail> orderDetails = DatabaseHelper.findAll(OrderDetail.class, orderDetailSearchModel);
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
                    orders.add(OrderUtils.buildOrderInfo(orderInfo, orderDetailsMap.get(orderInfo.getId())));
                }
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("rows", orders);
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
        DatabaseHelper.universalUpdate(orderInfoUpdateModel);

        UpdateModel orderDetailUpdateModel = new UpdateModel(true);
        orderDetailUpdateModel.setTableName("order_detail");
        orderDetailUpdateModel.addContentValue("deleted", 1);
        orderDetailUpdateModel.addContentValue("last_update_user_id", userId);
        orderDetailUpdateModel.addContentValue("last_update_remark", "删除订单详情信息！");
        orderDetailUpdateModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_IN, orderInfoIds);
        DatabaseHelper.universalUpdate(orderDetailUpdateModel);
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
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, searchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        orderInfo.setDeleted(true);
        orderInfo.setLastUpdateUserId(userId);
        orderInfo.setLastUpdateRemark("删除订单信息！");
        DatabaseHelper.update(orderInfo);

        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName("order_detail");
        updateModel.addContentValue("deleted", 1);
        updateModel.addContentValue("last_update_user_id", userId);
        updateModel.addContentValue("last_update_remark", "删除订单详情信息！");
        updateModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        DatabaseHelper.universalUpdate(updateModel);

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
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, doPayModel.getOrderInfoId());
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, searchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        Map<String, String> requestParameters = new HashMap<String, String>();
        requestParameters.put("tenantId", "0");
        requestParameters.put("branchId", "0");
        requestParameters.put("userId", doPayModel.getUserId().toString());

        ApiRest apiRest = null;
        String notifyUrl = SystemPartitionUtils.getUrl(Constants.SERVICE_NAME_PLATFORM, "order", "");
        int paidScene = doPayModel.getPaidScene();
        if (paidScene == Constants.PAID_SCENE_WEI_XIN_PUBLIC_ACCOUNT || paidScene == Constants.PAID_SCENE_WEI_XIN_H5 || paidScene == Constants.PAID_SCENE_WEI_XIN_APP || paidScene == Constants.PAID_SCENE_WEI_XIN_NATIVE || paidScene == Constants.PAID_SCENE_WEI_XIN_MINI_PROGRAM) {
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
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_MINI_PROGRAM) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_MINI_PROGRAM;
            }
            requestParameters.put("tradeType", tradeType);
            apiRest = ProxyUtils.doPostWithRequestParameters(Constants.SERVICE_NAME_OUT, "weiXinPay", "unifiedOrder", requestParameters);
        } else if (paidScene == Constants.PAID_SCENE_ALIPAY_MOBILE_WEBSITE || paidScene == Constants.PAID_SCENE_ALIPAY_PC_WEBSITE || paidScene == Constants.PAID_SCENE_ALIPAY_APP) {
            requestParameters.put("subject", "订单支付");
            requestParameters.put("outTradeNo", orderInfo.getOrderNumber());
            requestParameters.put("totalAmount", new DecimalFormat("0.00").format(orderInfo.getTotalAmount()));
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

    /**
     * 处理订单支付回调
     *
     * @param orderNumber
     * @param paidType
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String handleCallback(String orderNumber, int paidType) throws IOException, ParseException {
        SearchModel orderInfoSearchModel = new SearchModel(true);
        orderInfoSearchModel.addSearchCondition("order_number", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderNumber);
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, orderInfoSearchModel);
        Validate.notNull(orderInfo, "订单不存在！");

        int orderStatus = orderInfo.getOrderStatus();
        if (orderStatus == Constants.ORDER_STATUS_PAID) {
            return Constants.SUCCESS;
        }

        Validate.isTrue(orderStatus == Constants.ORDER_STATUS_UNPAID, "订单状态异常！");

        BigInteger userId = CommonUtils.getServiceSystemUserId();
        userId = BigInteger.ZERO;

        orderInfo.setOrderStatus(Constants.ORDER_STATUS_PAID);
        orderInfo.setPaidAmount(orderInfo.getPayableAmount());
        orderInfo.setPaidType(paidType);
        orderInfo.setLastUpdateUserId(userId);
        orderInfo.setLastUpdateRemark("处理支付回调，修改订单状态！");
        DatabaseHelper.update(orderInfo);

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition("order_info_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfo.getId());
        List<OrderDetail> orderDetails = DatabaseHelper.findAll(OrderDetail.class, orderDetailSearchModel);

        BigInteger orderId = orderInfo.getId();
        Date occurrenceTime = new Date();

        List<SaleFlow> saleFlows = new ArrayList<SaleFlow>();
        int orderType = orderInfo.getOrderType();
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            BigInteger tenantId = orderInfo.getTenantId();
            SearchModel tenantSearchModel = new SearchModel(true);
            tenantSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
            Tenant tenant = DatabaseHelper.find(Tenant.class, tenantSearchModel);
            String partitionCode = tenant.getPartitionCode();
            String serviceName = CommonUtils.getServiceName(tenant.getBusiness());

            for (OrderDetail orderDetail : orderDetails) {
                BigInteger branchId = orderDetail.getBranchId();
                BigInteger goodsId = orderDetail.getGoodsId();
                SearchModel tenantGoodsSearchModel = new SearchModel(true);
                tenantGoodsSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
                tenantGoodsSearchModel.addSearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
                tenantGoodsSearchModel.addSearchCondition("goods_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsId);

                SearchModel goodsSearchModel = new SearchModel(true);
                goodsSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderDetail.getGoodsId());
                Goods goods = DatabaseHelper.find(Goods.class, goodsSearchModel);

                SearchModel goodsSpecificationSearchModel = new SearchModel(true);
                goodsSpecificationSearchModel.addSearchCondition("goods_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderDetail.getGoodsId());
                goodsSpecificationSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderDetail.getGoodsSpecificationId());
                GoodsSpecification goodsSpecification = DatabaseHelper.find(GoodsSpecification.class, goodsSpecificationSearchModel);

                int meteringMode = goods.getMeteringMode();
                if (meteringMode == 1) {
                    TenantGoods tenantGoods = DatabaseHelper.find(TenantGoods.class, tenantGoodsSearchModel);
                    if (tenantGoods != null) {
                        tenantGoods.setExpireTime(GoodsUtils.obtainExpireTime(tenantGoods.getExpireTime(), goodsSpecification));
                        tenantGoods.setLastUpdateUserId(userId);
                        tenantGoods.setLastUpdateRemark("商户续费成功，增加商户商品有效期！");
                        DatabaseHelper.update(tenantGoods);
                    } else {
                        tenantGoods = new TenantGoods();
                        tenantGoods.setTenantId(tenantId);
                        tenantGoods.setBranchId(branchId);
                        tenantGoods.setGoodsId(goodsId);
                        tenantGoods.setExpireTime(GoodsUtils.obtainExpireTime(null, goodsSpecification));
                        tenantGoods.setCreateUserId(userId);
                        tenantGoods.setLastUpdateUserId(userId);
                        tenantGoods.setLastUpdateRemark("商户购买商品，新增商户商品信息！");
                        DatabaseHelper.insert(tenantGoods);
                    }

                    SearchModel goodsTypeSearchModel = new SearchModel(true);
                    goodsTypeSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, goods.getGoodsTypeId());
                    GoodsType goodsType = DatabaseHelper.find(GoodsType.class, goodsTypeSearchModel);
                    if (StringUtils.isNotBlank(goodsType.getRenewSql())) {
                        Map<String, String> renewCallbackRequestParameters = new HashMap<String, String>();
                        renewCallbackRequestParameters.put("tenantId", tenantId.toString());
                        renewCallbackRequestParameters.put("branchId", branchId.toString());
                        renewCallbackRequestParameters.put("renewSql", goodsType.getRenewSql());
                        ApiRest renewCallbackApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, serviceName, "branch", "renewCallback", renewCallbackRequestParameters);
                        Validate.isTrue(renewCallbackApiRest.isSuccessful(), renewCallbackApiRest.getError());
                    }
                } else if (meteringMode == 2) {

                }
                SaleFlow saleFlow = new SaleFlow();
                saleFlow.setOrderId(orderId);
                saleFlow.setType(Constants.SALE_FLOW_TYPE_TENANT_FLOW);
                saleFlow.setTenantId(orderInfo.getTenantId());
                saleFlow.setBranchId(orderDetail.getBranchId());
                saleFlow.setOccurrenceTime(occurrenceTime);
                saleFlow.setGoodsId(orderDetail.getGoodsId());
                saleFlow.setGoodsName(orderDetail.getGoodsName());
                saleFlow.setGoodsSpecificationId(orderDetail.getGoodsSpecificationId());
                saleFlow.setGoodsSpecificationName(orderDetail.getGoodsSpecificationName());
                saleFlow.setQuantity(orderDetail.getQuantity());
                saleFlow.setPaidType(paidType);
                saleFlow.setCreateUserId(userId);
                saleFlow.setLastUpdateUserId(userId);
                saleFlow.setLastUpdateRemark("处理支付回调，生成销售流水！");
                saleFlows.add(saleFlow);
            }
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            List<ActivationCodeInfo> activationCodeInfos = new ArrayList<ActivationCodeInfo>();
            for (OrderDetail orderDetail : orderDetails) {
                int quantity = orderDetail.getQuantity();
                for (int index = 0; index < quantity; index++) {
                    ActivationCodeInfo activationCodeInfo = new ActivationCodeInfo();
                    activationCodeInfo.setAgentId(orderInfo.getAgentId());
                    activationCodeInfo.setOrderId(orderInfo.getId());
                    activationCodeInfo.setStatus(Constants.ACTIVATION_CODE_STATUS_NOT_USED);
                    activationCodeInfo.setActivationCode(ActivationCodeUtils.generateActivationCode());
                    activationCodeInfo.setCreateUserId(userId);
                    activationCodeInfo.setLastUpdateUserId(userId);
                    activationCodeInfo.setLastUpdateRemark("处理支付回调，生成激活码！");
                    activationCodeInfo.setGoodsId(orderDetail.getGoodsId());
                    activationCodeInfo.setGoodsSpecificationId(orderDetail.getGoodsSpecificationId());
                    activationCodeInfos.add(activationCodeInfo);
                }
                SaleFlow saleFlow = new SaleFlow();
                saleFlow.setOrderId(orderId);
                saleFlow.setType(Constants.SALE_FLOW_TYPE_AGENT_FLOW);
                saleFlow.setAgentId(orderInfo.getAgentId());
                saleFlow.setOccurrenceTime(occurrenceTime);
                saleFlow.setGoodsId(orderDetail.getGoodsId());
                saleFlow.setGoodsName(orderDetail.getGoodsName());
                saleFlow.setGoodsSpecificationId(orderDetail.getGoodsSpecificationId());
                saleFlow.setGoodsSpecificationName(orderDetail.getGoodsSpecificationName());
                saleFlow.setQuantity(orderDetail.getQuantity());
                saleFlow.setPaidType(paidType);
                saleFlow.setCreateUserId(userId);
                saleFlow.setLastUpdateUserId(userId);
                saleFlow.setLastUpdateRemark("处理支付回调，生成销售流水！");
                saleFlows.add(saleFlow);
            }
            DatabaseHelper.insertAll(activationCodeInfos);
        }
        DatabaseHelper.insertAll(saleFlows);
        return Constants.SUCCESS;
    }
}
