package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.constants.ErrorConstants;
import build.dream.common.domains.saas.*;
import build.dream.common.tuples.Tuple3;
import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AppPrivilegeMapper;
import build.dream.platform.mappers.BackgroundPrivilegeMapper;
import build.dream.platform.mappers.PosPrivilegeMapper;
import build.dream.platform.models.user.BatchDeleteUsersModel;
import build.dream.platform.models.user.BatchGetUsersModel;
import build.dream.platform.models.user.ObtainAllPrivilegesModel;
import build.dream.platform.models.user.ObtainUserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     */
    @Transactional(readOnly = true)
    public ApiRest obtainUserInfo(ObtainUserInfoModel obtainUserInfoModel) {
        BigInteger userId = obtainUserInfoModel.getUserId();

        SystemUser systemUser = DatabaseHelper.find(SystemUser.class, userId);
        ValidateUtils.notNull(systemUser, "用户不存在！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);

        int userType = systemUser.getUserType();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("user", systemUser);
        if (userType == Constants.USER_TYPE_TENANT || userType == Constants.USER_TYPE_TENANT_EMPLOYEE) {
            Tenant tenant = DatabaseHelper.find(Tenant.class, systemUser.getTenantId());
            ValidateUtils.notNull(tenant, "商户不存在！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);
            BigInteger tenantId = tenant.getId();

            SearchModel tenantSecretKeySearchModel = new SearchModel(true);
            tenantSecretKeySearchModel.addSearchCondition(TenantSecretKey.ColumnName.TENANT_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId);
            TenantSecretKey tenantSecretKey = DatabaseHelper.find(TenantSecretKey.class, tenantSecretKeySearchModel);
            ValidateUtils.notNull(tenantSecretKey, "未检索到商户秘钥！", ErrorConstants.ERROR_CODE_HANDLING_ERROR);

            List<AppPrivilege> appPrivileges = appPrivilegeMapper.findAllAppPrivileges(userId);
            List<PosPrivilege> posPrivileges = posPrivilegeMapper.findAllPosPrivileges(userId);
            List<BackgroundPrivilege> backgroundPrivileges = backgroundPrivilegeMapper.findAllBackgroundPrivileges(userId);

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
        List<BigInteger> userIds = batchGetUsersModel.getUserIds();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition(SystemUser.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, userIds);
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
     * @param batchDeleteUsersModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest batchDeleteUsers(BatchDeleteUsersModel batchDeleteUsersModel) {
        BigInteger userId = batchDeleteUsersModel.getUserId();
        List<BigInteger> userIds = batchDeleteUsersModel.getUserIds();

        Tuple3[] searchConditions = {TupleUtils.buildTuple3(SystemUser.ColumnName.ID, Constants.SQL_OPERATION_SYMBOL_IN, userIds)};
        DatabaseHelper.markedDelete(SystemUser.class, userId, "删除用户信息！", searchConditions);

        return ApiRest.builder().message("批量删除用户成功！").successful(true).build();
    }

    /**
     * 缓存用户信息
     */
    @Transactional(readOnly = true)
    public void cacheUserInfos() {
        SearchModel searchModel = new SearchModel(true);
        List<SystemUser> systemUsers = DatabaseHelper.findAll(SystemUser.class, searchModel);
        UserUtils.rejoinCacheUserInfos(systemUsers);
    }
}
