package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.saas.OauthClientDetail;
import build.dream.common.utils.BCryptUtils;
import build.dream.common.utils.DatabaseHelper;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.oauthclientdetail.SaveOauthClientDetailModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class OauthClientDetailService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveOauthClientDetail(SaveOauthClientDetailModel saveOauthClientDetailModel) {
        String clientId = saveOauthClientDetailModel.getClientId();
        String clientSecret = saveOauthClientDetailModel.getClientSecret();
        String resourceIds = saveOauthClientDetailModel.getResourceIds();
        String scope = saveOauthClientDetailModel.getScope();
        String authorizedGrantTypes = saveOauthClientDetailModel.getAuthorizedGrantTypes();
        String webServerRedirectUri = saveOauthClientDetailModel.getWebServerRedirectUri();
        String authorities = saveOauthClientDetailModel.getAuthorities();
        Integer accessTokenValidity = saveOauthClientDetailModel.getAccessTokenValidity();
        Integer refreshTokenValidity = saveOauthClientDetailModel.getRefreshTokenValidity();
        String additionalInformation = saveOauthClientDetailModel.getAdditionalInformation();
        String autoApproveScope = saveOauthClientDetailModel.getAutoApproveScope();
        BigInteger userId = saveOauthClientDetailModel.getUserId();

        OauthClientDetail oauthClientDetail = OauthClientDetail.builder()
                .clientId(clientId)
                .clientSecret(BCryptUtils.encode(clientSecret))
                .resourceIds(resourceIds)
                .scope(scope)
                .authorizedGrantTypes(authorizedGrantTypes)
                .webServerRedirectUri(StringUtils.isBlank(webServerRedirectUri) ? Constants.VARCHAR_DEFAULT_VALUE : webServerRedirectUri)
                .authorities(StringUtils.isBlank(authorities) ? Constants.VARCHAR_DEFAULT_VALUE : authorities)
                .accessTokenValidity(accessTokenValidity)
                .refreshTokenValidity(refreshTokenValidity)
                .additionalInformation(StringUtils.isBlank(additionalInformation) ? Constants.VARCHAR_DEFAULT_VALUE : additionalInformation)
                .autoApproveScope(StringUtils.isBlank(autoApproveScope) ? Constants.VARCHAR_DEFAULT_VALUE : autoApproveScope)
                .createdUserId(userId)
                .updatedUserId(userId)
                .updatedRemark("新增信息！")
                .build();
        DatabaseHelper.insert(oauthClientDetail);
        return ApiRest.builder().data(oauthClientDetail).message("新增成功！").successful(true).build();
    }
}
