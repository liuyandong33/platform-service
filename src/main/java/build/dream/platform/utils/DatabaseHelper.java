package build.dream.platform.utils;

import build.dream.common.utils.*;
import build.dream.platform.constants.Constants;
import build.dream.platform.mappers.UniversalMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private static UniversalMapper UNIVERSAL_MAPPER;

    static {
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new BigIntegerConverter(null), BigInteger.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        ConvertUtils.register(new DateConverter(null), Date.class);
    }

    public static UniversalMapper obtainUniversalMapper() {
        if (UNIVERSAL_MAPPER == null) {
            UNIVERSAL_MAPPER = ApplicationHandler.getBean(UniversalMapper.class);
        }
        return UNIVERSAL_MAPPER;
    }

    public static long insert(Object domain) {
        return obtainUniversalMapper().insert(domain);
    }

    public static long insertAll(List<?> domains) {
        return obtainUniversalMapper().insertAll(domains);
    }

    public static long delete(Class<?> domainClass, DeleteModel deleteModel) {
        deleteModel.setTableName(DatabaseUtils.obtainTableName(null, domainClass));
        return obtainUniversalMapper().delete(deleteModel);
    }

    public static long delete(String tableName, BigInteger id) {
        DeleteModel deleteModel = new DeleteModel();
        deleteModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, id);
        deleteModel.setTableName(tableName);
        return obtainUniversalMapper().delete(deleteModel);
    }

    public static long update(Object domain) {
        return obtainUniversalMapper().update(domain);
    }

    public static long universalUpdate(UpdateModel updateModel) {
        return obtainUniversalMapper().universalUpdate(updateModel);
    }

    public static long executeUpdate(Map<String, Object> parameters) {
        return obtainUniversalMapper().executeUpdate(parameters);
    }

    public static long universalCount(Map<String, Object> parameters) {
        return obtainUniversalMapper().universalCount(parameters);
    }

    public static <T> T find(Class<T> domainClass, BigInteger id) {
        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUAL, id);
        return find(domainClass, searchModel);
    }

    public static <T> T find(Class<T> domainClass, SearchModel searchModel) {
        return find(domainClass, DatabaseUtils.obtainTableName(null, domainClass), searchModel);
    }

    public static <T> T find(Class<T> domainClass, String tableName, SearchModel searchModel) {
        try {
            searchModel.setTableName(tableName);
            List<String> columns = searchModel.getColumns();
            if (CollectionUtils.isEmpty(columns)) {
                searchModel.setColumns(DatabaseUtils.obtainAllAlias(domainClass));
            }
            Map<String, Object> map = obtainUniversalMapper().find(searchModel);
            T t = null;
            if (MapUtils.isNotEmpty(map)) {
                t = domainClass.newInstance();
                BeanUtils.populate(t, map);
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> findAll(Class<T> domainClass, SearchModel searchModel) {
        return findAll(domainClass, DatabaseUtils.obtainTableName(null, domainClass), searchModel);
    }

    public static <T> List<T> findAll(Class<T> domainClass, String tableName, SearchModel searchModel) {
        try {
            searchModel.setTableName(tableName);
            List<String> columns = searchModel.getColumns();
            if (CollectionUtils.isEmpty(columns)) {
                searchModel.setColumns(DatabaseUtils.obtainAllAlias(domainClass));
            }
            List<Map<String, Object>> result = obtainUniversalMapper().findAll(searchModel);
            List<T> list = new ArrayList<T>();
            for (Map<String, Object> map : result) {
                T t = domainClass.newInstance();
                BeanUtils.populate(t, map);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> long count(Class<T> domainClass, SearchModel searchModel) {
        return count(DatabaseUtils.obtainTableName(null, domainClass), searchModel);
    }

    public static long count(String tableName, SearchModel searchModel) {
        searchModel.setTableName(tableName);
        return obtainUniversalMapper().count(searchModel);
    }

    public static <T> List<T> findAllPaged(Class<T> domainClass, SearchModel searchModel) {
        return findAllPaged(domainClass, DatabaseUtils.obtainTableName(null, domainClass), searchModel);
    }

    public static <T> List<T> findAllPaged(Class<T> domainClass, String tableName, SearchModel searchModel) {
        try {
            searchModel.setTableName(tableName);
            List<String> columns = searchModel.getColumns();
            if (CollectionUtils.isEmpty(columns)) {
                searchModel.setColumns(DatabaseUtils.obtainAllAlias(domainClass));
            }
            List<Map<String, Object>> result = obtainUniversalMapper().findAllPaged(searchModel);
            List<T> list = new ArrayList<T>();
            for (Map<String, Object> map : result) {
                T t = domainClass.newInstance();
                BeanUtils.populate(t, map);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> executeQuery(Map<String, Object> parameters) {
        return obtainUniversalMapper().executeQuery(parameters);
    }

    public static Map<String, Object> executeUniqueResultQuery(Map<String, Object> parameters) {
        return obtainUniversalMapper().executeUniqueResultQuery(parameters);
    }
}
