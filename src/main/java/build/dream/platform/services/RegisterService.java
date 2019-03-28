package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.register.RegisterAgentModel;
import build.dream.platform.models.register.RegisterTenantModel;
import build.dream.platform.utils.SequenceUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {
    /**
     * 注册商户
     *
     * @param registerTenantModel
     * @return
     * @throws NoSuchAlgorithmException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest registerTenant(RegisterTenantModel registerTenantModel) throws NoSuchAlgorithmException, ParseException {
        String mobile = registerTenantModel.getMobile();
        String email = registerTenantModel.getEmail();
        SearchModel mobileCountSearchModel = new SearchModel(true);
        mobileCountSearchModel.addSearchCondition(SystemUser.ColumnName.MOBILE, Constants.SQL_OPERATION_SYMBOL_EQUAL, mobile);
        ValidateUtils.isTrue(DatabaseHelper.count(SystemUser.class, mobileCountSearchModel) == 0, "手机号码已注册！");

        SearchModel emailCountSearchModel = new SearchModel(true);
        emailCountSearchModel.addSearchCondition(SystemUser.ColumnName.EMAIL, Constants.SQL_OPERATION_SYMBOL_EQUAL, email);
        ValidateUtils.isTrue(DatabaseHelper.count(SystemUser.class, emailCountSearchModel) == 0, "邮箱已注册！");
        String business = registerTenantModel.getBusiness();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(Goods.ColumnName.GOODS_TYPE_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, BigInteger.ONE);
        searchModel.addSearchCondition(Goods.ColumnName.STATUS, Constants.SQL_OPERATION_SYMBOL_EQUAL, 1);
        searchModel.addSearchCondition(Goods.ColumnName.BUSINESS, Constants.SQL_OPERATION_SYMBOL_EQUAL, business);
        Goods goods = DatabaseHelper.find(Goods.class, searchModel);
        ValidateUtils.notNull(goods, "未查询到基础服务商品！");

        Tenant tenant = new Tenant();
        tenant.setName(registerTenantModel.getName());
        tenant.setBusiness(business);

        String partitionCode = null;
        if (Constants.BUSINESS_CATERING.equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.CATERING_CURRENT_PARTITION_CODE);
        } else if (Constants.BUSINESS_RETAIL.equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.RETAIL_CURRENT_PARTITION_CODE);
        }
        Integer currentPartitionQuantity = SequenceUtils.nextValue(partitionCode);
        ValidateUtils.isTrue(currentPartitionQuantity <= 2000, "分区已满无法创建商户！");
        tenant.setPartitionCode(partitionCode);
        tenant.setCode(SerialNumberGenerator.nextSerialNumber(8, SequenceUtils.nextValue(Constants.SEQUENCE_NAME_TENANT_CODE)));
        tenant.setTenantType(registerTenantModel.getTenantType());

        BigInteger userId = CommonUtils.getServiceSystemUserId();
        tenant.setCreatedUserId(userId);
        tenant.setUpdatedUserId(userId);
        DatabaseHelper.insert(tenant);

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
        systemUser.setCreatedUserId(userId);
        systemUser.setUpdatedUserId(userId);
        DatabaseHelper.insert(systemUser);

        TenantSecretKey tenantSecretKey = new TenantSecretKey();
        tenantSecretKey.setTenantId(tenant.getId());
        tenantSecretKey.setTenantCode(tenant.getCode());
        Map<String, byte[]> rsaKeys = RSAUtils.generateKeyPair(2048);
        String publicKey = Base64.encodeBase64String(rsaKeys.get(Constants.PUBLIC_KEY));
        tenantSecretKey.setPublicKey(publicKey);
        tenantSecretKey.setPrivateKey(Base64.encodeBase64String(rsaKeys.get(Constants.PRIVATE_KEY)));
        tenantSecretKey.setPlatformPublicKey(ConfigurationUtils.getConfiguration(Constants.PLATFORM_PUBLIC_KEY));
        tenantSecretKey.setCreatedUserId(userId);
        tenantSecretKey.setUpdatedUserId(userId);
        tenantSecretKey.setUpdatedRemark("新增商户，增加商户秘钥！");
        DatabaseHelper.insert(tenantSecretKey);

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
        initializeBranchRequestParameters.put("smartRestaurantStatus", Constants.SMART_RESTAURANT_STATUS_DISABLED.toString());
        ApiRest initializeBranchApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, serviceName, "branch", "initializeBranch", initializeBranchRequestParameters);
        ValidateUtils.isTrue(initializeBranchApiRest.isSuccessful(), initializeBranchApiRest.getError());

        Map<String, Object> branchInfo = ApplicationHandler.toMap(initializeBranchApiRest.getData());
        BigInteger branchId = BigInteger.valueOf(MapUtils.getLongValue(branchInfo, "id"));

        String basicServicesGoodsFreeTrialDays = ConfigurationUtils.getConfiguration(Constants.BASIC_SERVICES_GOODS_FREE_TRIAL_DAYS);
        if (StringUtils.isBlank(basicServicesGoodsFreeTrialDays)) {
            basicServicesGoodsFreeTrialDays = "30";
        }
        TenantGoods tenantGoods = new TenantGoods();
        tenantGoods.setTenantId(tenant.getId());
        tenantGoods.setBranchId(branchId);
        tenantGoods.setGoodsId(goods.getId());
        tenantGoods.setExpireTime(DateUtils.addDays(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00"), Integer.valueOf(basicServicesGoodsFreeTrialDays)));
        tenantGoods.setCreatedUserId(userId);
        tenantGoods.setUpdatedUserId(userId);
        tenantGoods.setUpdatedRemark("注册商户，创建试用商品！");
        DatabaseHelper.insert(tenantGoods);

        String tenantInfo = GsonUtils.toJson(tenant);
        RedisUtils.hset(Constants.KEY_TENANT_INFOS, "_id_" + tenant.getId(), tenantInfo);
        RedisUtils.hset(Constants.KEY_TENANT_INFOS, "_code_" + tenant.getCode(), tenantInfo);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("tenant", tenant);
        data.put("branch", branchInfo);
        return ApiRest.builder().data(data).message("注册商户成功！").successful(true).build();
    }

    private boolean mobileIsUnique(String mobile) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("mobile", Constants.SQL_OPERATION_SYMBOL_EQUAL, mobile);
        SystemUser systemUser = DatabaseHelper.find(SystemUser.class, searchModel);
        return systemUser == null;
    }

    private boolean emailIsUnique(String email) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("email", Constants.SQL_OPERATION_SYMBOL_EQUAL, email);
        SystemUser systemUser = DatabaseHelper.find(SystemUser.class, searchModel);
        return systemUser == null;
    }

    /**
     * 注册代理商
     *
     * @param registerAgentModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest registerAgent(RegisterAgentModel registerAgentModel) {
        String mobile = registerAgentModel.getMobile();
        String email = registerAgentModel.getEmail();
        ValidateUtils.isTrue(mobileIsUnique(mobile), "手机号码已经注册！");
        ValidateUtils.isTrue(emailIsUnique(email), "邮箱已经注册！");

        BigInteger userId = CommonUtils.getServiceSystemUserId();

        Agent agent = new Agent();
        agent.setCode(SerialNumberGenerator.nextSerialNumber(8, SequenceUtils.nextValue(Constants.SEQUENCE_NAME_AGENT_CODE)));
        agent.setName(registerAgentModel.getName());
        agent.setCreatedUserId(userId);
        agent.setUpdatedUserId(userId);
        agent.setUpdatedRemark("新增代理商信息！");
        DatabaseHelper.insert(agent);

        SystemUser systemUser = new SystemUser();
        systemUser.setName(registerAgentModel.getLinkman());
        systemUser.setMobile(mobile);
        systemUser.setEmail(email);
        systemUser.setLoginName(agent.getCode());
        systemUser.setPassword(DigestUtils.md5Hex(registerAgentModel.getPassword()));
        systemUser.setUserType(Constants.USER_TYPE_AGENT);
        systemUser.setAgentId(agent.getId());
        systemUser.setAccountNonExpired(true);
        systemUser.setAccountNonLocked(true);
        systemUser.setCredentialsNonExpired(true);
        systemUser.setEnabled(true);
        systemUser.setCreatedUserId(userId);
        systemUser.setUpdatedUserId(userId);
        systemUser.setUpdatedRemark("新增代理商登录账号！");
        DatabaseHelper.insert(systemUser);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("agent", agent);
        return ApiRest.builder().data(data).message("注册代理商成功！").successful(true).build();
    }
}
