package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.saas.AppPrivilege;
import build.dream.common.domains.saas.BackgroundPrivilege;
import build.dream.common.domains.saas.PosPrivilege;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.platform.beans.ZTreeNode;
import build.dream.platform.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {
    @Transactional(readOnly = true)
    public ApiRest listBackgroundPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("service_name", Constants.SQL_OPERATION_SYMBOL_EQUAL, Constants.SERVICE_NAME_CATERING);
        searchModel.addSearchCondition("hidden", Constants.SQL_OPERATION_SYMBOL_EQUAL, 0);
        List<BackgroundPrivilege> backgroundPrivileges = DatabaseHelper.findAll(BackgroundPrivilege.class, searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (BackgroundPrivilege backgroundPrivilege : backgroundPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(backgroundPrivilege.getId().toString(), backgroundPrivilege.getPrivilegeName(), backgroundPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return ApiRest.builder().data(zTreeNodes).message("获取后台权限列表成功！").successful(true).build();
    }

    @Transactional(readOnly = true)
    public ApiRest listAppPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        List<AppPrivilege> appPrivileges = DatabaseHelper.findAll(AppPrivilege.class, searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (AppPrivilege appPrivilege : appPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(appPrivilege.getId().toString(), appPrivilege.getPrivilegeName(), appPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return ApiRest.builder().data(zTreeNodes).message("获取APP权限列表成功！").successful(true).build();
    }

    @Transactional(readOnly = true)
    public ApiRest listPosPrivileges() {
        SearchModel searchModel = new SearchModel(true);
        List<PosPrivilege> posPrivileges = DatabaseHelper.findAll(PosPrivilege.class, searchModel);
        List<ZTreeNode> zTreeNodes = new ArrayList<ZTreeNode>();
        for (PosPrivilege posPrivilege : posPrivileges) {
            ZTreeNode zTreeNode = new ZTreeNode(posPrivilege.getId().toString(), posPrivilege.getPrivilegeName(), posPrivilege.getParentId().toString());
            zTreeNodes.add(zTreeNode);
        }
        return ApiRest.builder().data(zTreeNodes).message("获取APP权限列表成功！").successful(true).build();
    }
}
