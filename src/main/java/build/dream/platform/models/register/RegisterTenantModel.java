package build.dream.platform.models.register;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.platform.constants.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class RegisterTenantModel extends BasicModel {
    private static final String[] BUSINESSES = {Constants.BUSINESS_CATERING, Constants.BUSINESS_RETAIL};
    private static final Integer[] TENANT_TYPES = {1, 2};

    /**
     * 商户名称
     */
    @NotNull
    @Length(max = 20)
    private String name;

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

    /**
     * 联系人姓名
     */
    @NotNull
    @Length(max = 20)
    private String linkman;

    /**
     * 商户业态，1-餐饮，2-零售
     */
    private String business;

    /**
     * 总部门店省编码
     */
    @NotNull
    @Length(max = 10)
    private String provinceCode;

    /**
     * 总部门店省名称
     */
    @NotNull
    @Length(max = 10)
    private String provinceName;

    /**
     * 总部门店市编码
     */
    @NotNull
    @Length(max = 10)
    private String cityCode;

    /**
     * 总部门店市名称
     */
    @NotNull
    @Length(max = 10)
    private String cityName;

    /**
     * 总部门店区编码
     */
    @NotNull
    @Length(max = 10)
    private String districtCode;

    /**
     * 总部门店区名称
     */
    @NotNull
    @Length(max = 10)
    private String districtName;

    /**
     * 总部门店详细地址
     */
    @NotNull
    @Length(max = 255)
    private String address;

    /**
     * 总部门店经度
     */
    @NotNull
    @Length(max = 20)
    private String longitude;

    /**
     * 总部门店纬度
     */
    @NotNull
    @Length(max = 20)
    private String latitude;

    /**
     * 总部门店联系电话
     */
    @NotNull
    @Length(max = 20)
    private String contactPhone;

    /**
     * 总部门店账号密码
     */
    @NotNull
    @Length(max = 20)
    private String password;

    /**
     * 商户类型，1-标准版商户，2-单机版商户
     */
    private Integer tenantType;

    /**
     * 会员共享类型，1-全部共享，2-全部独立，3-分组共享
     */
    @NotNull
    private Integer vipSharedType;

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

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTenantType() {
        return tenantType;
    }

    public void setTenantType(Integer tenantType) {
        this.tenantType = tenantType;
    }

    public Integer getVipSharedType() {
        return vipSharedType;
    }

    public void setVipSharedType(Integer vipSharedType) {
        this.vipSharedType = vipSharedType;
    }

    @Override
    public boolean validate() {
        return super.validate() && ArrayUtils.contains(BUSINESSES, business) && ArrayUtils.contains(TENANT_TYPES, tenantType);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(BUSINESSES, business, "business");
        ApplicationHandler.inArray(TENANT_TYPES, tenantType, "tenantType");
    }
}
