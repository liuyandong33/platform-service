package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveWeiXinPayAccountModel extends BasicModel {
    @NotNull
    private BigInteger tenantId;

    @NotNull
    private BigInteger branchId;

    @NotNull
    private String appId;

    @NotNull
    private String mchId;

    @NotNull
    private String apiSecretKey;

    private String subPublicAccountAppId;

    private String subOpenPlatformAppId;

    private String subMiniProgramAppId;

    private String rsaPublicKey;

    private String subMchId;

    private String operationCertificate;

    private String operationCertificatePassword;

    @NotNull
    private Boolean acceptanceModel;

    @NotNull
    private BigInteger userId;

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public BigInteger getBranchId() {
        return branchId;
    }

    public void setBranchId(BigInteger branchId) {
        this.branchId = branchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
    }

    public String getSubPublicAccountAppId() {
        return subPublicAccountAppId;
    }

    public void setSubPublicAccountAppId(String subPublicAccountAppId) {
        this.subPublicAccountAppId = subPublicAccountAppId;
    }

    public String getSubOpenPlatformAppId() {
        return subOpenPlatformAppId;
    }

    public void setSubOpenPlatformAppId(String subOpenPlatformAppId) {
        this.subOpenPlatformAppId = subOpenPlatformAppId;
    }

    public String getSubMiniProgramAppId() {
        return subMiniProgramAppId;
    }

    public void setSubMiniProgramAppId(String subMiniProgramAppId) {
        this.subMiniProgramAppId = subMiniProgramAppId;
    }

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getOperationCertificate() {
        return operationCertificate;
    }

    public void setOperationCertificate(String operationCertificate) {
        this.operationCertificate = operationCertificate;
    }

    public String getOperationCertificatePassword() {
        return operationCertificatePassword;
    }

    public void setOperationCertificatePassword(String operationCertificatePassword) {
        this.operationCertificatePassword = operationCertificatePassword;
    }

    public Boolean getAcceptanceModel() {
        return acceptanceModel;
    }

    public void setAcceptanceModel(Boolean acceptanceModel) {
        this.acceptanceModel = acceptanceModel;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    @Override
    public boolean validate() {
        boolean isOk = super.validate();
        if (isOk && acceptanceModel) {
            isOk = StringUtils.isNotBlank(subMchId);
        }
        return isOk;
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        if (acceptanceModel) {
            ApplicationHandler.notBlank(subMchId, "subMchId");
        }
    }
}
