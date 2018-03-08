package build.dream.platform.utils;

import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GoodsUtils {
    public static Map<String, Object> buildGoodsInfo(Goods goods) {
        Map<String, Object> goodsInfo = new HashMap<String, Object>();
        goodsInfo.put("id", goods.getId());
        goodsInfo.put("name", goods.getName());
        goodsInfo.put("goodsTypeId", goods.getGoodsTypeId());
        goodsInfo.put("status", goods.getStatus());
        goodsInfo.put("photoUrl", goods.getPhotoUrl());
        goodsInfo.put("meteringMode", goods.getMeteringMode());
        return goodsInfo;
    }

    public static Map<String, Object> buildGoodsSpecificationInfo(GoodsSpecification goodsSpecification) {
        Map<String, Object> goodsSpecificationInfo = new HashMap<String, Object>();
        goodsSpecificationInfo.put("id", goodsSpecification.getId());
        goodsSpecificationInfo.put("name", goodsSpecification.getName());
        goodsSpecificationInfo.put("allowTenantBuy", goodsSpecification.isAllowTenantBuy());
        goodsSpecificationInfo.put("allowAgentBuy", goodsSpecification.isAllowAgentBuy());
        goodsSpecificationInfo.put("renewalTime", goodsSpecification.getRenewalTime());
        goodsSpecificationInfo.put("tenantPrice", goodsSpecification.getTenantPrice());
        goodsSpecificationInfo.put("agentPrice", goodsSpecification.getAgentPrice());
        return goodsSpecificationInfo;
    }

    public static Date obtainExpireTime(Date expireTime, GoodsSpecification goodsSpecification) throws ParseException {
        Date date = null;
        if (expireTime == null) {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
        } else {
            date = expireTime;
        }
        int renewalTime = goodsSpecification.getRenewalTime();
        if (renewalTime == 1) {
            date = DateUtils.addYears(date, renewalTime);
        }
        return date;
    }
}
