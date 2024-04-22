package com.zyuhoo.mini.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Mapper proxy instance factory.
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * create proxy instance.
     *
     * @param sqlSession sql session
     * @return new proxy object
     */
    @SuppressWarnings("unchecked")
    public T newInstance(Map<String, String> sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] {mapperInterface}, mapperProxy);
    }
}
