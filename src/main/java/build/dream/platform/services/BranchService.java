package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.CommonUtils;
import build.dream.common.utils.ProxyUtils;
import build.dream.common.utils.ValidateUtils;
import build.dream.platform.mappers.TenantGoodsMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BranchService {
    @Autowired
    private TenantGoodsMapper tenantGoodsMapper;


    /**
     * 禁用门店产品
     *
     * @throws ParseException
     */
    @Transactional(readOnly = true)
    public void disableGoods() throws ParseException {
        Date expireTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
        List<Map<String, Object>> expiredBranches = tenantGoodsMapper.findAllExpiredBranches(expireTime);
        for (Map<String, Object> expiredBranch : expiredBranches) {
            String disableSql = MapUtils.getString(expiredBranch, "disableSql");
            if (StringUtils.isBlank(disableSql)) {
                continue;
            }
            String business = MapUtils.getString(expiredBranch, "business");
            String partitionCode = MapUtils.getString(expiredBranch, "partitionCode");
            String tenantId = MapUtils.getString(expiredBranch, "tenantId");
            String branchId = MapUtils.getString(expiredBranch, "branchId");
            Map<String, String> disableGoodsRequestParameters = new HashMap<String, String>();
            disableGoodsRequestParameters.put("tenantId", tenantId);
            disableGoodsRequestParameters.put("branchId", branchId);
            disableGoodsRequestParameters.put("disableSql", disableSql);
            ApiRest disableGoodsApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, CommonUtils.getServiceName(business), "branch", "disableGoods", disableGoodsRequestParameters);
            ValidateUtils.isTrue(disableGoodsApiRest.isSuccessful(), disableGoodsApiRest.getError());
        }
    }
}
