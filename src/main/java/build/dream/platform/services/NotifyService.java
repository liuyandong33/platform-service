package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.models.notify.SaveNotifyRecordModel;
import build.dream.common.utils.NotifyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveNotifyRecord(SaveNotifyRecordModel saveNotifyRecordModel) {
        NotifyUtils.saveNotifyRecord(saveNotifyRecordModel);
        return ApiRest.builder().message("保存支付回调记录成功！").successful(true).build();
    }
}
