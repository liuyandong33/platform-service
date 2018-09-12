package build.dream.platform.services;

import build.dream.common.saas.domains.NewLandAccount;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewLandService {
    @Transactional(readOnly = true)
    public List<NewLandAccount> obtainAllNewLandAccounts() {
        SearchModel searchModel = new SearchModel(true);
        return DatabaseHelper.findAll(NewLandAccount.class, searchModel);
    }
}
