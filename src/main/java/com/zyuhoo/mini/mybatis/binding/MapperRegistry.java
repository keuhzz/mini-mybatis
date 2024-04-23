package com.zyuhoo.mini.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注册 Mapper.
 */
public class MapperRegistry {

    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * 注册指定包下的接口.
     *
     * @param packageName package name
     */
    public void addMappers(String packageName) {
        // 扫描包路径
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

    /**
     * 将指定的接口注册成 Mapper.
     *
     * @param type 接口类型
     */
    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (knownMappers.containsKey(type)) {
                // duplicate add
                throw new RuntimeException("Type " + type + " is already know to the MapperRegistry");
            }
            knownMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    /**
     * 返回代理对象.
     *
     * @param type interface class
     * @param sqlSession sql session
     * @return Mapper instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry");
        }
        return mapperProxyFactory.newInstance(sqlSession);
    }
}
