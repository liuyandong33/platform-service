package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AlipayAccount;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.saas.domains.WeiXinPayAccount;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.TenantGoodsMapper;
import build.dream.platform.models.tenant.FindAllGoodsInfosModel;
import build.dream.platform.models.tenant.FindGoodsInfoModel;
import build.dream.platform.models.tenant.ObtainPayAccountsModel;
import build.dream.platform.models.tenant.ObtainTenantInfoModel;
import build.dream.common.utils.DatabaseHelper;
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

    @Transactional(readOnly = true)
    public ApiRest findAllGoodsInfos(FindAllGoodsInfosModel findAllGoodsInfosModel) {
        List<Map<String, Object>> goodsInfos = tenantGoodsMapper.findAllGoodsInfos(findAllGoodsInfosModel.getTenantId(), findAllGoodsInfosModel.getBranchId());
        ApiRest apiRest = new ApiRest();
        apiRest.setData(goodsInfos);
        apiRest.setMessage("查询产品购买信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

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

    @Transactional(readOnly = true)
    public ApiRest obtainPayAccounts(ObtainPayAccountsModel obtainPayAccountsModel) {
        BigInteger tenantId = obtainPayAccountsModel.getTenantId();
        BigInteger branchId = obtainPayAccountsModel.getBranchId();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, tenantId));
        searchConditions.add(new SearchCondition("branch_id", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, branchId));
        searchConditions.add(new SearchCondition("deleted", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUALS, 0));

        SearchModel alipayAccountSearchModel = new SearchModel(true);
        alipayAccountSearchModel.setSearchConditions(searchConditions);

        SearchModel weiXinPayAccountSearchModel = new SearchModel(true);
        weiXinPayAccountSearchModel.setSearchConditions(searchConditions);

        AlipayAccount alipayAccount = DatabaseHelper.find(AlipayAccount.class, alipayAccountSearchModel);
        WeiXinPayAccount weiXinPayAccount = DatabaseHelper.find(WeiXinPayAccount.class, weiXinPayAccountSearchModel);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("alipay", alipayAccount);
        data.put("weiXin", weiXinPayAccount);
        return new ApiRest(data, "获取付款账号成功！");
    }
}
