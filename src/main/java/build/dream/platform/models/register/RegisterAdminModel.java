package build.dream.platform.models.register;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class RegisterAdminModel extends BasicModel {
    /**
     * 姓名
     */
    @NotNull
    @Length(max = 20)
    private String name;

    /**
     * 手机号码
     */
    @NotNull
    @Length(max = 20)
    private String mobile;

    /**
     * 邮箱
     */
    @NotNull
    @Length(max = 20)
    private String email;

    /**
     * 密码
     */
    @NotNull
    @Length(max = 20)
    private String password;

    @NotNull
    private BigInteger userId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
