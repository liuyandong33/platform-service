package build.dream.platform.models.login;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class LoginModel extends BasicModel {
    @NotNull
    private String loginName;

    @NotNull
    private String password;

    @NotNull
    private String sessionId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
