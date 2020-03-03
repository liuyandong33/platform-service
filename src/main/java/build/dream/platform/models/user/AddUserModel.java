package build.dream.platform.models.user;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;

import javax.validation.constraints.NotNull;

public class AddUserModel extends BasicModel {
    @NotNull
    private String name;

    @NotNull
    private String mobile;

    @NotNull
    private String email;

    @NotNull
    private String loginName;

    @NotNull
    private Integer userType;

    @NotNull
    private String password;

    private String weiXinPublicPlatformOpenId;

    private String weiXinOpenPlatformOpenId;

    private Long tenantId;

    private Long agentId;

    @NotNull
    private Boolean enabled;

    @NotNull
    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeiXinPublicPlatformOpenId() {
        return weiXinPublicPlatformOpenId;
    }

    public void setWeiXinPublicPlatformOpenId(String weiXinPublicPlatformOpenId) {
        this.weiXinPublicPlatformOpenId = weiXinPublicPlatformOpenId;
    }

    public String getWeiXinOpenPlatformOpenId() {
        return weiXinOpenPlatformOpenId;
    }

    public void setWeiXinOpenPlatformOpenId(String weiXinOpenPlatformOpenId) {
        this.weiXinOpenPlatformOpenId = weiXinOpenPlatformOpenId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean validate() {
        return super.validate();
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(new Integer[]{Constants.USER_TYPE_TENANT, Constants.USER_TYPE_TENANT_EMPLOYEE, Constants.USER_TYPE_AGENT}, userType, "userType");
        if (userType == Constants.USER_TYPE_TENANT || userType == Constants.USER_TYPE_TENANT_EMPLOYEE) {
            ApplicationHandler.notNull(tenantId, "tenantId");
        } else if (userType == Constants.USER_TYPE_AGENT) {
            ApplicationHandler.notNull(agentId, "agentId");
        }
    }
}
