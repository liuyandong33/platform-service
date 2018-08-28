package build.dream.platform.models.weixin;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

public class SaveWeiXinAuthorizerTokenModel extends BasicModel {
    @NotNull
    @Length(max = 50)
    private String componentAppId;

    @NotNull
    @Length(max = 50)
    private String authorizerAppId;

    @NotNull
    @Length(max = 255)
    private String authorizerAccessToken;

    @NotNull
    private Integer expiresIn;

    @NotNull
    @Length(max = 255)
    private String authorizerRefreshToken;

    @NotNull
    private Date fetchTime;

    @NotNull
    private BigInteger userId;

    public String getComponentAppId() {
        return componentAppId;
    }

    public void setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId;
    }

    public String getAuthorizerAppId() {
        return authorizerAppId;
    }

    public void setAuthorizerAppId(String authorizerAppId) {
        this.authorizerAppId = authorizerAppId;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    public Date getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Date fetchTime) {
        this.fetchTime = fetchTime;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
