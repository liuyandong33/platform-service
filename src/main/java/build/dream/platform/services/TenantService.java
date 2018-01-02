package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.TenantGoodsMapper;
import build.dream.platform.mappers.TenantMapper;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TenantService {
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private TenantGoodsMapper tenantGoodsMapper;

    @Transactional(readOnly = true)
    public ApiRest findTenantInfoById(BigInteger tenantId) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, tenantId);
        Tenant tenant = tenantMapper.find(searchModel);

        ApiRest apiRest = new ApiRest();
        apiRest.setClassName(Tenant.class.getName());
        apiRest.setData(tenant);
        apiRest.setMessage("查询商户信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest findAllGoodses(BigInteger tenantId, BigInteger branchId) {
        List<Map<String, Object>> allGoodses = tenantGoodsMapper.findAllGoodses(tenantId, branchId);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(allGoodses);
        apiRest.setMessage("查询产品购买信息成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest findGoods(BigInteger tenantId, BigInteger branchId, BigInteger goodsId) {
        Map<String, Object> goods = tenantGoodsMapper.findGoods(tenantId, branchId, goodsId);
        Validate.notEmpty(goods, "未检索到产品购买信息！");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("goods", goods);
        data.put("currentTime", new Date());
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("查询产品购买信息成功！");
        return apiRest;
    }
}
