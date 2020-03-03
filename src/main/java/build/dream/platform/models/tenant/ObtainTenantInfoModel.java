package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ValidateUtils;
import org.apache.commons.lang.StringUtils;

public class ObtainTenantInfoModel extends BasicModel {
    private Long tenantId;

    private String tenantCode;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    @Override
    public boolean validate() {
        return super.validate() && (tenantId != null || StringUtils.isNotBlank(tenantCode));
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ValidateUtils.isTrue(tenantId != null || StringUtils.isNotBlank(tenantCode), "参数tenantId和tenantCode不能同时为空！");
    }
}
