package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.TenantGoodsMapper;
import build.dream.platform.models.tenant.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class TenantService {
    @Autowired
    private TenantGoodsMapper tenantGoodsMapper;

    /**
     * 获取商户信息系
     *
     * @param obtainTenantInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainTenantInfo(ObtainTenantInfoModel obtainTenantInfoModel) {
        BigInteger tenantId = obtainTenantInfoModel.getTenantId();
        String tenantCode = obtainTenantInfoModel.getTenantCode();

        SearchModel searchModel = new SearchModel(true);
        if (tenantId != null) {
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        }
        if (StringUtils.isNotBlank(tenantCode)) {
            searchModel.addSearchCondition("code", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantCode);
        }
        Tenant tenant = DatabaseHelper.find(Tenant.class, searchModel);

        ApiRest apiRest = new ApiRest();
        apiRest.setClassName(Tenant.class.getName());
        apiRest.setData(tenant);
        apiRest.setMessage("查询商户信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 获取商户购买的所有商品
     *
     * @param findAllGoodsInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest findAllGoodsInfos(FindAllGoodsInfosModel findAllGoodsInfosModel) {
        List<Map<String, Object>> goodsInfos = tenantGoodsMapper.findAllGoodsInfos(findAllGoodsInfosModel.getTenantId(), findAllGoodsInfosModel.getBranchId());
        ApiRest apiRest = new ApiRest();
        apiRest.setData(goodsInfos);
        apiRest.setMessage("查询产品购买信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 获取商户购买商品信息
     *
     * @param findGoodsInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest findGoodsInfo(FindGoodsInfoModel findGoodsInfoModel) {
        Map<String, Object> goodsInfo = tenantGoodsMapper.findGoodsInfo(findGoodsInfoModel.getTenantId(), findGoodsInfoModel.getBranchId(), findGoodsInfoModel.getGoodsId());
        Validate.notEmpty(goodsInfo, "未检索到产品购买信息！");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("goods", goodsInfo);
        data.put("currentTime", new Date());
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("查询产品购买信息成功！");
        return apiRest;
    }

    /**
     * 获取支付账号
     *
     * @param obtainPayAccountsModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainPayAccounts(ObtainPayAccountsModel obtainPayAccountsModel) {
        BigInteger tenantId = obtainPayAccountsModel.getTenantId();
        BigInteger branchId = obtainPayAccountsModel.getBranchId();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, tenantId));
        searchConditions.add(new SearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, branchId));
        searchConditions.add(new SearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, 0));

        SearchModel alipayAccountSearchModel = new SearchModel();
        alipayAccountSearchModel.setSearchConditions(searchConditions);

        SearchModel weiXinPayAccountSearchModel = new SearchModel();
        weiXinPayAccountSearchModel.setSearchConditions(searchConditions);

        SearchModel bankAccountSearchModel = new SearchModel();
        bankAccountSearchModel.setSearchConditions(searchConditions);

        AlipayAccount alipayAccount = DatabaseHelper.find(AlipayAccount.class, alipayAccountSearchModel);
        WeiXinPayAccount weiXinPayAccount = DatabaseHelper.find(WeiXinPayAccount.class, weiXinPayAccountSearchModel);
        BankAccount bankAccount = DatabaseHelper.find(BankAccount.class, bankAccountSearchModel);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("alipay", alipayAccount);
        data.put("weiXin", weiXinPayAccount);
        data.put("bank", bankAccount);
        return new ApiRest(data, "获取付款账号成功！");
    }

    /**
     * 获取所有商户信息
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Tenant> obtainAllTenantInfos() {
        SearchModel searchModel = new SearchModel(true);
        return DatabaseHelper.findAll(Tenant.class, searchModel);
    }

    /**
     * 获取商户秘钥
     *
     * @param obtainTenantSecretKeyModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainTenantSecretKey(ObtainTenantSecretKeyModel obtainTenantSecretKeyModel) {
        BigInteger tenantId = obtainTenantSecretKeyModel.getTenantId();
        SearchModel tenantSecretKeySearchModel = new SearchModel(true);
        tenantSecretKeySearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        TenantSecretKey tenantSecretKey = DatabaseHelper.find(TenantSecretKey.class, tenantSecretKeySearchModel);
        ValidateUtils.notNull(tenantSecretKey, "未检索到商户秘钥！");

        ApiRest apiRest = new ApiRest();
        apiRest.setData(tenantSecretKey);
        apiRest.setClassName(TenantSecretKey.class.getName());
        apiRest.setMessage("获取商户秘钥成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest updateTenantInfo(UpdateTenantInfoModel updateTenantInfoModel) {
        BigInteger id = updateTenantInfoModel.getId();
        String name = updateTenantInfoModel.getName();
        Integer vipSharedType = updateTenantInfoModel.getVipSharedType();
        BigInteger userId = updateTenantInfoModel.getUserId();

        Tenant tenant = DatabaseHelper.find(Tenant.class, id);
        ValidateUtils.notNull(tenant, "商户不存在！");
        if (StringUtils.isNotBlank(name)) {
            tenant.setName(name);
        }

        if (vipSharedType != null) {
            tenant.setVipSharedType(vipSharedType);
        }

        tenant.setLastUpdateUserId(userId);
        DatabaseHelper.update(tenant);

        return ApiRest.builder().message("修改商户信息成功！").successful(true).build();
    }
}
