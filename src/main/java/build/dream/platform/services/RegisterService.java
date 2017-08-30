package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.SequenceMapper;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.mappers.TenantMapper;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private SequenceMapper sequenceMapper;

    @Transactional
    public Map<String, Object> registerTenant(Tenant tenant) throws IOException {
        String code = SerialNumberGenerator.nextSerialNumber(8, sequenceMapper.nextValue("tenant_code"));
        String partitionCode = null;
        String business = tenant.getBusiness();
        if ("1".equals(business)) {
            partitionCode = PropertyUtils.getProperty(Constants.CATERING_CURRENT_PARTITION_CODE);
        } else if ("2".equals(business)) {
            partitionCode = PropertyUtils.getProperty(Constants.RETAIL_CURRENT_PARTITION_CODE);
        }
        Integer currentPartitionQuantity = sequenceMapper.nextValue(partitionCode);
        Validate.isTrue(currentPartitionQuantity <= 2000, "分区已满无法创建商户！");
        tenant.setPartitionCode(partitionCode);
        tenant.setGoodsTableName("goods_" + code);
        tenant.setSaleTableName("sale_" + code);
        tenant.setCode(code);
        tenantMapper.insert(tenant);
        SystemUser systemUser = new SystemUser();
        systemUserMapper.insert(systemUser);
        tenantMapper.update(tenant);
        String serviceName = CommonUtils.getServiceName(business);
        Map<String, String> initializeBranchRequestParameters = new HashMap<String, String>();
        initializeBranchRequestParameters.put("tenantId", tenant.getId().toString());
        initializeBranchRequestParameters.put("tenantCode", tenant.getCode());
        initializeBranchRequestParameters.put("userId", tenant.getUserId().toString());
        String initializeBranchResult = ProxyUtils.doPostWithRequestParameters(partitionCode, serviceName, "", "", initializeBranchRequestParameters);
        ApiRest initializeBranchApiRest = GsonUtils.fromJson(initializeBranchResult, ApiRest.class);
        Validate.isTrue(initializeBranchApiRest.isSuccessful(), initializeBranchApiRest.getError());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("systemUser", systemUser);
        data.put("tenant", tenant);
        return data;
    }
}
