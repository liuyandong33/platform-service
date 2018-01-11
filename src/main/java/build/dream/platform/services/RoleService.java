package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.BackgroundRole;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.BackgroundRoleMapper;
import build.dream.platform.models.role.ListBackgroundRolesModel;
import build.dream.platform.models.role.SaveRolePrivilegesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    @Autowired
    private BackgroundRoleMapper backgroundRoleMapper;

    @Transactional(readOnly = true)
    public ApiRest listBackgroundRoles(ListBackgroundRolesModel listBackgroundRolesModel) {
        SearchModel countSearchModel = new SearchModel(true);
        countSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, listBackgroundRolesModel.getTenantId());
        long total = backgroundRoleMapper.count(countSearchModel);

        List<BackgroundRole> backgroundRoles = new ArrayList<BackgroundRole>();
        if (total > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel(true);
            pagedSearchModel.addSearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, listBackgroundRolesModel.getTenantId());
            pagedSearchModel.setPage(listBackgroundRolesModel.getPage());
            pagedSearchModel.setRows(listBackgroundRolesModel.getRows());
            backgroundRoles = backgroundRoleMapper.findAllPaged(pagedSearchModel);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", backgroundRoles);
        ApiRest apiRest = new ApiRest();
        apiRest.setData(data);
        apiRest.setMessage("查询权限列表成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveRolePrivileges(SaveRolePrivilegesModel saveRolePrivilegesModel) {
        if (Constants.PRIVILEGE_TYPE_BACKGROUND.equals(saveRolePrivilegesModel.getType())) {
            backgroundRoleMapper.deleteRolePrivileges(saveRolePrivilegesModel.getRoleId());
            backgroundRoleMapper.saveRolePrivileges(saveRolePrivilegesModel.getRoleId(), saveRolePrivilegesModel.getPrivilegeIds());
        }
        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("保存角色权限成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
