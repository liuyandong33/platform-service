package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveWeiXinPublicAccountModel extends BasicModel {
    @NotNull
    private BigInteger userId;

    @NotNull
    private BigInteger tenantId;

    @NotNull
    private String name;

    @NotNull
    private String appId;

    @NotNull
    private String appSecret;

    @NotNull
    private String originalId;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }
}
