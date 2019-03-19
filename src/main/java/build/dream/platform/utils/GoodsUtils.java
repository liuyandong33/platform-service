package build.dream.platform.utils;

import build.dream.common.saas.domains.Goods;
import build.dream.common.saas.domains.GoodsSpecification;
import build.dream.common.saas.domains.GoodsType;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoodsUtils {
    public static Map<String, Object> buildGoodsInfo(Goods goods) {
        Map<String, Object> goodsInfo = new HashMap<String, Object>();
        goodsInfo.put(Goods.FieldName.ID, goods.getId());
        goodsInfo.put(Goods.FieldName.NAME, goods.getName());
        goodsInfo.put(Goods.FieldName.GOODS_TYPE_ID, goods.getGoodsTypeId());
        goodsInfo.put(Goods.FieldName.STATUS, goods.getStatus());
        goodsInfo.put(Goods.FieldName.PHOTO_URL, goods.getPhotoUrl());
        goodsInfo.put(Goods.FieldName.METERING_MODE, goods.getMeteringMode());
        goodsInfo.put(Goods.FieldName.BUSINESS, goods.getBusiness());
        return goodsInfo;
    }

    public static Map<String, Object> buildGoodsSpecificationInfo(GoodsSpecification goodsSpecification) {
        Map<String, Object> goodsSpecificationInfo = new HashMap<String, Object>();
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.ID, goodsSpecification.getId());
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.NAME, goodsSpecification.getName());
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.ALLOW_TENANT_BUY, goodsSpecification.isAllowTenantBuy());
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.ALLOW_AGENT_BUY, goodsSpecification.isAllowAgentBuy());
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.RENEWAL_TIME, goodsSpecification.getRenewalTime());
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.TENANT_PRICE, goodsSpecification.getTenantPrice());
        goodsSpecificationInfo.put(GoodsSpecification.FieldName.AGENT_PRICE, goodsSpecification.getAgentPrice());
        return goodsSpecificationInfo;
    }

    public static Map<String, Object> buildGoodsTypeInfo(GoodsType goodsType) {
        Map<String, Object> goodsTypeInfo = new HashMap<String, Object>();
        goodsTypeInfo.put(GoodsType.FieldName.ID, goodsType.getId());
        goodsTypeInfo.put(GoodsType.FieldName.NAME, goodsType.getName());
        goodsTypeInfo.put(GoodsType.FieldName.DESCRIPTION, goodsType.getDescription());
        goodsTypeInfo.put(GoodsType.FieldName.SINGLE, goodsType.isSingle());
        goodsTypeInfo.put(GoodsType.FieldName.RENEW_SQL, goodsType.getRenewSql());
        goodsTypeInfo.put(GoodsType.FieldName.DISABLE_SQL, goodsType.getDisableSql());
        return goodsTypeInfo;
    }

    public static List<Map<String, Object>> buildGoodsSpecificationInfos(List<GoodsSpecification> goodsSpecifications) {
        List<Map<String, Object>> goodsSpecificationInfos = new ArrayList<Map<String, Object>>();
        for (GoodsSpecification goodsSpecification : goodsSpecifications) {
            goodsSpecificationInfos.add(buildGoodsSpecificationInfo(goodsSpecification));
        }
        return goodsSpecificationInfos;
    }

    public static Date obtainExpireTime(Date expireTime, GoodsSpecification goodsSpecification) throws ParseException {
        Date date = null;
        Date currentDate = new Date();
        if (expireTime == null || expireTime.before(currentDate)) {
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
