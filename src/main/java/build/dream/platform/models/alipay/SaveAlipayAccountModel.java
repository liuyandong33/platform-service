package build.dream.platform.models.alipay;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.ArrayUtils;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveAlipayAccountModel extends BasicModel {
    private static final String[] SIGN_TYPES = {Constants.RSA, Constants.RSA2};
    @NotNull
    private BigInteger tenantId;

    @NotNull
    private BigInteger branchId;

    @NotNull
    private String account;

    @NotNull
    private String appId;

    @NotNull
    private String partnerId;

    private String storeId;

    @NotNull
    private String alipayPublicKey;

    @NotNull
    private String applicationPublicKey;

    @NotNull
    private String applicationPrivateKey;

    @NotNull
    private String signType;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getApplicationPublicKey() {
        return applicationPublicKey;
    }

    public void setApplicationPublicKey(String applicationPublicKey) {
        this.applicationPublicKey = applicationPublicKey;
    }

    public String getApplicationPrivateKey() {
        return applicationPrivateKey;
    }

    public void setApplicationPrivateKey(String applicationPrivateKey) {
        this.applicationPrivateKey = applicationPrivateKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    @Override
    public boolean validate() {
        return super.validate() && ArrayUtils.contains(SIGN_TYPES, signType);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(SIGN_TYPES, signType, "signType");
    }
}
