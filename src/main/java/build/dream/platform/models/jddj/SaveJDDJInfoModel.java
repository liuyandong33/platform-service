package build.dream.platform.models.jddj;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class SaveJDDJInfoModel extends BasicModel {
    @NotNull
    private Long tenantId;

    @NotNull
    private String appKey;

    @NotNull
    private String appSecret;

    @NotNull
    private String venderId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getVenderId() {
        return venderId;
    }

    public void setVenderId(String venderId) {
        this.venderId = venderId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
