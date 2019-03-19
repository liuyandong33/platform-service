package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.models.alipay.AlipayTradeAppPayModel;
import build.dream.common.models.alipay.AlipayTradePagePayModel;
import build.dream.common.models.alipay.AlipayTradePayModel;
import build.dream.common.models.alipay.AlipayTradeWapPayModel;
import build.dream.common.models.weixinpay.MicroPayModel;
import build.dream.common.models.weixinpay.UnifiedOrderModel;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.order.*;
import build.dream.platform.utils.ActivationCodeUtils;
import build.dream.platform.utils.GoodsUtils;
import build.dream.platform.utils.SequenceUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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
        goodsSearchModel.addSearchCondition(Goods.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, goodsIds);
        List<Goods> goodses = DatabaseHelper.findAll(Goods.class, goodsSearchModel);
        Map<BigInteger, Goods> goodsMap = new LinkedHashMap<BigInteger, Goods>();
        for (Goods goods : goodses) {
            goodsMap.put(goods.getId(), goods);
        }

        SearchModel goodsSpecificationSearchModel = new SearchModel(true);
        goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, goodsSpecificationIds);
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
        orderInfo.setCreatedUserId(userId);
        orderInfo.setUpdatedUserId(userId);
        DatabaseHelper.insert(orderInfo);

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (SaveOrderModel.GoodsInfo goodsInfo : goodsInfos) {
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            ValidateUtils.notNull(goods, "产品不存在！");

            GoodsSpecification goodsSpecification = goodsSpecificationMap.get(goodsInfo.getGoodsSpecificationId());
            ValidateUtils.notNull(goodsSpecification, "产品规格不存在！");

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
            orderDetail.setCreatedUserId(userId);
            orderDetail.setUpdatedUserId(userId);
            orderDetail.setUpdatedRemark("保存订单详情信息！");
            orderDetails.add(orderDetail);
        }
        DatabaseHelper.insertAll(orderDetails);

        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setDiscountAmount(discountAmount);
        orderInfo.setPayableAmount(totalAmount.subtract(discountAmount));
        orderInfo.setPaidAmount(BigDecimal.ZERO);
        orderInfo.setUpdatedRemark("保存订单信息！");
        DatabaseHelper.update(orderInfo);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("orderInfo", orderInfo);
        data.put("orderDetails", orderDetails);

        return ApiRest.builder().data(data).message("保存订单成功！").successful(true).build();
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
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, orderInfoId);
        ValidateUtils.notNull(orderInfo, "订单不存在！");

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition(OrderDetail.ColumnName.ORDER_INFO_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        List<OrderDetail> orderDetails = DatabaseHelper.findAll(OrderDetail.class, orderDetailSearchModel);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("orderInfo", orderInfo);
        data.put("orderDetails", orderDetails);
        return ApiRest.builder().data(data).message("获取订单信息成功！").successful(true).build();
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
        searchConditions.add(new SearchCondition(OrderInfo.ColumnName.DELETED, Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, 0));
        if (tenantId != null) {
            searchConditions.add(new SearchCondition(OrderInfo.ColumnName.TENANT_ID, Constants.ELEME_MESSAGE_SCHEMA_FILE_PATH, tenantId));
        }

        if (agentId != null) {
            searchConditions.add(new SearchCondition(OrderInfo.ColumnName.AGENT_ID, Constants.ELEME_MESSAGE_SCHEMA_FILE_PATH, agentId));
        }
        SearchModel searchModel = new SearchModel();
        searchModel.setSearchConditions(searchConditions);
        long total = DatabaseHelper.count(OrderInfo.class, searchModel);

        List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
        if (total > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel();
            pagedSearchModel.setSearchConditions(searchConditions);
            pagedSearchModel.setPage(page);
            pagedSearchModel.setRows(rows);
            List<OrderInfo> orderInfos = DatabaseHelper.findAllPaged(OrderInfo.class, pagedSearchModel);

            List<BigInteger> orderIds = new ArrayList<BigInteger>();
            for (OrderInfo orderInfo : orderInfos) {
                orderIds.add(orderInfo.getId());
            }

            SearchModel orderDetailSearchModel = new SearchModel(true);
            orderDetailSearchModel.addSearchCondition(OrderDetail.ColumnName.ORDER_INFO_ID, Constants.SQL_OPERATION_SYMBOL_IN, orderIds);
            List<OrderDetail> orderDetails = DatabaseHelper.findAll(OrderDetail.class, orderDetailSearchModel);
            Map<BigInteger, List<OrderDetail>> orderDetailsMap = orderDetails.stream().collect(Collectors.groupingBy(OrderDetail::getOrderInfoId));

            for (OrderInfo orderInfo : orderInfos) {
                Map<String, Object> order = new HashMap<String, Object>();
                order.put("orderInfo", orderInfo);
                order.put("orderDetails", orderDetailsMap.get(orderInfo.getId()));
                orders.add(order);
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("rows", orders);
        data.put("total", total);
        return ApiRest.builder().data(data).message("获取订单信息成功！").successful(true).build();
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
        orderInfoUpdateModel.setTableName(OrderInfo.TABLE_NAME);
        orderInfoUpdateModel.addContentValue(OrderInfo.ColumnName.DELETED, 1);
        orderInfoUpdateModel.addContentValue(OrderInfo.ColumnName.UPDATED_USER_ID, userId);
        orderInfoUpdateModel.addContentValue(OrderInfo.ColumnName.UPDATED_REMARK, "删除订单信息！");
        orderInfoUpdateModel.addSearchCondition(OrderInfo.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, orderInfoIds);
        DatabaseHelper.universalUpdate(orderInfoUpdateModel);

        UpdateModel orderDetailUpdateModel = new UpdateModel(true);
        orderDetailUpdateModel.setTableName(OrderDetail.TABLE_NAME);
        orderDetailUpdateModel.addContentValue(OrderDetail.ColumnName.DELETED, 1);
        orderDetailUpdateModel.addContentValue(OrderDetail.ColumnName.UPDATED_USER_ID, userId);
        orderDetailUpdateModel.addContentValue(OrderDetail.ColumnName.UPDATED_REMARK, "删除订单详情信息！");
        orderDetailUpdateModel.addSearchCondition(OrderDetail.ColumnName.ORDER_INFO_ID, Constants.SQL_OPERATION_SYMBOL_IN, orderInfoIds);
        DatabaseHelper.universalUpdate(orderDetailUpdateModel);
        return ApiRest.builder().message("删除订单信息成功！").successful(true).build();
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
        searchModel.addSearchCondition(OrderInfo.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, searchModel);
        ValidateUtils.notNull(orderInfo, "订单不存在！");

        orderInfo.setDeleted(true);
        orderInfo.setUpdatedUserId(userId);
        orderInfo.setUpdatedRemark("删除订单信息！");
        DatabaseHelper.update(orderInfo);

        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName(OrderDetail.TABLE_NAME);
        updateModel.addContentValue(OrderDetail.ColumnName.ID, 1);
        updateModel.addContentValue(OrderDetail.ColumnName.UPDATED_USER_ID, userId);
        updateModel.addContentValue(OrderDetail.ColumnName.UPDATED_REMARK, "删除订单详情信息！");
        updateModel.addSearchCondition(OrderDetail.ColumnName.ORDER_INFO_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        DatabaseHelper.universalUpdate(updateModel);

        return ApiRest.builder().message("删除订单信息成功！").successful(true).build();
    }

    /**
     * 发起支付
     *
     * @param doPayModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest doPay(DoPayModel doPayModel) throws DocumentException {
        BigInteger orderInfoId = doPayModel.getOrderInfoId();
        int paidScene = doPayModel.getPaidScene();
        String authCode = doPayModel.getAuthCode();
        String openId = doPayModel.getOpenId();
        String subOpenId = doPayModel.getSubOpenId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfoId);
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, searchModel);
        ValidateUtils.notNull(orderInfo, "订单不存在！");

        String orderNumber = orderInfo.getOrderNumber();
        String tenantId = "0";
        String branchId = "0";

        String serviceDomain = CommonUtils.getServiceDomain(Constants.SERVICE_NAME_PLATFORM);
        BigDecimal payableAmount = orderInfo.getPayableAmount().setScale(2, RoundingMode.DOWN);

        Object data = null;
        if (paidScene == Constants.PAID_SCENE_WEI_XIN_MICROPAY) {
            MicroPayModel microPayModel = MicroPayModel.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .signType(Constants.MD5)
                    .body("订单支付")
                    .outTradeNo(orderNumber)
                    .totalFee(payableAmount.multiply(Constants.BIG_DECIMAL_ONE_HUNDRED).intValue())
                    .spbillCreateIp(ApplicationHandler.getRemoteAddress())
                    .authCode(authCode)
                    .build();
            data = WeiXinPayUtils.microPay(microPayModel);
        } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_JSAPI_PUBLIC_ACCOUNT || paidScene == Constants.PAID_SCENE_WEI_XIN_NATIVE || paidScene == Constants.PAID_SCENE_WEI_XIN_APP || paidScene == Constants.PAID_SCENE_WEI_XIN_MWEB || paidScene == Constants.PAID_SCENE_WEI_XIN_JSAPI_MINI_PROGRAM) {
            String tradeType = null;
            if (paidScene == Constants.PAID_SCENE_WEI_XIN_JSAPI_PUBLIC_ACCOUNT) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_JSAPI;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_NATIVE) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_MWEB;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_APP) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_APP;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_MWEB) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_NATIVE;
            } else if (paidScene == Constants.PAID_SCENE_WEI_XIN_JSAPI_MINI_PROGRAM) {
                tradeType = Constants.WEI_XIN_PAY_TRADE_TYPE_MINI_PROGRAM;
            }

            UnifiedOrderModel unifiedOrderModel = UnifiedOrderModel.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .signType(Constants.MD5)
                    .body("订单支付")
                    .outTradeNo(orderNumber)
                    .totalFee(payableAmount.multiply(Constants.BIG_DECIMAL_ONE_HUNDRED).intValue())
                    .spbillCreateIp(ApplicationHandler.getRemoteAddress())
                    .notifyUrl(serviceDomain + "/order/weiXinPayCallback")
                    .tradeType(tradeType)
                    .openId(openId)
                    .subOpenId(subOpenId)
                    .build();

            data = WeiXinPayUtils.unifiedOrder(unifiedOrderModel);
        } else if (paidScene == Constants.PAID_SCENE_ALIPAY_MOBILE_WEBSITE) {
            AlipayTradeWapPayModel alipayTradeWapPayModel = AlipayTradeWapPayModel.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .notifyUrl(serviceDomain + "/order/alipayCallback")
                    .subject("订单支付")
                    .outTradeNo(orderNumber)
                    .totalAmount(payableAmount)
                    .productCode(orderNumber)
                    .build();
            data = AlipayUtils.alipayTradeWapPay(alipayTradeWapPayModel);
        } else if (paidScene == Constants.PAID_SCENE_ALIPAY_PC_WEBSITE) {
            AlipayTradePagePayModel alipayTradePagePayModel = AlipayTradePagePayModel.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .notifyUrl(serviceDomain + "/order/alipayCallback")
                    .outTradeNo(orderNumber)
                    .productCode(orderNumber)
                    .totalAmount(payableAmount)
                    .subject("订单支付")
                    .build();
            data = AlipayUtils.alipayTradePagePay(alipayTradePagePayModel);
        } else if (paidScene == Constants.PAID_SCENE_ALIPAY_APP) {
            AlipayTradeAppPayModel alipayTradeAppPayModel = AlipayTradeAppPayModel.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .notifyUrl(serviceDomain + "/order/alipayCallback")
                    .outTradeNo(orderNumber)
                    .totalAmount(payableAmount)
                    .subject("订单支付")
                    .build();
            data = AlipayUtils.alipayTradeAppPay(alipayTradeAppPayModel);
        } else if (paidScene == Constants.PAID_SCENE_ALIPAY_FAC_TO_FACE) {
            AlipayTradePayModel alipayTradePayModel = AlipayTradePayModel.builder()
                    .tenantId(tenantId)
                    .branchId(branchId)
                    .notifyUrl(serviceDomain + "/order/alipayCallback")
                    .outTradeNo(orderNumber)
                    .totalAmount(payableAmount)
                    .scene(Constants.SCENE_BAR_CODE)
                    .authCode(authCode)
                    .subject("订单支付")
                    .build();
            data = AlipayUtils.alipayTradePay(alipayTradePayModel);
        }
        return ApiRest.builder().data(data).message("发起支付成功！").successful(true).build();
    }

    /**
     * 处理订单支付回调
     *
     * @param orderNumber
     * @param paidType
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String handleCallback(String orderNumber, int paidType) throws ParseException {
        SearchModel orderInfoSearchModel = new SearchModel(true);
        orderInfoSearchModel.addSearchCondition(OrderInfo.ColumnName.ORDER_NUMBER, Constants.SQL_OPERATION_SYMBOL_EQUAL, orderNumber);
        OrderInfo orderInfo = DatabaseHelper.find(OrderInfo.class, orderInfoSearchModel);
        ValidateUtils.notNull(orderInfo, "订单不存在！");

        int orderStatus = orderInfo.getOrderStatus();
        if (orderStatus == Constants.ORDER_STATUS_PAID) {
            return Constants.SUCCESS;
        }

        ValidateUtils.isTrue(orderStatus == Constants.ORDER_STATUS_UNPAID, "订单状态异常！");

        BigInteger userId = CommonUtils.getServiceSystemUserId();

        orderInfo.setOrderStatus(Constants.ORDER_STATUS_PAID);
        orderInfo.setPaidAmount(orderInfo.getPayableAmount());
        orderInfo.setPaidType(paidType);
        orderInfo.setUpdatedUserId(userId);
        orderInfo.setUpdatedRemark("处理支付回调，修改订单状态！");
        DatabaseHelper.update(orderInfo);

        SearchModel orderDetailSearchModel = new SearchModel(true);
        orderDetailSearchModel.addSearchCondition(OrderDetail.ColumnName.ORDER_INFO_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, orderInfo.getId());
        List<OrderDetail> orderDetails = DatabaseHelper.findAll(OrderDetail.class, orderDetailSearchModel);

        BigInteger orderId = orderInfo.getId();
        Date occurrenceTime = new Date();

        List<SaleFlow> saleFlows = new ArrayList<SaleFlow>();
        int orderType = orderInfo.getOrderType();
        if (orderType == Constants.ORDER_TYPE_TENANT_ORDER) {
            BigInteger tenantId = orderInfo.getTenantId();
            Tenant tenant = DatabaseHelper.find(Tenant.class, tenantId);
            String partitionCode = tenant.getPartitionCode();
            String serviceName = CommonUtils.getServiceName(tenant.getBusiness());

            for (OrderDetail orderDetail : orderDetails) {
                BigInteger branchId = orderDetail.getBranchId();
                BigInteger goodsId = orderDetail.getGoodsId();
                BigInteger goodsSpecificationId = orderDetail.getGoodsSpecificationId();
                int quantity = orderDetail.getQuantity();

                SearchModel tenantGoodsSearchModel = new SearchModel(true);
                tenantGoodsSearchModel.addSearchCondition(TenantGoods.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
                tenantGoodsSearchModel.addSearchCondition(TenantGoods.ColumnName.BRANCH_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId);
                tenantGoodsSearchModel.addSearchCondition(TenantGoods.ColumnName.GOODS_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsId);

                Goods goods = DatabaseHelper.find(Goods.class, goodsId);
                ValidateUtils.notNull(goods, "商品不存在！");

                SearchModel goodsSpecificationSearchModel = new SearchModel(true);
                goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.GOODS_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsId);
                goodsSpecificationSearchModel.addSearchCondition(GoodsSpecification.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, goodsSpecificationId);
                GoodsSpecification goodsSpecification = DatabaseHelper.find(GoodsSpecification.class, goodsSpecificationSearchModel);
                ValidateUtils.notNull(goodsSpecification, "商品规格不存在！");

                int meteringMode = goods.getMeteringMode();
                if (meteringMode == 1) {
                    TenantGoods tenantGoods = DatabaseHelper.find(TenantGoods.class, tenantGoodsSearchModel);
                    if (tenantGoods != null) {
                        tenantGoods.setExpireTime(GoodsUtils.obtainExpireTime(tenantGoods.getExpireTime(), goodsSpecification));
                        tenantGoods.setUpdatedUserId(userId);
                        tenantGoods.setUpdatedRemark("商户续费成功，增加商户商品有效期！");
                        DatabaseHelper.update(tenantGoods);
                    } else {
                        tenantGoods = TenantGoods.builder()
                                .tenantId(tenantId)
                                .branchId(branchId)
                                .goodsId(goodsId)
                                .expireTime(GoodsUtils.obtainExpireTime(null, goodsSpecification))
                                .createdUserId(userId)
                                .updatedUserId(userId)
                                .updatedRemark("商户购买商品，新增商户商品信息！")
                                .build();
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
                        ValidateUtils.isTrue(renewCallbackApiRest.isSuccessful(), renewCallbackApiRest.getError());
                    }
                } else if (meteringMode == 2) {

                }
                SaleFlow saleFlow = SaleFlow.builder()
                        .orderId(orderId)
                        .type(Constants.SALE_FLOW_TYPE_TENANT_FLOW)
                        .tenantId(tenantId)
                        .branchId(branchId)
                        .occurrenceTime(occurrenceTime)
                        .goodsId(goodsId)
                        .goodsName(orderDetail.getGoodsName())
                        .goodsSpecificationId(goodsSpecificationId)
                        .goodsSpecificationName(orderDetail.getGoodsSpecificationName())
                        .quantity(quantity)
                        .paidType(paidType)
                        .createdUserId(userId)
                        .updatedUserId(userId)
                        .updatedRemark("处理支付回调，生成销售流水！")
                        .build();
                saleFlows.add(saleFlow);
            }
        } else if (orderType == Constants.ORDER_TYPE_AGENT_ORDER) {
            BigInteger agentId = orderInfo.getAgentId();
            List<ActivationCodeInfo> activationCodeInfos = new ArrayList<ActivationCodeInfo>();
            for (OrderDetail orderDetail : orderDetails) {
                BigInteger goodsId = orderDetail.getGoodsId();
                BigInteger goodsSpecificationId = orderDetail.getGoodsSpecificationId();
                int quantity = orderDetail.getQuantity();
                for (int index = 0; index < quantity; index++) {
                    ActivationCodeInfo activationCodeInfo = ActivationCodeInfo.builder()
                            .agentId(agentId)
                            .orderId(orderId)
                            .status(Constants.ACTIVATION_CODE_STATUS_NOT_USED)
                            .activationCode(ActivationCodeUtils.generateActivationCode())
                            .goodsId(goodsId)
                            .goodsSpecificationId(goodsSpecificationId)
                            .createdUserId(userId)
                            .updatedUserId(userId)
                            .updatedRemark("处理支付回调，生成激活码！")
                            .build();
                    activationCodeInfos.add(activationCodeInfo);
                }
                SaleFlow saleFlow = SaleFlow.builder()
                        .orderId(orderId)
                        .type(Constants.SALE_FLOW_TYPE_AGENT_FLOW)
                        .agentId(agentId)
                        .occurrenceTime(occurrenceTime)
                        .goodsId(goodsId)
                        .goodsName(orderDetail.getGoodsName())
                        .goodsSpecificationId(goodsSpecificationId)
                        .goodsSpecificationName(orderDetail.getGoodsSpecificationName())
                        .quantity(quantity)
                        .paidType(paidType)
                        .createdUserId(userId)
                        .updatedUserId(userId)
                        .updatedRemark("处理支付回调，生成销售流水！")
                        .build();
                saleFlows.add(saleFlow);
            }
            DatabaseHelper.insertAll(activationCodeInfos);
        }
        DatabaseHelper.insertAll(saleFlows);
        return Constants.SUCCESS;
    }
}
