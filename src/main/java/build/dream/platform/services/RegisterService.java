package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.SequenceMapper;
import build.dream.platform.mappers.SystemUserMapper;
import build.dream.platform.mappers.TenantMapper;
import build.dream.platform.mappers.TenantSecretKeyMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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
    @Autowired
    private TenantSecretKeyMapper tenantSecretKeyMapper;

    @Transactional(rollbackFor = Exception.class)
    public ApiRest registerTenant(Map<String, String> parameters) throws NoSuchFieldException, InstantiationException, ParseException, IllegalAccessException, IOException {
        Tenant tenant = ApplicationHandler.instantiateObject(Tenant.class, parameters);
        String partitionCode = null;
        String business = tenant.getBusiness();
        if ("1".equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.CATERING_CURRENT_PARTITION_CODE);
        } else if ("2".equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.RETAIL_CURRENT_PARTITION_CODE);
        }
        Integer currentPartitionQuantity = sequenceMapper.nextValue(partitionCode);
        Validate.isTrue(currentPartitionQuantity <= 2000, "分区已满无法创建商户！");
        tenant.setPartitionCode(partitionCode);
        tenant.setCode(SerialNumberGenerator.nextSerialNumber(8, sequenceMapper.nextValue("tenant_code")));
        tenant.setCreateUserId(BigInteger.ZERO);
        tenant.setLastUpdateUserId(BigInteger.ZERO);

        SystemUser systemUser = new SystemUser();
        systemUser.setName(tenant.getLinkman());
        systemUser.setMobile(tenant.getMobile());
        systemUser.setEmail(tenant.getEmail());
        systemUser.setLoginName(tenant.getCode());
        systemUser.setUserType(Constants.USER_TYPE_TENANT);
        systemUser.setPassword(DigestUtils.md5Hex(parameters.get("password")));
        systemUser.setTenantId(tenant.getId());
        systemUser.setAccountNonExpired(true);
        systemUser.setAccountNonLocked(true);
        systemUser.setCredentialsNonExpired(true);
        systemUser.setEnabled(true);
        systemUser.setCreateUserId(BigInteger.ZERO);
        systemUser.setLastUpdateUserId(BigInteger.ZERO);
        systemUserMapper.insert(systemUser);

        tenant.setUserId(systemUser.getId());
        tenantMapper.insert(tenant);

        systemUser.setTenantId(tenant.getId());
        systemUserMapper.update(systemUser);
        String serviceName = CommonUtils.getServiceName(business);
        Map<String, String> initializeBranchRequestParameters = new HashMap<String, String>();
        initializeBranchRequestParameters.put("tenantId", tenant.getId().toString());
        initializeBranchRequestParameters.put("tenantCode", tenant.getCode());
        initializeBranchRequestParameters.put("userId", tenant.getUserId().toString());
        ApiRest initializeBranchApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, serviceName, "branch", "initializeBranch", initializeBranchRequestParameters);
        Validate.isTrue(initializeBranchApiRest.isSuccessful(), initializeBranchApiRest.getError());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("tenant", tenant);
        data.put("branch", initializeBranchApiRest.getData());
        ApiRest apiRest = new ApiRest(data, "注册商户成功！");
        return apiRest;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest test() throws NoSuchAlgorithmException {
        TenantSecretKey tenantSecretKey = new TenantSecretKey();
        tenantSecretKey.setTenantId(BigInteger.ZERO);
        tenantSecretKey.setTenantCode("61011888");
        String[] aa = RSAUtils.generateKeyPair(2048);
        tenantSecretKey.setPublicKey(aa[0]);
        tenantSecretKey.setPrivateKey(aa[1]);
        tenantSecretKey.setCreateUserId(BigInteger.ONE);
        tenantSecretKey.setLastUpdateUserId(BigInteger.TEN);
        tenantSecretKeyMapper.insert(tenantSecretKey);
        return new ApiRest();
    }
}
