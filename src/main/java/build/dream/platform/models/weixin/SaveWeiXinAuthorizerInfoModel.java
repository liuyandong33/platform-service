package build.dream.platform.models.weixin;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveWeiXinAuthorizerInfoModel extends BasicModel {
    @NotNull
    private BigInteger tenantId;

    @NotNull
    private Integer authorizerType;

    @NotNull
    @Length(max = 20)
    private String nickName;

    @NotNull
    @Length(max = 255)
    private String headImg;

    @NotNull
    @Length(max = 255)
    private String serviceTypeInfo;

    @NotNull
    @Length(max = 255)
    private String verifyTypeInfo;

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

    @Length(max = 255)
    private String signature;

    @Length(max = 255)
    private String miniProgramInfo;

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

    public Integer getAuthorizerType() {
        return authorizerType;
    }

    public void setAuthorizerType(Integer authorizerType) {
        this.authorizerType = authorizerType;
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

    public String getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(String serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    public String getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(String verifyTypeInfo) {
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMiniProgramInfo() {
        return miniProgramInfo;
    }

    public void setMiniProgramInfo(String miniProgramInfo) {
        this.miniProgramInfo = miniProgramInfo;
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
