package build.dream.platform.models.tenant;

import build.dream.common.models.BasicModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.math.BigInteger;

public class ObtainTenantInfoModel extends BasicModel {
    private BigInteger tenantId;

    private String tenantCode;

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
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
        Validate.isTrue(tenantId != null || StringUtils.isNotBlank(tenantCode), "参数tenantId和tenantCode不能同时为空！");
    }
}
