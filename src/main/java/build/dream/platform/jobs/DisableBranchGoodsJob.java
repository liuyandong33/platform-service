package build.dream.platform.jobs;

import build.dream.common.utils.CommonRedisUtils;
import build.dream.common.utils.LogUtils;
import build.dream.platform.constants.Constants;
import build.dream.platform.services.BranchService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DisableBranchGoodsJob implements Job {
    private static final String DISABLE_BRANCH_GOODS_JOB_SIMPLE_NAME = "DisableBranchGoodsJob";
    @Autowired
    private BranchService branchService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            boolean setnxSuccessful = CommonRedisUtils.setnx(Constants.KEY_DISABLE_BRANCH_GOODS_JOB_EXECUTE_SIGN, UUID.randomUUID().toString());
            branchService.disableGoods();
            if (setnxSuccessful) {
                CommonRedisUtils.expire(Constants.KEY_DISABLE_BRANCH_GOODS_JOB_EXECUTE_SIGN, 30, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            CommonRedisUtils.del(Constants.KEY_SYNCHRONIZE_BRANCH_INFO_JOB_EXECUTE_SIGN);
            LogUtils.error("禁用门店产品失败", DISABLE_BRANCH_GOODS_JOB_SIMPLE_NAME, "execute", e);
        }
    }
}
