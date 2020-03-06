package build.dream.platform.services;

import build.dream.common.domains.saas.RsaKeyPair;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.RsaKeyPairUtils;
import build.dream.common.utils.SearchModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RsaKeyPairService {
    @Transactional(readOnly = true)
    public void cacheRsaKeyPairs() {
        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .build();
        List<RsaKeyPair> rsaKeyPairs = DatabaseHelper.findAll(RsaKeyPair.class, searchModel);
        RsaKeyPairUtils.cacheRsaKeyPairs(rsaKeyPairs);
    }
}
