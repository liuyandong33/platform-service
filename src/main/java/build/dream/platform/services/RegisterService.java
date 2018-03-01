package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.SystemUser;
import build.dream.common.saas.domains.Tenant;
import build.dream.common.saas.domains.TenantGoods;
import build.dream.common.saas.domains.TenantSecretKey;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.*;
import build.dream.platform.models.register.RegisterTenantModel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
    @Autowired
    private TenantGoodsMapper tenantGoodsMapper;

    @Transactional(rollbackFor = Exception.class)
    public ApiRest registerTenant(RegisterTenantModel registerTenantModel) throws IOException, NoSuchAlgorithmException {
        Tenant tenant = new Tenant();
        tenant.setName(registerTenantModel.getName());

        String business = registerTenantModel.getBusiness();
        tenant.setBusiness(business);

        String partitionCode = null;
        if (Constants.BUSINESS_CATERING.equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.CATERING_CURRENT_PARTITION_CODE);
        } else if (Constants.BUSINESS_RETAIL.equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.RETAIL_CURRENT_PARTITION_CODE);
        }
        Integer currentPartitionQuantity = sequenceMapper.nextValue(partitionCode);
        Validate.isTrue(currentPartitionQuantity <= 2000, "分区已满无法创建商户！");
        tenant.setPartitionCode(partitionCode);
        tenant.setCode(SerialNumberGenerator.nextSerialNumber(8, sequenceMapper.nextValue("tenant_code")));
        tenant.setTenantType(registerTenantModel.getTenantType());

        BigInteger userId = CommonUtils.getServiceSystemUserId();
        tenant.setCreateUserId(userId);
        tenant.setLastUpdateUserId(userId);
        tenantMapper.insert(tenant);

        SystemUser systemUser = new SystemUser();
        systemUser.setName(registerTenantModel.getLinkman());
        systemUser.setMobile(registerTenantModel.getMobile());
        systemUser.setEmail(registerTenantModel.getEmail());
        systemUser.setLoginName(tenant.getCode());
        systemUser.setPassword(DigestUtils.md5Hex(registerTenantModel.getPassword()));
        systemUser.setUserType(Constants.USER_TYPE_TENANT);
        systemUser.setTenantId(tenant.getId());
        systemUser.setAccountNonExpired(true);
        systemUser.setAccountNonLocked(true);
        systemUser.setCredentialsNonExpired(true);
        systemUser.setEnabled(true);
        systemUser.setCreateUserId(userId);
        systemUser.setLastUpdateUserId(userId);
        systemUserMapper.insert(systemUser);

        TenantSecretKey tenantSecretKey = new TenantSecretKey();
        tenantSecretKey.setTenantId(tenant.getId());
        tenantSecretKey.setTenantCode(tenant.getCode());
        Map<String, byte[]> rsaKeys = RSAUtils.generateKeyPair(2048);
        tenantSecretKey.setPublicKey(Base64.encodeBase64String(rsaKeys.get("publicKey")));
        tenantSecretKey.setPrivateKey(Base64.encodeBase64String(rsaKeys.get("privateKey")));
        tenantSecretKey.setCreateUserId(userId);
        tenantSecretKey.setLastUpdateUserId(userId);
        tenantSecretKey.setLastUpdateRemark("新增商户，增加商户秘钥！");
        tenantSecretKeyMapper.insert(tenantSecretKey);

        String serviceName = CommonUtils.getServiceName(business);
        Map<String, String> initializeBranchRequestParameters = new HashMap<String, String>();
        initializeBranchRequestParameters.put("tenantId", tenant.getId().toString());
        initializeBranchRequestParameters.put("tenantCode", tenant.getCode());
        initializeBranchRequestParameters.put("provinceCode", registerTenantModel.getProvinceCode());
        initializeBranchRequestParameters.put("provinceName", registerTenantModel.getProvinceName());
        initializeBranchRequestParameters.put("cityCode", registerTenantModel.getCityCode());
        initializeBranchRequestParameters.put("cityName", registerTenantModel.getCityName());
        initializeBranchRequestParameters.put("districtCode", registerTenantModel.getDistrictCode());
        initializeBranchRequestParameters.put("districtName", registerTenantModel.getDistrictName());
        initializeBranchRequestParameters.put("address", registerTenantModel.getAddress());
        initializeBranchRequestParameters.put("longitude", registerTenantModel.getLongitude());
        initializeBranchRequestParameters.put("latitude", registerTenantModel.getLatitude());
        initializeBranchRequestParameters.put("userId", systemUser.getId().toString());
        initializeBranchRequestParameters.put("linkman", registerTenantModel.getLinkman());
        initializeBranchRequestParameters.put("contactPhone", registerTenantModel.getContactPhone());
        ApiRest initializeBranchApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, serviceName, "branch", "initializeBranch", initializeBranchRequestParameters);
        Validate.isTrue(initializeBranchApiRest.isSuccessful(), initializeBranchApiRest.getError());

        Map<String, Object> branchInfo = BeanUtils.beanToMap(initializeBranchApiRest.getData());
        BigInteger branchId = BigInteger.valueOf(MapUtils.getLongValue(branchInfo, "id"));

        TenantGoods tenantGoods = new TenantGoods();
        tenantGoods.setTenantId(tenant.getId());
        tenantGoods.setBranchId(branchId);
        tenantGoods.setGoodsId(BigInteger.ZERO);
        tenantGoods.setExpireTime(new Date());
        tenantGoods.setCreateUserId(userId);
        tenantGoods.setLastUpdateUserId(userId);
        tenantGoods.setLastUpdateRemark("注册商户，创建使用商品！");
        tenantGoodsMapper.insert(tenantGoods);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("tenant", tenant);
        data.put("branch", branchInfo);
        ApiRest apiRest = new ApiRest(data, "注册商户成功！");
        return apiRest;
    }
}
