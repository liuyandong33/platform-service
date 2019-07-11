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

    @NotNull
    @Length(max = 20)
    private String provinceCode;

    @NotNull
    @Length(max = 20)
    private String cityCode;

    @NotNull
    @Length(max = 20)
    private String districtCode;

    @NotNull
    @Length(max = 255)
    private String address;

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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
