package build.dream.platform.services;

import build.dream.common.beans.AlipayAccount;
import build.dream.common.saas.domains.AlipayDeveloperAccount;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.platform.mappers.AlipayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlipayService {
    @Autowired
    private AlipayMapper alipayMapper;

    @Transactional(readOnly = true)
    public List<AlipayAccount> obtainAllAlipayAccounts() {
        return alipayMapper.obtainAllAlipayAccounts();
    }

    /**
     * 获取所有支付宝开发者账号
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AlipayDeveloperAccount> obtainAllAlipayDeveloperAccounts() {
        SearchModel searchModel = new SearchModel(true);
        return DatabaseHelper.findAll(AlipayDeveloperAccount.class, searchModel);
    }
}
