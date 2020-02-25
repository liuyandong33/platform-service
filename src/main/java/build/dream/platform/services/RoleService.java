package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.saas.*;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.RoleMapper;
import build.dream.platform.models.role.ListRolePrivilegesModel;
import build.dream.platform.models.role.ListRolesModel;
import build.dream.platform.models.role.SaveRolePrivilegesModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 分页查询商户角色
     *
     * @param listRolesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listRoles(ListRolesModel listRolesModel) {
        BigInteger tenantId = listRolesModel.getTenantId();
        Integer page = listRolesModel.getPage();
        Integer rows = listRolesModel.getRows();

        SearchModel countSearchModel = new SearchModel(true);
        countSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, listRolesModel.getTenantId());

        PagedSearchModel pagedSearchModel = new PagedSearchModel(true);
        pagedSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, listRolesModel.getTenantId());
        pagedSearchModel.setPage(listRolesModel.getPage());
        pagedSearchModel.setRows(listRolesModel.getRows());

        long total = DatabaseHelper.count(BackgroundRole.class, countSearchModel);
        List<TenantRole> tenantRoles = new ArrayList<TenantRole>();
        if (total > 0) {
            tenantRoles = DatabaseHelper.findAllPaged(TenantRole.class, pagedSearchModel);
        } else {
            tenantRoles = new ArrayList<TenantRole>();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", tenantRoles);
        return ApiRest.builder().data(data).message("查询角色列表成功！").successful(true).build();
    }

    /**
     * 查询商户角色权限
     *
     * @param listRolePrivilegesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listRolePrivileges(ListRolePrivilegesModel listRolePrivilegesModel) {
        BigInteger roleId = listRolePrivilegesModel.getRoleId();
        List<AppPrivilege> appPrivileges = roleMapper.listRoleAppPrivileges(roleId);
        List<PosPrivilege> posPrivileges = roleMapper.listRolePosPrivileges(roleId);
        List<BackgroundPrivilege> backgroundPrivileges = roleMapper.listRoleBackgroundPrivileges(roleId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("appPrivileges", appPrivileges);
        data.put("posPrivileges", posPrivileges);
        data.put("backgroundPrivileges", backgroundPrivileges);
        return ApiRest.builder().data(data).message("查询角色权限成功！").successful(true).build();
    }

    /**
     * 保存商户角色权限
     *
     * @param saveRolePrivilegesModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveRolePrivileges(SaveRolePrivilegesModel saveRolePrivilegesModel) {
        BigInteger roleId = saveRolePrivilegesModel.getRoleId();
        List<BigInteger> appPrivilegeIds = saveRolePrivilegesModel.getAppPrivilegeIds();
        List<BigInteger> posPrivilegeIds = saveRolePrivilegesModel.getPosPrivilegeIds();
        List<BigInteger> backgroundPrivilegeIds = saveRolePrivilegesModel.getBackgroundPrivilegeIds();

        roleMapper.deleteTenantAppRolePrivileges(roleId);
        if (CollectionUtils.isNotEmpty(appPrivilegeIds)) {
            roleMapper.saveTenantRoleAppPrivileges(roleId, appPrivilegeIds);
        }

        roleMapper.deleteTenantPosRolePrivileges(roleId);
        if (CollectionUtils.isNotEmpty(posPrivilegeIds)) {
            roleMapper.saveTenantRolePosPrivileges(roleId, posPrivilegeIds);
        }

        roleMapper.deleteTenantBackgroundRolePrivileges(roleId);
        if (CollectionUtils.isNotEmpty(backgroundPrivilegeIds)) {
            roleMapper.saveTenantRoleBackgroundPrivileges(roleId, backgroundPrivilegeIds);
        }
        return ApiRest.builder().message("保存角色权限成功！").successful(true).build();
    }
}
