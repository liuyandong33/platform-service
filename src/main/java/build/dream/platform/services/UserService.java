package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AppPrivilegeMapper;
import build.dream.platform.mappers.BackgroundPrivilegeMapper;
import build.dream.platform.mappers.PosPrivilegeMapper;
import build.dream.platform.models.user.BatchDeleteUserModel;
import build.dream.platform.models.user.BatchGetUsersModel;
import build.dream.platform.models.user.ObtainAllPrivilegesModel;
import build.dream.platform.models.user.ObtainUserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private BackgroundPrivilegeMapper backgroundPrivilegeMapper;
    @Autowired
    private AppPrivilegeMapper appPrivilegeMapper;
    @Autowired
    private PosPrivilegeMapper posPrivilegeMapper;

    /**
     * 获取用户信息
     *
     * @param obtainUserInfoModel
     * @return
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public ApiRest obtainUserInfo(ObtainUserInfoModel obtainUserInfoModel) throws IOException {
        String loginName = obtainUserInfoModel.getLoginName();

        SearchModel userSearchModel = new SearchModel(true);
        userSearchModel.setWhereClause("login_name = #{loginName} OR email = #{loginName} OR mobile = #{loginName}");
        userSearchModel.addNamedParameter("loginName", loginName);
        SystemUser systemUser = DatabaseHelper.find(SystemUser.class, userSearchModel);
        ValidateUtils.notNull(systemUser, "用户不存在！");

        BigInteger userId = systemUser.getId();

        Tenant tenant = DatabaseHelper.find(Tenant.class, systemUser.getTenantId());
        ValidateUtils.notNull(tenant, "商户不存在！");
        BigInteger tenantId = tenant.getId();

        SearchModel tenantSecretKeySearchModel = new SearchModel(true);
        tenantSecretKeySearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        TenantSecretKey tenantSecretKey = DatabaseHelper.find(TenantSecretKey.class, tenantSecretKeySearchModel);
        ValidateUtils.notNull(tenantSecretKey, "未检索到商户秘钥！");

        List<AppPrivilege> appPrivileges = appPrivilegeMapper.findAllAppPrivileges(userId);
        List<PosPrivilege> posPrivileges = posPrivilegeMapper.findAllPosPrivileges(userId);
        List<BackgroundPrivilege> backgroundPrivileges = backgroundPrivilegeMapper.findAllBackgroundPrivileges(userId);

        Map<String, String> obtainBranchInfoRequestParameters = new HashMap<String, String>();
        obtainBranchInfoRequestParameters.put("tenantId", tenantId.toString());
        obtainBranchInfoRequestParameters.put("userId", userId.toString());
        ApiRest obtainBranchInfoApiRest = ProxyUtils.doGetWithRequestParameters(tenant.getPartitionCode(), CommonUtils.getServiceName(tenant.getBusiness()), "branch", "obtainBranchInfo", obtainBranchInfoRequestParameters);
        ValidateUtils.isTrue(obtainBranchInfoApiRest.isSuccessful(), obtainBranchInfoApiRest.getError());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("tenant", tenant);
        data.put("tenantSecretKey", tenantSecretKey);
        data.put("appPrivileges", appPrivileges);
        data.put("posPrivileges", posPrivileges);
        data.put("backgroundPrivileges", backgroundPrivileges);
        data.put("branch", obtainBranchInfoApiRest.getData());
        return ApiRest.builder().data(data).message("获取用户信息成功！").successful(true).build();
    }

    /**
     * 批量获取用户信息
     *
     * @param batchGetUsersModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest batchObtainUser(BatchGetUsersModel batchGetUsersModel) {
        List<BigInteger> userIds = batchGetUsersModel.getUserIds();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, userIds);
        List<SystemUser> systemUsers = DatabaseHelper.findAll(SystemUser.class, searchModel);
        return ApiRest.builder().data(systemUsers).message("批量获取用户信息成功！").successful(true).build();
    }

    /**
     * 获取用户所有权限
     *
     * @param obtainAllPrivilegesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest obtainAllPrivileges(ObtainAllPrivilegesModel obtainAllPrivilegesModel) {
        Object data = null;
        if (Constants.PRIVILEGE_TYPE_BACKGROUND.equals(obtainAllPrivilegesModel.getType())) {
            data = backgroundPrivilegeMapper.findAllBackgroundPrivileges(obtainAllPrivilegesModel.getUserId());
        } else if (Constants.PRIVILEGE_TYPE_APP.equals(obtainAllPrivilegesModel.getType())) {
            data = appPrivilegeMapper.findAllAppPrivileges(obtainAllPrivilegesModel.getUserId());
        } else if (Constants.PRIVILEGE_TYPE_POS.equals(obtainAllPrivilegesModel.getType())) {
            data = posPrivilegeMapper.findAllPosPrivileges(obtainAllPrivilegesModel.getUserId());
        }
        return ApiRest.builder().data(data).message("查询权限成功！").successful(true).build();
    }

    /**
     * 批量删除用户
     *
     * @param batchDeleteUserModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest batchDeleteUser(BatchDeleteUserModel batchDeleteUserModel) {
        BigInteger userId = batchDeleteUserModel.getUserId();
        List<BigInteger> userIds = batchDeleteUserModel.getUserIds();

        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName("system_user");
        updateModel.addContentValue("deleted", 1);
        updateModel.addContentValue("last_update_user_id", userId);
        updateModel.addContentValue("last_update_remark", "删除用户信息！");
        updateModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, userIds);
        DatabaseHelper.universalUpdate(updateModel);

        return ApiRest.builder().message("批量删除用户成功！").successful(true).build();
    }
}
