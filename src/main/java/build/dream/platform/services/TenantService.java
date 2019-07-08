package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.beans.AlipayAccount;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.TenantGoodsMapper;
import build.dream.platform.mappers.TenantMapper;
import build.dream.platform.models.tenant.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class TenantService {
    @Autowired
    private TenantGoodsMapper tenantGoodsMapper;
    @Autowired
    private TenantMapper tenantMapper;

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
            searchModel.addSearchCondition(Tenant.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        }
        if (StringUtils.isNotBlank(tenantCode)) {
            searchModel.addSearchCondition(Tenant.ColumnName.CODE, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantCode);
        }
        Tenant tenant = DatabaseHelper.find(Tenant.class, searchModel);

        return ApiRest.builder().className(Tenant.class.getName()).data(tenant).message("查询商户信息成功！").successful(true).build();
    }

    /**
     * 获取商户购买的所有商品
     *
     * @param obtainAllGoodsInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAllGoodsInfos(ObtainAllGoodsInfosModel obtainAllGoodsInfosModel) {
        BigInteger tenantId = obtainAllGoodsInfosModel.getTenantId();
        BigInteger branchId = obtainAllGoodsInfosModel.getBranchId();

        List<Map<String, Object>> goodsInfos = tenantGoodsMapper.findAllGoodsInfos(tenantId, branchId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("goodsInfos", goodsInfos);
        data.put("currentTime", new Date());

        return ApiRest.builder().data(data).message("查询产品购买信息成功！").successful(true).build();
    }

    /**
     * 获取商户购买商品信息
     *
     * @param obtainGoodsInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainGoodsInfo(ObtainGoodsInfoModel obtainGoodsInfoModel) {
        BigInteger tenantId = obtainGoodsInfoModel.getTenantId();
        BigInteger branchId = obtainGoodsInfoModel.getBranchId();
        BigInteger goodsId = obtainGoodsInfoModel.getGoodsId();

        Map<String, Object> goodsInfo = tenantGoodsMapper.findGoodsInfo(tenantId, branchId, goodsId);
        ValidateUtils.notEmpty(goodsInfo, "未检索到产品购买信息！");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("goodsInfo", goodsInfo);
        data.put("currentTime", new Date());

        return ApiRest.builder().data(data).message("查询产品购买信息成功！").successful(true).build();
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
        searchConditions.add(new SearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId));
        searchConditions.add(new SearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, branchId));
        searchConditions.add(new SearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));

        SearchModel alipayAccountSearchModel = new SearchModel(searchConditions);
        SearchModel weiXinPayAccountSearchModel = new SearchModel(searchConditions);
        SearchModel bankAccountSearchModel = new SearchModel(searchConditions);
        SearchModel miyaAccountSearchModel = new SearchModel(searchConditions);
        SearchModel umPayAccountSearchModel = new SearchModel(searchConditions);
        SearchModel newLandAccountSearchModel = new SearchModel(searchConditions);

        AlipayAccount alipayAccount = DatabaseHelper.find(AlipayAccount.class, alipayAccountSearchModel);
        WeiXinPayAccount weiXinPayAccount = DatabaseHelper.find(WeiXinPayAccount.class, weiXinPayAccountSearchModel);
        BankAccount bankAccount = DatabaseHelper.find(BankAccount.class, bankAccountSearchModel);
        MiyaAccount miyaAccount = DatabaseHelper.find(MiyaAccount.class, miyaAccountSearchModel);
        UmPayAccount umPayAccount = DatabaseHelper.find(UmPayAccount.class, umPayAccountSearchModel);
        NewLandAccount newLandAccount = DatabaseHelper.find(NewLandAccount.class, newLandAccountSearchModel);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("alipay", alipayAccount);
        data.put("weiXin", weiXinPayAccount);
        data.put("bank", bankAccount);
        data.put("miya", miyaAccount);
        data.put("umPay", umPayAccount);
        data.put("newLand", newLandAccount);

        return ApiRest.builder().data(data).message("获取付款账号成功！").successful(true).build();
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
        tenantSecretKeySearchModel.addSearchCondition(TenantSecretKey.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        TenantSecretKey tenantSecretKey = DatabaseHelper.find(TenantSecretKey.class, tenantSecretKeySearchModel);
        ValidateUtils.notNull(tenantSecretKey, "未检索到商户秘钥！");

        return ApiRest.builder().data(tenantSecretKey).className(TenantSecretKey.class.getName()).message("获取商户秘钥成功！").successful(true).build();
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

        tenant.setUpdatedUserId(userId);
        DatabaseHelper.update(tenant);

        String tenantInfo = GsonUtils.toJson(tenant);
        CommonRedisUtils.hset(Constants.KEY_TENANT_INFOS, "_id_" + id, tenantInfo);
        CommonRedisUtils.hset(Constants.KEY_TENANT_INFOS, "_code_" + tenant.getCode(), tenantInfo);

        return ApiRest.builder().message("修改商户信息成功！").successful(true).build();
    }

    /**
     * 分页查询商户信息
     *
     * @param listTenantInfosModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listTenantInfos(ListTenantInfosModel listTenantInfosModel) {
        int page = listTenantInfosModel.getPage();
        int rows = listTenantInfosModel.getRows();
        String keyword = listTenantInfosModel.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }

        long count = tenantMapper.count(keyword);

        List<Map<String, Object>> tenantInfos = null;
        if (count > 0) {
            tenantInfos = tenantMapper.listTenantInfos(keyword, page, rows);
        } else {
            tenantInfos = new ArrayList<Map<String, Object>>();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", tenantInfos);

        return ApiRest.builder().data(data).message("查询成功！").successful(true).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest updateBranchCount(UpdateBranchCountModel updateBranchCountModel) {
        BigInteger tenantId = updateBranchCountModel.getTenantId();
        int changeCount = updateBranchCountModel.getChangeCount();
        int type = updateBranchCountModel.getType();

        UpdateModel updateModel = UpdateModel.builder()
                .autoSetDeletedFalse()
                .addContentValue(Tenant.ColumnName.DELETED, 1, 1)
                .addSearchCondition(Tenant.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, Constants.BIG_INTEGER_ONE)
                .build();
        DatabaseHelper.universalUpdate(updateModel, Tenant.TABLE_NAME);
        return ApiRest.builder().message("更新门店数量成功！").successful(true).build();
    }
}
