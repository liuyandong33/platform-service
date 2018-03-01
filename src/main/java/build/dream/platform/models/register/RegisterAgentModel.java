package build.dream.platform.models.register;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class RegisterAgentModel extends BasicModel {
    /**
     * 代理商名称
     */
    @NotNull
    @Length(max = 20)
    private String name;

    /**
     * 联系人
     */
    @NotNull
    @Length(max = 20)
    private String linkman;

    /**
     * 联系人电话
     */
    @NotNull
    @Length(max = 20)
    private String mobile;

    /**
     * 联系人邮箱
     */
    @NotNull
    @Length(max = 20)
    private String email;

    @NotNull
    @Length(max = 20)
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
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
}
