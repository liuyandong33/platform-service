package build.dream.platform.services;

import build.dream.common.api.ApiRest;
import build.dream.platform.constants.Constants;
import build.dream.platform.models.activity.SaveSpecialGoodsActivityModel;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ActivityService {
    public ApiRest saveSpecialGoodsActivity(SaveSpecialGoodsActivityModel saveSpecialGoodsActivityModel) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN);
        Date startTime = simpleDateFormat.parse(saveSpecialGoodsActivityModel.getStartTime() + " 00:00:00");
        Date endTime = simpleDateFormat.parse(saveSpecialGoodsActivityModel.getEndTime() + " 23:59:59");
        Validate.isTrue(endTime.after(startTime), "活动结束时间必须大于开始时间！");
        
        return null;
    }
}
