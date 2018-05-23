package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.*;
import build.dream.common.utils.CommonUtils;
import build.dream.common.utils.ProxyUtils;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.UpdateModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AppPrivilegeMapper;
import build.dream.platform.mappers.BackgroundPrivilegeMapper;
import build.dream.platform.mappers.PosPrivilegeMapper;
import build.dream.platform.models.user.BatchDeleteUserModel;
import build.dream.platform.models.user.BatchGetUsersModel;
import build.dream.platform.models.user.ObtainAllPrivilegesModel;
import build.dream.platform.models.user.ObtainUserInfoModel;
import build.dream.platform.utils.DatabaseHelper;
import org.apache.commons.lang.Validate;
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
        Validate.notNull(systemUser, "用户不存在！");

        BigInteger userId = systemUser.getId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, userId);
        Tenant tenant = DatabaseHelper.find(Tenant.class, searchModel);
        Validate.notNull(tenant, "商户不存在！");
        BigInteger tenantId = tenant.getId();

        SearchModel tenantSecretKeySearchModel = new SearchModel(true);
        tenantSecretKeySearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
        TenantSecretKey tenantSecretKey = DatabaseHelper.find(TenantSecretKey.class, tenantSecretKeySearchModel);
        Validate.notNull(tenantSecretKey, "未检索到商户秘钥！");

        List<AppPrivilege> appPrivileges = appPrivilegeMapper.findAllAppPrivileges(userId);
        List<PosPrivilege> posPrivileges = posPrivilegeMapper.findAllPosPrivileges(userId);
        List<BackgroundPrivilege> backgroundPrivileges = backgroundPrivilegeMapper.findAllBackgroundPrivileges(userId);

        Map<String, String> obtainBranchInfoRequestParameters = new HashMap<String, String>();
        obtainBranchInfoRequestParameters.put("tenantId", tenantId.toString());
        obtainBranchInfoRequestParameters.put("userId", userId.toString());
        ApiRest obtainBranchInfoApiRest = ProxyUtils.doGetWithRequestParameters(tenant.getPartitionCode(), CommonUtils.getServiceName(tenant.getBusiness()), "branch", "obtainBranchInfo", obtainBranchInfoRequestParameters);
        Validate.isTrue(obtainBranchInfoApiRest.isSuccessful(), obtainBranchInfoApiRest.getError());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        data.put("tenant", tenant);
        data.put("tenantSecretKey", tenantSecretKey);
        data.put("appPrivileges", appPrivileges);
        data.put("posPrivileges", posPrivileges);
        data.put("backgroundPrivileges", backgroundPrivileges);
        data.put("branch", obtainBranchInfoApiRest.getData());
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 批量获取用户信息
     *
     * @param batchGetUsersModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest batchObtainUser(BatchGetUsersModel batchGetUsersModel) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", "IN", batchGetUsersModel.getUserIds());
        List<SystemUser> systemUsers = DatabaseHelper.findAll(SystemUser.class, searchModel);
        return new ApiRest(systemUsers, "批量获取用户信息成功！");
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
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("查询权限成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 批量删除用户
     *
     * @param batchDeleteUserModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest batchDeleteUser(BatchDeleteUserModel batchDeleteUserModel) {
        UpdateModel updateModel = new UpdateModel(true);
        updateModel.setTableName("system_user");
        updateModel.addContentValue("deleted", 1);
        updateModel.addContentValue("last_update_user_id", batchDeleteUserModel.getUserId());
        updateModel.addContentValue("last_update_remark", "删除用户信息！");
        updateModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_IN, batchDeleteUserModel.getUserIds());
        DatabaseHelper.universalUpdate(updateModel);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("批量删除用户成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
