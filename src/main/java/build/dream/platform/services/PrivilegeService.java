package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.AppPrivilege;
import build.dream.common.saas.domains.BackgroundPrivilege;
import build.dream.common.saas.domains.PosPrivilege;
import build.dream.common.utils.SearchModel;
import build.dream.platform.beans.ZTreeNode;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.AppPrivilegeMapper;
import build.dream.platform.mappers.BackgroundPrivilegeMapper;
import build.dream.platform.mappers.PosPrivilegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {
    @Autowired
    private BackgroundPrivilegeMapper backgroundPrivilegeMapper;
    @Autowired
    private AppPrivilegeMapper appPrivilegeMapper;
    @Autowired
    private PosPrivilegeMapper posPrivilegeMapper;

    @Transactional(readOnly = true)
    public ApiRest listBackgroundPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("service_name", Constants.SQL_OPERATION_SYMBOL_EQUALS, Constants.SERVICE_NAME_CATERING);
        searchModel.addSearchCondition("hidden", Constants.SQL_OPERATION_SYMBOL_EQUALS, 0);
        List<BackgroundPrivilege> backgroundPrivileges = backgroundPrivilegeMapper.findAll(searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(backgroundPrivilege.getId().toString(), backgroundPrivilege.getPrivilegeName(), backgroundPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return new ApiRest(zTreeNodes, "获取后台权限列表成功！");
    }

    @Transactional(readOnly = true)
    public ApiRest listAppPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        List<AppPrivilege> appPrivileges = appPrivilegeMapper.findAll(searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (AppPrivilege appPrivilege : appPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(appPrivilege.getId().toString(), appPrivilege.getPrivilegeName(), appPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return new ApiRest(zTreeNodes, "获取APP权限列表成功！");
    }

    @Transactional(readOnly = true)
    public ApiRest listPosPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        List<PosPrivilege> posPrivileges = posPrivilegeMapper.findAll(searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (PosPrivilege posPrivilege : posPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(posPrivilege.getId().toString(), posPrivilege.getPrivilegeName(), posPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return new ApiRest(zTreeNodes, "获取APP权限列表成功！");
    }
}
