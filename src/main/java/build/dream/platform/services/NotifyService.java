package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.saas.domains.NotifyRecord;
import build.dream.common.utils.CommonUtils;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.notify.SaveNotifyRecordModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class NotifyService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveNotifyRecord(SaveNotifyRecordModel saveNotifyRecordModel) throws IOException {
        String uuid = saveNotifyRecordModel.getUuid();
        String notifyUrl = saveNotifyRecordModel.getNotifyUrl();
        String alipayPublicKey = saveNotifyRecordModel.getAlipayPublicKey();
        String alipaySignType = saveNotifyRecordModel.getAlipaySignType();
        BigInteger userId = CommonUtils.getServiceSystemUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("uuid", Constants.SQL_OPERATION_SYMBOL_EQUAL, uuid);
        NotifyRecord notifyRecord = DatabaseHelper.find(NotifyRecord.class, searchModel);
        if (notifyRecord == null) {
            notifyRecord = new NotifyRecord();
            notifyRecord.setUuid(uuid);
            notifyRecord.setNotifyUrl(notifyUrl);
            if (StringUtils.isNotBlank(alipayPublicKey)) {
                notifyRecord.setAlipayPublicKey(alipayPublicKey);
            }
            if (StringUtils.isNotBlank(alipaySignType)) {
                notifyRecord.setAlipaySignType(alipaySignType);
            }
            notifyRecord.setCreateUserId(userId);
            notifyRecord.setLastUpdateUserId(userId);
            notifyRecord.setLastUpdateRemark("保存回调记录！");
            DatabaseHelper.insert(notifyRecord);
        } else {
            notifyRecord.setNotifyUrl(notifyUrl);
            notifyRecord.setAlipayPublicKey(StringUtils.isNotBlank(alipayPublicKey) ? alipayPublicKey : Constants.VARCHAR_DEFAULT_VALUE);
            notifyRecord.setAlipaySignType(StringUtils.isNotBlank(alipaySignType) ? alipaySignType : Constants.VARCHAR_DEFAULT_VALUE);
            notifyRecord.setLastUpdateUserId(userId);
            notifyRecord.setLastUpdateRemark("修改回调记录！");
            DatabaseHelper.update(notifyRecord);
        }

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("保存支付回调记录成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
