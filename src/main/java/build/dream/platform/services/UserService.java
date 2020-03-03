package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.constants.ErrorConstants;
import build.dream.common.domains.saas.*;
import build.dream.common.tuples.Tuple3;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.PrivilegeMapper;
import build.dream.platform.models.user.AddUserModel;
import build.dream.platform.models.user.BatchDeleteUsersModel;
import build.dream.platform.models.user.BatchGetUsersModel;
import build.dream.platform.models.user.ObtainUserInfoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private PrivilegeMapper privilegeMapper;

    /**
     * 获取用户信息
     *
     * @param obtainUserInfoModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainUserInfo(ObtainUserInfoModel obtainUserInfoModel) {
        Long userId = obtainUserInfoModel.getUserId();

        SystemUser systemUser = DatabaseHelper.find(SystemUser.class, userId);
        ValidateUtils.notNull(systemUser, "用户不存在！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);

        int userType = systemUser.getUserType();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        if (userType == Constants.USER_TYPE_TENANT || userType == Constants.USER_TYPE_TENANT_EMPLOYEE) {
            Tenant tenant = DatabaseHelper.find(Tenant.class, systemUser.getTenantId());
            ValidateUtils.notNull(tenant, "商户不存在！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);
            Long tenantId = tenant.getId();

            SearchModel tenantSecretKeySearchModel = SearchModel.builder()
                    .autoSetDeletedFalse()
                    .addSearchCondition(TenantSecretKey.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId)
                    .build();
            TenantSecretKey tenantSecretKey = DatabaseHelper.find(TenantSecretKey.class, tenantSecretKeySearchModel);
            ValidateUtils.notNull(tenantSecretKey, "未检索到商户秘钥！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);

            List<AppPrivilege> appPrivileges = privilegeMapper.findAllAppPrivileges(userId);
            List<PosPrivilege> posPrivileges = privilegeMapper.findAllPosPrivileges(userId);
            List<BackgroundPrivilege> backgroundPrivileges = privilegeMapper.findAllBackgroundPrivileges(userId);

            data.put("tenant", tenant);
            data.put("tenantSecretKey", tenantSecretKey);
            data.put("appPrivileges", appPrivileges);
            data.put("posPrivileges", posPrivileges);
            data.put("backgroundPrivileges", backgroundPrivileges);
        } else if (userType == Constants.USER_TYPE_AGENT) {
            Agent agent = DatabaseHelper.find(Agent.class, systemUser.getAgentId());
            ValidateUtils.notNull(agent, "代理商不存在！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);
            data.put("agent", agent);
        }
        return ApiRest.builder().data(data).message("获取用户信息成功！").successful(true).build();
    }

    /**
     * 批量获取用户信息
     *
     * @param batchGetUsersModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest batchGetUsers(BatchGetUsersModel batchGetUsersModel) {
        List<Long> userIds = batchGetUsersModel.getUserIds();

        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .addSearchCondition(SystemUser.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, userIds)
                .build();
        List<SystemUser> systemUsers = DatabaseHelper.findAll(SystemUser.class, searchModel);
        return ApiRest.builder().data(systemUsers).message("批量获取用户信息成功！").successful(true).build();
    }

    /**
     * 批量删除用户
     *
     * @param batchDeleteUsersModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest batchDeleteUsers(BatchDeleteUsersModel batchDeleteUsersModel) {
        Long userId = batchDeleteUsersModel.getUserId();
        List<Long> userIds = batchDeleteUsersModel.getUserIds();

        Tuple3[] searchConditions = {TupleUtils.buildTuple3(SystemUser.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, userIds)};
        DatabaseHelper.markedDelete(SystemUser.class, userId, "删除用户信息！", searchConditions);

        return ApiRest.builder().message("批量删除用户成功！").successful(true).build();
    }

    /**
     * 缓存用户信息
     */
    @Transactional(readOnly = true)
    public void cacheUserInfos() {
        SearchModel searchModel = SearchModel.builder().autoSetDeletedFalse().build();
        List<SystemUser> systemUsers = DatabaseHelper.findAll(SystemUser.class, searchModel);
        UserUtils.rejoinCacheUserInfos(systemUsers);
    }

    /**
     * 新增用户
     *
     * @param addUserModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest addUser(AddUserModel addUserModel) {
        String name = addUserModel.getName();
        String mobile = addUserModel.getMobile();
        String email = addUserModel.getEmail();
        String loginName = addUserModel.getLoginName();
        Integer userType = addUserModel.getUserType();
        String password = addUserModel.getPassword();
        String weiXinPublicPlatformOpenId = addUserModel.getWeiXinPublicPlatformOpenId();
        String weiXinOpenPlatformOpenId = addUserModel.getWeiXinOpenPlatformOpenId();
        Long tenantId = addUserModel.getTenantId();
        Long agentId = addUserModel.getAgentId();
        Boolean enabled = addUserModel.getEnabled();
        Long userId = addUserModel.getUserId();

        SearchModel mobileCountSearchModel = SearchModel.builder()
                .addSearchCondition(SystemUser.ColumnName.MOBILE, Constants.SQL_OPERATION_SYMBOL_EQUAL, mobile)
                .build();
        ValidateUtils.isTrue(DatabaseHelper.count(SystemUser.class, mobileCountSearchModel) == 0, "手机号码已注册！");

        SearchModel emailCountSearchModel = SearchModel.builder()
                .addSearchCondition(SystemUser.ColumnName.EMAIL, Constants.SQL_OPERATION_SYMBOL_EQUAL, email)
                .build();
        ValidateUtils.isTrue(DatabaseHelper.count(SystemUser.class, emailCountSearchModel) == 0, "邮箱已注册！");

        SystemUser systemUser = SystemUser.builder()
                .name(name)
                .mobile(mobile)
                .email(email)
                .loginName(loginName)
                .userType(userType)
                .password(BCryptUtils.encode(password))
                .weiXinPublicPlatformOpenId(StringUtils.isBlank(weiXinPublicPlatformOpenId) ? Constants.VARCHAR_DEFAULT_VALUE : weiXinPublicPlatformOpenId)
                .weiXinOpenPlatformOpenId(StringUtils.isBlank(weiXinOpenPlatformOpenId) ? Constants.VARCHAR_DEFAULT_VALUE : weiXinOpenPlatformOpenId)
                .tenantId(Objects.nonNull(tenantId) ? tenantId : Constants.BIGINT_DEFAULT_VALUE)
                .agentId(Objects.nonNull(agentId) ? agentId : Constants.BIGINT_DEFAULT_VALUE)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(enabled)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("新增用户信息！")
                .build();
        DatabaseHelper.insert(systemUser);
        UserUtils.cacheUserInfo(systemUser);
        return ApiRest.builder().data(systemUser).className(SystemUser.class.getName()).message("新增用户信息成功！").successful(true).build();
    }
}
