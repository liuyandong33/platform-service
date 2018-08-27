package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveWeiXinAuthorizerInfoModel extends BasicModel {
    @NotNull
    private BigInteger tenantId;

    @NotNull
    @Length(max = 20)
    private String nickName;

    @NotNull
    @Length(max = 255)
    private String headImg;

    @NotNull
    private Integer serviceTypeInfo;

    @NotNull
    private Integer verifyTypeInfo;

    @NotNull
    @Length(max = 50)
    private String originalId;

    @NotNull
    @Length(max = 50)
    private String principalName;

    @Length(max = 255)
    private String alias;

    @NotNull
    @Length(max = 50)
    private String businessInfo;

    @NotNull
    @Length(max = 255)
    private String qrcodeUrl;

    @NotNull
    @Length(max = 50)
    private String authorizationAppId;

    @NotNull
    @Length(max = 255)
    private String funcInfo;

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(Integer serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public Integer getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(Integer verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getAuthorizationAppId() {
        return authorizationAppId;
    }

    public void setAuthorizationAppId(String authorizationAppId) {
        this.authorizationAppId = authorizationAppId;
    }

    public String getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(String funcInfo) {
        this.funcInfo = funcInfo;
    }
}
