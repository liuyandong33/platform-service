package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.common.models.notify.SaveAsyncNotifyModel;
import build.dream.common.domains.saas.AsyncNotify;
import build.dream.common.utils.NotifyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyService {
    /**
     * 保存异步通知
     *
     * @param saveAsyncNotifyModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveAsyncNotify(SaveAsyncNotifyModel saveAsyncNotifyModel) {
        AsyncNotify asyncNotify = NotifyUtils.saveAsyncNotify(saveAsyncNotifyModel);
        return ApiRest.builder().data(asyncNotify).message("保存异步通知成功！").successful(true).build();
    }
}
