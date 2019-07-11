package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.beans.District;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.register.RegisterAgentModel;
import build.dream.platform.models.register.RegisterTenantModel;
import build.dream.platform.utils.SequenceUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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
    public ApiRest registerTenant(RegisterTenantModel registerTenantModel) {
        String name = registerTenantModel.getName();
        String mobile = registerTenantModel.getMobile();
        String email = registerTenantModel.getEmail();
        String linkman = registerTenantModel.getLinkman();
        String business = registerTenantModel.getBusiness();
        String provinceCode = registerTenantModel.getProvinceCode();
        String cityCode = registerTenantModel.getCityCode();
        String districtCode = registerTenantModel.getDistrictCode();
        String address = registerTenantModel.getAddress();
        String longitude = registerTenantModel.getLongitude();
        String latitude = registerTenantModel.getLatitude();
        String contactPhone = registerTenantModel.getContactPhone();
        String password = registerTenantModel.getPassword();
        Integer tenantType = registerTenantModel.getTenantType();
        Integer vipSharedType = registerTenantModel.getVipSharedType();

        SearchModel mobileCountSearchModel = new SearchModel(true);
        mobileCountSearchModel.addSearchCondition(SystemUser.ColumnName.MOBILE, Constants.SQL_OPERATION_SYMBOL_EQUAL, mobile);
        ValidateUtils.isTrue(DatabaseHelper.count(SystemUser.class, mobileCountSearchModel) == 0, "手机号码已注册！");

        SearchModel emailCountSearchModel = new SearchModel(true);
        emailCountSearchModel.addSearchCondition(SystemUser.ColumnName.EMAIL, Constants.SQL_OPERATION_SYMBOL_EQUAL, email);
        ValidateUtils.isTrue(DatabaseHelper.count(SystemUser.class, emailCountSearchModel) == 0, "邮箱已注册！");

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(Goods.ColumnName.GOODS_TYPE_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, BigInteger.ONE);
        searchModel.addSearchCondition(Goods.ColumnName.STATUS, Constants.SQL_OPERATION_SYMBOL_EQUAL, 1);
        searchModel.addSearchCondition(Goods.ColumnName.BUSINESS, Constants.SQL_OPERATION_SYMBOL_EQUAL, business);
        Goods goods = DatabaseHelper.find(Goods.class, searchModel);
        ValidateUtils.notNull(goods, "未查询到基础服务商品！");

        String partitionCode = null;
        if (Constants.BUSINESS_CATERING.equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.CATERING_CURRENT_PARTITION_CODE);
        } else if (Constants.BUSINESS_RETAIL.equals(business)) {
            partitionCode = ConfigurationUtils.getConfiguration(Constants.RETAIL_CURRENT_PARTITION_CODE);
        }
        Integer currentPartitionQuantity = SequenceUtils.nextValue(partitionCode);
        ValidateUtils.isTrue(currentPartitionQuantity <= 2000, "分区已满无法创建商户！");

        BigInteger userId = CommonUtils.getServiceSystemUserId();

        District province = DistrictUtils.obtainDistrictById(provinceCode);
        ValidateUtils.notNull(province, "省编码错误！");

        District city = DistrictUtils.obtainDistrictById(cityCode);
        ValidateUtils.notNull(province, "市编码错误！");

        District district = DistrictUtils.obtainDistrictById(districtCode);
        ValidateUtils.notNull(province, "区域编码错误！");

        String tenantCode = SerialNumberGenerator.nextSerialNumber(8, SequenceUtils.nextValue(Constants.SEQUENCE_NAME_TENANT_CODE));
        Tenant tenant = Tenant.builder()
                .code(tenantCode)
                .name(name)
                .business(business)
                .provinceCode(provinceCode)
                .provinceName(province.getName())
                .cityCode(cityCode)
                .cityName(city.getName())
                .districtCode(districtCode)
                .districtName(district.getName())
                .address(address)
                .email(email)
                .partitionCode(partitionCode)
                .tenantType(tenantType)
                .vipSharedType(vipSharedType)
                .agentId(Constants.BIGINT_DEFAULT_VALUE)
                .usedChannelType(Constants.TINYINT_DEFAULT_VALUE)
                .dadaSourceId(Constants.BIGINT_DEFAULT_VALUE)
                .jddjVenderId(Constants.VARCHAR_DEFAULT_VALUE)
                .jddjAppKey(Constants.VARCHAR_DEFAULT_VALUE)
                .jddjAppSecret(Constants.VARCHAR_DEFAULT_VALUE)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("注册商户！")
                .build();
        DatabaseHelper.insert(tenant);

        BigInteger tenantId = tenant.getId();

        SystemUser systemUser = SystemUser.builder()
                .name(linkman)
                .mobile(mobile)
                .email(email)
                .loginName(tenantCode)
                .password(BCryptUtils.encode(password))
                .userType(Constants.USER_TYPE_TENANT)
                .tenantId(tenantId)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .createdUserId(userId)
                .updatedUserId(userId)
                .build();
        DatabaseHelper.insert(systemUser);

        Map<String, byte[]> rsaKeys = RSAUtils.generateKeyPair(2048);
        String publicKey = Base64.encodeBase64String(rsaKeys.get(Constants.PUBLIC_KEY));
        String privateKey = Base64.encodeBase64String(rsaKeys.get(Constants.PRIVATE_KEY));

        TenantSecretKey tenantSecretKey = TenantSecretKey.builder()
                .tenantId(tenantId)
                .tenantCode(tenantCode)
                .publicKey(publicKey)
                .privateKey(privateKey)
                .platformPublicKey(ConfigurationUtils.getConfiguration(Constants.PLATFORM_PUBLIC_KEY))
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("新增商户，增加商户秘钥！")
                .build();
        DatabaseHelper.insert(tenantSecretKey);

        String serviceName = CommonUtils.getServiceName(business);
        Map<String, String> initializeBranchRequestParameters = new HashMap<String, String>();
        initializeBranchRequestParameters.put("tenantId", tenantId.toString());
        initializeBranchRequestParameters.put("tenantCode", tenantCode);
        initializeBranchRequestParameters.put("provinceCode", provinceCode);
        initializeBranchRequestParameters.put("cityCode", cityCode);
        initializeBranchRequestParameters.put("districtCode", districtCode);
        initializeBranchRequestParameters.put("address", address);
        initializeBranchRequestParameters.put("longitude", longitude);
        initializeBranchRequestParameters.put("latitude", latitude);
        initializeBranchRequestParameters.put("userId", systemUser.getId().toString());
        initializeBranchRequestParameters.put("linkman", linkman);
        initializeBranchRequestParameters.put("contactPhone", contactPhone);
        initializeBranchRequestParameters.put("smartRestaurantStatus", Constants.SMART_RESTAURANT_STATUS_DISABLED.toString());
        ApiRest initializeBranchApiRest = ProxyUtils.doPostWithRequestParameters(partitionCode, serviceName, "branch", "initializeBranch", initializeBranchRequestParameters);
        ValidateUtils.isTrue(initializeBranchApiRest.isSuccessful(), initializeBranchApiRest.getError());

        Map<String, Object> branchInfo = ApplicationHandler.toMap(initializeBranchApiRest.getData());
        BigInteger branchId = BigInteger.valueOf(MapUtils.getLongValue(branchInfo, "id"));

        String basicServicesGoodsFreeTrialDays = ConfigurationUtils.getConfiguration(Constants.BASIC_SERVICES_GOODS_FREE_TRIAL_DAYS);
        if (StringUtils.isBlank(basicServicesGoodsFreeTrialDays)) {
            basicServicesGoodsFreeTrialDays = "30";
        }

        Date expireTime = DateUtils.addDays(CustomDateUtils.parse(CustomDateUtils.format(new Date(), "yyyy-MM-dd") + " 00:00:00", Constants.DEFAULT_DATE_PATTERN), Integer.valueOf(basicServicesGoodsFreeTrialDays));
        TenantGoods tenantGoods = TenantGoods.builder()
                .tenantId(tenantId)
                .branchId(branchId)
                .goodsId(goods.getId())
                .expireTime(expireTime)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("注册商户，创建试用商品！")
                .build();
        DatabaseHelper.insert(tenantGoods);

        String tenantInfo = JacksonUtils.writeValueAsString(tenant);
        CommonRedisUtils.hset(Constants.KEY_TENANT_INFOS, "_id_" + tenantId, tenantInfo);
        CommonRedisUtils.hset(Constants.KEY_TENANT_INFOS, "_code_" + tenantCode, tenantInfo);
        CommonRedisUtils.hset(Constants.KEY_USER_INFOS, systemUser.getId().toString(), JacksonUtils.writeValueAsString(systemUser));

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
        String name = registerAgentModel.getName();
        String linkman = registerAgentModel.getLinkman();
        String mobile = registerAgentModel.getMobile();
        String email = registerAgentModel.getEmail();
        String password = registerAgentModel.getPassword();
        ValidateUtils.isTrue(mobileIsUnique(mobile), "手机号码已经注册！");
        ValidateUtils.isTrue(emailIsUnique(email), "邮箱已经注册！");

        BigInteger userId = CommonUtils.getServiceSystemUserId();

        String agentCode = SerialNumberGenerator.nextSerialNumber(8, SequenceUtils.nextValue(Constants.SEQUENCE_NAME_AGENT_CODE));
        Agent agent = Agent.builder()
                .code(agentCode)
                .name(name)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("新增代理商信息！")
                .build();
        DatabaseHelper.insert(agent);

        SystemUser systemUser = SystemUser.builder()
                .name(linkman)
                .mobile(mobile)
                .email(email)
                .loginName(agent.getCode())
                .password(BCryptUtils.encode(password))
                .userType(Constants.USER_TYPE_AGENT)
                .agentId(agent.getId())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("新增代理商登录账号！")
                .build();
        DatabaseHelper.insert(systemUser);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("agent", agent);
        return ApiRest.builder().data(data).message("注册代理商成功！").successful(true).build();
    }
}
